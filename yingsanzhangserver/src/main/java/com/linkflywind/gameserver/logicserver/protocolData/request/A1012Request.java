package com.linkflywind.gameserver.logicserver.protocolData.request;

import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.room.message.baseMessage.UnhandledMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Protocol(1012)
public class A1012Request extends UnhandledMessage {
    private String name;
    private String roomId;
}
