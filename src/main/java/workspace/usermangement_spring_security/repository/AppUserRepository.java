package workspace.usermangement_spring_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import workspace.usermangement_spring_security.model.AppUser;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByUserName(String username);
}
