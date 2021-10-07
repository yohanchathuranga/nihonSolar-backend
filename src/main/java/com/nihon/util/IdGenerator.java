/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.util;

import com.nihon.entity.DOSysNextId;
import com.nihon.repository.SysNextIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yohan.exceptions.DoesNotExistException;

/**
 *
 * @author yohan
 */
@Service
public class IdGenerator {

    @Autowired
    SysNextIdRepository sysNextIdRepository;

    public String generateId(String type, long date) {
        String id = "";
        try {

            String month = DateTimeUtil.getStringMonth(date);
            DOSysNextId sysNextId = getId(type);
            int nextId = sysNextId.getNextId();

            if (!sysNextId.getPrefix().equals(month)) {
                sysNextIdRepository.setPrefix(month.toUpperCase(), type);
                setId(type, 1);
                nextId = 1;
            }

            String year = DateTimeUtil.getStringYear(date);
            id = "NS-" + month.toUpperCase() + "-" + year + "-" + String.format("%08d", nextId);
            setId(type, nextId + 1);
            System.out.println("Id : " + id);

        } catch (DoesNotExistException ex) {
            System.out.println("Time get Error");
        }
        return id;
    }

    public DOSysNextId getId(String type) {
        DOSysNextId sysNextId = sysNextIdRepository.getNextId(type);
        return sysNextId;
    }

    public void setId(String type, int id) {
        sysNextIdRepository.setNextId(id, type);
    }

}
