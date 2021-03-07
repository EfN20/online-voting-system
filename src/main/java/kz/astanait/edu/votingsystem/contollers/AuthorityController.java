package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.interfaces.AuthorityService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/authorities")
public class AuthorityController {
    private final UserService userService;
    private final AuthorityService authorityService;

    @Autowired
    public AuthorityController(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }

    @DeleteMapping
    public String deleteAuthority(@RequestParam("authority") Authority authority, Principal principal) {
        try {
            User currUser = userService.findUserByNickname(principal.getName());
            authorityService.delete(authority);
            if (currUser.getRole().getAuthorities().contains(authority)) {
                SecurityContextHolder.clearContext();
            }
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "redirect:/admin/authorities?success";
    }

    @PutMapping
    public String updateAuthority(@RequestParam("authority") Authority authority,
                                  @RequestParam("name") String newName,
                                  Principal principal) {
        try {
            User currUser = userService.findUserByNickname(principal.getName());
            authorityService.updateAuthority(authority, newName);
            if (currUser.getRole().getAuthorities().contains(authority)) {
                SecurityContextHolder.clearContext();
            }
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "redirect:/admin/authorities?success";
    }

    @PostMapping
    public String createNewAuthority(@RequestParam("authorityName") String authorityName) {
        authorityService.save(new Authority(authorityName));
        return "redirect:/admin/authorities?success";
    }
}
