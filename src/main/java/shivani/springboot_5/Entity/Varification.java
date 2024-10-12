package shivani.springboot_5.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Varification {
    private static final int EXPIRARION_TIME = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private Date exirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "name_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private UserEntity user;

    public Varification(UserEntity user, String token) {
        super();
        this.user = user;
        this.token = token;
        this.exirationTime = calculateExpirationTime(EXPIRARION_TIME);
    }

    public Varification(String token) {
        super();
        this.token = token;
        this.exirationTime = calculateExpirationTime(EXPIRARION_TIME);
    }

    private Date calculateExpirationTime(int expirarionTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expirarionTime);
        return new Date(calendar.getTime().getTime());
    }
}
