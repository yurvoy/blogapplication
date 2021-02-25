package be.intecbrussel.blogapplication.web;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreatePostDto {

    @NotEmpty
    private String postTitle;

    @NotEmpty
    private String postText;

}
