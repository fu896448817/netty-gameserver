package com.linkflywind.gameserver.loginserver.controller.from;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm {
    private String name;
    private String password;
    private String sex;
    private String confirmPassword;
    private String mobileNumber;
    private String verificationCode;
    private String sponsor;
}
