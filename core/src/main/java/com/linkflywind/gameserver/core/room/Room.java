package com.linkflywind.gameserver.core.room;


import com.linkflywind.gameserver.core.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    List<Player> playerList = new ArrayList<>();

    public void join(Player player){
        playerList.add(player);
    }

    public void exit(Player player){
        playerList.removeIf(player1 -> player1.getName().equals(player.getName()));
    }

    public void exit(String name){
        playerList.removeIf(player1 -> player1.getName().equals(name));
    }

}
