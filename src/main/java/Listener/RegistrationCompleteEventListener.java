package shivani.springboot_5.Listener;

import Event.RegistrationCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import shivani.springboot_5.Entity.UserEntity;
import shivani.springboot_5.Service.UserService;

import java.util.UUID;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private static final Logger log = LoggerFactory.getLogger(RegistrationCompleteEventListener.class);

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        log.info("RegistrationCompleteEvent triggered for user: {}", event.getUser().getEmail());

        UserEntity user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVarificationTokenForUser(token, user);
        String url = event.getApplicationUrl() + "/verifiyRegistrstion?token=" + token;

        log.info("Click the link to verify your account: {}", url);
    }
}
