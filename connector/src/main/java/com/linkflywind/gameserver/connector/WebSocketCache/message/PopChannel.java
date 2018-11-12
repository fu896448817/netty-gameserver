/*
* @author   作者: qugang 
* @E-mail   邮箱: qgass@163.com
* @date     创建时间：2018/1/22
* 类说明     取出socket channel
*/ 
package com.linkflywind.gameserver.connector.WebSocketCache.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopChannel {
    private String webSocketSessionId;
    private byte[] message;
}