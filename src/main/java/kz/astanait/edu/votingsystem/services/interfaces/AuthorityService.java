package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.models.Authority;

public interface AuthorityService extends GenericService<Authority> {
    void updateAuthority(Authority authority, String newName);
}
