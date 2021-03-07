package kz.astanait.edu.votingsystem.contollers.utils;

import kz.astanait.edu.votingsystem.models.Interest;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.models.Vote;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ControllerUtil {

    /**
     * Method, which getting and sorting by Id all question,
     * for each question, sorting their own option by Id.
     *
     * @param user Current signed User
     * @param questionService to get All Question
     * @param voteService to get All Votes
     * @return Map, which as key containing Question and as value Boolean
     * True if Current User voted for this Question
     * False if Current User did not vote for this Question
     */
    public static Map<Question, Boolean> getQuestionBooleanMap(User user, QuestionService questionService,
                                                               VoteService voteService) {
        List<Question> questionList = questionService.findAll();

        questionList = questionList.stream().
                sorted(Comparator.comparingLong(Question::getId)).
                collect(Collectors.toList());

        questionList.forEach(question -> {
            Set<Option> sortedOptions = question.getOptions().stream()
                    .sorted(Comparator.comparingLong(Option::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            question.setOptions(sortedOptions);
        });

        List<Vote> voteList = voteService.findVotesByUser(user);
        Map<Question, Boolean> questionBooleanMap = new LinkedHashMap<>();

        if (voteList.isEmpty()) {
            questionList.forEach(question -> questionBooleanMap.put(question, false));
        } else {
            for (Question question : questionList) {
                for (Vote vote : voteList) {
                    if (vote.getQuestion() == question) {
                        questionBooleanMap.put(question, true);
                        break;
                    }
                    questionBooleanMap.put(question, false);
                }
            }
        }

        return questionBooleanMap;
    }

    /**
     * Method, which finding all users,
     * Who voted for this question,
     * And mapping by Option and common Interests of Current User
     * @param question Which statistics want to see
     * @param allOptionMap Map, which contain Option,
     *                     And Interest of Current User
     *                     And sharing them Users
     * @param currentUser Current signed User
     * @param voteService To find all votes by Question and Option
     */
    public static void getMapByQuestionAllOption(Question question, Map<Option, Map<Interest, Set<User>>> allOptionMap,
                                                 User currentUser, VoteService voteService) {
        // For every Option of Question
        for (Option option : question.getOptions()) {
            // If option has 1 or less vote, then no need to process this option
            if (option.getVoteCount() < 1) {
                continue;
            }
            Map<Interest, Set<User>> sameInterestUsers = new LinkedHashMap<>();
            // Find votes for every Option
            List<Vote> votes = voteService.findVotesByQuestionAndOption(question, option);
            // For every Interest of Current User
            for (Interest interest : currentUser.getInterests()) {
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
                if (commonInterestUsers.size() == 1 && commonInterestUsers.stream().findFirst().get().equals(currentUser)) {

                }
                else if (commonInterestUsers.size() > 0) {
                    sameInterestUsers.put(interest, commonInterestUsers);
                }
            }
            if (sameInterestUsers.size() > 0) {
                allOptionMap.put(option, sameInterestUsers);
            }
        }
    }

    /**
     * Method, which finding Group mates of Current User,
     * And their voted Options for Question
     * @param question Which statistics want to see
     * @param usersVotedOptions Map, which contains Option,
     *                          And Users, who voted for this Option
     * @param groupMates Users, who is group mates for Current User
     * @param voteService To find all votes by Question and Option
     */
    public static void getUsersVotedOptions(Question question, Map<Option, List<User>> usersVotedOptions,
                                            List<User> groupMates, VoteService voteService) {
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
    }
}
