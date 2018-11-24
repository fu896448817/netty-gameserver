package com.linkflywind.gameserver.loginserver.controller.protocol;

import com.linkflywind.gameserver.loginserver.controller.config.GameServerList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestResponse {
    private Long id;
    private String name;
    private String nickName;
    private String token;
    private double balance;
    private int cardNumber;

    private List<GameServerList> gameServerLists;
}
