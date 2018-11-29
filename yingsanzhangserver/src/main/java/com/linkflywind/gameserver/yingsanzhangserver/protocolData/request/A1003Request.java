package com.linkflywind.gameserver.yingsanzhangserver.protocolData.request;


import com.linkflywind.gameserver.core.annotation.Protocol;
import com.linkflywind.gameserver.core.room.message.baseMessage.RoomMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Protocol(1003)
public class A1003Request implements RoomMessage {
    private int playerLowerlimit;
    private int playerUpLimit;
    private  int xiaZhuTop;
    private int juShu;
}
