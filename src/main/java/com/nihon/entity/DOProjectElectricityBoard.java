package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "electricity_board_process")
public class DOProjectElectricityBoard implements Serializable {

    @Id
    private String id;
    private String projectId;
    private long inspectionDate;
    private long settingDate;
    private long agreementDate;
    private long estimateDate;
    private long connectionDate;
    private long harmonyDate;
    private String inspectionRemark;
    private String settingRemark;
    private String agreementRemark;
    private String estimateRemark;
    private String connectionRemark;
    private String harmonyRemark;
    private String status;
    private boolean deleted;

    public DOProjectElectricityBoard() {
    }

    public DOProjectElectricityBoard(String id, String projectId, long inspectionDate, long settingDate, long agreementDate, long estimateDate, long connectionDate, long harmonyDate, String inspectionRemark, String settingRemark, String agreementRemark, String estimateRemark, String connectionRemark, String harmonyRemark, String status) {
        this.id = id;
        this.projectId = projectId;
        this.inspectionDate = inspectionDate;
        this.settingDate = settingDate;
        this.agreementDate = agreementDate;
        this.estimateDate = estimateDate;
        this.connectionDate = connectionDate;
        this.harmonyDate = harmonyDate;
        this.inspectionRemark = inspectionRemark;
        this.settingRemark = settingRemark;
        this.agreementRemark = agreementRemark;
        this.estimateRemark = estimateRemark;
        this.connectionRemark = connectionRemark;
        this.harmonyRemark = harmonyRemark;
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

    public long getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(long inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public long getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(long settingDate) {
        this.settingDate = settingDate;
    }

    public long getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(long agreementDate) {
        this.agreementDate = agreementDate;
    }

    public long getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(long estimateDate) {
        this.estimateDate = estimateDate;
    }

    public long getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(long connectionDate) {
        this.connectionDate = connectionDate;
    }

    public long getHarmonyDate() {
        return harmonyDate;
    }

    public void setHarmonyDate(long harmonyDate) {
        this.harmonyDate = harmonyDate;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInspectionRemark() {
        return inspectionRemark;
    }

    public void setInspectionRemark(String inspectionRemark) {
        this.inspectionRemark = inspectionRemark;
    }

    public String getSettingRemark() {
        return settingRemark;
    }

    public void setSettingRemark(String settingRemark) {
        this.settingRemark = settingRemark;
    }

    public String getAgreementRemark() {
        return agreementRemark;
    }

    public void setAgreementRemark(String agreementRemark) {
        this.agreementRemark = agreementRemark;
    }

    public String getEstimateRemark() {
        return estimateRemark;
    }

    public void setEstimateRemark(String estimateRemark) {
        this.estimateRemark = estimateRemark;
    }

    public String getConnectionRemark() {
        return connectionRemark;
    }

    public void setConnectionRemark(String connectionRemark) {
        this.connectionRemark = connectionRemark;
    }

    public String getHarmonyRemark() {
        return harmonyRemark;
    }

    public void setHarmonyRemark(String harmonyRemark) {
        this.harmonyRemark = harmonyRemark;
    }
    

}
