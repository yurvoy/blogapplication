package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.web.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    User findByEmail(String email);

    User save(UserRegistrationDto registration);

}
