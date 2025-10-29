package uz.com.applicationmodule.model.dto.responses.auth;

import java.time.LocalDateTime;

public record LoginResponse(String token, LocalDateTime expiresAt, String message, String role) {
}
