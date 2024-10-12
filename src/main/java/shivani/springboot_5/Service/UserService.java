package shivani.springboot_5.Service;

import org.springframework.security.core.userdetails.User;
import shivani.springboot_5.Entity.UserEntity;
import shivani.springboot_5.Entity.Varification;
import shivani.springboot_5.Model.UserModel;

import java.util.Optional;

public interface UserService {
    UserEntity registerUser(UserModel usermodel);

    void saveVarificationTokenForUser(String token, UserEntity user);

    String verification_registrition_valid_token(String token);

    Varification generateNewVarificationToken(String oldToken);

    UserEntity findUserByEmail(String email);

    void createPasswordResentTokenForUser(UserEntity userEntity, String token);

    String validatePasswordResetToken(String token);

    Optional<UserEntity> getUserByPasswordResetToken(String token);

    void changePassword(UserEntity userEntity, String newPassword);

    boolean checkIfValideOldPassword(UserEntity userEntity, String oldPassword);
}
