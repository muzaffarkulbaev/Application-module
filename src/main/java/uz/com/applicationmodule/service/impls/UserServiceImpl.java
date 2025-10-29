package uz.com.applicationmodule.service.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.applicationmodule.model.dto.responses.users.UserGetDto;
import uz.com.applicationmodule.model.dto.requests.users.UserSaveDto;
import uz.com.applicationmodule.model.entity.Role;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.repo.RoleRepository;
import uz.com.applicationmodule.repo.SessionRepository;
import uz.com.applicationmodule.repo.UserRepository;
import uz.com.applicationmodule.security.UserAndSession;
import uz.com.applicationmodule.service.abstractions.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final RoleRepository roleRepository;

    @Override
    public void save(UserSaveDto userSaveDto) {
        User user = new User();
        user.setEmail(userSaveDto.email());
        user.setFullName(userSaveDto.fullName());
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public List<UserGetDto> getUsers() {
        List<User> users = userRepository.findAllUsers();
        return users
                .stream()
                .map(user -> {
                    String[] roleNames = new String[user.getRoles().size()];
                    int count = 0;
                    for (Role role : user.getRoles()) {
                        roleNames[count++] = role.getName();
                    }
                    return new UserGetDto(user.getId(), user.getFullName(), user.getEmail(), roleNames);
                }).toList();
    }

    @Override
    public void delete() {
        User currentUser = UserAndSession.currentUser;
        sessionRepository.deleteAllByUserId(currentUser.getId());
        userRepository.delete(currentUser);
        UserAndSession.currentUser = null;
        UserAndSession.currentSession = null;
    }

    @Override
    public void assignRoleManager(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new RuntimeException("USER NOT FOUND");
        }
        User user = userOptional.get();
        Role manager = roleRepository.findByName("MANAGER");
        if (user.getRoles().contains(manager))
            throw new RuntimeException("The user is already manager!!!");
        user.getRoles().add(manager);
    }

}
