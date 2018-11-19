/*
* @author   作者: qugang
* @E-mail   邮箱: qgass@163.com
* @date     创建时间：2018/11/19
* 类说明     准备响应
*/
package com.linkflywind.gameserver.logicserver.protocolData;

import com.linkflywind.gameserver.logicserver.player.YingSanZhangPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class A1009Response {
    YingSanZhangPlayer player;
}
