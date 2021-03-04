package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
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

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class MainController {

    private final UserService userService;
    private final RoleService roleService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    private final VoteService voteService;
    private final QuestionService questionService;

    @Autowired
    public MainController(UserService userService, RoleService roleService, GroupService groupService,
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

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("interests", interestService.findAll());
        return "registration";
    }

    @GetMapping(value = {"/home" , "/"})
    public String getHomePage(Model model, Principal principal){
        try {
            User user = userService.findUserByNickname(principal.getName());
            List<Question> questionList = questionService.findAll();
            List<Vote> voteList = voteService.findVotesByUser(user);
            Map<Question, Boolean> questionBooleanMap = new LinkedHashMap<>();
            if (voteList.isEmpty()) {
                questionList.forEach(question -> questionBooleanMap.put(question, false));
            } else {
                for (Question question : questionList) {
                    for (Vote vote : voteList) {
                        if (vote.getQuestion() == question) {
                            questionBooleanMap.put(question, true);
                            break;
                        }
                        questionBooleanMap.put(question, false);
                    }
                }
            }
            model.addAttribute("questions", questionBooleanMap);
            model.addAttribute("votes", voteList);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "home";
    }
}
