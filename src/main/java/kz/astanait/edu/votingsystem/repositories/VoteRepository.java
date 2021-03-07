package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findTop5ByUserOrderByIdDesc(User user);
    List<Vote> findVotesByUser(User user);
    List<Vote> findVotesByQuestionAndOption(Question question, Option option);
    void deleteAllByOption(Option option);
    void deleteAllByQuestion(Question question);
}
