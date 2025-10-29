package uz.com.applicationmodule.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.com.applicationmodule.model.entity.Role;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.repo.RoleRepository;
import uz.com.applicationmodule.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0) {
            Role user = new Role("USER");
            Role manager = new Role("MANAGER");
            Role admin = new Role("ADMIN");
            Set<Role> roles = new HashSet<>(List.of(user, manager, admin));
            roleRepository.saveAll(roles);
        }

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin");
            admin.setFullName("Admin");
            admin.setCreatedTime(LocalDateTime.now());
            admin.setUpdateTime(LocalDateTime.now());
            admin.setRoles(new HashSet<>(List.of(roleRepository.findByName("ADMIN"))));
            userRepository.save(admin);
        }
    }
}
