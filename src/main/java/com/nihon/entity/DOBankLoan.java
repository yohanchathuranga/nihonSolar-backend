package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bank_loan")
public class DOBankLoan implements Serializable {

    @Id
    private String id;
    private String projectId;
    private String userId;   
    private String bankName;
    private String branch;
    private long submitDate;
    private String officer;
    private String contactNo;
    private String status;
    private boolean deleted;
    

    public DOBankLoan() {
    }

    public DOBankLoan(String id, String projectId, String userId, String bankName, String branch, long submitDate, String officer, String contactNo, String status, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.bankName = bankName;
        this.branch = branch;
        this.submitDate = submitDate;
        this.officer = officer;
        this.contactNo = contactNo;
        this.status = status;
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public long getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(long submitDate) {
        this.submitDate = submitDate;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}
