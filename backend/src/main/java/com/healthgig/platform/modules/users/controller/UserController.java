package com.healthgig.platform.modules.users.controller;

import com.healthgig.platform.modules.users.dto.UserDto;
import com.healthgig.platform.modules.users.dto.UserProfileDto;
import com.healthgig.platform.modules.users.model.EmployerProfile;
import com.healthgig.platform.modules.users.model.User;
import com.healthgig.platform.modules.users.model.WorkerProfile;
import com.healthgig.platform.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    @Operation(summary = "Get user by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/current")
    @Operation(summary = "Get current user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByEmail(authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    @Operation(summary = "Update user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @PostMapping("/{id}/password")
    @PreAuthorize("@userSecurity.isCurrentUser(#id)")
    @Operation(summary = "Update password", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        userService.updatePassword(id, currentPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    // Worker profile endpoints
    @PostMapping("/{id}/worker-profile")
    @PreAuthorize("@userSecurity.isCurrentUser(#id) and @userSecurity.hasRole('WORKER')")
    @Operation(summary = "Create worker profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<WorkerProfile> createWorkerProfile(
            @PathVariable Long id, 
            @Valid @RequestBody UserProfileDto profileDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createWorkerProfile(id, profileDto));
    }

    @GetMapping("/{id}/worker-profile")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id) or hasRole('EMPLOYER')")
    @Operation(summary = "Get worker profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<WorkerProfile> getWorkerProfile(@PathVariable Long id) {
        return userService.getWorkerProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/worker-profile")
    @PreAuthorize("@userSecurity.isCurrentUser(#id) and @userSecurity.hasRole('WORKER')")
    @Operation(summary = "Update worker profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<WorkerProfile> updateWorkerProfile(
            @PathVariable Long id, 
            @Valid @RequestBody UserProfileDto profileDto) {
        return ResponseEntity.ok(userService.updateWorkerProfile(id, profileDto));
    }

    // Employer profile endpoints
    @PostMapping("/{id}/employer-profile")
    @PreAuthorize("@userSecurity.isCurrentUser(#id) and @userSecurity.hasRole('EMPLOYER')")
    @Operation(summary = "Create employer profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<EmployerProfile> createEmployerProfile(
            @PathVariable Long id, 
            @Valid @RequestBody UserProfileDto profileDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createEmployerProfile(id, profileDto));
    }

    @GetMapping("/{id}/employer-profile")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id) or hasRole('WORKER')")
    @Operation(summary = "Get employer profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<EmployerProfile> getEmployerProfile(@PathVariable Long id) {
        return userService.getEmployerProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/employer-profile")
    @PreAuthorize("@userSecurity.isCurrentUser(#id) and @userSecurity.hasRole('EMPLOYER')")
    @Operation(summary = "Update employer profile", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<EmployerProfile> updateEmployerProfile(
            @PathVariable Long id, 
            @Valid @RequestBody UserProfileDto profileDto) {
        return ResponseEntity.ok(userService.updateEmployerProfile(id, profileDto));
    }

    // Admin operations
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reactivate user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> reactivateUser(@PathVariable Long id) {
        userService.reactivateUser(id);
        return ResponseEntity.ok().build();
    }
}