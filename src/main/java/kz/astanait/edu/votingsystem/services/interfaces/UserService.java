package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Group;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.models.User;

import java.util.List;
import java.util.Set;

public interface UserService extends GenericService<User> {

    User findUserByNickname(String nickname) throws UserNotFoundException;
    void updateUserDetails(String nickname, String firstName, String lastName, Group group, Integer age, Set<Interest> interests) throws UserNotFoundException;
    List<User> findUsersByGroup(Group group);
    void updateUserRole(User user, Role role);
}
