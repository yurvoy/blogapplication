package be.intecbrussel.blogapplication.web_security_config;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreatePostDto {

    @Size(max = 70, min = 1, message = "Title : 70 max characters")
    private String postTitle;

    @Size(max = 1000, min = 1, message = "Post : 500 max characters")
    private String postText;

    @Size(max = 50, min = 1, message = "Tag: 50 max characters")
    private List<String> tags;

}
