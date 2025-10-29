package uz.com.applicationmodule.service.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.com.applicationmodule.model.dto.responses.app.ApplicationGetDto;
import uz.com.applicationmodule.model.dto.requests.app.ApplicationUpdateDto;
import uz.com.applicationmodule.model.dto.requests.app.ChangeStatusDto;
import uz.com.applicationmodule.model.entity.Application;
import uz.com.applicationmodule.model.entity.User;
import uz.com.applicationmodule.model.enums.AppStatus;
import uz.com.applicationmodule.model.history.AppStatusHistory;
import uz.com.applicationmodule.model.history.AppTextHistory;
import uz.com.applicationmodule.model.history.ApplicationHistory;
import uz.com.applicationmodule.repo.*;
import uz.com.applicationmodule.security.UserAndSession;
import uz.com.applicationmodule.service.abstractions.ApplicationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final AppStatusHistoryRepository appStatusHistoryRepository;
    private final AppTextHistoryRepository appTextHistoryRepository;
    private final RoleRepository roleRepository;
    private final ApplicationHistoryRepo applicationHistoryRepo;

    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Finds all applications which status is not deleted
     * */
    @Override
    public List<ApplicationGetDto> findAll() {
        List<Application> applications = applicationRepository.findAllNoDeletedStatus();
        return applications.stream()
//                .filter(application -> application.getUser().getRoles().contains(user))
                .map(application ->
                        new ApplicationGetDto(application.getId(),  application.getText(), application.getCreatedTime().format(formatter), application.getUser().getFullName(), application.getStatus().toString()))
                .toList();
    }

    /**
     * Finds applications which status is not deleted and belongs current user
     * */
    @Override
    public List<ApplicationGetDto> findByUser() {
        List<Application> applications = applicationRepository.findByUserIdWithNoDeletedStatus(UserAndSession.currentUser.getId());

        List<ApplicationGetDto> resultList = applications
                .stream()
                .map(application -> {
                    System.out.println("application.getCreatedTime().format(formatter) = " + application.getCreatedTime().format(formatter));
                    return new ApplicationGetDto(application.getId(), application.getText(), application.getCreatedTime().format(formatter), application.getUser().getFullName(),application.getStatus().toString());
                })
                .toList();
        return resultList;
    }

    /**
     * Method creates new application with new message
     * It will belong current user
     * And it is default status is ACTIVE
     * */
    @Override
    public Application save(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Body of application cannot be null or empty");
        }
        Application application = new Application(message,AppStatus.ACTIVE,UserAndSession.currentUser);
        applicationHistoryRepo.save(new ApplicationHistory(application));
        return applicationRepository.save(application);
    }

    /**
     * Method updates the text of application
     * It accepts dto which contains application id and new text
     * There are checks for text null and application not existing
     * */
    @Override
    public Application update(ApplicationUpdateDto applicationUpdateDto) {
        if (applicationUpdateDto.text()==null || applicationUpdateDto.text().isEmpty()) {
            throw new IllegalArgumentException("New text cannot be null or empty");
        }
        Optional<Application> optionalApplication = applicationRepository.findById(applicationUpdateDto.id());
        if (optionalApplication.isEmpty()) {
            throw new RuntimeException("There is no application with id " + applicationUpdateDto.id());
        }

        Application application = optionalApplication.get();
        ApplicationHistory applicationHistory = new ApplicationHistory(application, application.getText(), applicationUpdateDto.text(), UserAndSession.currentUser);
        applicationHistoryRepo.save(applicationHistory);
        //        AppTextHistory appTextHistory = new AppTextHistory(application, UserAndSession.currentUser, application.getText(), applicationUpdateDto.text(), LocalDateTime.now());
//        appTextHistoryRepository.save(appTextHistory);
        application.setText(applicationUpdateDto.text());
        return application;
    }

    /**
     * Method changes the status of application to DELETED
     * There checks for application existing and the status for DELETING
     * It accepts only app id
     * */
    @Override
    public Integer delete(Integer applicationId) {
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
        if (optionalApplication.isEmpty()) {
            throw new RuntimeException("There is no application with id " + applicationId);
        }
        Application application = optionalApplication.get();
        if (application.getStatus() == AppStatus.DELETED){
            throw new RuntimeException("The application with id " + applicationId + " has already been deleted");
        }
        ApplicationHistory applicationHistory = new ApplicationHistory(application, application.getStatus(), AppStatus.DELETED, UserAndSession.currentUser);
        applicationHistoryRepo.save(applicationHistory);
//        AppStatusHistory appStatusHistory = new AppStatusHistory(application, UserAndSession.currentUser, application.getStatus(), AppStatus.DELETED, LocalDateTime.now());
//        appStatusHistoryRepository.save(appStatusHistory);
        application.setStatus(AppStatus.DELETED);
        return applicationId;
    }

    /**
     * Method changes the status of application
     * There checks for similar status and application existing
     * It accepts dto which contains app id and new status
     * */
    @Override
    public void changeStatus(ChangeStatusDto changeStatusDto) {
        Optional<Application> optionalApplication = applicationRepository.findById(changeStatusDto.appId());
        if(optionalApplication.isEmpty()){
            throw new RuntimeException("There is no application with id " + changeStatusDto.appId());
        }
        Application application = optionalApplication.get();

        try {
            AppStatus appNewStatus = AppStatus.valueOf(changeStatusDto.status());
            if (appNewStatus == AppStatus.DELETED) {
                throw new RuntimeException("Changing status of application will be with other api!");
            }
            if (appNewStatus.equals(application.getStatus())) {
                throw new RuntimeException("The application status is same with new status");
            }
//            AppStatusHistory appStatusHistory = new AppStatusHistory(application, UserAndSession.currentUser, application.getStatus(), appNewStatus, LocalDateTime.now());
//            appStatusHistoryRepository.save(appStatusHistory);
            ApplicationHistory applicationHistory = new ApplicationHistory(application, application.getStatus(), appNewStatus, UserAndSession.currentUser);
            applicationHistoryRepo.save(applicationHistory);
            application.setStatus(appNewStatus);
        }catch (IllegalArgumentException e){
            throw new RuntimeException("Application status is not valid");
        }catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Finds applications which status is not deleted by user id
     * */
    @Override
    public List<ApplicationGetDto> findByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("There is no user with id " + userId);
        }
        List<Application> applications = applicationRepository.findByUserIdWithNoDeletedStatus(userOptional.get().getId());
        List<ApplicationGetDto> resultList = applications
                .stream()
                .map(application -> new ApplicationGetDto(application.getId(), application.getText(), application.getCreatedTime().format(formatter), application.getUser().getFullName(),application.getStatus().toString()))
                .toList();
        return resultList;
    }

    @Override
    public Application saveWithDate(String date) {
        LocalDate localDate = LocalDate.parse(date,dayFormatter);
        LocalDateTime createdTime = localDate.atStartOfDay();
        Application application = new Application("message",AppStatus.ACTIVE,UserAndSession.currentUser);
        application.setCreatedTime(createdTime);
        applicationHistoryRepo.save(new ApplicationHistory(application));
        return applicationRepository.save(application);
    }
}
