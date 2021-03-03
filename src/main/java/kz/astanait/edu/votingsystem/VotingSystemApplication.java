package kz.astanait.edu.votingsystem;

import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.Group;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.repositories.GroupRepository;
import kz.astanait.edu.votingsystem.repositories.InterestRepository;
import kz.astanait.edu.votingsystem.repositories.RoleRepository;
import kz.astanait.edu.votingsystem.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
@SpringBootApplication
public class VotingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository,
											   GroupRepository groupRepository, InterestRepository interestRepository) {
		return args -> {
			Authority authority1 = new Authority("users:create");
			Authority authority2 = new Authority("users:update-self-profile");

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

			// Set of interests
			Interest interest1 = new Interest("iOS Development");
			Interest interest2 = new Interest("Android Development");
			Interest interest3 = new Interest("Django Development");
			Interest interest4 = new Interest("Laravel Development");
			Interest interest5 = new Interest("Spring Development");
			Interest interest6 = new Interest("React Development");
			Interest interest7 = new Interest("C# (Lox) Development");

			User user1 = new User(
					"Admin",
					"Admin",
					"admin",
					"admin@gmail.com",
					group1,
					99,
					"$2y$12$Dn4hctUuI7eCkYfbC/rSfeSPWsy1NGNXG7dtkGyyUhTYJ0p88ZbEq",
					adminRole
			);
			user1.addInterest(interest4);
			user1.addInterest(interest5);

			User user2 = new User(
					"Chingiz",
					"Azimbayev",
					"chingiz",
					"chingiz@gmail.com",
					group1,
					59,
					"$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa",
					adminRole
			);
			user2.addInterest(interest1);
			user2.addInterest(interest4);
			user2.addInterest(interest5);

			User user3 = new User(
					"Azatkali",
					"Nurumgaliyev",
					"azat",
					"azat@gmail.com",
					group2,
					69,
					"$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa",
					adminRole
			);
			user3.addInterest(interest3);
			user3.addInterest(interest5);

			User user4 = new User(
					"Nurym",
					"Siyrbayev",
					"nurym",
					"nurym@gmail.com",
					group3,
					18,
					"$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa",
					adminRole
			);
			user4.addInterest(interest1);

			User user5 = new User(
					"Syir",
					"Cartier",
					"syirCartier",
					"syir_cartier@yahoo.com",
					group4,
					33,
					"$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa",
					userRole
			);
			user5.addInterest(interest1);
			user5.addInterest(interest2);
			user5.addInterest(interest3);
			user5.addInterest(interest4);
			user5.addInterest(interest5);
			user5.addInterest(interest6);
			user5.addInterest(interest7);

			User user6 = new User(
					"Azat",
					"Raven",
					"azatRaven",
					"azat_raven@gmail.com",
					group5,
					34,
					"$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa",
					userRole
			);
			user6.addInterest(interest1);
			user6.addInterest(interest2);
			user6.addInterest(interest3);
			user6.addInterest(interest4);
			user6.addInterest(interest5);
			user6.addInterest(interest6);
			user6.addInterest(interest7);

			User user7 = new User(
					"Madiyar",
					"Ussabekov",
					"madok",
					"madok@gmail.com",
					group1,
					21,
					"$2y$12$iI2nzSwe3SBIjmWmOuBeaem9uSzoFbek8ghEUETepbDMeco0Gj3Oa",
					userRole
			);
			user7.addInterest(interest7);

			userRepository.saveAll(
					List.of(user1, user2, user3, user4, user5, user6, user7)
			);

			interest1.setName("Lfdsf");

			interestRepository.save(interest1);
		};
	}
}
