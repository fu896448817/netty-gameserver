package com.linkflywind.gameserver.core.redisModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorData implements Serializable {
    private int channel;
    private String name;
    private String sessionId;
    private int protocol;
    private byte[] data;
}
