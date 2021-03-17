package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.web_security_config.WebConfig;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
class UserControllerTest {

    @InjectMocks
    WebConfig webConfig;

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    User user;

    Principal mockPrincipal;


    @BeforeEach
    void setUp() {
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("abc@gmail.com");

        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).email("abc@gmail.com").password("abcdef").build();
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setViewResolvers(webConfig.viewResolver())
                .build();
    }

    @Test
    void updateProfile() throws Exception {

        when(userService.findById(1L)).thenReturn(user);
        when(userService.findByEmail(any())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/" + user.getId() +"/edit")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("user/updateProfile"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    void processUpdateProfile() throws Exception {

        mockMvc.perform(post("/user/" + user.getId() +"/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/" + user.getId() + "/profile"));
    }

    @Test
    void showProfile() throws Exception {

        when(userService.findById(1L)).thenReturn(user);
        when(userService.findByEmail(any())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/1/profile")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"));

    }


}