package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

public class PasswordController {

    @Autowired
    private UserRepository userRepository;

    // Receive the address and send an email
    // TODO
    @PostMapping(value="/forgot-password")
    public void forgotUserPassword(String email) {
    }
}
