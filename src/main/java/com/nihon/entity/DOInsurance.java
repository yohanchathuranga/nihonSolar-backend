package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "insurance")
public class DOInsurance implements Serializable {

    @Id
    private String id;
    private String projectId;
    private long applyDate;
    private int amountPerAnum;
    private long receivedDate;
    private String year;
    private String status;
    private boolean deleted;


    public DOInsurance() {
    }

    public DOInsurance(String id, String projectId, long applyDate, int amountPerAnum, long receivedDate, String year, String status, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.applyDate = applyDate;
        this.amountPerAnum = amountPerAnum;
        this.receivedDate = receivedDate;
        this.year = year;
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

    public long getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(long applyDate) {
        this.applyDate = applyDate;
    }

    public int getAmountPerAnum() {
        return amountPerAnum;
    }

    public void setAmountPerAnum(int amountPerAnum) {
        this.amountPerAnum = amountPerAnum;
    }

    public long getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(long receivedDate) {
        this.receivedDate = receivedDate;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
}
