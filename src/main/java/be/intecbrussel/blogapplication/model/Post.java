package be.intecbrussel.blogapplication.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;

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

    public Post(Long id, String postTitle, String postText) {
        this.postTitle = postTitle;
        this.postText = postText;
    }

    public Post() {
    }

    public Long getId() {
        return id;
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