package be.intecbrussel.blogapplication.controllers;

<<<<<<< HEAD
<<<<<<< HEAD
=======
import be.intecbrussel.blogapplication.model.User;
>>>>>>> user-controller-test
=======
import be.intecbrussel.blogapplication.model.User;
>>>>>>> user-controller-test
import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
<<<<<<< HEAD
<<<<<<< HEAD
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
=======
=======
>>>>>>> user-controller-test

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

<<<<<<< HEAD
import java.security.Principal;

=======
>>>>>>> user-controller-test
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
<<<<<<< HEAD
>>>>>>> user-controller-test
=======
>>>>>>> user-controller-test
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

<<<<<<< HEAD
<<<<<<< HEAD
    @BeforeEach
    void setUp() {

        userController = new UserController(userService);

=======
=======
>>>>>>> user-controller-test
    User user;

    @BeforeEach
    void setUp() {

        user = User.builder().id(1L).email("abc@gmail.com").password("abcdef").build();
        userController = new UserController(userService);
<<<<<<< HEAD
>>>>>>> user-controller-test
=======
>>>>>>> user-controller-test
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void updateProfile() throws Exception {
<<<<<<< HEAD
<<<<<<< HEAD

        mockMvc.perform(get("/user/1/edit"))
                .andExpect(status().isOk());

=======
=======
>>>>>>> user-controller-test
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/user/" + user.getId() +"/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/updateProfile"))
                .andExpect(model().attributeExists("user"));
<<<<<<< HEAD
>>>>>>> user-controller-test
=======
>>>>>>> user-controller-test

    }

    @Test
    void processUpdateProfile() throws Exception {

<<<<<<< HEAD
<<<<<<< HEAD
        mockMvc.perform(get("/user/1/edit"))
                .andExpect(status().isOk());
=======
        mockMvc.perform(post("/user/" + user.getId() +"/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/" + user.getId() + "/profile"));
>>>>>>> user-controller-test
=======
        mockMvc.perform(post("/user/" + user.getId() +"/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/" + user.getId() + "/profile"));
>>>>>>> user-controller-test
    }

    @Test
    void showProfile() throws Exception {
<<<<<<< HEAD
<<<<<<< HEAD
        mockMvc.perform(get("/user/1/profile"))
                .andExpect(status().isOk());
=======
=======
>>>>>>> user-controller-test

        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/user/1/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/profile"));
<<<<<<< HEAD
>>>>>>> user-controller-test
=======
>>>>>>> user-controller-test

    }


}