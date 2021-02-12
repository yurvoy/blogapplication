package be.intecbrussel.blogapplication.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    private String email;
    @Column
    private String password;


    public User() {
    }

    public User(Long id, String email, String password) {
    }


}
