package com.linkflywind.gameserver.loginserver.controller.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServerList {
    private String ip;
    private String port;
    private String name;
}
