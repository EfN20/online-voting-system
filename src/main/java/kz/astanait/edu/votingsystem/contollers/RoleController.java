package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.interfaces.AuthorityService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/roles")
public class RoleController {
    private final UserService userService;
    private final RoleService roleService;
    private final AuthorityService authorityService;

    @Autowired
    public RoleController(UserService userService, RoleService roleService,
                          AuthorityService authorityService) {
        this.userService = userService;
        this.roleService = roleService;
        this.authorityService = authorityService;
    }

    @DeleteMapping
    public String deleteRole(@RequestParam("role") Role role) {
        roleService.delete(role);
        return "redirect:/admin/roles?success";
    }

    @PutMapping
    public String updateRole(@RequestParam("role") Role role,
                             @RequestParam("name") String oldName,
                             @RequestParam("authorities") Set<Authority> authorities, Principal principal) {
        try{
            User currUser = userService.findUserByNickname(principal.getName());
            roleService.updateRoleDetails(role.getName(), oldName, authorities);
            if (currUser.getRole().equals(role)) {
                SecurityContextHolder.clearContext();
            }
        } catch (RoleNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "redirect:/admin/roles?success";
    }
}
