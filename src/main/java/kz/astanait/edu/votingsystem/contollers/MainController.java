package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.Role;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public String getHomePage(Model model, Principal principal) {
        try {
            User user = userService.findUserByNickname(principal.getName());
            Map<Question, Boolean> questionBooleanMap = getQuestionBooleanMap(user);
            model.addAttribute("questions", questionBooleanMap);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "home";
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model, Principal principal) {
        try {
            User user = userService.findUserByNickname(principal.getName());
            Map<Question, Boolean> questionBooleanMap = getQuestionBooleanMap(user);
            model.addAttribute("questions", questionBooleanMap);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "admin";
    }

    private Map<Question, Boolean> getQuestionBooleanMap(User user) {
        List<Question> questionList = questionService.findAll();

        // Сортируем лист (???)
        questionList = questionList.stream().
                sorted(Comparator.comparingLong(Question::getId)).
                collect(Collectors.toList());

        // Сортируем options (???)
        questionList.forEach(question -> {
            Set<Option> sortedOptions = question.getOptions().stream()
                    .sorted(Comparator.comparingLong(Option::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            question.setOptions(sortedOptions);
        });

        List<Vote> voteList = voteService.findVotesByUser(user);
        Map<Question, Boolean> questionBooleanMap = new LinkedHashMap<>();

        if (voteList.isEmpty()) {
            questionList.forEach(question -> questionBooleanMap.put(question, false));
        } else {
            questionList.forEach(question -> voteList.forEach(vote -> {
                if (vote.getQuestion() == question) {
                    questionBooleanMap.put(question, true);
                    return;
                }
                questionBooleanMap.put(question, false);
            }));
        }

        return questionBooleanMap;
    }

    @GetMapping("/admin/all-users")
    public String getAllUsersPage(Model model) {
        List<User> users = userService.findAll();
        users = users.stream()
                .sorted(Comparator.comparingLong(User::getId))
                .collect(Collectors.toList());

        List<Role> roles = roleService.findAll();
        roles = roles.stream()
                .sorted(Comparator.comparingLong(Role::getId))
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "all-users";
    }

    @PatchMapping("/admin/role-update")
    public String updateUserRole(@RequestParam("user") User user, @RequestParam("role") Role role) {
        userService.updateUserRole(user, role);
        return "redirect:/admin/all-users?success";
    }
}
