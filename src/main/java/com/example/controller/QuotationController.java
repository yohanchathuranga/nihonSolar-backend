package com.example.controller;

import com.example.base.QuotationManager;
import com.example.entity.DOCountRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOQuotation;
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
@RequestMapping("/quotations")
public class QuotationController {

    private final QuotationManager quotationManager;

    @Autowired
    public QuotationController(QuotationManager quotationManager) {
        this.quotationManager = quotationManager;
    }

    //  quotation list
    @PostMapping("/list")
    public ResponseEntity<List<DOQuotation>> getQuotationList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.quotationManager.listQuotations(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    //quotation count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getQuotationCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.quotationManager.countQuotations(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get quotation by id
    @GetMapping("/{id}")
    public ResponseEntity<DOQuotation> getQuotationById(@PathVariable(value = "id") String quotationId) {
        try {
            return new ResponseEntity(this.quotationManager.getQuotationById(quotationId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create quotation
    @PostMapping
    public ResponseEntity<DOQuotation> createQuotation(@RequestBody DOQuotation quotation) {
        try {
            return new ResponseEntity(this.quotationManager.createQuotation(quotation), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete quotation by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOQuotation> deleteQuotation(@PathVariable("id") String quotationId) {
        try {
            return new ResponseEntity(this.quotationManager.deleteQuotation(quotationId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update quotation
    @PutMapping
    public ResponseEntity<DOQuotation> updateQuotation(@RequestBody DOQuotation quotation) {
        try {
            return new ResponseEntity(this.quotationManager.updateQuotation(quotation), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
