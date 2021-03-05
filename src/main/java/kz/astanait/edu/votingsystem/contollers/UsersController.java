package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Group;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final RoleService roleService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    private final VoteService voteService;

    @Autowired
    public UsersController(UserService userService, RoleService roleService, GroupService groupService,
                           InterestService interestService, PasswordEncoder passwordEncoder,
                           VoteService voteService) {
        this.userService = userService;
        this.roleService = roleService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.passwordEncoder = passwordEncoder;
        this.voteService = voteService;
    }

    @GetMapping
    public String show(Model model, Principal principal) {
        try {
            User user = userService.findUserByNickname(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("lastVotes", voteService.findTop5ByUserOrderByIdDesc(user));
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "users/profile";
    }

    @PostMapping
    public String create(Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("interests", interestService.findAll());
            return "registration";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            user.setRole(roleService.findRoleByName("USER"));
        } catch (RoleNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, Principal principal) {
        try {
            model.addAttribute("user", userService.findUserByNickname(principal.getName()));
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("interests", interestService.findAll());
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "users/edit";
    }

    @PutMapping("/edit")
    public String update(@RequestParam("firstName") @Valid @NotBlank @Size(max = 30) String firstName,
                         @RequestParam("lastName") @Valid @NotBlank @Size(max = 30) String lastName,
                         @RequestParam("group") @Valid @NotNull Group group,
                         @RequestParam("age") @Valid @Min(value = 16) @Max(value = 100) Integer age,
                         @RequestParam("interests") Set<Interest> interests,
                         Principal principal) {
        try {
            userService.updateUserDetails(principal.getName(), firstName, lastName, group, age, interests);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "redirect:/users/edit?success";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException e,
                                                     RedirectAttributes redirectAttributes,
                                                     HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errors", e.getConstraintViolations());
        return String.format("redirect:%s?error", request.getRequestURI());
    }

}
