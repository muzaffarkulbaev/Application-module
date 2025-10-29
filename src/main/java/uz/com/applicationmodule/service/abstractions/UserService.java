package uz.com.applicationmodule.service.abstractions;

import uz.com.applicationmodule.model.dto.responses.users.UserGetDto;
import uz.com.applicationmodule.model.dto.requests.users.UserSaveDto;

import java.util.List;

public interface UserService {
    void save(UserSaveDto userSaveDto);
    List<UserGetDto> getUsers();
    void delete();
    void assignRoleManager(Integer userId);
}
