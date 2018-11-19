package com.linkflywind.gameserver.logicserver.protocolData;

import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1008Response {
    YingSanZhangPlayer player;
    YingSanZhangPlayer toPlayer;
}
