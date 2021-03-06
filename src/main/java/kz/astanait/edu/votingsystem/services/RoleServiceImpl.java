package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.RoleNotFoundException;
import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.repositories.RoleRepository;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByName(String name) throws RoleNotFoundException {
        return roleRepository.findRoleByName(name).orElseThrow(RoleNotFoundException::new);
    }

    @Transactional
    @Override
    public void updateRoleDetails(String oldName, String newName, Set<Authority> newAuthorities) throws RoleNotFoundException {
        Role role = roleRepository.findRoleByName(oldName).orElseThrow(RoleNotFoundException::new);
        role.setName(newName);
        role.setAuthorities(newAuthorities);
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
