package be.intecbrussel.blogapplication.web_security_config;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreatePostDto {

    @Size(max = 70, min = 1, message = "Title : 70 max characters")
    private String postTitle;

    @Size(max = 500, min = 1, message = "Post : 500 max characters")
    private String postText;


}
