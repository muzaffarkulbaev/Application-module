package uz.com.applicationmodule.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.com.applicationmodule.model.dto.requests.auth.LoginRequest;
import uz.com.applicationmodule.model.dto.responses.auth.LoginResponse;
import uz.com.applicationmodule.model.dto.requests.auth.RegisterRequest;
import uz.com.applicationmodule.service.abstractions.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        try {
            authService.register(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        try {
            LoginResponse response = authService.login(loginRequest,request);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new LoginResponse(null,null, e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        try {
            authService.logout();
            return ResponseEntity.ok("logout successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
