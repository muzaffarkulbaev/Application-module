package uz.com.applicationmodule.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.com.applicationmodule.model.entity.Session;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.repo.SessionRepository;
import uz.com.applicationmodule.service.impls.AuthServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service - part of security configuration
 * */
@Component
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final SessionRepository sessionRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public User user;
    public Session session;

    /**
     * We take token
     * Find session from the database
     * Find user and keep user in object SecurityContext
     * */
    @Override
    public void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("token = " + token);
            Optional<Session> optionalSession = sessionRepository.findByToken(token);
            if (optionalSession.isPresent()) {

                Session session = optionalSession.get();
                boolean isExpired = session.getExpiresAt().isBefore(LocalDateTime.now());
                if (isExpired) {
                    sessionRepository.delete(session);
                    sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Session expired");
                    return;
                }
                UserAndSession.currentSession = session;

                if (!session.getIp().equals(request.getRemoteAddr())) {
                    logger.warn("Session IP does not match: expected={} actual={}", session.getIp(), request.getRemoteAddr());
                    sendError(response,HttpServletResponse.SC_CONFLICT,"ip does not match");
                    return;
                } else if (!session.getUserAgent().equals(request.getHeader("User-Agent"))) {
                    logger.warn("User agent does not match: expected={} actual{}", session.getUserAgent(), request.getHeader("User-Agent"));
                    sendError(response,HttpServletResponse.SC_CONFLICT,"User agent does not match");
                    return;
                }

                User user = session.getUser();
                UserAndSession.currentUser = user;

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(user.getEmail());

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } /*else {
                sendError(response,HttpServletResponse.SC_UNAUTHORIZED,"Session does not exist with this token!");
                return;
            }*/
        }/*else {
            sendError(response,HttpServletResponse.SC_BAD_REQUEST,"Not valid token!");
            return;
        }*/

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/text");
        response.getWriter().write(message);
    }
}

