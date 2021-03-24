package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import be.intecbrussel.blogapplication.web_security_config.CreateCommentDto;
import be.intecbrussel.blogapplication.web_security_config.CreatePostDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
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

    @ModelAttribute("comment")
    public CreateCommentDto CreateCommentDto() {
        return new CreateCommentDto();
    }


    @ModelAttribute("post")
    public CreatePostDto CreatePostDto() {
        return new CreatePostDto();
    }

    @PostMapping("/follow/{userEmail}")
    public String follow(@PathVariable("userEmail") String userEmail,
                         HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser();
        User userToFollow = userService.findByEmail(userEmail);
        List<User> followers = userToFollow.getFollowers();

        followers.add(loggedInUser);
        userToFollow.setFollowers(followers);
        userService.save(userToFollow);



        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/unfollow/{userEmail}")
    public String unfollow(@PathVariable("userEmail") String userEmail, HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser();
        User userToUnfollow = userService.findByEmail(userEmail);
        List<User> followers = userToUnfollow.getFollowers();
        followers.remove(loggedInUser);
        userToUnfollow.setFollowers(followers);
        userService.save(userToUnfollow);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/users")
    public String getUsers(@RequestParam(value = "filter", required = false) String filter, Model model) {
        List<User> users;

        User loggedInUser = userService.getLoggedInUser();

        List<User> usersFollowing = loggedInUser.getFollowing();
        List<User> usersFollowers = loggedInUser.getFollowers();
        if (filter == null) {
            filter = "all";
        }
        if (filter.equalsIgnoreCase("followers")) {
            users = usersFollowers;
            model.addAttribute("filter", "followers");
        } else if (filter.equalsIgnoreCase("following")) {
            users = usersFollowing;
            model.addAttribute("filter", "following");
        } else {
            users = userService.findAll();
            model.addAttribute("filter", "all");
        }
        model.addAttribute("users", users);

        SetFollowingStatus(users, usersFollowing, model);

        return "users";
    }

    private void SetFollowingStatus(List<User> users, List<User> usersFollowing, Model model) {
        HashMap<String, Boolean> followingStatus = new HashMap<>();
        String email = userService.getLoggedInUser().getEmail();
        for (User user : users) {
            if (usersFollowing.contains(user)) {
                followingStatus.put(user.getEmail(), true);
            } else if (!user.getEmail().equals(email)) {
                followingStatus.put(user.getEmail(), false);
            }
        }
        model.addAttribute("followingStatus", followingStatus);
    }



}
