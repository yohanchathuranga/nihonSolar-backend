package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "service")
public class DOService implements Serializable {

    @Id
    private String id;
    private String projectId;
    private long date;
    private int serviceNo;
    private String remarks;
    private String officer;
    private String status;
    private long nextServiceDate;
    private boolean deleted;

    public DOService() {
    }

    public DOService(String id, String projectId, long date, int serviceNo, String remarks, String officer, String status, long nextServiceDate) {
        this.id = id;
        this.projectId = projectId;
        this.date = date;
        this.serviceNo = serviceNo;
        this.remarks = remarks;
        this.officer = officer;
        this.status = status;
        this.nextServiceDate = nextServiceDate;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(int serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
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

    public long getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(long nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

}
