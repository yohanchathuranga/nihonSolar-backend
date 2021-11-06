package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "site_visit")
public class DOSiteVisit implements Serializable {

    @Id
    private String id;
    private String projectId;
    private long visitedDate;
    private String visitBy;
    private String visitBy2;
    private String roofType;
    private int noOfStories;
    private String remarks;
    private String status;
    private boolean deleted;

    public DOSiteVisit() {
    }

    public DOSiteVisit(String id, String projectId, long visitedDate, String visitBy, String visitBy2, String roofType, int noOfStories, String remarks, String status) {
        this.id = id;
        this.projectId = projectId;
        this.visitedDate = visitedDate;
        this.visitBy = visitBy;
        this.visitBy2 = visitBy2;
        this.roofType = roofType;
        this.noOfStories = noOfStories;
        this.remarks = remarks;
        this.status = status;
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

    public long getVisitedDate() {
        return visitedDate;
    }

    public void setVisitedDate(long visitedDate) {
        this.visitedDate = visitedDate;
    }

    public String getVisitBy() {
        return visitBy;
    }

    public void setVisitBy(String visitBy) {
        this.visitBy = visitBy;
    }

    public String getRoofType() {
        return roofType;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }

    public int getNoOfStories() {
        return noOfStories;
    }

    public void setNoOfStories(int noOfStories) {
        this.noOfStories = noOfStories;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getVisitBy2() {
        return visitBy2;
    }

    public void setVisitBy2(String visitBy2) {
        this.visitBy2 = visitBy2;
    }

}
