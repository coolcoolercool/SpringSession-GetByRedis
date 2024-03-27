package org.example.redissession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.redissession.entity.Student;
import org.example.redissession.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;

@SpringBootTest
@Slf4j
class RedisSessionApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private RedisTemplate redisTemplate;

	String hashKey = "hashKey";
	String hashField = "hashField";
	String hashValue = "hashValue";

	String hashSessionKey = "spring:session:sessions:f8c97aff-d424-434a-941e-9bdaeac38f3b";

	@Test
	void testRedis() {
		redisTemplate.opsForHash().put(hashKey, hashField, hashValue);

		String hashValue = (String) redisTemplate.opsForHash().get(hashKey, hashField);
		log.info("hashValue: {}", hashValue);

		String sessionValue = (String) redisTemplate.opsForHash().get("spring:session:sessions:f0be6a60-d307-4301-9282-3afa02a34e5c",
				"sessionAttr:sessionKey");
		log.info("sessionValue: {}", sessionValue);

		if (sessionValue != null && sessionValue.substring(1, sessionValue.length() - 1).equals("sessionValue")) {
			log.info("sessionValue is sessionValue");
		}
	}

	@Test
	void testRedisSessionKV() throws JsonProcessingException {
		redisTemplate.opsForHash().put(hashKey, hashField, hashValue);

		String hashValue = (String) redisTemplate.opsForHash().get(hashKey, hashField);
		log.info("hashValue: {}", hashValue);

		String sessionStudentValue = (String) redisTemplate.opsForHash().get(hashSessionKey, "sessionAttr:sessionKey");
		log.info("sessionStudentValue: {}", sessionStudentValue);

		ObjectMapper objectMapper = new ObjectMapper();
		Student student = objectMapper.readValue(sessionStudentValue, Student.class);
		log.info("Student: {}", student);
	}

	@Test
	void testRedisSessionUSerInfo() throws JsonProcessingException {
		String sessionStudentValue = (String) redisTemplate.opsForHash().get(hashSessionKey, "sessionAttr:sessionKey");
		log.info("sessionStudentValue: {}", sessionStudentValue);

		ObjectMapper objectMapper = new ObjectMapper();
		UserInfo user = objectMapper.readValue(sessionStudentValue, UserInfo.class);
		log.info("UserInfo: {}", user);
	}

	@Test
	void testDecodeRedisSessionJsonValue() {
		redisTemplate.opsForHash().put(hashKey, hashField, hashValue);

		// 如果redis的对于hash value的序列化，设置为json，那么这里通过key获取，那么就是一个HashMap
		String hashValue = (String) redisTemplate.opsForHash().get(hashKey, hashField);
		log.info("hashValue: {}", hashValue);

		HashMap sessionStudentValue = (HashMap) redisTemplate.opsForHash().get("spring:session:sessions:77b39889-17bf-4da6-b471-aa9bf38890e8",
				"sessionAttr:sessionKey");
		log.info("sessionStudentValue: {}", sessionStudentValue);
	}
}
