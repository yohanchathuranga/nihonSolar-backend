package com.example.controller;

import com.example.base.InsuranceManager;
import com.example.entity.DOCountRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOInsurance;
import com.yohan.exceptions.CustomException;
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
@RequestMapping("/insurances")
public class InsuranceController {

    private final InsuranceManager insuranceManager;

    @Autowired
    public InsuranceController(InsuranceManager insuranceManager) {
        this.insuranceManager = insuranceManager;
    }

    //  insurance list
    @PostMapping("/list")
    public ResponseEntity<List<DOInsurance>> getInsuranceList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.insuranceManager.listInsurances(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    //insurance count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getInsuranceCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.insuranceManager.countInsurances(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get insurance by id
    @GetMapping("/{id}")
    public ResponseEntity<DOInsurance> getInsuranceById(@PathVariable(value = "id") String insuranceId) {
        try {
            return new ResponseEntity(this.insuranceManager.getInsuranceById(insuranceId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create insurance
    @PostMapping
    public ResponseEntity<DOInsurance> createInsurance(@RequestBody DOInsurance insurance) {
        try {
            return new ResponseEntity(this.insuranceManager.createInsurance(insurance), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete insurance by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOInsurance> deleteInsurance(@PathVariable("id") String insuranceId) {
        try {
            return new ResponseEntity(this.insuranceManager.deleteInsurance(insuranceId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update insurance
    @PutMapping
    public ResponseEntity<DOInsurance> updateInsurance(@RequestBody DOInsurance insurance) {
        try {
            return new ResponseEntity(this.insuranceManager.updateInsurance(insurance), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
