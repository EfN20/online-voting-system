package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.GroupNotFoundException;
import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.UserServiceImpl;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
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

@Slf4j
@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final RoleService roleService;
    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersController(UserServiceImpl userServiceImpl, RoleService roleService,
                           GroupService groupService, PasswordEncoder passwordEncoder) {
        this.userService = userServiceImpl;
        this.roleService = roleService;
        this.groupService = groupService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String show(Model model, Principal principal) {
        try {
            model.addAttribute("user", userService.findUserByNickname(principal.getName()));
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
        model.addAttribute("user", userService.findUserByNickname(principal.getName()));
        model.addAttribute("groups", groupService.findAll());
        return "users/edit";
    }

    @PutMapping("/edit")
    public String update(@RequestParam("firstName") @Valid @NotBlank @Size(max = 30) String firstName,
                         @RequestParam("lastName") @Valid @NotBlank @Size(max = 30) String lastName,
                         @RequestParam("group") @Valid @NotNull Long group,
                         @RequestParam("age") @Valid @Min(value = 16) @Max(value = 100) Integer age,
                         Principal principal) {
        try {
            userService.updateUserDetails(principal.getName(), firstName, lastName, group, age);
        } catch (UserNotFoundException | GroupNotFoundException e) {
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
