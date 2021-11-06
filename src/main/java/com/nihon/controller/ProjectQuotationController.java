package com.nihon.controller;

import com.nihon.base.ProjectQuotationManager;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOProjectQuotation;
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
@RequestMapping("/project_quotations")
public class ProjectQuotationController {

    private final ProjectQuotationManager projectQuotationManager;

    @Autowired
    public ProjectQuotationController(ProjectQuotationManager projectQuotationManager) {
        this.projectQuotationManager = projectQuotationManager;
    }

    //  Project Quotation list
    @PostMapping("/list")
    public ResponseEntity<List<DOProjectQuotation>> getProjectQuotationList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.projectQuotationManager.listProjectQuotations(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    //Project Quotation count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getProjectQuotationCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.projectQuotationManager.countProjectQuotations(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get Project Quotation by id
    @GetMapping("/{id}")
    public ResponseEntity<DOProjectQuotation> getProjectQuotationById(@PathVariable(value = "id") String projectQuotationId) {
        try {
            return new ResponseEntity(this.projectQuotationManager.getProjectQuotationById(projectQuotationId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create Project Quotation
    @PostMapping
    public ResponseEntity<DOProjectQuotation> createProjectQuotation(@RequestBody DOProjectQuotation projectQuotation) {
        try {
            return new ResponseEntity(this.projectQuotationManager.createProjectQuotation(projectQuotation), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete Project Quotation by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOProjectQuotation> deleteProjectQuotation(@PathVariable("id") String projectQuotationId) {
        try {
            return new ResponseEntity(this.projectQuotationManager.deleteProjectQuotation(projectQuotationId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update Project Quotation
    @PutMapping
    public ResponseEntity<DOProjectQuotation> updateProjectQuotation(@RequestBody DOProjectQuotation projectQuotation) {
        try {
            return new ResponseEntity(this.projectQuotationManager.updateProjectQuotation(projectQuotation), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    // get Project Quotation by id
    @PostMapping("/select/{id}")
    public ResponseEntity<DOProjectQuotation> finalizeProjectQuotation(@PathVariable(value = "id") String projectQuotationId) {
        try {
            return new ResponseEntity(this.projectQuotationManager.selectProjectQuotation(projectQuotationId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
