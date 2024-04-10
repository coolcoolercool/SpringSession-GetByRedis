package org.example.redissession.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.redissession.entity.Student;
import org.example.redissession.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/test/")
public class RedisSessionController {
    @Autowired
    private RedisTemplate redisTemplate;

    String key = "sessionKey";
    /**
     * session设置
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/setSession")
    public String setSession(HttpServletRequest request){
        String key = "sessionKey";
        String value = "sessionValue";
        request.getSession().setAttribute(key,value);
        request.getSession().setMaxInactiveInterval(30 * 60 * 60);
        log.info("sessionId: {}", request.getSession().getId());
        return request.getSession().getId();
    }

    @ResponseBody
    @RequestMapping("/setSessionStudent")
    public String setSessionBoy(HttpServletRequest request){
        String key = "sessionKey";
        String value = "sessionValue";
        Student student = new Student();
        student.setAge(30);
        student.setName("zz2");
        request.getSession().setAttribute(key,student);
        request.getSession().setMaxInactiveInterval(30 * 60 * 60);
        log.info("sessionId: {}", request.getSession().getId());
        return request.getSession().getId();
    }

    @ResponseBody
    @RequestMapping("/setSessionUserInfo")
    public String setSessionUSerInfo(HttpServletRequest request){
        UserInfo user = new UserInfo();
        user.setUserName("userName");
        List<String> urlList = new ArrayList<>();
        urlList.add("/test/setSessionBoy");
        urlList.add("/test/getSessionUserInfo");
        user.setUrlList(urlList);
        request.getSession().setAttribute(key,user);
        request.getSession().setMaxInactiveInterval(30 * 60 * 60);
        log.info("sessionId: {}", request.getSession().getId());
        return request.getSession().getId();
    }

    private String TOKEN_HEADER_KEY = "auth_token";
    private String HASH_SESSION_KEY_PREFIX = "spring:session:sessions:";
    @GetMapping("/getSessionUserInfo")
    public String getSessionUSerInfo(HttpServletRequest request){
        String token = request.getHeader(TOKEN_HEADER_KEY);
        if (StringUtils.isEmpty(token)) {
            log.error("token is null");
        }
        log.info("token: {}", token);
        String hashKey = HASH_SESSION_KEY_PREFIX + token;

        String userInfoStr = (String) redisTemplate.opsForHash().get(hashKey, "sessionAttr:sessionKey");
        log.info("userInfoStr: {}", userInfoStr);

        ObjectMapper objectMapper = new ObjectMapper();
        UserInfo userIno = null;
        try {
            userIno = objectMapper.readValue(userInfoStr, UserInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("UserInfo: {}", userIno);
        return userInfoStr;
    }

    /**
     * 读取session
     * @param key
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getSession/{key}")
    public String getSession(@PathVariable String key , HttpServletRequest request){
        HttpSession session = request.getSession();
        log.info("session: {}", session);
        HashMap student = (HashMap) request.getSession().getAttribute(key);
        log.info("student: {}", student);
        log.info("name: {}", student.get("name"));
        log.info("age: {}", student.get("age"));
        return request.getSession().getAttribute(key) + "---- sessionId:" + request.getSession().getId() ;
    }

    /**
     * 使用redis直接读取session
     * @return
     */
    @ResponseBody
    @RequestMapping("/getSessionBySessionId/{sessionId}")
    public void getSession(@PathVariable String sessionId){
        Map<String, Object> entries = redisTemplate.opsForHash().entries("spring:session:sessions:688a2c6d-ae3d-4611-ab25-318b66df70a4");
        log.info("sessionMap: {}", entries);

        String redisValue = (String) redisTemplate.opsForHash().get("sessionAttr:sessionKey", "spring:session:sessions:688a2c6d-ae3d-4611-ab25-318b66df70a4");
        log.info("redisValue: {}", redisValue);

        String redisValue1 = (String) redisTemplate.opsForHash().get("spring:session:sessions:688a2c6d-ae3d-4611-ab25-318b66df70a4",
                "sessionAttr:sessionKey"                );
        log.info("redisValue1: {}", redisValue1);

    }

    @GetMapping("/autoSession/")
    public void addRedisSession(HttpServletRequest request){
        log.info("RedisSessionController autoSession");
        HttpSession session = request.getSession();
        session.setAttribute("userName", "xiaoMimng");
    }


    @GetMapping("/testRequest")
    public void getRequestContent(HttpServletRequest request) {
        log.info("request.getRequestURI(): {} ", request.getRequestURI());
    }

    @GetMapping("/testRedisGetAndSet")
    public void getRedisGetAndSet(HttpServletRequest request) {
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

        Map<String, Object> map = new HashMap<>();
        map.put("userName", userInfo.getUserName());
        map.put("urlList", userInfo.getUrlList());
        map.put("functionList", userInfo.getFunctionList());

        redisTemplate.opsForValue().set("userInfo", map);
    }
}
