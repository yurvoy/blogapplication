package be.intecbrussel.blogapplication.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDateTime;
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
    private LocalDateTime postTimeStamp;
    @ManyToOne(fetch=FetchType.LAZY)
    private User user;
    @OneToMany
    private List<Comment> comments;

    @Builder
    public Post(Long id, String postTitle, String postText, LocalDateTime postTimeStamp, User user) {
        this.id = id;
        this.postTitle = postTitle;
        this.postText = postText;
        this.postTimeStamp = postTimeStamp;
        this.user = user;
    }

    public Post(String postTitle, String postText, User user) {
        this.postTitle = postTitle;
        this.postText = postText;
        this.postTimeStamp = getPostTimeStamp();
        this.user = user;

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

