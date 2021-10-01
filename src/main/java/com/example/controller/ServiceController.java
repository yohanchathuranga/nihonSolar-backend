package com.example.controller;

import com.example.base.ServiceManager;
import com.example.entity.DOCountRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOService;
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
@RequestMapping("/services")
public class ServiceController {

    private final ServiceManager serviceManager;

    @Autowired
    public ServiceController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    //  service list
    @PostMapping("/list")
    public ResponseEntity<List<DOService>> getServiceList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.serviceManager.listServices(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    //service count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getServiceCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.serviceManager.countServices(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get service by id
    @GetMapping("/{id}")
    public ResponseEntity<DOService> getServiceById(@PathVariable(value = "id") String serviceId) {
        try {
            return new ResponseEntity(this.serviceManager.getServiceById(serviceId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create service
    @PostMapping
    public ResponseEntity<DOService> createService(@RequestBody DOService service) {
        try {
            return new ResponseEntity(this.serviceManager.createService(service), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete service by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOService> deleteService(@PathVariable("id") String serviceId) {
        try {
            return new ResponseEntity(this.serviceManager.deleteService(serviceId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update service
    @PutMapping
    public ResponseEntity<DOService> updateService(@RequestBody DOService service) {
        try {
            return new ResponseEntity(this.serviceManager.updateService(service), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
