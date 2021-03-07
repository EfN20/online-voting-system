package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.models.Authority;

public interface AuthorityService extends GenericService<Authority> {
    Authority findAuthorityByName(String name);
    void updateAuthorityDetails(String oldName, String newName);
}
