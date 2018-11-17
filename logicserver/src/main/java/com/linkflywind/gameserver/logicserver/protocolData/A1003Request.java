package com.linkflywind.gameserver.logicserver.protocolData;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1003Request {
    private int playerLowerlimit;
    private int playerUpLimit;
}
