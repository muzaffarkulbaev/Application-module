package uz.com.applicationmodule.service.impls;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.applicationmodule.model.dto.requests.auth.LoginRequest;
import uz.com.applicationmodule.model.dto.responses.auth.LoginResponse;
import uz.com.applicationmodule.model.dto.requests.auth.RegisterRequest;
import uz.com.applicationmodule.model.entity.Role;
import uz.com.applicationmodule.model.entity.Session;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.repo.RoleRepository;
import uz.com.applicationmodule.repo.SessionRepository;
import uz.com.applicationmodule.repo.UserRepository;
import uz.com.applicationmodule.security.UserAndSession;
import uz.com.applicationmodule.service.abstractions.AuthService;
import uz.com.applicationmodule.service.abstractions.SessionService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void register(RegisterRequest registerDto) {

        logger.info("Начата регистрация пользователя с email: {}",registerDto.email());
        if (!Objects.equals(registerDto.password(), registerDto.confirmPassword())) {
            logger.warn("Пароли не совпадают с email: {}",registerDto.email());
            throw new RuntimeException("Passwords don't match");
        }
        if(userRepository.findByEmail(registerDto.email()).isPresent()){
            logger.warn("Попытка регистрации с уже существуюшим email: {}",registerDto.email());
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(registerDto.email());
        user.setFullName(registerDto.fullName());
        user.setPassword(registerDto.password());
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setRoles(new HashSet<>(List.of(roleRepository.findByName("USER"))));
        userRepository.save(user);
        logger.info("Пользователь с id:{} успешно зарегестрирован с email: {}",user.getId(),registerDto.email());
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.email());
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User with this email does not exist");
        }
        User user = optionalUser.get();
        if (!user.getPassword().equals(loginRequest.password())){
            throw new RuntimeException("Passwords is not correct!");
        }
        Session session = sessionService.creaateSession(user, httpRequest.getRemoteAddr(), httpRequest.getHeader("User-Agent"));
        Set<Role> roles = user.getRoles();
        String role;
        Role admin = roleRepository.findByName("ADMIN");
        Role manager = roleRepository.findByName("MANAGER");
        Role userRole = roleRepository.findByName("USER");
        if (roles.contains(admin)){
            role = "ADMIN";
        } else if (roles.contains(manager)) {
            role = "MANAGER";
        }else role = "USER";
        return new LoginResponse(session.getToken(),session.getExpiresAt(),"Login successful!", role);
    }

    @Override
    public void logout() {
        Session currentSession = UserAndSession.currentSession;
        sessionRepository.delete(currentSession);
        UserAndSession.currentSession = null;
        UserAndSession.currentUser = null;
    }
}
