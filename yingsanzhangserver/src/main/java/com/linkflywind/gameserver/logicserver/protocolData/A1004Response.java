package com.linkflywind.gameserver.logicserver.protocolData;

import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1004Response {
    private YingSanZhangPlayer[] gameWebSocketSessions;
    private String roomNumber;
}
