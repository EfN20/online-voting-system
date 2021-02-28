package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.repositories.RoleRepository;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Long id) throws RoleNotFoundException {
        return roleRepository.findById(id).orElseThrow(RoleNotFoundException::new);
    }

    @Override
    public Role save(Role entity) {
        return roleRepository.save(entity);
    }

    @Override
    public void delete(Role entity) {
        roleRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return roleRepository.count();
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
