/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.entity;

/**
 *
 * @author yohan
 */
public class DOStatusChecked {
    private String statusCheckId;
    private long checkedDate;

    public String getStatusCheckId() {
        return statusCheckId;
    }

    public void setStatusCheckId(String statusCheckId) {
        this.statusCheckId = statusCheckId;
    }

    public long getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(long checkedDate) {
        this.checkedDate = checkedDate;
    }
    
}
