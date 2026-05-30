package com.edgecloud.gateway.exception;

import java.time.LocalDateTime;

public record GatewayErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}