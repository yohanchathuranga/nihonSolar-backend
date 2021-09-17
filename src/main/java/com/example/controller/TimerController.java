/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.controller;

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

    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTask() {
//        System.out.println("Fixed rate task - " + System.currentTimeMillis() / 1000);
    }
}
