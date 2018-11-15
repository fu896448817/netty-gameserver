/*
 * @author 作者: qugang
 * @E-mail 邮箱: qgass@163.com
 * @date 创建时间：2018/1/10
 * 类说明     用于返回消息找寻原Socket 为了保证无锁使用actor
 */
package com.linkflywind.gameserver.core.network.websocket.websocketcache;

import akka.actor.UntypedActor;
import com.linkflywind.gameserver.core.network.websocket.GameWebSocketSession;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class WebSocketCacheActor extends UntypedActor {

    private Map<String, GameWebSocketSession> map = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 3个消息，分别为删除，存储，获取webSocket session 为了保证无锁使用actor
     */
    @Override
    public void onReceive(Object message) {
        if (message instanceof PutChannel) {
            PutChannel putChannel = (PutChannel) message;
            map.put(putChannel.getWebSocketSessionId(), putChannel.getGameWebSocketSession());
        } else if (message instanceof RemoveChannel) {
            RemoveChannel removeChannel = (RemoveChannel) message;
            GameWebSocketSession gameWebSocketSession =  map.remove(removeChannel.getWebSocketSessionId());

            getSender().tell(new TellRemoveChannel(gameWebSocketSession.getSessionId(),gameWebSocketSession), getSelf());
        } else if (message instanceof PopChannel) {
            PopChannel popChannel = (PopChannel) message;
            getSender().tell(new TellPopChannel(popChannel.getWebSocketSessionId(),
                    map.get(popChannel.getWebSocketSessionId()),
                    popChannel.getMessage()), getSelf());
        } else {
            logger.warn("not found class type:" + message.getClass());
        }
    }
}
