package kz.astanait.edu.votingsystem;

import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.repositories.RoleRepository;
import kz.astanait.edu.votingsystem.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class VotingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository) {
		return args -> {
			Authority authority1 = new Authority("users:create");
			Authority authority2 = new Authority("users:update-self-password");

			Role adminRole = new Role("ADMIN");
			Role userRole = new Role("USER");

			adminRole.addAuthority(authority1);
			adminRole.addAuthority(authority2);

			userRole.addAuthority(authority2);

			roleRepository.saveAll(
					List.of(adminRole, userRole)
			);

			User user1 = new User("admin", "admin@gmail.com", "$2y$12$Dn4hctUuI7eCkYfbC/rSfeSPWsy1NGNXG7dtkGyyUhTYJ0p88ZbEq", adminRole);
			User user2 = new User("chingiz", "chingiz@gmail.com", "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", adminRole);
			User user3 = new User("azat", "azat@gmail.com", "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", adminRole);
			User user4 = new User("nurym", "nurym@gmail.com", "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", adminRole);
			User user5 = new User("syirCartier", "syir_cartier@yahoo.com", "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", userRole);
			User user6 = new User("azatRaven", "azat_raven@gmail.com", "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", userRole);
			User user7 = new User("madok", "madok@gmail.com", "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", userRole);

			userRepository.saveAll(
					List.of(user1, user2, user3, user4, user5, user6, user7)
			);
		};
	}
}
