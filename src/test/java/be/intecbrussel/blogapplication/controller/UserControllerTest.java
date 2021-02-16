package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    Set<User> users;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        users = new HashSet<>();
        userController = new UserController(userService);
        //users.add(User.builder().id(1L).build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void newUserForm() throws Exception {
        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));


    }

    @Test
    void updateUserForm() throws Exception {
        //User user = new User();

        //when(userService.findById(anyLong())).thenReturn(User.builder().id(1L).build());

        mockMvc.perform(get("/users/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));




    }
}