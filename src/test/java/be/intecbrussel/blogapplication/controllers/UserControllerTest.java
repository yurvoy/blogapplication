package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    User user;

    @BeforeEach
    void setUp() {

        user = User.builder().id(1L).email("abc@gmail.com").password("abcdef").build();
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void updateProfile() throws Exception {
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/user/" + user.getId() +"/edit"))
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

        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/user/1/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/profile"));

    }


}