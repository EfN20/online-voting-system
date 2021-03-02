package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.GroupNotFoundException;
import kz.astanait.edu.votingsystem.models.Group;
import kz.astanait.edu.votingsystem.repositories.GroupRepository;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group findById(Long id) throws GroupNotFoundException {
        return groupRepository.findById(id).orElseThrow(GroupNotFoundException::new);
    }

    @Override
    public Group save(Group entity) {
        return groupRepository.save(entity);
    }

    @Override
    public void delete(Group entity) {
        groupRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return groupRepository.count();
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

}
