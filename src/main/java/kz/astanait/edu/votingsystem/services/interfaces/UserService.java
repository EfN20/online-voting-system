package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.exceptions.GroupNotFoundException;
import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.User;

public interface UserService extends GenericService<User> {

    User findUserByNickname(String nickname) throws UserNotFoundException;
    void updateUserDetails(String nickname, String firstName, String lastName, Long group, Integer age) throws UserNotFoundException, GroupNotFoundException;
}
