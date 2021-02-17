package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.ImageService;
import be.intecbrussel.blogapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    ImageService imageService;

    @InjectMocks
    UserController userController;

    Set<User> users;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        users = new HashSet<>();
        userController = new UserController(userService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void updateProfile() throws Exception {

        User user = new User();
        user.setId(1L);

        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/users/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("profileUpdate"));

    }
}