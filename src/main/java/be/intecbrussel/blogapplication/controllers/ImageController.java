package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.ImageService;
import be.intecbrussel.blogapplication.services.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @GetMapping("user/{userId}/image")
    public String showUploadForm(@PathVariable String userId, Model model){

        model.addAttribute("user", userService.findById(Long.valueOf(userId)));

        return "user/uploadImage";
    }

    @PostMapping("user/{userId}/image")
    public String handleImage(@PathVariable String userId, @RequestParam("fileToUpload") MultipartFile file,
                              RedirectAttributes redirectAttributes){

        imageService.saveImageFile(Long.valueOf(userId), file);
        redirectAttributes.addFlashAttribute("message", "You uploaded");
        return "redirect:/user/" + userId + "/edit";
    }

    @GetMapping("user/{id}/profileimage")
    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        User user = userService.findById(Long.valueOf(id));

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
    }
}