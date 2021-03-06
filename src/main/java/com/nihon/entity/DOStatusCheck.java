package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "status_check")
public class DOStatusCheck implements Serializable {

    @Id
    private String id;
    private String projectId;
    private String type;
    private String remark;
    private int checkNo;
    private long actualDate;
    private long checkedDate;
    private String status;
    private boolean deleted;


    public DOStatusCheck() {
    }

    public DOStatusCheck(String id, String projectId, String type, String remark, int checkNo, long actualDate, long checkedDate, String status) {
        this.id = id;
        this.projectId = projectId;
        this.type = type;
        this.remark = remark;
        this.checkNo = checkNo;
        this.actualDate = actualDate;
        this.checkedDate = checkedDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(int checkNo) {
        this.checkNo = checkNo;
    }

    public long getActualDate() {
        return actualDate;
    }

    public void setActualDate(long actualDate) {
        this.actualDate = actualDate;
    }

    public long getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(long checkedDate) {
        this.checkedDate = checkedDate;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
