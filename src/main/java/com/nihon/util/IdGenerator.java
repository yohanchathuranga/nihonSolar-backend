/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.util;

import com.nihon.entity.DOSysNextId;
import com.nihon.repository.SysNextIdRepository;
import java.util.UUID;
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

//    public String generateId(String type, long date) {
//        String id = "";
//        try {
//
//            String month = DateTimeUtil.getStringMonth(date).toUpperCase();
//            DOSysNextId sysNextId = getId(type);
//            int nextId = sysNextId.getNextId();
//
//            if (!sysNextId.getPrefix().equals(month)) {
//                sysNextIdRepository.setPrefix(month.toUpperCase(), type);
//                setId(type, 1);
//                nextId = 1;
//            }
//
//            String year = DateTimeUtil.getStringYear(date);
//            id = "NS-" + month.toUpperCase() + "-" + year + "-" + String.format("%03d", nextId);
//            setId(type, nextId + 1);
//            System.out.println("Id : " + id);
//
//        } catch (DoesNotExistException ex) {
//            System.out.println("Time get Error");
//        }
//        return id;
//    }
    public DOSysNextId getId(String type) {
        DOSysNextId sysNextId = sysNextIdRepository.getNextId(type);
        return sysNextId;
    }

    public void setId(String type, int id) {
        sysNextIdRepository.setNextId(id, type);
    }

    public String generateId(String type, long date) {
        String id = "";
        String prefix = "";
        DOSysNextId sysNextId = null;
        int nextId = 0;
        try {

            String month = DateTimeUtil.getStringMonth(date).toUpperCase();
            String year = DateTimeUtil.getStringYear(date);

            prefix = month.toUpperCase() + "-" + year;

            if (sysNextIdRepository.isExistsByPrefix(prefix)) {
                sysNextId = sysNextIdRepository.getNextIdByPrefix(type, prefix);
                nextId = sysNextId.getNextId();
            } else {
                sysNextIdRepository.insert(UUID.randomUUID().toString(), 1, prefix, DataUtil.STATE_NEW, type);
                nextId = 1;
            }

            id = "NS-" + prefix + "-" + String.format("%03d", nextId);
            sysNextIdRepository.setNextIdByPrefix(nextId + 1, type, prefix);
            System.out.println("Id : " + id);

        } catch (DoesNotExistException ex) {
            System.out.println("Time get Error");
        }
        return id;
    }
}
