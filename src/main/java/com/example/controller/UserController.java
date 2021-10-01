package com.example.controller;

import com.example.base.UserManager;
import com.example.entity.DOCountRequest;
import com.example.entity.DOForgotPassword;
import com.example.entity.DOForgotPasswordRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOLoginRequest;
import com.example.entity.DOResetPasswordRequest;
import com.example.entity.DOUser;
import yohan.exceptions.CustomException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/users")
public class UserController {

   private final UserManager userManager;

    @Autowired
    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    // get all users
    @PostMapping("/list")
    public ResponseEntity<List<DOUser>> getUserList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.userManager.listUsers(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getUserCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.userManager.countUsers(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<DOUser> getUserById(@PathVariable(value = "id") String userId) {
        try {
            return new ResponseEntity(this.userManager.getUserById(userId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create user
    @PostMapping
    public ResponseEntity<DOUser> createUser(@RequestBody DOUser user) {
        try {
            return new ResponseEntity(this.userManager.createUser(user), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOUser> deleteUser(@PathVariable("id") String userId) {
        try {
            return new ResponseEntity(this.userManager.deleteUser(userId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    // create user
    @PutMapping
    public ResponseEntity<DOUser> updateUser(@RequestBody DOUser user) {
        try {
            return new ResponseEntity(this.userManager.updateUser(user), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<DOUser> userLogin(@RequestBody DOLoginRequest loginRequest) {
        try {
            return new ResponseEntity(this.userManager.userLogin(loginRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
        
    @PostMapping("/forgot_password_request")
    public ResponseEntity<DOUser> forgotPasswordRequest(@RequestBody DOForgotPasswordRequest forgotPasswordRequest) {
        try {
            this.userManager.forgotPasswordRequest(forgotPasswordRequest);
            return new ResponseEntity(HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/forgot_password")
    public ResponseEntity<DOUser> forgotPassword(@RequestBody DOForgotPassword forgotPassword) {
        try {
            this.userManager.forgotPassword(forgotPassword);
            return new ResponseEntity(HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/reset_password")
    public ResponseEntity<DOUser> resetPassword(@RequestBody DOResetPasswordRequest resetPasswordRequest) {
        try {
            this.userManager.resetPassword(resetPasswordRequest);
            return new ResponseEntity(HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
