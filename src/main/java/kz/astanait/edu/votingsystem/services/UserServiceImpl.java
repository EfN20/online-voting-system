package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.Group;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.repositories.GroupRepository;
import kz.astanait.edu.votingsystem.repositories.UserRepository;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("userDetailsServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findUserByNickname(nickname).orElseThrow(() -> new UsernameNotFoundException("User with such nickname was not found"));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<Authority> authorities = user.getRole().getAuthorities();

        grantedAuthorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", user.getRole().getName())));
        authorities.forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName())));

        return new org.springframework.security.core.userdetails.User(
                user.getNickname(),
                user.getPassword(),
                grantedAuthorities
        );
    }

    @Override
    public User findUserByNickname(String nickname) throws UserNotFoundException {
        return userRepository.findUserByNickname(nickname).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public void updateUserDetails(String nickname, String firstName, String lastName,
                                  Group group, Integer age, Set<Interest> interests) throws UserNotFoundException {
        User user = userRepository.findUserByNickname(nickname).orElseThrow(UserNotFoundException::new);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGroup(group);
        user.setAge(age);
        user.setInterests(interests);
    }

    @Override
    public List<User> findUsersByGroup(Group group) {
        return userRepository.findUsersByGroup(group);
    }

    @Transactional
    @Override
    public void updateUserRole(User user, Role role) {
        user.setRole(role);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
