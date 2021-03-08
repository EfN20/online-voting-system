package kz.astanait.edu.votingsystem;

import kz.astanait.edu.votingsystem.models.Authority;
import kz.astanait.edu.votingsystem.models.Group;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.Role;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.repositories.GroupRepository;
import kz.astanait.edu.votingsystem.repositories.InterestRepository;
import kz.astanait.edu.votingsystem.repositories.QuestionRepository;
import kz.astanait.edu.votingsystem.repositories.RoleRepository;
import kz.astanait.edu.votingsystem.repositories.UserRepository;
import kz.astanait.edu.votingsystem.repositories.VoteRepository;
import kz.astanait.edu.votingsystem.services.interfaces.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootApplication
public class VotingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotingSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository,
                                               GroupRepository groupRepository, InterestRepository interestRepository,
                                               QuestionRepository questionRepository, VoteRepository voteRepository,
                                               OptionService optionService) {
        return args -> {
            // Set of authorities
            Authority authority1 = new Authority("users:create");
            Authority authority2 = new Authority("users:update-self-profile");

            // Set of roles
            Role adminRole = new Role("ADMIN");
            Role userRole = new Role("USER");

            adminRole.addAuthority(authority1);
            adminRole.addAuthority(authority2);

            userRole.addAuthority(authority2);

            roleRepository.saveAll(
                    List.of(adminRole, userRole)
            );

            // Set of groups
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
            user3.addInterest(interest4);

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

            // Set of question
            Question question1 = new Question("How old are you?", 0L);
            Option option1 = new Option("12-18", 0L);
            Option option2 = new Option("18-24", 0L);
            Option option3 = new Option("24-30", 0L);
            Option option4 = new Option("Older than 30", 0L);
            question1.setOptions(Set.of(option1, option2, option3, option4));

            Question question2 = new Question("What is your educational program?", 0L);
            Option option5 = new Option("Software Engineering", 0L);
            Option option6 = new Option("Cyber Security", 0L);
            Option option7 = new Option("Big Data Analysis", 0L);
            question2.setOptions(Set.of(option5, option6, option7));

            Question question3 = new Question("What university are you from?", 0L);
            Option option8 = new Option("AITU", 0L);
            Option option9 = new Option("NU", 0L);
            Option option10 = new Option("KBTU", 0L);
            Option option11 = new Option("Another", 0L);
            question3.setOptions(Set.of(option8, option9, option10, option11));

            Question question4 = new Question("What is your programming experience?", 0L);
            Option option12 = new Option("Less than 1 year", 0L);
            Option option13 = new Option("2-3 years", 0L);
            Option option14 = new Option("More than 3 years", 0L);
            Option option15 = new Option("I don't have any experience", 0L);
            question4.setOptions(Set.of(option12, option13, option14, option15));

            Question question5 = new Question("Choose your place of residence:", 0L);
            Option option16 = new Option("City", 0L);
            Option option17 = new Option("Town", 0L);
            Option option18 = new Option("Village", 0L);
            question5.setOptions(Set.of(option16, option17, option18));

            Question question6 = new Question("What is your current living situation?", 0L);
            Option option19 = new Option("University dorm", 0L);
            Option option20 = new Option("I have my own apartment", 0L);
            Option option21 = new Option("I rent an apartment on my own", 0L);
            Option option22 = new Option("I rent an apartment with friends", 0L);
            Option option23 = new Option("I rent a room in a hostel", 0L);
            Option option24 = new Option("I live with my relatives", 0L);
            question6.setOptions(Set.of(option19, option20, option21, option22, option23, option24));

            Question question7 = new Question("What sources do you usually find out about how you serve in the army?", 0L);
            Option option25 = new Option("From newspapers", 0L);
            Option option26 = new Option("From the news on TV", 0L);
            Option option27 = new Option("From radio", 0L);
            Option option28 = new Option("From the Internet", 0L);
            Option option29 = new Option("From the stories of friends", 0L);
            Option option30 = new Option("I find out by chance from different sources", 0L);
            question7.setOptions(Set.of(option25, option26, option27, option28, option29, option30));

            Question question8 = new Question("How do you feel about military service?", 0L);
            Option option31 = new Option("Positively", 0L);
            Option option32 = new Option("Neutral", 0L);
            Option option33 = new Option("Negatively", 0L);
            question8.setOptions(Set.of(option31, option32, option33));

            Question question9 = new Question("Do you think every man should complete military service?", 0L);
            Option option34 = new Option("Yes", 0L);
            Option option35 = new Option("No", 0L);
            Option option36 = new Option("It shouldn't be mandatory", 0L);
            question9.setOptions(Set.of(option34, option35, option36));

            Question question10 = new Question("Are you planning to move to another country", 0L);
            Option option37 = new Option("Yes", 0L);
            Option option38 = new Option("No", 0L);
            Option option39 = new Option("Perhaps", 0L);
            question10.setOptions(Set.of(option37, option38, option39));

            questionRepository.saveAll(List.of(question1, question2, question3, question4, question5, question6, question7, question8, question9, question10));
        };
    }
}
