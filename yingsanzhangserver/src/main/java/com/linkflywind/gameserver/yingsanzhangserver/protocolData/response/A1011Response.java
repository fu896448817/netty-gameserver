package com.linkflywind.gameserver.yingsanzhangserver.protocolData.response;


import com.linkflywind.gameserver.yingsanzhangserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1011Response {
    private double zhuoMain;
    YingSanZhangPlayer[] players;
}
