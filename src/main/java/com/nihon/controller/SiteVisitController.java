package com.nihon.controller;

import com.nihon.base.SiteVisitManager;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOSiteVisit;
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
@RequestMapping("/site_visits")
public class SiteVisitController {

    private final SiteVisitManager siteVisitManager;

    @Autowired
    public SiteVisitController(SiteVisitManager siteVisitManager) {
        this.siteVisitManager = siteVisitManager;
    }

    // get all siteVisits
    @PostMapping("/list")
    public ResponseEntity<List<DOSiteVisit>> getSiteVisitList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.siteVisitManager.listSiteVisits(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getSiteVisitCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.siteVisitManager.countSiteVisits(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get siteVisit by id
    @GetMapping("/{id}")
    public ResponseEntity<DOSiteVisit> getSiteVisitById(@PathVariable(value = "id") String siteVisitId) {
        try {
            return new ResponseEntity(this.siteVisitManager.getSiteVisitById(siteVisitId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create siteVisit
    @PostMapping
    public ResponseEntity<DOSiteVisit> createSiteVisit(@RequestBody DOSiteVisit siteVisit) {
        try {
            return new ResponseEntity(this.siteVisitManager.createSiteVisit(siteVisit), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete siteVisit by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOSiteVisit> deleteSiteVisit(@PathVariable("id") String siteVisitId) {
        try {
            return new ResponseEntity(this.siteVisitManager.deleteSiteVisit(siteVisitId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update site visit
    @PutMapping
    public ResponseEntity<DOSiteVisit> updateSiteVisit(@RequestBody DOSiteVisit siteVisit) {
        try {
            return new ResponseEntity(this.siteVisitManager.updateSiteVisit(siteVisit), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
