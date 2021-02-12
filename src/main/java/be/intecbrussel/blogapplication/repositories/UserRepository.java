package be.intecbrussel.blogapplication.repositories;

import be.intecbrussel.blogapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
