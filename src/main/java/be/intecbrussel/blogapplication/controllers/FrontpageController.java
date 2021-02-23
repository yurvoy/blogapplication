package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.PostService;
import be.intecbrussel.blogapplication.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
public class FrontpageController {

    private static final String USER_FRONTPAGE_URL = "user/frontpage";

    private final UserService userService;
    private final PostService postService;


    public FrontpageController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }


    @GetMapping("/index/frontpage")
    public String root(Model model) {

        log.debug("getting frontpage");


        return "user/frontpage";
    }




    @GetMapping({"/user/{userId}/frontpage"})
    public String showUserFrontPage(@PathVariable Long userId, Model model) {

        //model.addAttribute("view", "user/frontpage");
        model.addAttribute("user", userService.findById(userId));
        return "/user/frontpage";
    }


    @GetMapping({"frontpage/login"})
    public String login(Model model) {
        return "login";
    }

    @GetMapping("frontpage/user")
    public String userIndex() {
        return "user/index";
    }

    @RequestMapping("frontpage/error")
    public String handleError() {
        return "redirect:/user/frontpage";
    }


}
