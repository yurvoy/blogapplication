package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.services.PostService;
import be.intecbrussel.blogapplication.web_security_config.WebConfig;
import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
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

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).email("abc@gmail.com").password("abcdef").build();

        MockitoAnnotations.openMocks(this);
        controller = new HomeController(userService, postService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(webConfig.viewResolver())
                .build();
    }


    @Test
    void guestUserRoot() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/frontpage"));
    }


    @Test
    void loggedInUserRoot() throws Exception {

        User user = new User();
        user.setId(1L);

        when(userService.findById(anyLong())).thenReturn(user);

        // misschien op deze manier??
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/frontpage"));
        //

        String viewName = controller.root(principal, model);

        assertEquals("user/frontpage",viewName);
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