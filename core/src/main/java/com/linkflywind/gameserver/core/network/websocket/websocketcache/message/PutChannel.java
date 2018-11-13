/*
 * @author   作者: qugang
 * @E-mail   邮箱: qgass@163.com
 * @date     创建时间：2018/1/22
 * 类说明     存储socket channel
 */
package com.linkflywind.gameserver.core.network.websocket.websocketcache.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.UnicastProcessor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutChannel {
    private String webSocketSessionId;
    private UnicastProcessor<WebSocketMessage> unicastProcessor;
}