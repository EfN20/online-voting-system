package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.UserServiceImpl;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model){
        model.addAttribute("userRegistrationForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userRegistrationForm") @Valid User userRegistrationForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "registration";
        userService.save(userRegistrationForm);
        return "redirect:/login";
    }


}
