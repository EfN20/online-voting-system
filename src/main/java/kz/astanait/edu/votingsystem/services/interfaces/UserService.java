package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.models.User;

public interface UserService extends GenericService<User> {

    User findUserByNickname(String nickname);
}
