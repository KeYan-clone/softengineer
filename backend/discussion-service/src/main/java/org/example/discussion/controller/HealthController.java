package org.example.discussion.controller;

import org.example.discussion.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for health check and system information
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    /**
     * Health check endpoint
     *
     * @return Health status
     */
    @GetMapping
    public ResponseEntity<ApiResponse.Result<Map<String, Object>>> healthCheck() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "discussion-service");
        healthInfo.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(ApiResponse.success(healthInfo));
    }
}
