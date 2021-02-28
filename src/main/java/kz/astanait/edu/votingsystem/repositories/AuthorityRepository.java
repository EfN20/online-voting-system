package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
