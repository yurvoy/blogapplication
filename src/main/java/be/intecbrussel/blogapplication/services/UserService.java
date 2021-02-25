package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByEmail(String email);

    User findById(Long id);

    User save(UserRegistrationDto registration);

    String updateBio(Long id, String userBio);


}
