package com.example.controller;

import com.example.base.StatusCheckManager;
import com.example.entity.DOCountRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOStatusCheck;
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
@RequestMapping("/status_checks")
public class StatusCheckController {

    private final StatusCheckManager statusCheckManager;

    @Autowired
    public StatusCheckController(StatusCheckManager statusCheckManager) {
        this.statusCheckManager = statusCheckManager;
    }

    // get all bank loan
    @PostMapping("/list")
    public ResponseEntity<List<DOStatusCheck>> getStatusCheckList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.statusCheckManager.listStatusChecks(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // bank loan count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getStatusCheckCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.statusCheckManager.countStatusChecks(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get bank loan by id
    @GetMapping("/{id}")
    public ResponseEntity<DOStatusCheck> getStatusCheckById(@PathVariable(value = "id") String statusCheckId) {
        try {
            return new ResponseEntity(this.statusCheckManager.getStatusCheckById(statusCheckId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create bank loan
    @PostMapping
    public ResponseEntity<DOStatusCheck> createStatusCheck(@RequestBody DOStatusCheck statusCheck) {
        try {
            return new ResponseEntity(this.statusCheckManager.createStatusCheck(statusCheck), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete bank loan by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOStatusCheck> deleteStatusCheck(@PathVariable("id") String statusCheckId) {
        try {
            return new ResponseEntity(this.statusCheckManager.deleteStatusCheck(statusCheckId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update bank loan visit
    @PutMapping
    public ResponseEntity<DOStatusCheck> updateStatusCheck(@RequestBody DOStatusCheck statusCheck) {
        try {
            return new ResponseEntity(this.statusCheckManager.updateStatusCheck(statusCheck), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
