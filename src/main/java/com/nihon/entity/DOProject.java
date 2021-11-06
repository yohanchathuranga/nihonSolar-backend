package com.nihon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project")
public class DOProject implements Serializable {

    @Id
    private String id;
    private String userId;
    private String maketingOfficer;
    private String contactMode;
    private String electricityProvider;
    private int avgElectricityBill;
    private String connectionType;
    private String paymentMethod;
    private double systemVolume;
    private int systemPrice;
    private int outstandingPayment;
    private long systemInstallationDate;
    private long systemInstallationDate1;
    private long connectionFileSubmitDate;
    private long customerAppDate;
    private long insuranceAgreementDate;
    private long createdDate;
    private String status;
    private boolean deleted;

    public DOProject() {
    }    

    public DOProject(String id, String userId, String maketingOfficer, String contactMode, String electricityProvider, int avgElectricityBill, String connectionType, String paymentMethod, double systemVolume, int systemPrice, int outstandingPayment, long systemInstallationDate, long systemInstallationDate1, long connectionFileSubmitDate, long customerAppDate, long insuranceAgreementDate, long createdDate, String status) {
        this.id = id;
        this.userId = userId;
        this.maketingOfficer = maketingOfficer;
        this.contactMode = contactMode;
        this.electricityProvider = electricityProvider;
        this.avgElectricityBill = avgElectricityBill;
        this.connectionType = connectionType;
        this.paymentMethod = paymentMethod;
        this.systemVolume = systemVolume;
        this.systemPrice = systemPrice;
        this.outstandingPayment = outstandingPayment;
        this.systemInstallationDate = systemInstallationDate;
        this.systemInstallationDate1 = systemInstallationDate1;
        this.connectionFileSubmitDate = connectionFileSubmitDate;
        this.customerAppDate = customerAppDate;
        this.insuranceAgreementDate = insuranceAgreementDate;
        this.createdDate = createdDate;
        this.status = status;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMaketingOfficer() {
        return maketingOfficer;
    }

    public void setMaketingOfficer(String maketingOfficer) {
        this.maketingOfficer = maketingOfficer;
    }

    public String getContactMode() {
        return contactMode;
    }

    public void setContactMode(String contactMode) {
        this.contactMode = contactMode;
    }

    public String getElectricityProvider() {
        return electricityProvider;
    }

    public void setElectricityProvider(String electricityProvider) {
        this.electricityProvider = electricityProvider;
    }

    public int getAvgElectricityBill() {
        return avgElectricityBill;
    }

    public void setAvgElectricityBill(int avgElectricityBill) {
        this.avgElectricityBill = avgElectricityBill;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getSystemVolume() {
        return systemVolume;
    }

    public void setSystemVolume(double systemVolume) {
        this.systemVolume = systemVolume;
    }

    public int getSystemPrice() {
        return systemPrice;
    }

    public void setSystemPrice(int systemPrice) {
        this.systemPrice = systemPrice;
    }

    public int getOutstandingPayment() {
        return outstandingPayment;
    }

    public void setOutstandingPayment(int outstandingPayment) {
        this.outstandingPayment = outstandingPayment;
    }

    public long getSystemInstallationDate() {
        return systemInstallationDate;
    }

    public void setSystemInstallationDate(long systemInstallationDate) {
        this.systemInstallationDate = systemInstallationDate;
    }

    public long getConnectionFileSubmitDate() {
        return connectionFileSubmitDate;
    }

    public void setConnectionFileSubmitDate(long connectionFileSubmitDate) {
        this.connectionFileSubmitDate = connectionFileSubmitDate;
    }

    public long getCustomerAppDate() {
        return customerAppDate;
    }

    public void setCustomerAppDate(long customerAppDate) {
        this.customerAppDate = customerAppDate;
    }

    public long getInsuranceAgreementDate() {
        return insuranceAgreementDate;
    }

    public void setInsuranceAgreementDate(long insuranceAgreementDate) {
        this.insuranceAgreementDate = insuranceAgreementDate;
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

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getSystemInstallationDate1() {
        return systemInstallationDate1;
    }

    public void setSystemInstallationDate1(long systemInstallationDate1) {
        this.systemInstallationDate1 = systemInstallationDate1;
    }
    
}
