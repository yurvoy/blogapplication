package be.intecbrussel.blogapplication.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
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

    public SecurityToken() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }
}
