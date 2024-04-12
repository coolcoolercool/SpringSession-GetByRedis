package org.example.redissession.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        //序列化配置
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        serializer.setObjectMapper(mapper);
        return serializer;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        RedisSerializer<String> stringRedisSerialize = new StringRedisSerializer();

        Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        template.setConnectionFactory(factory);
        //key序列化方式
        template.setKeySerializer(stringRedisSerialize);
        //value序列化
        template.setValueSerializer(jsonRedisSerializer);
        //template.setValueSerializer(stringRedisSerialize);

        // 如果是value 是json序列化，那么直接通过key获取，返回的是一个 HashMap 结构
        // template.setHashKeySerializer(stringRedisSerialize);
        // template.setHashValueSerializer(jsonRedisSerializer);

        // 如果是value 是String序列化，那么直接通过key获取，返回得是一个 json 字符串
        // key haspmap序列化
        template.setHashKeySerializer(stringRedisSerialize);
        //value hashmap序列化
        template.setHashValueSerializer(jsonRedisSerializer);

        return template;
    }
}