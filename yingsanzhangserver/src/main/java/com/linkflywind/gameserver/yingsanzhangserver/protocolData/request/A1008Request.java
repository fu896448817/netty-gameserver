package com.linkflywind.gameserver.yingsanzhangserver.protocolData.request;

import com.linkflywind.gameserver.core.annotation.Protocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Protocol(1006)
public class A1008Request {
    String roomId;
    String toName;
}
