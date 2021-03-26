package be.intecbrussel.blogapplication.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class SecurityToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String token;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime expireAt;

    @ManyToOne
    private User user;

    public SecurityToken(String token, LocalDateTime createdAt, LocalDateTime expireAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
        this.user = user;
    }

    public SecurityToken(String token) {
        this.token = token;
    }

    public SecurityToken() {}

}
