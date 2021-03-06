package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.Role;

import java.util.Set;

public interface RoleService extends GenericService<Role> {

    Role findRoleByName(String name) throws RoleNotFoundException;
    void updateRoleDetails(String oldName, String newName, Set<Authority> newAuthorities) throws RoleNotFoundException;
}
