package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.models.Vote;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

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

    @Autowired
    public QuestionsController(UserService userService, RoleService roleService, GroupService groupService,
                               InterestService interestService, PasswordEncoder passwordEncoder,
                               VoteService voteService, QuestionService questionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.passwordEncoder = passwordEncoder;
        this.voteService = voteService;
        this.questionService = questionService;
    }

    @GetMapping
    public String getQuestionPage(Model model, Principal principal){
        try {
            User user = userService.findUserByNickname(principal.getName());
            List<Question> questionList = questionService.findAll();
            List<Vote> voteList = voteService.findVotesByUser(user);
//            int length = questionList.size();
//            for (int i = 0; i < length; i++) {
//                for (Vote vote : voteList) {
//                    if (questionList.get(i) == vote.getQuestion()) {
//                        questionList.remove(i);
//                        i--;
//                        length--;
//                        break;
//                    }
//                }
//            }
//            questionList.forEach(question -> log.info(String.valueOf(question)));
            model.addAttribute("questions", questionList);
            model.addAttribute("votes", voteList);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "questions/vote-page";
    }

    @PostMapping
    public String submitVote(@RequestParam("question") Question question,
                             @RequestParam("option") Option option,
                             Principal principal) {
        log.info("[Question] " + question);
        log.info("[Option] " + option);
        return "redirect:/questions?success";
    }

}
