package be.intecbrussel.blogapplication.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    @ManyToMany
    private List<User> likes;


    @Builder
    public Post(Long id, String postTitle, String postText, LocalDate postTimeStamp, User user) {
        this.id = id;
        this.postTitle = postTitle;
        this.postText = postText;
        this.postTimeStamp = postTimeStamp;
        this.user = user;
        this.likes = new ArrayList<>();
    }

    public Post(String postTitle, String postText, User user) {
        this.postTitle = postTitle;
        this.postText = postText;
        this.postTimeStamp = getPostTimeStamp();
        this.user = user;
        this.likes = new ArrayList<>();
    }
    public Post() {
    }



    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }


}

