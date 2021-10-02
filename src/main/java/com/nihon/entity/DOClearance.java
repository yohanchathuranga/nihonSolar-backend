package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clearance")
public class DOClearance implements Serializable {

    @Id
    private String id;
    private String projectId;
    private String areaOffice;
    private String contactNo;
    private String officerName;
    private long submitDate;
    private long collectDate;
    private String status;
    private boolean deleted;

    public DOClearance() {
    }

    public DOClearance(String id, String projectId, String areaOffice, String contactNo, String officerName, long checkDate, long submitDate, long collectDate, String status, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.areaOffice = areaOffice;
        this.contactNo = contactNo;
        this.officerName = officerName;
        this.submitDate = submitDate;
        this.collectDate = collectDate;
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

    public String getAreaOffice() {
        return areaOffice;
    }

    public void setAreaOffice(String areaOffice) {
        this.areaOffice = areaOffice;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public long getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(long submitDate) {
        this.submitDate = submitDate;
    }

    public long getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(long collectDate) {
        this.collectDate = collectDate;
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
