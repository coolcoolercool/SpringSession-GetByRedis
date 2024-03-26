package org.example.redissession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.redissession.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

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

		String sessionStudentValue = (String) redisTemplate.opsForHash().get("spring:session:sessions:1871c903-d683-402b-af88-dcc820e49f27",
				"sessionAttr:sessionKey");
		log.info("sessionStudentValue: {}", sessionStudentValue);

		ObjectMapper  objectMapper = new ObjectMapper();
		Student student = objectMapper.readValue(sessionStudentValue, Student.class);
		log.info("Student: {}", student);


	}

}
