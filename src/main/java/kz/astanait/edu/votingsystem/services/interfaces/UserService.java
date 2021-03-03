package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Group;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.User;

import java.util.Set;

public interface UserService extends GenericService<User> {

    User findUserByNickname(String nickname) throws UserNotFoundException;
    void updateUserDetails(String nickname, String firstName, String lastName, Group group, Integer age, Set<Interest> interests) throws UserNotFoundException;
}
