package shivani.springboot_5.Controller;

import Event.RegistrationCompleteEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shivani.springboot_5.Entity.UserEntity;
import shivani.springboot_5.Entity.Varification;
import shivani.springboot_5.Model.PasswordModel;
import shivani.springboot_5.Model.UserModel;
import shivani.springboot_5.Service.UserService;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody UserModel usermodel, HttpServletRequest request) {
        UserEntity userEntity = userService.registerUser(usermodel);

        // Generate the application URL
        String applicationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        // Publish the registration complete event
        RegistrationCompleteEvent event = new RegistrationCompleteEvent(userEntity, applicationUrl);
        applicationEventPublisher.publishEvent(event);

        return ResponseEntity.ok("success");
    }
    @GetMapping("verifiyRegistrstion")
    public String verifiyRegistrstion(@RequestParam ("token") String token) {
        String result = userService.verification_registrition_valid_token(token);
        if(result.equalsIgnoreCase("Valid")){
            return "User Verified";
        }
        return "Invalid Token";
    }

    @GetMapping("resendVarificationToken")
    public String resendVarificationToken(@RequestParam ("token") String oldToken, HttpServletRequest request) {
        Varification varification = userService.generateNewVarificationToken(oldToken);
        UserEntity userEntity = varification.getUser();
        resendVarificationTokenMail(userEntity, applicationUrl(request), varification);
        return "Varification Link sent";
    }

    @PostMapping("/resendPassword")
    public String resendPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        UserEntity userEntity = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(userEntity != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResentTokenForUser(userEntity, token);
            url = passwordResetTokenMail(userEntity, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam ("token") String token, @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);

        if(!result.equalsIgnoreCase("Valid")){
            return "Invalid Token";
        }
        Optional<UserEntity> userEntity = userService.getUserByPasswordResetToken(token);

        if(userEntity.isPresent()){
            userService.changePassword(userEntity.get(), passwordModel.getNewPassword());
            return "Password Reset Successfully";
        }
        else{
            return "Invalid Token";
        }

    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        UserEntity userEntity = userService.findUserByEmail(passwordModel.getEmail());
        if(!userService.checkIfValideOldPassword(userEntity, passwordModel.getOldPassword())){
            return "Invalid Old Password";
        }
        userService.changePassword(userEntity, passwordModel.getNewPassword());
        return "Password Changed Successfully";

    }

    private String passwordResetTokenMail(UserEntity userEntity, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;
        log.info("Click the link to Reset your Password : {} "+ url);
        return url;
    }

    private void resendVarificationTokenMail(UserEntity userEntity, String applicationUrl, Varification varification) {
        String url = applicationUrl + "/verifiyRegistrstion?token=" + varification.getToken();
        log.info("Click the link to verify your account : {} "+ url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+  request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}
