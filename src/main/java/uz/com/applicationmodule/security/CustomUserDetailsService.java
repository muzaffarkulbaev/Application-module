package uz.com.applicationmodule.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.com.applicationmodule.model.entity.Role;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.repo.UserRepository;

/**
 * Service - part of security configuration
 * */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * In this method we create UserDetails which spring understands
     * */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String[] roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roleNames)
                .build();
    }
}

