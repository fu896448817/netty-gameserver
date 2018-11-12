package com.linkflywind.gameserver.zhajinhua.redisModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectorData implements Serializable {
    private String name;
    private String sessionId;
    private int protocol;
    private byte[] data;
}
