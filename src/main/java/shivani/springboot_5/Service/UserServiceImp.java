package shivani.springboot_5.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shivani.springboot_5.Entity.PasswordResetTokenEntity;
import shivani.springboot_5.Entity.UserEntity;
import shivani.springboot_5.Entity.Varification;
import shivani.springboot_5.Model.UserModel;
import shivani.springboot_5.Repository.PasswordResetTokenRepository;
import shivani.springboot_5.Repository.UserRepository;
import shivani.springboot_5.Repository.VarificationTokenRepository;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    VarificationTokenRepository varificationTokenRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;


    @Override
    public UserEntity registerUser(UserModel usermodel) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(usermodel.getEmail());
        userEntity.setFirstName(usermodel.getFirstName());
        userEntity.setLastName(usermodel.getLastName());
        userEntity.setRole("ROLE_USER");
        userEntity.setPassword(passwordEncoder.encode(usermodel.getPassword()));
        userRepository.save(userEntity);
        return userEntity;
    }

    @Override
    public void saveVarificationTokenForUser(String token, UserEntity user) {
        Varification varification = new Varification(user, token);
        varificationTokenRepository.save(varification);

    }

    @Override
    public String verification_registrition_valid_token(String token) {
        Varification varification = varificationTokenRepository.findByToken(token); // Updated call

        if (varification == null) {
            return "Invalid Token"; // Fix: If no token found, it's invalid
        }

        UserEntity userEntity = varification.getUser();
        Calendar cal = Calendar.getInstance();

        if (varification.getExirationTime().getTime() - cal.getTime().getTime() <= 0) {
            varificationTokenRepository.delete(varification);
            return "Expired Token";
        }

        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        return "Valid";
    }

    @Override
    public Varification generateNewVarificationToken(String oldToken) {
        Varification varification =varificationTokenRepository.findByToken(oldToken);
        varification.setToken(UUID.randomUUID().toString());
        varificationTokenRepository.save(varification);
        return varification;
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResentTokenForUser(UserEntity userEntity, String token) {
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity(userEntity, token);
        passwordResetTokenRepository.save(passwordResetTokenEntity);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token); // Updated call

        if (passwordResetTokenEntity == null) {
            return "Invalid Token";
        }

        UserEntity userEntity = passwordResetTokenEntity.getUser();
        Calendar cal = Calendar.getInstance();

        if (passwordResetTokenEntity.getExirationTime().getTime() - cal.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetTokenEntity);
            return "Expired Token";
        }

        return "Valid";
    }

    @Override
    public Optional<UserEntity> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override

    public void changePassword(UserEntity user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValideOldPassword(UserEntity userEntity, String oldPassword) {
        return passwordEncoder.matches(oldPassword, userEntity.getPassword());
    }


}
