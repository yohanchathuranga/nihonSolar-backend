package com.example.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment")
public class DOPayment implements Serializable {

    @Id
    private String id;
    private String projectId;
    private long date;
    private int amount;
    private String type;
    private String recordedBy;
    private boolean deleted;

    public DOPayment() {
    }

    public DOPayment(String id, String projectId, long date, int amount, String type, String recordedBy, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.recordedBy = recordedBy;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
