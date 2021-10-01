package com.example.controller;

import com.example.base.PaymentManager;
import com.example.entity.DOCountRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOPayment;
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
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentManager paymentManager;

    @Autowired
    public PaymentController(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

    //  payment list
    @PostMapping("/list")
    public ResponseEntity<List<DOPayment>> getPaymentList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.paymentManager.listPayments(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    //payment count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getPaymentCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.paymentManager.countPayments(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get payment by id
    @GetMapping("/{id}")
    public ResponseEntity<DOPayment> getPaymentById(@PathVariable(value = "id") String paymentId) {
        try {
            return new ResponseEntity(this.paymentManager.getPaymentById(paymentId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create payment
    @PostMapping
    public ResponseEntity<DOPayment> createPayment(@RequestBody DOPayment payment) {
        try {
            return new ResponseEntity(this.paymentManager.createPayment(payment), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete payment by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOPayment> deletePayment(@PathVariable("id") String paymentId) {
        try {
            return new ResponseEntity(this.paymentManager.deletePayment(paymentId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update payment
    @PutMapping
    public ResponseEntity<DOPayment> updatePayment(@RequestBody DOPayment payment) {
        try {
            return new ResponseEntity(this.paymentManager.updatePayment(payment), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
