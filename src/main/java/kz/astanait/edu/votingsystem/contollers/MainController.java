package kz.astanait.edu.votingsystem.contollers;

import kz.astanait.edu.votingsystem.contollers.utils.ControllerUtil;
import kz.astanait.edu.votingsystem.exceptions.UserNotFoundException;
import kz.astanait.edu.votingsystem.hadoop.WordCount;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.services.interfaces.GroupService;
import kz.astanait.edu.votingsystem.services.interfaces.InterestService;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import kz.astanait.edu.votingsystem.services.interfaces.UserService;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileWriter;
import java.security.Principal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class MainController {

    private final UserService userService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final VoteService voteService;
    private final QuestionService questionService;

    @Autowired
    public MainController(UserService userService, GroupService groupService,
                          InterestService interestService, VoteService voteService,
                          QuestionService questionService) {
        this.userService = userService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.voteService = voteService;
        this.questionService = questionService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("interests", interestService.findAll());
        return "registration";
    }

    @GetMapping(value = {"/home", "/"})
    public String getHomePage(Model model, Principal principal) {
        try {
            User user = userService.findUserByNickname(principal.getName());
            Map<Question, Boolean> questionBooleanMap = ControllerUtil.getQuestionBooleanMap(user, questionService, voteService);
            model.addAttribute("questions", questionBooleanMap);

            Map<String, Integer> wordAndCount = new LinkedHashMap<>();
            try {
                File checkInput = new File("src/main/java/kz/astanait/edu/votingsystem/hadoop/input");
                if (checkInput.isDirectory()) {
                    FileUtils.cleanDirectory(checkInput);
                }
                File questionTitles = new File("src/main/java/kz/astanait/edu/votingsystem/hadoop/input/file01.txt");
                if (questionTitles.createNewFile()) {
                    log.info("[HADOOP INPUT] Created file: " + questionTitles.getName());
                    FileWriter fileWriter = new FileWriter(questionTitles);
                    List<Question> questions = questionService.findAll();
                    for (Question questionTmp : questions) {
                        String line = questionTmp.getTitle();
                        line =  line.trim();
                        fileWriter.write(line
                                .replace("?", "")
                                .replaceAll("What", "")
                                .replaceAll("Why", "")
                                .replaceAll("How", "")
                                .replaceAll("Which", "")
                                .replaceAll("is", "")
                                .replaceAll("are", "")
                                .replaceAll("he", "")
                                .replaceAll("she", "")
                                .replaceAll("you", "")
                                .replaceAll("\\s\\s\\s", "") + "\n");
                    }
                    fileWriter.close();
                }

                File checkOutput = new File("src/main/java/kz/astanait/edu/votingsystem/hadoop/output");
                if (checkOutput.isDirectory()) {
                    FileUtils.cleanDirectory(checkOutput);
                    FileUtils.forceDelete(checkOutput);
                }

                WordCount.run();

                File file = new File("src/main/java/kz/astanait/edu/votingsystem/hadoop/output/part-r-00000");
                Scanner reader = new Scanner(file);

                while (reader.hasNext()) {
                    String word = reader.next();
                    Integer count = reader.nextInt();
                    if (wordAndCount.size() < 5) {
                        wordAndCount.put(word, count);
                    } else if(wordAndCount.size() == 5) {
                        for (Map.Entry<String, Integer> entry : wordAndCount.entrySet()) {
                            if (entry.getValue() < count) {
                                wordAndCount.remove(entry.getKey());
                                wordAndCount.put(word, count);
                                break;
                            }
                        }
                    }
                    wordAndCount = wordAndCount.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                }
                wordAndCount.forEach((s, integer) -> {
                    log.info("[WORD] " + s + ": [COUNT] " + integer);
                });
            } catch (Exception e) {
                log.info("[HADOOP GET]" + e.getMessage());
            }

            wordAndCount = wordAndCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            model.addAttribute("topWords", wordAndCount);

        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            return "error/500";
        }
        return "home";
    }
}
