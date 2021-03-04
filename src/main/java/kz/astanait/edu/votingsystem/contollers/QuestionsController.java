package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.models.Vote;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import kz.astanait.edu.votingsystem.services.interfaces.OptionService;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/questions")
public class QuestionsController {

    private final UserService userService;
    private final RoleService roleService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    private final VoteService voteService;
    private final QuestionService questionService;
    private final OptionService optionService;

    @Autowired
    public QuestionsController(UserService userService, RoleService roleService, GroupService groupService,
                               InterestService interestService, PasswordEncoder passwordEncoder,
                               VoteService voteService, QuestionService questionService, OptionService optionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.passwordEncoder = passwordEncoder;
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

    @GetMapping("/fullStatistics")
    public String getFullStatistics(@RequestParam("question") Question question, Model model, Principal principal) {
        User user = userService.findUserByNickname(principal.getName());
        List<User> groupMates = userService.findUsersByGroup(user.getGroup());
        Map<Option, List<User>> usersVotedOptions = new LinkedHashMap<>();
        for (Option option: question.getOptions()) {
            List<User> sameVotedUsers = new ArrayList<>();
            List<Vote> votes = voteService.findVotesByQuestionAndOption(question, option);
            votes.stream()
                    .filter(vote -> groupMates.contains(vote.getUser()))
                    .collect(Collectors.toList())
                    .forEach(vote -> sameVotedUsers.add(vote.getUser()));
            if(!votes.isEmpty()) {
                usersVotedOptions.put(option, sameVotedUsers);
            }
        }
        model.addAttribute("statistics", usersVotedOptions);
        return "questions/vote-page";
    }
}
