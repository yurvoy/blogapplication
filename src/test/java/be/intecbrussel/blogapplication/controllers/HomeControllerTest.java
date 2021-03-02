package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class HomeControllerTest {


    @InjectMocks
    WebConfig webConfig;

    @Mock
    UserService userService;

    @Mock
    PostService postService;

    @Mock
    Model model;

    @Mock
    Principal principal;

    MockMvc mockMvc;

    HomeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new HomeController(userService,postService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(webConfig.viewResolver())
                .build();
    }


    @Test
    void guestUserRoot() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void loggedInUserRoot() {

        User user = new User();

        when(userService.findById(anyLong())).thenReturn(user);

        String viewName = controller.root(principal, model);

        assertEquals("index",viewName);
    }

    @Test
    void login() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }


    @Test
    void notFoundError() throws Exception {

        mockMvc.perform(post("/404").contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

}