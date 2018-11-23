package com.linkflywind.gameserver.loginserver.controller.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix="game")
public class GameConfig {
    private List<GameServerList> gameServerLists = new ArrayList<>();

    public List<GameServerList> getGameServerLists() {
        return gameServerLists;
    }

    public void setGameServerLists(List<GameServerList> gameServerLists) {
        this.gameServerLists = gameServerLists;
    }
}
