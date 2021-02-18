package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping({"user/{userId}/edit"})
    public String updateProfile(@PathVariable Long userId, Model model){

        model.addAttribute("user", userService.findById(userId));
        return "user/updateProfile.html";
    }

    @PostMapping({"user/{userId}/edit"})
    public String processUpdateProfile(Principal principal, @ModelAttribute("user") User userForm){
        User user = userRepository.findByEmail(principal.getName());

        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setUserBio(userForm.getUserBio());
        user.setProfileImage(userForm.getProfileImage());
        user.setBirthday(userForm.getBirthday());
        user.setGender(userForm.getGender());

        userRepository.save(user);
        return "user/index";
    }

    @GetMapping("user/{userId}/profile")
    public String showProfile(@PathVariable Long userId, Model model) {

        User user = userService.findById(userId);
        user.setUserBio(userService.updateBio(userId, user.getUserBio()));
        model.addAttribute("view", "user/profile");
        model.addAttribute("user", user);
        return "/user/profile.html";
    }


}
