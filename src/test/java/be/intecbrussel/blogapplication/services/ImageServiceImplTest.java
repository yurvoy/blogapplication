package be.intecbrussel.blogapplication.services;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @Mock
    UserRepository userRepository;

    ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        imageService = new ImageServiceImpl(userRepository);
    }

    @Test
    void saveImageFile() throws IOException {

        Long id = 1L;
        MultipartFile multipartFile = new MockMultipartFile("fileToUpload", "testing.txt", "text/plain",
                "some test text".getBytes());

        User user = new User();
        user.setId(id);
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(anyLong())).thenReturn(userOptional);
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

        imageService.saveImageFile(id, multipartFile);

        verify(userRepository, times(1)).save(argumentCaptor.capture());

        User savedUser = argumentCaptor.getValue()  ;

        assertEquals(multipartFile.getBytes().length, savedUser.getProfileImage().length);

    }
}