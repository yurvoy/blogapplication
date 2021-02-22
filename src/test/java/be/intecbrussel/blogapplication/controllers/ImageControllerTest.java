package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.ImageService;
import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    UserService userService;

    ImageController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = new ImageController(imageService, userService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void showUploadForm() throws Exception {
        User user = new User();
        user.setId(1L);

        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/user/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).findById(anyLong());
    }

    @Test
    void handleImage() {
    }

    @Test
    void renderImageFromDB() {
    }
}