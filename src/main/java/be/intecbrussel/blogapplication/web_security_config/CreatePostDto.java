package be.intecbrussel.blogapplication.web_security_config;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CreatePostDto {

    @Size(max = 70, min = 1, message = "Title : 70 max characters")
    private String postTitle;

    @Size(max = 1000, min = 1, message = "Post : 1000 max characters")
    private String postText;

    @Size(max = 1000, message = "Post : 1000 max characters")
    private String embedURL;

    @Size(max = 10, message = "Tag: 10 max tags")
    private List<String> tags;

}
