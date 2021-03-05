package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.models.Vote;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import kz.astanait.edu.votingsystem.services.interfaces.OptionService;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import kz.astanait.edu.votingsystem.services.interfaces.RoleService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/questions")
public class QuestionsController {

    private final UserService userService;
    private final RoleService roleService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    private final VoteService voteService;
    private final QuestionService questionService;
    private final OptionService optionService;

    @Autowired
    public QuestionsController(UserService userService, RoleService roleService, GroupService groupService,
                               InterestService interestService, PasswordEncoder passwordEncoder,
                               VoteService voteService, QuestionService questionService, OptionService optionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.passwordEncoder = passwordEncoder;
        this.voteService = voteService;
        this.questionService = questionService;
        this.optionService = optionService;
    }

    @PostMapping
    public String submitVote(@RequestParam("question") Question question,
                             @RequestParam("option") Option option,
                             Principal principal) {
        try {
            voteService.save(new Vote(
                    userService.findUserByNickname(principal.getName()),
                    option,
                    question)
            );
            optionService.increaseOptionCount(option);
            questionService.increaseVoteCount(question);
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "redirect:/home?success";
    }

    @GetMapping("/full-statistics/{id}")
    public String getFullStatistics(@PathVariable("id") Question question, Model model, Principal principal) {
        User user = userService.findUserByNickname(principal.getName());
        List<User> groupMates = userService.findUsersByGroup(user.getGroup());
        Map<Option, List<User>> usersVotedOptions = new LinkedHashMap<>();

        Set<Option> sortedOptions = question.getOptions().stream()
                .sorted(Comparator.comparingLong(Option::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        question.setOptions(sortedOptions);

        question.getOptions().forEach(option -> {
            List<User> sameVotedUsers = new ArrayList<>();
            List<Vote> votes = voteService.findVotesByQuestionAndOption(question, option);

            votes.stream()
                    .filter(vote -> groupMates.contains(vote.getUser()))
                    .collect(Collectors.toList())
                    .forEach(vote -> sameVotedUsers.add(vote.getUser()));

            if (!sameVotedUsers.isEmpty()) {
                usersVotedOptions.put(option, sameVotedUsers);
            }
        });

//Interest
/*
        Map<Option, Map<Interest, List<User>>> usersCommonInterest = new LinkedHashMap<>();

        Set<Interest> sortedInterests = user.getInterests().stream()
                .sorted(Comparator.comparingLong(Interest::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        user.setInterests(sortedInterests);

//        Set<User> usersWithSameInterest = new LinkedHashSet<>();

        user.getInterests().forEach(interest -> {
            List<User> usersWithSameInterest = userService.findUsersByInterestsContains(interest);
                question.getOptions().forEach(option -> {
                    List<User> sameVotedUsers = new ArrayList<>();
                    List<Vote> votes = voteService.findVotesByQuestionAndOption(question, option);

                    votes.stream()
                            .filter(vote -> usersWithSameInterest.contains(vote.getUser()))
                            .collect(Collectors.toList())
                            .forEach(vote -> sameVotedUsers.add(vote.getUser()));

                    if (!sameVotedUsers.isEmpty()) {
                        Map<Interest, List<User>> interestListMap = new LinkedHashMap<>();
                        interestListMap.put(interest, sameVotedUsers);
                        usersCommonInterest.put(option, interestListMap);
                    }
                });
        });
*/

        // Block with for every option, find All users, sharing same Interest
        List<Interest> allInterest = interestService.findAll();
        Map<Option, Map<Interest, Set<User>>> usersCommonInterest = new LinkedHashMap<>();
        // For every Option of Question
        for (Option option : question.getOptions()) {
            // If option has 1 or less vote, then no need to process this option
            if (option.getVoteCount() <= 1) {
                continue;
            }
            Map<Interest, Set<User>> sameInterestUsers = new LinkedHashMap<>();
            // Find votes for every Option
            List<Vote> votes = voteService.findVotesByQuestionAndOption(question, option);
            // For every Interest
            for (Interest interest : allInterest) {
                // If this interest have 1 or less users, exit loop
                if (interest.getUsers().size() <= 1) {
                    continue;
                }
                Set<User> commonInterestUsers = new LinkedHashSet<>();
                for (Vote vote : votes) {
                    // Check, if voted User have this Interest
                    if (vote.getUser().getInterests().contains(interest)) {
                        commonInterestUsers.add(vote.getUser());
                    }
                }
                if (commonInterestUsers.size() > 1) {
                    sameInterestUsers.put(interest, commonInterestUsers);
                }
            }
            usersCommonInterest.put(option, sameInterestUsers);
        }
        // Block with for every option, find All users, sharing same Interest

        log.info(String.valueOf(usersCommonInterest));
        model.addAttribute("question", question);
        model.addAttribute("statisticsByGroupMates", usersVotedOptions);
        model.addAttribute("statisticsByInterests", usersCommonInterest);
        return "questions/full-statistics";
    }
}
