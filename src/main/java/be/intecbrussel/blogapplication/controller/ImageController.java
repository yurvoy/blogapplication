package be.intecbrussel.blogapplication.controller;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.service.ImageService;
import be.intecbrussel.blogapplication.service.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private final ImageService imageService;
    private final UserService userService;


    public ImageController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }


    @GetMapping("users/{id}/image")
    public String showUploadForm(@PathVariable Long id, Model model){

        model.addAttribute("user", userService.findById(id));

        return "users/imageupload";
    }

    @PostMapping("users/{userId}/image")
    public String handleImage(@PathVariable Long userId, @RequestParam("file") MultipartFile file){

        imageService.saveImageFile(userId, file);
        return "redirect:/users/" + userId + "/profile";
    }

    @GetMapping("users/{id}/profileimage")
    public void renderImageFromDB(@PathVariable Long id, HttpServletResponse response) throws IOException {
        User user = userService.findById(id);

        if(user.getProfileImage() != null){
            byte[] byteArray = new byte[user.getProfileImage().length];
            int i = 0;

            for (Byte wrappedByte : user.getProfileImage()){
                byteArray[i++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream is  = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
        System.out.println("No profile image");
    }
}
