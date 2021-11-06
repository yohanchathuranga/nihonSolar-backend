package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "complain")
public class DOComplain implements Serializable {

    @Id
    private String id;
    private String projectId;
    private long date;
    private String type;
    private String complain;
    private String officer;
    private String officerNote;
    private String solution;
    private String solvedOfficer;
    private long solvedDate;
    private String status;
    private boolean deleted;

    public DOComplain() {
    }

    public DOComplain(String id, String projectId, long date, String type, String complain, String officer, String officerNote, String solution, String status, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.date = date;
        this.type = type;
        this.complain = complain;
        this.officer = officer;
        this.officerNote = officerNote;
        this.solution = solution;
        this.status = status;
        this.deleted = deleted;
    }

    public String getOfficerNote() {
        return officerNote;
    }

    public void setOfficerNote(String officerNote) {
        this.officerNote = officerNote;
    }


    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
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

    public String getSolvedOfficer() {
        return solvedOfficer;
    }

    public void setSolvedOfficer(String solvedOfficer) {
        this.solvedOfficer = solvedOfficer;
    }

    public long getSolvedDate() {
        return solvedDate;
    }

    public void setSolvedDate(long solvedDate) {
        this.solvedDate = solvedDate;
    }
    
    

}
