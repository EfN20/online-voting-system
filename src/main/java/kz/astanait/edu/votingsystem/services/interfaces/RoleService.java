package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.models.Role;

public interface RoleService extends GenericService<Role> {

    Role findRoleByName(String name) throws RoleNotFoundException;
}
