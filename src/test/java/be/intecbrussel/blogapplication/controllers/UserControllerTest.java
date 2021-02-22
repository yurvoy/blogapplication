package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        userController = new UserController(userService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void updateProfile() throws Exception {

        mockMvc.perform(get("/user/1/edit"))
                .andExpect(status().isOk());


    }

    @Test
    void processUpdateProfile() throws Exception {

        mockMvc.perform(get("/user/1/edit"))
                .andExpect(status().isOk());
    }

    @Test
    void showProfile() throws Exception {
        mockMvc.perform(get("/user/1/profile"))
                .andExpect(status().isOk());

    }


}