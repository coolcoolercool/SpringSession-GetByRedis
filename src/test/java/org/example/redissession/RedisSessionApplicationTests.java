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

import java.util.*;

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

	String hashSessionKey = "spring:session:sessions:a2fa19b6-5777-4f86-8f8e-cffae8cd0649";

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

	@Test
	void testRedisGetAndSet() {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName("userName");
		List<String> urlList = new ArrayList<>();
		urlList.add("/good/url");
		urlList.add("/bad/url");
		userInfo.setUrlList(urlList);

		List<String> functionList = new ArrayList<>();
		functionList.add("function_id_1");
		functionList.add("function_id_0");
		userInfo.setFunctionList(functionList);

		redisTemplate.opsForValue().set("userInfo", userInfo);
		//redisTemplate.opsForHash().put("hashKey", "userInfo", userInfo);
	}

	@Test
	void testTokenSubString() {
		String tokenOrg = "bearertokenstr";
		String token = tokenOrg.substring(6);
		log.info("token:{}", token);

		String tokenPrefix = tokenOrg.substring(0, 6);
		log.info("tokenPrefix:{}", tokenPrefix);
	}
}
