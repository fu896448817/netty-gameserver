package com.linkflywind.gameserver.loginserver.controller.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String nickName;
    private String password;
    private String sex;
    private String confirmPassword;
    private String mobileNumber;
    private String verificationCode;
    private String sponsor;
}
