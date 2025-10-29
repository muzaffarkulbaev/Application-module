package uz.com.applicationmodule.model.dto.responses.users;


public record UserGetDto(Integer id, String fullName, String email, String[] roles) {
}
