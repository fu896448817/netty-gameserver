package com.linkflywind.gameserver.logicserver.protocolData.request;

import com.linkflywind.gameserver.core.room.message.baseMessage.UnhandledMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1012Request extends UnhandledMessage {
    private String name;
    private String roomId;
}
