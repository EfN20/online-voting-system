package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.Vote;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import kz.astanait.edu.votingsystem.services.interfaces.OptionService;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/options")
public class OptionsController {

    private final UserService userService;
    private final RoleService roleService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    private final VoteService voteService;
    private final QuestionService questionService;
    private final OptionService optionService;

    @Autowired
    public OptionsController(UserService userService, RoleService roleService, GroupService groupService,
                             InterestService interestService, PasswordEncoder passwordEncoder,
                             VoteService voteService, QuestionService questionService, OptionService optionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.passwordEncoder = passwordEncoder;
        this.voteService = voteService;
        this.questionService = questionService;
        this.optionService = optionService;
    }

    @PutMapping
    public String updateOption(@RequestParam("option") Option option, @RequestParam("title") String newTitle) {
        optionService.updateOption(option, newTitle);
        return "redirect:/admin?success";
    }

    @DeleteMapping
    public String deleteOption(@RequestParam("option") Option option) {
        voteService.deleteAllByOption(option);
        questionService.deleteOption(option);
        return "redirect:/admin?success";
    }

    @PostMapping
    public String createOption(@RequestParam("question") Question question, @RequestParam("title") String title) {
        questionService.addOption(question, new Option(title, 0L));
        return "redirect:/admin?success";
    }
}
