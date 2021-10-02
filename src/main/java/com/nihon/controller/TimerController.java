/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.controller;

import com.nihon.base.NotificationManager;
import yohan.exceptions.CustomException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author yohan
 */
@SpringBootApplication
@EnableScheduling
public class TimerController {
    
    private final NotificationManager notificationManager;

    @Autowired
    public TimerController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleFixedRateTask() {
//        try {
//            System.out.println("Fixed rate task - " + System.currentTimeMillis() / 1000);
//            notificationManager.processNotification();
//        } catch (CustomException ex) {
//            System.out.println(ex);
//        }
    }
}
