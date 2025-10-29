package uz.com.applicationmodule.model.dto.requests.auth;

public record RegisterRequest(String email, String fullName, String password, String confirmPassword) {
}
