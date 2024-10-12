package shivani.springboot_5.Model;

import lombok.Data;

@Data
public class PasswordModel {

    private String Email;
    private String oldPassword;
    private String newPassword;

}
