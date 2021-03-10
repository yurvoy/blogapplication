package be.intecbrussel.blogapplication.web_security_config;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateCommentDto {

    @Size(max = 500, min = 1, message = "Comment : 500 max characters")
    private String commentText;

}