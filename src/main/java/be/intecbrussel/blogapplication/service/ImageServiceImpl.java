package be.intecbrussel.blogapplication.service;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService{

    private final UserRepository userRepository;

    public ImageServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public void saveImageFile(Long userId, MultipartFile file) {

        try{
            User user = userRepository.findUserById(userId).get();

            Byte[] bytes = new Byte[file.getBytes().length];

            int i = 0;

            for(byte b : file.getBytes()){
                bytes[i++] = b;
            }

            user.setProfileImage(bytes);

            userRepository.save(user);
        } catch (IOException e){
            log.error("upload error!");
            e.printStackTrace();
        }

    }
}
