package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByNickname(String nickname);
}
