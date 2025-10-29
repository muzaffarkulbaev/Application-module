package uz.com.applicationmodule.service.abstractions;

import jakarta.servlet.http.HttpServletRequest;
import uz.com.applicationmodule.model.dto.requests.auth.LoginRequest;
import uz.com.applicationmodule.model.dto.responses.auth.LoginResponse;
import uz.com.applicationmodule.model.dto.requests.auth.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest registerDto);
    LoginResponse login(LoginRequest loginRequest, HttpServletRequest httpRequest);
    void logout();
}
