package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.exceptions.UserNotFoundException;

import be.intecbrussel.blogapplication.model.SecurityToken;
import be.intecbrussel.blogapplication.web_security_config.UserRegistrationDto;
import be.intecbrussel.blogapplication.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.util.List;


@Service
public interface UserService extends UserDetailsService{

    User findByEmail(String email);

    User findById(Long id);

    User save(UserRegistrationDto registration);

    void updateProfile(Long userId, Principal principal, User userForm);

    void updateResetPasswordToken(String token, String email) throws UserNotFoundException;

    User getByResetPasswordToken(String token);

    User getByVerifyAccountToken(String token);

    void updatePassword(User user, String newPassword);

    User getLoggedInUser();

    void save(User user);

    List<User> findAll();

}
