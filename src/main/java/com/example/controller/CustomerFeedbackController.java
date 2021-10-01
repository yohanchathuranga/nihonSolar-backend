package com.example.controller;

import com.example.base.CustomerFeedbackManager;
import com.example.entity.DOCountRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOCustomerFeedback;
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
@RequestMapping("/customer_feedbacks")
public class CustomerFeedbackController {

    private final CustomerFeedbackManager customerFeedbackManager;

    @Autowired
    public CustomerFeedbackController(CustomerFeedbackManager customerFeedbackManager) {
        this.customerFeedbackManager = customerFeedbackManager;
    }

    //  customer feedbacks list
    @PostMapping("/list")
    public ResponseEntity<List<DOCustomerFeedback>> getCustomerFeedbackList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.customerFeedbackManager.listCustomerFeedbacks(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    //  customer feedbacks count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getCustomerFeedbackCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.customerFeedbackManager.countCustomerFeedbacks(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get customer feedback by id
    @GetMapping("/{id}")
    public ResponseEntity<DOCustomerFeedback> getCustomerFeedbackById(@PathVariable(value = "id") String customerFeedbackId) {
        try {
            return new ResponseEntity(this.customerFeedbackManager.getCustomerFeedbackById(customerFeedbackId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create customer feedback
    @PostMapping
    public ResponseEntity<DOCustomerFeedback> createCustomerFeedback(@RequestBody DOCustomerFeedback customerFeedback) {
        try {
            return new ResponseEntity(this.customerFeedbackManager.createCustomerFeedback(customerFeedback), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete customer feedback by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOCustomerFeedback> deleteCustomerFeedback(@PathVariable("id") String customerFeedbackId) {
        try {
            return new ResponseEntity(this.customerFeedbackManager.deleteCustomerFeedback(customerFeedbackId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update customer feedback
    @PutMapping
    public ResponseEntity<DOCustomerFeedback> updateCustomerFeedback(@RequestBody DOCustomerFeedback customerFeedback) {
        try {
            return new ResponseEntity(this.customerFeedbackManager.updateCustomerFeedback(customerFeedback), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
