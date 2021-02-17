package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.ImageService;
import be.intecbrussel.blogapplication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

        model.addAttribute("user", userService.findById(userId));
        return UPDATE_USER;
    }

    @PostMapping("users")
    public String processUpdateProfile(@Valid @ModelAttribute("user")User user, BindingResult result){
      if(result.hasErrors()){
          result.getAllErrors().forEach(objectError -> {
              log.debug(objectError.toString());
          });

          userService.updateBio(user.getId(), user.getUserBio());

          return UPDATE_USER;
      }
        userService.save(user);
      return "redirect:/users/" + user.getId() +  "/profile.html";
    }

    @GetMapping("users/{userId}/profile.html")
    public String showProfile(@PathVariable Long userId, Model model) {

        User user = userService.findById(userId);
        user.setUserBio(userService.updateBio(userId, user.getUserBio()));
        model.addAttribute("view", "users/profile");
        model.addAttribute("user", user);
        return USER_PROFILE;
    }


}
