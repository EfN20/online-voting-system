package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
}
