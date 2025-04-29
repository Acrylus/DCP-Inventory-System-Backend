package com.example.DCP.Inventory.System.Controller;

import com.example.DCP.Inventory.System.Entity.UserEntity;
import com.example.DCP.Inventory.System.Exception.IncorrectPasswordException;
import com.example.DCP.Inventory.System.Exception.LoggedOutException;
import com.example.DCP.Inventory.System.Exception.UsernameNotFoundException;
import com.example.DCP.Inventory.System.Response.LoginRequest;
import com.example.DCP.Inventory.System.Response.LoginResponse;
import com.example.DCP.Inventory.System.Response.NoDataResponse;
import com.example.DCP.Inventory.System.Response.Response;
import com.example.DCP.Inventory.System.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get_all")
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<UserEntity> users = userService.getAllUsers();
            return Response.response(HttpStatus.OK, "Users fetched successfully", users);
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch users");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        try {
            UserEntity user = userService.getUserById(id);
            return Response.response(HttpStatus.OK, "User fetched successfully", user);
        } catch (NoSuchElementException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch user");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserEntity user) {
        try {
            String registrationStatus = userService.register(user);
            return registrationStatus.equals("Registration Successful")
                    ? NoDataResponse.noDataResponse(HttpStatus.CREATED, registrationStatus)
                    : NoDataResponse.noDataResponse(HttpStatus.BAD_REQUEST, registrationStatus);
        } catch (IllegalArgumentException ex) {
            return NoDataResponse.noDataResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest logReq) {
        try {
            LoginResponse loginResponse = userService.login(logReq);
            return Response.response(HttpStatus.OK, "Login successful", loginResponse);
        } catch (UsernameNotFoundException | IncorrectPasswordException e) {
            return NoDataResponse.noDataResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error occurred");
        }
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.PATCH)
    public ResponseEntity<Object> logoutUser(@RequestParam Long userID) {
        try {
            userService.logout(userID);
            return NoDataResponse.noDataResponse(HttpStatus.OK, "User logged out successfully");
        } catch (LoggedOutException e) {
            return NoDataResponse.noDataResponse(HttpStatus.CONFLICT, e.getMessage());
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to logout user");
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserEntity userDetails) {
        try {
            UserEntity updatedUser = userService.updateUser(id, userDetails);
            return Response.response(HttpStatus.OK, "User updated successfully", updatedUser);
        } catch (NoSuchElementException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user");
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return NoDataResponse.noDataResponse(HttpStatus.NO_CONTENT, "User deleted successfully");
        } catch (NoSuchElementException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "User not found");
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user");
        }
    }

    @PatchMapping("/change_password")
    public ResponseEntity<Object> changePassword(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            String oldPassword = payload.get("oldPassword").toString();
            String newPassword = payload.get("newPassword").toString();

            boolean passwordChanged = userService.changePassword(userId, oldPassword, newPassword);
            return passwordChanged
                    ? NoDataResponse.noDataResponse(HttpStatus.OK, "Password changed successfully")
                    : NoDataResponse.noDataResponse(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        } catch (NoSuchElementException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, "User not found");
        } catch (IncorrectPasswordException e) {
            return NoDataResponse.noDataResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to change password");
        }
    }

    @PatchMapping("/reset_password/{schoolRecordId}")
    public ResponseEntity<Object> resetPassword(@PathVariable Long schoolRecordId) {
        try {
            boolean success = userService.resetPassword(schoolRecordId);

            return success
                    ? NoDataResponse.noDataResponse(HttpStatus.OK, "Password reset successfully to @Password123")
                    : NoDataResponse.noDataResponse(HttpStatus.BAD_REQUEST, "Failed to reset password");

        } catch (NoSuchElementException e) {
            return NoDataResponse.noDataResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return NoDataResponse.noDataResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to reset password");
        }
    }

}
