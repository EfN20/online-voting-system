package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.contollers.utils.ControllerUtil;
import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.models.Vote;
import kz.astanait.edu.votingsystem.services.interfaces.OptionService;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/questions")
public class QuestionsController {

    private final UserService userService;
    private final VoteService voteService;
    private final QuestionService questionService;
    private final OptionService optionService;

    @Autowired
    public QuestionsController(UserService userService, VoteService voteService,
                               QuestionService questionService, OptionService optionService) {
        this.userService = userService;
        this.voteService = voteService;
        this.questionService = questionService;
        this.optionService = optionService;
    }

    @PostMapping
    public String submitVote(@RequestParam("question") Question question,
                             @RequestParam("option") Option option,
                             Principal principal) {
        try {
            voteService.save(new Vote(
                    userService.findUserByNickname(principal.getName()),
                    option,
                    question)
            );
            optionService.increaseOptionCount(option);
            questionService.increaseVoteCount(question);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "redirect:/home?success";
    }

    @GetMapping("/full-statistics/{id}")
    public String getFullStatistics(@PathVariable("id") Question question, Model model, Principal principal) {
        try {
            User currentUser = userService.findUserByNickname(principal.getName());
            List<User> groupMates = userService.findUsersByGroup(currentUser.getGroup());

            Map<Option, List<User>> usersVotedOptions = new LinkedHashMap<>();
            Map<Option, Map<Interest, Set<User>>> allOptionMap = new LinkedHashMap<>();

            ExecutorService executorService = Executors.newCachedThreadPool();

            executorService.submit(() -> ControllerUtil.getUsersVotedOptions(question, usersVotedOptions, groupMates, voteService));
            executorService.submit(() -> ControllerUtil.getMapByQuestionAllOption(question, allOptionMap, currentUser, voteService));

            executorService.shutdownNow();

            try {
                if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                    return "error/500";
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "error/500";
            }
            model.addAttribute("question", question);
            model.addAttribute("statisticsByGroupMates", usersVotedOptions);
            model.addAttribute("statisticsByInterests", allOptionMap);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "questions/full-statistics";
    }

    @DeleteMapping
    public String deleteQuestion(@RequestParam("question") Question question) {
        voteService.deleteAllByQuestion(question);
        questionService.delete(question);
        return "redirect:/admin?success";
    }

    @PutMapping
    public String createQuestion(@RequestParam("title") String title) {
        questionService.save(new Question(title, 0L));
        return "redirect:/admin?success";
    }

    @PatchMapping
    public String updateQuestion(@RequestParam("question") Question question,
                                 @RequestParam("title") String newTitle) {
        questionService.updateQuestion(question, newTitle);
        return "redirect:/admin?success";
    }
}
