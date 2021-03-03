package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final GroupService groupService;
    private final InterestService interestService;

    @Autowired
    public MainController(GroupService groupService, InterestService interestService) {
        this.groupService = groupService;
        this.interestService = interestService;
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

}
