package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.AuthorityNotFoundException;
import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.repositories.AuthorityRepository;
import kz.astanait.edu.votingsystem.services.interfaces.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority findById(Long id) throws AuthorityNotFoundException {
        return authorityRepository.findById(id).orElseThrow(AuthorityNotFoundException::new);
    }

    @Transactional
    @Override
    public void updateAuthorityDetails(String oldName, String newName) throws RoleNotFoundException {
        Authority authority = authorityRepository.findAuthorityByName(oldName).orElseThrow(RoleNotFoundException::new);
        authority.setName(newName);
    }

    @Override
    public Authority save(Authority entity) {
        return authorityRepository.save(entity);
    }

    @Override
    public void delete(Authority entity) {
        authorityRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        authorityRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return authorityRepository.count();
    }

    @Override
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    @Override
    public Authority findAuthorityByName(String name) throws AuthorityNotFoundException{
        return authorityRepository.findAuthorityByName(name).orElseThrow(AuthorityNotFoundException::new);
    }
}
