package org.example.redissession.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Slf4j
@RequestMapping("/gw/")
public class SentinelController {

    /**
     * route 维度。这样就可以看到我们的配置信息
     * @return
     */
    @GetMapping("/flow")
    public Object GetGatewayFlowConfig(){
        log.info("SentinelController GetGatewayFlowConfig");
        return GatewayRuleManager.getRules();
    }

    /**
     * 用户自定义的 API 定义分组。这样就可以看到我们的配置信息
     * @return
     */
    @GetMapping("/api-group")
    public Object GetGatewayApiGroupConfig(){
        return GatewayApiDefinitionManager.getApiDefinitions();
    }
}
