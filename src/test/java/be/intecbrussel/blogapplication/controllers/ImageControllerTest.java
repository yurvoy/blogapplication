package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.ImageService;
import be.intecbrussel.blogapplication.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void handleImage() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("fileToUpload","test.txt","text/plain","some text".getBytes());

        mockMvc.perform(multipart("/user/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/user/1/edit"));

        verify(imageService, times(1)).saveImageFile(anyLong(), any());

    }

    @Test
    void renderImageFromDB() {
    }
}