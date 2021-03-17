package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.Post;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"user/{userId}/edit"})
    public String updateProfile(@PathVariable Long userId, Model model, Principal principal){

        User existing = userService.findById(userId);
        User visitor = userService.findByEmail(principal.getName());
        if (existing == null || existing != visitor) {
            return "redirect:/index";
        }

        model.addAttribute("user", userService.findById(userId));
        return "user/updateProfile";
    }

    @PostMapping({"user/{userId}/edit"})
    public String processUpdateProfile(@PathVariable Long userId, Principal principal, @ModelAttribute("user") User userForm){

        userService.updateProfile(userId, principal, userForm);
        return "redirect:/user/" + userId + "/profile";
    }


    @GetMapping("user/{userId}/profile")
    public String showProfile(@PathVariable Long userId, Model model, Principal principal) {

        User userProfile = userService.findById(userId);
        User userVisitor = userService.findByEmail(principal.getName());
        model.addAttribute("view", "user/profile");
        model.addAttribute("userVisitor", userVisitor);
        model.addAttribute("user", userProfile);

        List<User> following = userVisitor.getFollowing();

        boolean isFollowing = false;
        for (User followedUser : following) {
            if (followedUser.getEmail().equals(userProfile.getEmail())) {
                isFollowing = true;
                break;
            }
        }

        model.addAttribute("currentUser", userVisitor);
        model.addAttribute("following", isFollowing);



        return "user/profile";
    }

    @GetMapping("/userSearch")
    public String postSearch(Model model, @Param("email") String email, Principal principal){

        List<User> userList = userService.findUsers(email);

        model.addAttribute("email", email);
        model.addAttribute("userList", userList);

        if(principal != null) {
            User user = userService.findByEmail(principal.getName());
            model.addAttribute("user", userService.findById(user.getId()));
        }

        if(userList.isEmpty()){
            return "searchNotFound";
        }
        return "user/frontpage";
    }

    @ModelAttribute("comment")
    public CreateCommentDto CreateCommentDto() {
        return new CreateCommentDto();
    }


    @ModelAttribute("post")
    public CreatePostDto CreatePostDto() {
        return new CreatePostDto();
    }

    @PostMapping(value = "/follow/{userEmail}")
    public String follow(@PathVariable(value="userEmail") String userEmail,
                         HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser();
        User userToFollow = userService.findByEmail(userEmail);
        List<User> followers = userToFollow.getFollowers();

        followers.add(loggedInUser);
        userToFollow.setFollowers(followers);
        userService.save(userToFollow);



        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping(value = "/unfollow/{userEmail}")
    public String unfollow(@PathVariable(value="userEmail") String userEmail, HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser();
        User userToUnfollow = userService.findByEmail(userEmail);
        List<User> followers = userToUnfollow.getFollowers();
        followers.remove(loggedInUser);
        userToUnfollow.setFollowers(followers);
        userService.save(userToUnfollow);

        return "redirect:" + request.getHeader("Referer");
    }



}
