package uz.com.applicationmodule.service.abstractions;

import uz.com.applicationmodule.model.dto.responses.app.ApplicationGetDto;
import uz.com.applicationmodule.model.dto.requests.app.ApplicationUpdateDto;
import uz.com.applicationmodule.model.dto.requests.app.ChangeStatusDto;
import uz.com.applicationmodule.model.entity.Application;

import java.util.List;

public interface ApplicationService {

    List<ApplicationGetDto> findAll();
    List<ApplicationGetDto> findByUser();
    Application save(String message);
    Application update(ApplicationUpdateDto applicationSaveDto);
    Integer delete(Integer applicationId);
    void changeStatus(ChangeStatusDto changeStatusDto);
    List<ApplicationGetDto> findByUserId(Integer userId);

    Application saveWithDate(String date);
}
