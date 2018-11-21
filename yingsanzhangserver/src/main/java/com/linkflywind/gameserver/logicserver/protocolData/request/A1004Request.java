package com.linkflywind.gameserver.logicserver.protocolData.request;

import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.room.message.baseMessage.GameInitMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Protocol(1004)
public class A1004Request extends GameInitMessage {
    private String name;
    private String roomId;
}
