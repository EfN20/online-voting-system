package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.Option;
import kz.astanait.edu.votingsystem.models.Question;
import kz.astanait.edu.votingsystem.models.User;
import kz.astanait.edu.votingsystem.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findVoteByUserAndOptionAndQuestion(User user, Option option, Question question);
    List<Vote> findTop5ByUserOrderByIdDesc(User user);
    List<Vote> findVotesByUser(User user);
    Long countByQuestion(Question question);
    Long countByOption(Option option);
}
