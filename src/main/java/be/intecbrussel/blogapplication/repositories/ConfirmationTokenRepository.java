package be.intecbrussel.blogapplication.repositories;

import be.intecbrussel.blogapplication.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> { }
