package com.linkflywind.gameserver.zhajinhua.listen;

import com.linkflywind.gameserver.zhajinhua.DispatcherAction;
import com.linkflywind.gameserver.zhajinhua.redisModel.ConnectorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisListen {

    @Autowired
    private DispatcherAction dispatcherAction;

    @Autowired
    private ReactiveRedisOperations<String, ConnectorData> reactiveRedisOperations;


    @Value("${zhajinhua.name}")
    private String listenName;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostConstruct
    private void init() {
        reactiveRedisOperations.listenToChannel(listenName).doOnNext(onNext -> {

            dispatcherAction.createAction(onNext.getMessage().getProtocol()).proccess(onNext.getMessage());

        }).subscribe();
    }

}
