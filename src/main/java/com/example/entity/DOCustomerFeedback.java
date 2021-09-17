package com.example.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_feedback")
public class DOCustomerFeedback implements Serializable {

    @Id
    private String id;
    private String projectId;
    private String customerId;
    private String feedback;
    private int weekNo;
    private String officerId;
    private long actualDate;
    private long dateReceived;   
    private String status;
    private boolean deleted;

    public DOCustomerFeedback() {
    }

    public DOCustomerFeedback(String id, String projectId, String customerId, String feedback, int weekNo, String officerId, long actualDate, long dateReceived, String status, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.customerId = customerId;
        this.feedback = feedback;
        this.weekNo = weekNo;
        this.officerId = officerId;
        this.actualDate = actualDate;
        this.dateReceived = dateReceived;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public String getOfficerId() {
        return officerId;
    }

    public void setOfficerId(String officerId) {
        this.officerId = officerId;
    }

    public long getActualDate() {
        return actualDate;
    }

    public void setActualDate(long actualDate) {
        this.actualDate = actualDate;
    }

    public long getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(long dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}
