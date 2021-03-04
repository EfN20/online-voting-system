package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.QuestionNotFoundException;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.repositories.QuestionRepository;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    @Override
    public Question save(Question entity) {
        return questionRepository.save(entity);
    }

    @Override
    public void delete(Question entity) {
        questionRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return questionRepository.count();
    }

    @Override
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Transactional
    @Override
    public void increaseVoteCount(Question question) {
            question.setVoteCount(question.getVoteCount() + 1);
    }

    @Transactional
    @Override
    public void addOption(Question question ,Option option) {
        question.addOption(option);
    }

    @Transactional
    @Override
    public void deleteOption(Option option) throws QuestionNotFoundException {
        Question question = questionRepository.findQuestionByOptionsContaining(option).orElseThrow(QuestionNotFoundException::new);
        question.removeOption(option);
    }
}
