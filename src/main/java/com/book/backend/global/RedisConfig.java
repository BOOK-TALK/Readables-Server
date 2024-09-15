package com.book.backend.global;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.trace("RedisConfig > redisConnectionFactory() > host: {}, port: {}, password: {}", host, port, password);
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
        redisConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        log.trace("RedisConfig > redisTemplate() > host: {}, port: {}, password: {}", host, port, password);

        // redisTemplate를 받아와서 set, get, delete를 사용
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        // setKeySerializer, setValueSerializer 설정
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @PostConstruct
    public void test(){
        log.trace("!!!!!! RedisConfig > test() > host: {}, port: {}, password: {}", host, port, password);
    }
}
