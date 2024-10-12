package shivani.springboot_5.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    @Column(length = 400)
    private String password;
    private String role;
    private Boolean enabled = false;
}
