package kz.astanait.edu.votingsystem.repositories;

import kz.astanait.edu.votingsystem.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
