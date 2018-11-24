package com.linkflywind.gameserver.core.network.websocket;

import com.linkflywind.gameserver.core.security.JwtTokenUtil;
import io.netty.channel.ChannelId;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.yeauty.annotation.OnBinary;
import org.yeauty.annotation.OnClose;
import org.yeauty.annotation.OnError;
import org.yeauty.annotation.OnOpen;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

public abstract class GameWebSocket {

    private final RedisTemplate redisTemplate;
    protected final ValueOperations<String, GameWebSocketSession> valueOperationsByGameWebSocketSession;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static DefaultChannelGroup defaultChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final AttributeKey<String> channelNameKey = AttributeKey.valueOf("WEBSOCKET_GAME_ID");

    private final JwtTokenUtil jwtTokenUtil;

    public GameWebSocket(RedisTemplate redisTemplate, JwtTokenUtil jwtTokenUtil) {
        this.redisTemplate = redisTemplate;
        this.valueOperationsByGameWebSocketSession = redisTemplate.opsForValue();
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
        String id = parameterMap.getParameter("id");
        String token = parameterMap.getParameter("token");


        if (jwtTokenUtil.validateToken(id, token)) {

            defaultChannelGroup.add(session.channel());

            Attribute<String> attributeName = session.channel().attr(channelNameKey);
            attributeName.set(id);

            GameWebSocketSession gameWebSocketSession = this.valueOperationsByGameWebSocketSession.get(id);
            if (gameWebSocketSession != null) {
                gameWebSocketSession.setSessionId(session.id());
                gameWebSocketSession.setState("0");
                this.valueOperationsByGameWebSocketSession.set(id, gameWebSocketSession);
            } else {
                gameWebSocketSession = new GameWebSocketSession(id,
                        session.id(),
                        token,
                        Instant.now().toString(),
                        "",
                        "0",
                        session.remoteAddress().toString(),
                        Optional.empty(),
                        Optional.empty());
                this.valueOperationsByGameWebSocketSession.set(id, gameWebSocketSession);
            }
            openHandle(gameWebSocketSession);
        } else {
            session.close();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        Attribute<String> attributeName = session.channel().attr(channelNameKey);

        GameWebSocketSession gameWebSocketSession = valueOperationsByGameWebSocketSession.get(attributeName.get());
        gameWebSocketSession.setState("1");
        valueOperationsByGameWebSocketSession.set(attributeName.get(), gameWebSocketSession);

        closeHandle(gameWebSocketSession);
        logger.info("close client:" + session.remoteAddress().toString());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("close error:" + session.remoteAddress().toString(), throwable);
    }


    @OnBinary
    public void onBinary(Session session, byte[] bytes) {
        int channel = (int) bytes[0];
        int protocol = (bytes[1] << 8 & bytes[2]);
        Optional<byte[]> buffer = Optional.empty();
        if (bytes.length > 3)
            buffer = Optional.of(Arrays.copyOfRange(bytes, 2, bytes.length - 1));

        Attribute<String> attributeName = session.channel().attr(channelNameKey);

        GameWebSocketSession gameWebSocketSession = valueOperationsByGameWebSocketSession.get(attributeName.get());

        receiveHandle(gameWebSocketSession,
                channel,
                protocol,
                buffer
        );
    }

    protected abstract boolean receiveHandle(GameWebSocketSession session, int channel, int protocol, Optional<
            byte[]> buffer);

    protected abstract void openHandle(GameWebSocketSession session);

    protected abstract void closeHandle(GameWebSocketSession session);

    public static void send(ChannelId sessionId,byte[] buffer){
        defaultChannelGroup.find(sessionId).writeAndFlush(buffer);
    }

}