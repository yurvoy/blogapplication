package be.intecbrussel.blogapplication.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    private Post post;

    @Lob
    @Column
    private String commentText;

    @Column
    private LocalDateTime commentTimeStamp;


    public Comment(User user, Post post, String commentText, LocalDateTime commentTimeStamp) {
        this.user = user;
        this.post = post;
        this.commentText = commentText;
        this.commentTimeStamp = commentTimeStamp;
    }


    public Comment() {
    }
}