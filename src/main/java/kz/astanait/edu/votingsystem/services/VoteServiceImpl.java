package kz.astanait.edu.votingsystem.services;

import kz.astanait.edu.votingsystem.exceptions.VoteNotFoundException;
import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.models.Vote;
import kz.astanait.edu.votingsystem.repositories.VoteRepository;
import kz.astanait.edu.votingsystem.services.interfaces.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository){
        this.voteRepository = voteRepository;
    }

    @Override
    public List<Vote> findTop5ByUserOrderByIdDesc(User user) {
        return voteRepository.findTop5ByUserOrderByIdDesc(user);
    }

    @Override
    public List<Vote> findVotesByUser(User user) {
        return voteRepository.findVotesByUser(user);
    }

    @Override
    public List<Vote> findVotesByQuestionAndOption(Question question, Option option) {
        return voteRepository.findVotesByQuestionAndOption(question, option);
    }

    @Transactional
    @Override
    public void deleteAllByOption(Option option) {
        voteRepository.deleteAllByOption(option);
    }

    @Transactional
    @Override
    public void deleteAllByQuestion(Question question) {
        voteRepository.deleteAllByQuestion(question);
    }

    @Override
    public Vote findById(Long id) {
        return voteRepository.findById(id).orElseThrow(VoteNotFoundException::new);
    }

    @Override
    public Vote save(Vote entity) {
        return voteRepository.save(entity);
    }

    @Override
    public void delete(Vote entity) {
        voteRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        voteRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return voteRepository.count();
    }

    @Override
    public List<Vote> findAll() {
        return voteRepository.findAll();
    }
}
