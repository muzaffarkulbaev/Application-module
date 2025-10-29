package uz.com.applicationmodule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.applicationmodule.model.dto.responses.users.UserGetDto;
import uz.com.applicationmodule.model.dto.requests.users.UserSaveDto;
import uz.com.applicationmodule.service.abstractions.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<UserGetDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody UserSaveDto userSaveDto){
        userService.save(userSaveDto);
        return ResponseEntity.ok("Saved successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(){
        userService.delete();
        return ResponseEntity.ok("Deleted Successfully");
    }

    @PostMapping("/assign/manager/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignRoleManager(@PathVariable Integer userId){
        try {
            userService.assignRoleManager(userId);
            return ResponseEntity.ok("Assigned Role Manager Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
