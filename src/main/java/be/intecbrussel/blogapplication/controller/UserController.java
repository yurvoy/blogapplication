package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;
    private final String UPDATE_USER = "users/updateProfile.html";
    private final String USER_PROFILE = "/users/profile.html";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"users/{userId}/edit"})
    public String updateProfile(@PathVariable Long userId, Model model){

        model.addAttribute("profileUpdate", userService.findById(userId));
        return UPDATE_USER;
    }

    @PostMapping("users")
    public String processUpdateProfile(@Valid @ModelAttribute("user")User user, BindingResult result){
      if(result.hasErrors()){
          result.getAllErrors().forEach(objectError -> {
              log.debug(objectError.toString());
          });
          return UPDATE_USER;
      }
      User userToSave = userService.save(user);
      return "redirect:/users/" + userToSave.getId() +  "/profile.html";
    }

    @GetMapping("users/{userId}/profile.html")
    public String showProfile(@PathVariable Long userId, Model model) {

        User user = userService.findById(1L);
        model.addAttribute("view", "users/profile");
        model.addAttribute("user", user);
        return USER_PROFILE;
    }

}
