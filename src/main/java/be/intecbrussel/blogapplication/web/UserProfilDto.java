package be.intecbrussel.blogapplication.web;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
})
@Data
public class UserProfilDto {

    private String firstName;

    private String lastName;

    private String password;

    private String confirmPassword;

    private LocalDateTime birthday;

    private String gender;

    private String userBio;

    private Byte[] profileImage;


}
