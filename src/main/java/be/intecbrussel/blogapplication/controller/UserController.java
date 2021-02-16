package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.builder.UserBuilder;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/users")
@Slf4j
@Controller
public class UserController {

    private final String CREATE_USER = "users/createUserForm.html";
    private final String UPDATE_USER = "users/updateUserForm.html";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newUserForm(Model model){
        model.addAttribute("user", new UserBuilder().build());
        return CREATE_USER;
    }



    @PostMapping("/new")
    public String processNewForm(@Valid User user, BindingResult result){

        if(result.hasErrors()){
            return CREATE_USER;
        }else{
            User savedUser = userService.save(user);
            return UPDATE_USER;
        }
    }

    @GetMapping("/{userId}/edit")
    public String updateUserForm(@PathVariable Long userId, Model model, Principal principal){

/*
        model.addAttribute("userUpdate", userService.findById(userId));
        return UPDATE_USER;

 */

        model.addAttribute("userUpdate", userService.findById(userId));
        if(principal != null) {
            return UPDATE_USER;
        }
        return CREATE_USER;
    }

    @PostMapping("/{userId}/edit")
    public String processUpdateUserForm(@Valid @ModelAttribute("user") User user, BindingResult result, @PathVariable Long userId){

        if(result.hasErrors()){
            return CREATE_USER;
        }else{
            user.setId(userId);
            User savedUser = userService.save(user);
            return "redirect:/users/" + savedUser.getId();
        }
    }




}
