package com.nihon.controller;

import com.nihon.base.NotificationManager;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DONotification;
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
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationManager notificationManager;

    @Autowired
    public NotificationController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    //  notification list
    @PostMapping("/list")
    public ResponseEntity<List<DONotification>> getNotificationList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.notificationManager.listNotifications(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    //notification count
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getNotificationCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.notificationManager.countNotifications(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get notification by id
    @GetMapping("/{id}")
    public ResponseEntity<DONotification> getNotificationById(@PathVariable(value = "id") String notificationId) {
        try {
            return new ResponseEntity(this.notificationManager.getNotificationById(notificationId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create notification
    @PostMapping
    public ResponseEntity<DONotification> createNotification(@RequestBody DONotification notification) {
        try {
            return new ResponseEntity(this.notificationManager.createNotification(notification), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete notification by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DONotification> deleteNotification(@PathVariable("id") String notificationId) {
        try {
            return new ResponseEntity(this.notificationManager.deleteNotification(notificationId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update notification
    @PutMapping
    public ResponseEntity<DONotification> updateNotification(@RequestBody DONotification notification) {
        try {
            return new ResponseEntity(this.notificationManager.updateNotification(notification), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    // get notification by id
    @GetMapping("/read/{id}")
    public ResponseEntity<DONotification> readNotification(@PathVariable(value = "id") String notificationId) {
        try {
            return new ResponseEntity(this.notificationManager.readNotification(notificationId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
