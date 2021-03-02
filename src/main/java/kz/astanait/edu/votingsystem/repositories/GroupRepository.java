package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
