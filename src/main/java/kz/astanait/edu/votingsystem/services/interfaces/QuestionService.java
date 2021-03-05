package kz.astanait.edu.votingsystem.services.interfaces;

import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;

public interface QuestionService extends GenericService<Question> {
    void increaseVoteCount(Question question);
    void addOption(Question question, Option option);
    void deleteOption(Option option);
}
