package Event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import shivani.springboot_5.Entity.UserEntity;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private UserEntity user;
    private String applicationUrl;
    public RegistrationCompleteEvent(UserEntity user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
