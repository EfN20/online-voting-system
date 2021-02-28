package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.services.UserServiceImpl;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserServiceImpl userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @GetMapping
    public String getUserProfile(Model model){
        String nickName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("user", userService.findUserByNickname(nickName));
        return "profile";
    }

}
