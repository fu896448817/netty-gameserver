package com.linkflywind.gameserver.logicserver.protocolData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1007Request {
    String roomId;
    String name;
    int chouma;
    String type;

}
