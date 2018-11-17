package com.linkflywind.gameserver.logicserver.protocolData;

import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1005Response {
    private List<YingSanZhangPlayer> gameWebSocketSessions;
    private YingSanZhangPlayer yingSanZhangPlayer;

}
