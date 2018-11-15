package com.linkflywind.gameserver.core.redisModel;

import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferData implements Serializable {
    private GameWebSocketSession gameWebSocketSession;
    private String channel;
    private int protocol;
    private Optional<byte[]> data;
}
