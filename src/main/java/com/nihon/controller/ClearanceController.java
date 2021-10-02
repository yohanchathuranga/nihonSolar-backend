package com.nihon.controller;

import com.nihon.base.ClearanceManager;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOClearance;
import com.nihon.entity.DOClearanceCollectedDate;
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
@RequestMapping("/clearances")
public class ClearanceController {

    private final ClearanceManager clearanceManager;

    @Autowired
    public ClearanceController(ClearanceManager clearanceManager) {
        this.clearanceManager = clearanceManager;
    }

    // list clearances
    @PostMapping("/list")
    public ResponseEntity<List<DOClearance>> getClearanceList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.clearanceManager.listClearances(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // count clearance
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getClearanceCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.clearanceManager.countClearances(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get clearance by id
    @GetMapping("/{id}")
    public ResponseEntity<DOClearance> getClearanceById(@PathVariable(value = "id") String clearanceId) {
        try {
            return new ResponseEntity(this.clearanceManager.getClearanceById(clearanceId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create clearance
    @PostMapping
    public ResponseEntity<DOClearance> createClearance(@RequestBody DOClearance clearance) {
        try {
            return new ResponseEntity(this.clearanceManager.createClearance(clearance), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete clearance by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOClearance> deleteClearance(@PathVariable("id") String clearanceId) {
        try {
            return new ResponseEntity(this.clearanceManager.deleteClearance(clearanceId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update project
    @PutMapping
    public ResponseEntity<DOClearance> updateClearance(@RequestBody DOClearance clearance) {
        try {
            return new ResponseEntity(this.clearanceManager.updateClearance(clearance), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/set_collected_date")
    public ResponseEntity<List<DOClearance>> setCollectedDate(@RequestBody DOClearanceCollectedDate clearanceCollectedDate) {
        try {
            return new ResponseEntity(this.clearanceManager.setCollecteDate(clearanceCollectedDate), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
