package com.smartassist.mechanic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.smartassist.mechanic.model.MechanicLiveState;

@Configuration
public class RedisConfig {

    public static final String MECHANIC_LIVE_STATE_REDIS_TEMPLATE = "mechanicLiveStateRedisTemplate";

    @Bean(MECHANIC_LIVE_STATE_REDIS_TEMPLATE)
    public RedisTemplate<String, MechanicLiveState> mechanicLiveStateRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, MechanicLiveState> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        configureSerializers(redisTemplate);

        return redisTemplate;
    }

    private void configureSerializers(RedisTemplate<String, MechanicLiveState> redisTemplate) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
    }
}
