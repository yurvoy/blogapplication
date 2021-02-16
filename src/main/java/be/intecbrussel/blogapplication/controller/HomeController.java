package be.intecbrussel.blogapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping({"", "/", "index", "index.html"})
    public String index(){

        return "index";
    }
}