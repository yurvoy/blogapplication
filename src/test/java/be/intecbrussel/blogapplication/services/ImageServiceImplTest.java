package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.controllers.ImageController;
import be.intecbrussel.blogapplication.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
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
        MockitoAnnotations.openMocks(this);

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
    void renderImageFromDB() throws Exception {
        User user = new User();
        user.setId(1L);

        String str = "Fake Image Text";
        Byte[] bytesBoxed = new Byte[str.getBytes().length];

        int i = 0;

        for(byte primByte: str.getBytes()){
            bytesBoxed[i++] = primByte;
        }

        user.setProfileImage(bytesBoxed);

        when(userService.findById(anyLong())).thenReturn(user);

        MockHttpServletResponse response = mockMvc.perform(get("/user/1/profileimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        assertEquals(str.getBytes().length, responseBytes.length);



    }
}