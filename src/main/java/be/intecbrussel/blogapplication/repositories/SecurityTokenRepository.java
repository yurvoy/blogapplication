package be.intecbrussel.blogapplication.repositories;

import be.intecbrussel.blogapplication.model.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityTokenRepository extends JpaRepository<SecurityToken, Long> {

    SecurityToken findByToken(String Token);
}
