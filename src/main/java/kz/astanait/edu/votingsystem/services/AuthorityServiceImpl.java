package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.AuthorityNotFoundException;
import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.repositories.AuthorityRepository;
import kz.astanait.edu.votingsystem.services.interfaces.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
