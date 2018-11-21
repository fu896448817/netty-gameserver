package com.linkflywind.gameserver.logicserver.protocolData.request;

import com.linkflywind.gameserver.core.room.message.baseMessage.GameInitMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1004Request extends GameInitMessage {
    private String name;
    private String roomId;
}
