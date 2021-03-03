package be.intecbrussel.blogapplication.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "posts")
public class Post implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    private String postTitle;
    @Lob
    @Column
    private String postText;
    @Column
    private LocalDate postTimeStamp;
    @ManyToOne
    private User user;

    @Builder
    public Post(Long id, String postTitle, String postText, LocalDate postTimeStamp, User user) {
        this.id = id;
        this.postTitle = postTitle;
        this.postText = postText;
        this.postTimeStamp = postTimeStamp;
        this.user = user;
    }

    public Post(String postTitle, String postText) {
        this.postTitle = postTitle;
        this.postText = postText;
        this.postTimeStamp = getPostTimeStamp();

    }
    public Post() {
    }



    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}

