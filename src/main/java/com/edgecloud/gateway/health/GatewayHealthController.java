package com.edgecloud.gateway.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class GatewayHealthController {

    @GetMapping("/gateway/status")
    public Map<String, Object> gatewayStatus() {

        return Map.of(
                "service", "edgecloud-api-gateway",
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "description", "API Gateway is running successfully"
        );
    }
}