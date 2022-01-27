/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.util;

import com.nihon.entity.DOMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 *
 * @author yohan
 */
@Service
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(DOMailSender mailSender) {

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(mailSender.getReceiver().toArray(new String[1]));

            msg.setSubject(mailSender.getSubject());
            msg.setText(mailSender.getMessage());

            javaMailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Mail sender error: " + e.getMessage());
            throw e;
        }

    }
}
