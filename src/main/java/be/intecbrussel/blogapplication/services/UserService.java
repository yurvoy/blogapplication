package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.web.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
public interface UserService extends UserDetailsService{
    User findByEmail(String email);

    User findById(Long id);

    User save(UserRegistrationDto registration);

    void updateProfile(Long userId, Principal principal, User userForm);

}
