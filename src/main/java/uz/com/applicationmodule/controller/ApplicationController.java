package uz.com.applicationmodule.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.applicationmodule.model.dto.requests.app.AppReportOneDayRequestDto;
import uz.com.applicationmodule.model.dto.requests.app.AppReportPeriodRequestDto;
import uz.com.applicationmodule.model.dto.responses.app.AppReportResponseDto;
import uz.com.applicationmodule.model.dto.responses.app.ApplicationGetDto;
import uz.com.applicationmodule.model.dto.requests.app.ApplicationUpdateDto;
import uz.com.applicationmodule.model.dto.requests.app.ChangeStatusDto;
import uz.com.applicationmodule.model.entity.Application;
import uz.com.applicationmodule.security.UserAndSession;
import uz.com.applicationmodule.service.abstractions.AppReportService;
import uz.com.applicationmodule.service.abstractions.ApplicationService;

import java.util.List;


/**
 * RestController which has API's to do actions  with applications (CRUD)
 * */
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final ApplicationService applicationService;
    private final AppReportService appReportService;

    /**
     * Returns all applications in database
     * Which status is not deleted
     */
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<ApplicationGetDto>> getAllApplications() {
        logger.info("User with id {} gets all applications",UserAndSession.currentUser.getId());
        return ResponseEntity.ok(applicationService.findAll());
    }

    /**
     * Returns applications which belong to a current user
     * Which status is not deleted
     */
    @GetMapping("/get")
    public ResponseEntity<List<ApplicationGetDto>> getApplicationsByUser() {
        logger.info("User with id {} gets own applications",UserAndSession.currentUser.getId());
        return ResponseEntity.ok(applicationService.findByUser());
    }

    /**
     * Returns applications by userId
     * Which status is not deleted
     * */
    @GetMapping("/get/{userId}")
    public ResponseEntity<List<ApplicationGetDto>> getApplicationById(@PathVariable Integer userId) {
        try {
            List<ApplicationGetDto> applicationList = applicationService.findByUserId(userId);
            logger.info("User with id {} got all applications belonging to user with id {}",UserAndSession.currentUser.getId(),userId);
            return ResponseEntity.ok(applicationList);
        }catch (Exception e){
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * API creates new application and saves in database
     * It accepts text(message)
     * */
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> saveApplication(@RequestBody String message) {
        try {
            Application savedApplication = applicationService.save(message);
            logger.info("Created new application with id {} by user with id {}",savedApplication.getId(), UserAndSession.currentUser.getId());
            return ResponseEntity.ok("Application saved successfully");
        }catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create/withDate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> saveApplicationWithDate(@RequestBody Dto dto) {
        try {
            Application savedApplication = applicationService.saveWithDate(dto.date());
            logger.info("Created new application with id {} by user with id {}",savedApplication.getId(), UserAndSession.currentUser.getId());
            return ResponseEntity.ok("Application saved successfully");
        }catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API updates application text
     * It accepts applicationId and new text(inside ApplicationUpdateDto)
     * */
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<String> updateApplication(@RequestBody ApplicationUpdateDto applicationUpdateDto) {
        try {
            applicationService.update(applicationUpdateDto);
            logger.info("Application with id {} updated by User with id {}",applicationUpdateDto.id(),UserAndSession.currentUser.getId());
            return ResponseEntity.ok("Application updated successfully");
        }catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API deletes application from database
     * It accepts only applicationId
     * */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteApplication(@PathVariable Integer id) {
        try {
            Integer deleteApplicationId = applicationService.delete(id);
            logger.info("Application with id {} deleted by User with id {}",deleteApplicationId,UserAndSession.currentUser.getId());
            return ResponseEntity.ok("Application deleted successfully");
        }catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API changes application status
     * It accepts applicationId and new status
     * */
    @PostMapping("/changeStatus")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> changeStatus(@RequestBody ChangeStatusDto changeStatusDto){
        try {
            applicationService.changeStatus(changeStatusDto);
            logger.info("User with id {} changed the status of application with id {}",UserAndSession.currentUser.getId(),changeStatusDto.appId());
            return ResponseEntity.ok("Status changed successfully");
        }catch (Exception e){
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppReportResponseDto> reportOfDay(@RequestBody AppReportOneDayRequestDto appReportRequestDto){
        try {
            long start = System.nanoTime();
            AppReportResponseDto appReportResponseDto = appReportService.reportForDay(appReportRequestDto.date());
            logger.info("User with id {} got report about applications",UserAndSession.currentUser.getId());
            long end = System.nanoTime();
            System.out.println("Time of process: " + (end - start));
            return ResponseEntity.ok(appReportResponseDto);
        }catch (Exception e){
            e.printStackTrace();
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/report/period")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppReportResponseDto>> reportOfPeriod(@RequestBody AppReportPeriodRequestDto appReportPeriodRequestDto){
        try{
            long start = System.nanoTime();
            List<AppReportResponseDto> result = appReportService.reportForPeriod(appReportPeriodRequestDto);
            long end = System.nanoTime();
            System.out.println("Time of process: " + (end - start));
            logger.info("User with id {} got report about applications for period exact period",UserAndSession.currentUser.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
