package be.intecbrussel.blogapplication.repositories;

import be.intecbrussel.blogapplication.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository <ConfirmationToken, Long> { }
