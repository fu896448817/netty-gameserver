/*
 * @author   作者: qugang
 * @E-mail   邮箱: qgass@163.com
 * @date     创建时间：2018/1/22
 * 类说明     存储socket channel
 */
package com.linkflywind.gameserver.core.network.websocket.websocketcache.message;

import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutChannel {
    private String webSocketSessionId;
    private GameWebSocketSession gameWebSocketSession;
}