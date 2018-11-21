package com.linkflywind.gameserver.logicserver.protocolData.request;

import com.linkflywind.gameserver.core.room.message.baseMessage.GameRunMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1007Request extends GameRunMessage {
    String roomId;
    String name;
    double chip;
    String type;

}
