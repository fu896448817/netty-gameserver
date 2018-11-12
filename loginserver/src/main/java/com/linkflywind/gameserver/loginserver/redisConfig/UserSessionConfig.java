package com.linkflywind.gameserver.loginserver.redisConfig;


import com.linkflywind.gameserver.loginserver.redisModel.UserSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class UserSessionConfig{
    @Bean
    ReactiveRedisOperations<String, UserSession> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<UserSession> serializer = new Jackson2JsonRedisSerializer<>(UserSession.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, UserSession> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, UserSession> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
