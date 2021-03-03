package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.services.QuestionServiceImpl;
import kz.astanait.edu.votingsystem.services.UserServiceImpl;
import kz.astanait.edu.votingsystem.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(name = "/questions")
public class QuestionsController {

    private final UserService userService;
    private final RoleService roleService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    private final VoteService voteService;
    private final QuestionService questionService;

    @Autowired
    public QuestionsController(UserServiceImpl userServiceImpl, RoleService roleService, GroupService groupService,
                               InterestService interestService, PasswordEncoder passwordEncoder,
                               VoteService voteService, QuestionServiceImpl questionService) {
        this.userService = userServiceImpl;
        this.roleService = roleService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.passwordEncoder = passwordEncoder;
        this.voteService = voteService;
        this.questionService = questionService;
    }

    @GetMapping
    public String getQuestionPage(Model model){
        model.addAttribute("questions", questionService.findAll());
        return "questions";
    }

}
