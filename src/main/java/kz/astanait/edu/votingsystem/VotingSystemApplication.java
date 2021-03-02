package kz.astanait.edu.votingsystem;

import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.Group;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.repositories.GroupRepository;
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
	public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, GroupRepository groupRepository) {
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

			Group group1 = new Group("SE-1905");
			Group group2 = new Group("SE-1906");
			Group group3 = new Group("SE-1907");
			Group group4 = new Group("SE-1908");
			Group group5 = new Group("SE-1909");

			groupRepository.saveAll(
					List.of(group1, group2, group3, group4, group5)
			);

			User user1 = new User("Admin", "Admin", "admin", "admin@gmail.com", group1, 99, "$2y$12$Dn4hctUuI7eCkYfbC/rSfeSPWsy1NGNXG7dtkGyyUhTYJ0p88ZbEq", adminRole);
			User user2 = new User("Chingiz", "Azimbayev", "chingiz", "chingiz@gmail.com", group1,59,"$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", adminRole);
			User user3 = new User("Azatkali", "Nurumgaliyev", "azat", "azat@gmail.com", group2, 69, "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", adminRole);
			User user4 = new User("Nurym", "Siyrbayev", "nurym", "nurym@gmail.com", group3, 18, "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", adminRole);
			User user5 = new User("Syir", "Cartier", "syirCartier", "syir_cartier@yahoo.com", group4, 33,"$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", userRole);
			User user6 = new User("Azat", "Raven", "azatRaven", "azat_raven@gmail.com", group5, 34, "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", userRole);
			User user7 = new User("Madiyar", "Ussabekov", "madok", "madok@gmail.com", group1, 21, "$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa", userRole);

			userRepository.saveAll(
					List.of(user1, user2, user3, user4, user5, user6, user7)
			);
		};
	}
}
