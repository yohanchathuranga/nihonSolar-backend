package com.example.controller;

import com.example.entity.DOMailSender;
import com.example.entity.DOUser;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.UserRepository;
import com.example.repository.UserRepository.Or;
import com.example.util.EmailSender;
import com.example.util.IdGenerator;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;
    private IdGenerator idg;
    private EmailSender emailSender;

    public TestController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    // get all users
    @GetMapping
    public List<DOUser> getAllUsers() {
        return this.userRepository.findAll();
    }

    // get user by id
    @GetMapping("/{id}")
    public DOUser getUserById(@PathVariable(value = "id") String userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
    }

    // create user
    @PostMapping
    public DOUser createUser(@RequestBody DOUser user) {
        return this.userRepository.save(user);
    }

    // update user
    @PutMapping("/{id}")
    public DOUser updateUser(@RequestBody DOUser user, @PathVariable("id") String userId) {
        DOUser existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        return this.userRepository.save(existingUser);
    }

    // delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOUser> deleteUser(@PathVariable("id") String userId) {
        DOUser existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
        this.userRepository.delete(existingUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/{id}")
    public Or userOrder(@PathVariable("id") int id) {
        return this.userRepository.getOrder(id);
    }

    @PostMapping("/email/{email}")
    public void getId(@PathVariable("email") String email) {
        DOMailSender mail = new DOMailSender();
        ArrayList<String> arr = new ArrayList();
        arr.add(email);
        mail.setReceiver(arr);
        mail.setSubject("Test message");
        mail.setMessage("This is test Email");
        emailSender.sendEmail(mail);
    }


}
