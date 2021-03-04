package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.QuestionNotFoundException;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.repositories.QuestionRepository;
import kz.astanait.edu.votingsystem.services.interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
}
