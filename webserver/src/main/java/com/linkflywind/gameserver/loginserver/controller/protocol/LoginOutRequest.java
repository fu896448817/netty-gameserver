package com.linkflywind.gameserver.loginserver.controller.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOutRequest {
    private String name;
    private String token;
}
