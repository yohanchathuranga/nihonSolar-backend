package com.nihon.controller;

import com.nihon.base.BankLoanManager;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOBankLoan;
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
@RequestMapping("/bank_loans")
public class BankLoanController {

    private final BankLoanManager bankLoanManager;

    @Autowired
    public BankLoanController(BankLoanManager bankLoanManager) {
        this.bankLoanManager = bankLoanManager;
    }

    // get all bank loan
    @PostMapping("/list")
    public ResponseEntity<List<DOBankLoan>> getBankLoanList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.bankLoanManager.listBankLoans(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // bank loan count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getBankLoanCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.bankLoanManager.countBankLoans(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get bank loan by id
    @GetMapping("/{id}")
    public ResponseEntity<DOBankLoan> getBankLoanById(@PathVariable(value = "id") String bankLoanId) {
        try {
            return new ResponseEntity(this.bankLoanManager.getBankLoanById(bankLoanId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create bank loan
    @PostMapping
    public ResponseEntity<DOBankLoan> createBankLoan(@RequestBody DOBankLoan bankLoan) {
        try {
            return new ResponseEntity(this.bankLoanManager.createBankLoan(bankLoan), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete bank loan by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOBankLoan> deleteBankLoan(@PathVariable("id") String bankLoanId) {
        try {
            return new ResponseEntity(this.bankLoanManager.deleteBankLoan(bankLoanId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update bank loan visit
    @PutMapping
    public ResponseEntity<DOBankLoan> updateBankLoan(@RequestBody DOBankLoan bankLoan) {
        try {
            return new ResponseEntity(this.bankLoanManager.updateBankLoan(bankLoan), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/approve")
    public ResponseEntity<DOBankLoan> approveBankLoan(@RequestBody DOBankLoan bankLoan) {
        try {
            return new ResponseEntity(this.bankLoanManager.approve(bankLoan), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/cancel/{id}")
    public ResponseEntity<DOBankLoan> cancelBankLoan(@PathVariable(value = "id") String bankLoanId) {
        try {
            return new ResponseEntity(this.bankLoanManager.cancel(bankLoanId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
