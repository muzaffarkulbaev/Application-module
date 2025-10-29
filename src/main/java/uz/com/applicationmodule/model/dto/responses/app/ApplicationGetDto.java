package uz.com.applicationmodule.model.dto.responses.app;

public record ApplicationGetDto(Integer id,String text, String createdTime, String userFullName, String status) {
}
