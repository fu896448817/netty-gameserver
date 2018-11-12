package com.linkflywind.gameserver.loginserver.controller.from;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOutForm {
    private String name;
    private String token;
}
