package com.linkflywind.gameserver.core.network.websocket.websocketcache.message;


import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TellRemoveChannel {
    private String webSocketSessionId;
    private GameWebSocketSession gameWebSocketSession;
}
