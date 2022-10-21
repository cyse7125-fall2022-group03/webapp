package cyse7125.fall2022.group03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cyse7125.fall2022.group03.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    boolean existsByEmail(String email);

    User findByEmail(String email);
    
}
