package com.linkflywind.gameserver.logicserver.protocolData.request;

import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.room.message.baseMessage.UnhandledMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Protocol(1001)
public class A1001Request extends UnhandledMessage {
    private GameWebSocketSession session;
    private String roomNumber;
}
