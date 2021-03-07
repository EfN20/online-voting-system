package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.contollers.utils.ControllerUtil;
import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.interfaces.AuthorityService;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final AuthorityService authorityService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    private final VoteService voteService;
    private final QuestionService questionService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, AuthorityService authorityService,
                           GroupService groupService, InterestService interestService, PasswordEncoder passwordEncoder,
                           VoteService voteService, QuestionService questionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.authorityService = authorityService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.passwordEncoder = passwordEncoder;
        this.voteService = voteService;
        this.questionService = questionService;
    }

    @GetMapping
    public String getAdminPage(Model model, Principal principal) {
        try {
            Map<Question, Boolean> questionBooleanMap = ControllerUtil.getQuestionBooleanMap(userService.findUserByNickname(principal.getName()), questionService, voteService);
            model.addAttribute("questions", questionBooleanMap);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "admin";
    }

    @GetMapping("/roles")
    public String getRolesPage(Model model) {
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("authorities", authorityService.findAll());
        return "all-roles";
    }

    @GetMapping("/authorities")
    public String getAuthoritiesPage(Model model) {
        model.addAttribute("authorities", authorityService.findAll());
        return "all-authorities";
    }

    @GetMapping("/all-users")
    public String getAllUsersPage(Model model) {
        List<User> users = userService.findAll();
        List<Role> roles = roleService.findAll();

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(() -> users.sort((Comparator.comparing(User::getId))));
        executorService.submit(() -> roles.sort((Comparator.comparing(Role::getId))));

        executorService.shutdownNow();

        try {
            if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
                return "error/500";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "error/500";
        }

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "all-users";
    }

    @PutMapping("/update-role")
    public String updateUserRole(@RequestParam("user") User user,
                                 @RequestParam("role") Role role,
                                 Principal principal) {
        userService.updateUserRole(user, role);
        if (user.getNickname().equals(principal.getName()) && role.getName().equals("USER")) {
            SecurityContextHolder.clearContext();
        }
        return "redirect:/admin/all-users?success";
    }
}
