package org.example.redissession.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/gw")
public class SentinelTestController {
    @PostMapping("/flow")
    public void GetGatewayFlowConfig(@RequestParam String token){
        log.info("SentinelTestController GetGatewayFlowConfig, token:{}", token);
    }

    @GetMapping("/api-group")
    public void GetGatewayApiGroupConfig(){
        log.info("SentinelTestController GetGatewayApiGroupConfig get request");
    }
}
