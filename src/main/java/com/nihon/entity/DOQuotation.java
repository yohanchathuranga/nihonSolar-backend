package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quotation")
public class DOQuotation implements Serializable {

    @Id
    private String id;
    private String projectId;
    private String userId;
    private long issuedDate;
    private String sendMode;
    private String inventerType;
    private String panelType;
    private String acDcSpd;
    private int systemPrice;
    private boolean finalized;
    private String status;
    private boolean deleted;

    public DOQuotation() {
    }

    public DOQuotation(String id, String projectId, String userId, long issuedDate, String sendMode, String inventerType, String panelType, String acDcSpd, int systemPrice, boolean finalized, String status, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.issuedDate = issuedDate;
        this.sendMode = sendMode;
        this.inventerType = inventerType;
        this.panelType = panelType;
        this.acDcSpd = acDcSpd;
        this.systemPrice = systemPrice;
        this.finalized = finalized;
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

    public long getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(long issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getSendMode() {
        return sendMode;
    }

    public void setSendMode(String sendMode) {
        this.sendMode = sendMode;
    }

    public String getInventerType() {
        return inventerType;
    }

    public void setInventerType(String inventerType) {
        this.inventerType = inventerType;
    }

    public String getPanelType() {
        return panelType;
    }

    public void setPanelType(String panelType) {
        this.panelType = panelType;
    }

    public String getAcDcSpd() {
        return acDcSpd;
    }

    public void setAcDcSpd(String acDcSpd) {
        this.acDcSpd = acDcSpd;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
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

    public int getSystemPrice() {
        return systemPrice;
    }

    public void setSystemPrice(int systemPrice) {
        this.systemPrice = systemPrice;
    }

}
