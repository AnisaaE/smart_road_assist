package com.smartassist.mechanic.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.smartassist.mechanic.model.MechanicLiveState;

class RedisConfigTest {

    @Test
    void mechanicLiveStateRedisTemplateShouldUseJsonValueSerializer() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(TestRedisConnectionFactoryConfiguration.class, RedisConfig.class);
            context.refresh();

            @SuppressWarnings("unchecked")
            RedisTemplate<String, MechanicLiveState> redisTemplate = context.getBean(
                    "mechanicLiveStateRedisTemplate",
                    RedisTemplate.class);

            assertThat(redisTemplate.getKeySerializer()).isInstanceOf(StringRedisSerializer.class);
            assertThat(redisTemplate.getHashKeySerializer()).isInstanceOf(StringRedisSerializer.class);
            assertThat(redisTemplate.getValueSerializer()).isInstanceOf(GenericJackson2JsonRedisSerializer.class);
            assertThat(redisTemplate.getHashValueSerializer()).isInstanceOf(GenericJackson2JsonRedisSerializer.class);
        }
    }

    @Configuration
    static class TestRedisConnectionFactoryConfiguration {

        @Bean
        RedisConnectionFactory redisConnectionFactory() {
            return org.mockito.Mockito.mock(RedisConnectionFactory.class);
        }
    }
}
