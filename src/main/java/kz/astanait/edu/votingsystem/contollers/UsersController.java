package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.UserServiceImpl;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final GroupService groupService;

    @Autowired
    public UsersController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder,
                           RoleService roleService, GroupService groupService) {
        this.userService = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.groupService = groupService;
    }

    @GetMapping
    public String getUserProfile(Model model){
        String nickName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("user", userService.findUserByNickname(nickName));
        return "users/profile";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            return "registration";
        }
        System.out.println("[ UserController ]" + user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            user.setRole(roleService.findRoleByName("USER"));
        } catch (RoleNotFoundException e) {
            System.out.println(e.getMessage());
        }
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String updatePage(Model model) {
        String nickName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("user", userService.findUserByNickname(nickName));
        model.addAttribute("groups", groupService.findAll());
        return "users/edit";
    }

    @PutMapping("/edit")
    public String update(Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            return "users/edit";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }

}
