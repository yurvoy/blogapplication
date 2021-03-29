package be.intecbrussel.blogapplication.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private String embedURL;

    @Column
    private LocalDateTime postTimeStamp;

    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    @ManyToMany(fetch=FetchType.LAZY)
    private List<User> likes;
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;


    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> tags;


    @Builder
    public Post(Long id, String postTitle, String postText, LocalDateTime postTimeStamp, User user) {
        this.id = id;
        this.postTitle = postTitle;
        this.postText = postText;
        this.postTimeStamp = postTimeStamp;
        this.user = user;
        this.likes = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public Post(String postTitle, String postText, User user) {
        this.postTitle = postTitle;
        this.postText = postText;
        this.postTimeStamp = getPostTimeStamp();
        this.user = user;
        this.likes = new ArrayList<>();
        this.tags = new ArrayList<>();
    }
    public Post() {
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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

