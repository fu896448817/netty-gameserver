package com.linkflywind.gameserver.core.network.websocket;

import akka.pattern.Patterns;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.WebSocketCacheActorManager;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.PopChannel;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.PutChannel;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.RemoveChannel;
import com.linkflywind.gameserver.core.network.websocket.websocketcache.message.TellPopChannel;
import com.linkflywind.gameserver.core.security.JwtTokenUtil;
import io.netty.handler.codec.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yeauty.annotation.OnBinary;
import org.yeauty.annotation.OnClose;
import org.yeauty.annotation.OnError;
import org.yeauty.annotation.OnOpen;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;
import scala.concurrent.Future;
import scala.reflect.ClassTag;
import scala.reflect.ClassTag$;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

public abstract class GameWebSocket {

    private final WebSocketCacheActorManager webSocketCacheActorManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public GameWebSocket(WebSocketCacheActorManager webSocketCacheActorManager) {
        this.webSocketCacheActorManager = webSocketCacheActorManager;
    }


    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {

        String name = parameterMap.getParameter("name");
        String token = parameterMap.getParameter("token");


        if(JwtTokenUtil.validateToken(name,token)) {
            String sessionId = session.id().asLongText();
            GameWebSocketSession  gameWebSocketSession = new GameWebSocketSession(name,
                    sessionId ,
                    token,
                    Instant.now().toString(),
                    "",
                    "0",
                    session.remoteAddress().toString(),
                    Optional.empty(),
                    session);

            webSocketCacheActorManager
                    .getTcpCacheActor()
                    .tell(new PutChannel(sessionId,gameWebSocketSession ), null);

            openHandle(gameWebSocketSession);
        }
        else
        {
            session.close();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        ClassTag<TellPopChannel> tag = ClassTag$.MODULE$.apply(TellPopChannel.class);
        Future<TellPopChannel> future = Patterns.ask(webSocketCacheActorManager.getTcpCacheActor(),
                new RemoveChannel(session.id().asLongText()),
                3000).mapTo(tag);
        future.foreach(p-> closeHandle(p.getGameWebSocketSession()),webSocketCacheActorManager.getActorSystem().dispatcher());
        logger.info("close client:" + session.remoteAddress().toString());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("close error:" + session.remoteAddress().toString(),throwable);
    }


    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        int channel = (int) bytes[0];
        int protocol = (bytes[1] << 8 & bytes[2]);
        Optional<byte[]> buffer = Optional.empty();
        if(bytes.length > 3)
            buffer = Optional.of(Arrays.copyOfRange(bytes, 2, bytes.length - 1));

        ClassTag<TellPopChannel> tag = ClassTag$.MODULE$.apply(TellPopChannel.class);
        Future<TellPopChannel> future = Patterns.ask(webSocketCacheActorManager.getTcpCacheActor(),
                new PopChannel(session.id().asLongText(),
                        Optional.ofNullable(bytes)),
                3000).mapTo(tag);
        Optional<byte[]> finalBuffer = buffer;
        future.foreach(p-> receiveHandle(p.getGameWebSocketSession(),
                channel,
                protocol,
                finalBuffer
                ),webSocketCacheActorManager.getActorSystem().dispatcher());
    }

    protected abstract boolean receiveHandle(GameWebSocketSession session,int channel,int protocol,Optional<byte[]> buffer);

    protected abstract void openHandle(GameWebSocketSession session);

    protected abstract boolean closeHandle(GameWebSocketSession session);

}