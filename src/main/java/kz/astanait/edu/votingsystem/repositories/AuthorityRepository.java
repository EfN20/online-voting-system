package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
