package shivani.springboot_5.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shivani.springboot_5.Entity.Varification;

public interface VarificationTokenRepository extends JpaRepository<Varification, Long> {
    Varification findByToken(String token);
}


