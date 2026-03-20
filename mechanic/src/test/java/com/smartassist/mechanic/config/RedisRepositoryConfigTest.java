package com.smartassist.mechanic.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.smartassist.mechanic.repository.MechanicLiveStateRepository;

class RedisRepositoryConfigTest {

    @Test
    void redisConfigShouldRegisterMechanicLiveStateRepository() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(TestRedisConnectionFactoryConfiguration.class, RedisConfig.class);
            context.refresh();

            assertThat(context.getBeansOfType(MechanicLiveStateRepository.class)).hasSize(1);
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
