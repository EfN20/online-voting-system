package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
