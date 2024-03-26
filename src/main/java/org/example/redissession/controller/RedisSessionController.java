package org.example.redissession.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.example.redissession.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/test/")
public class RedisSessionController {
    @Autowired
    private RedisTemplate redisTemplate;

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
    @RequestMapping("/setSessionBoy")
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

    /**
     * 读取session
     * @param key
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getSession/{key}")
    public String getSession(@PathVariable String key , HttpServletRequest request){
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
        HttpSession session = request.getSession();
        session.setAttribute("userName", "xiaoMimng");
    }


}
