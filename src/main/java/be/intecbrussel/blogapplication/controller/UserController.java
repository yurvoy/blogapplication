package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;
    private final String UPDATE_USER = "users/updateProfile.html";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/{userId}/edit"})
    public String updateProfile(@PathVariable Long userId, Model model){

        model.addAttribute("profileUpdate", userService.findById(userId));
        return UPDATE_USER;
    }

    /*@PostMapping("/{userId/edit")
    public String processUpdateProfile(@Valid @ModelAttribute("user")User user, BindingResult result, @PathVariable Long userId){
        if(result.hasErrors()){
            return UPDATE_USER;
        }else{
            user.setId(userId);
            User savedUser = userService.save(user);
            return UPDATE_USER;
        }
    } */
}
