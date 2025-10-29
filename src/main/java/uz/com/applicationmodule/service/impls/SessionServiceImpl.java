package uz.com.applicationmodule.service.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.com.applicationmodule.model.entity.Session;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.repo.SessionRepository;
import uz.com.applicationmodule.service.abstractions.SessionService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public Session creaateSession(User user, String ip, String userAgent) {
        Session session = new Session();
        session.setToken(UUID.randomUUID().toString());
        session.setIp(ip);
        session.setUser(user);
        session.setUserAgent(userAgent);
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusDays(1));
        return sessionRepository.save(session);
    }

    @Override
    public Session getSessionByToken(String token) {
        Session session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return session;
    }
}
