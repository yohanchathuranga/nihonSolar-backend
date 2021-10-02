package com.nihon.controller;

import com.nihon.base.ComplainManager;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOComplain;
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
@RequestMapping("/complains")
public class ComplainController {

    private final ComplainManager complainManager;

    @Autowired
    public ComplainController(ComplainManager complainManager) {
        this.complainManager = complainManager;
    }

    // complain list
    @PostMapping("/list")
    public ResponseEntity<List<DOComplain>> getComplainList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.complainManager.listComplains(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    //complain count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getComplainCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.complainManager.countComplains(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get complain by id
    @GetMapping("/{id}")
    public ResponseEntity<DOComplain> getComplainById(@PathVariable(value = "id") String complainId) {
        try {
            return new ResponseEntity(this.complainManager.getComplainById(complainId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create complain
    @PostMapping
    public ResponseEntity<DOComplain> createComplain(@RequestBody DOComplain complain) {
        try {
            return new ResponseEntity(this.complainManager.createComplain(complain), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete complain by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOComplain> deleteComplain(@PathVariable("id") String complainId) {
        try {
            return new ResponseEntity(this.complainManager.deleteComplain(complainId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update complain
    @PutMapping
    public ResponseEntity<DOComplain> updateComplain(@RequestBody DOComplain complain) {
        try {
            return new ResponseEntity(this.complainManager.updateComplain(complain), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
