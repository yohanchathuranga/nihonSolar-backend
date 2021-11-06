package com.nihon.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project_quotation")
public class DOProjectQuotation implements Serializable {

    @Id
    private String id;
    private String projectId;
    private String systemVolume;
    private String inventorType;
    private String panelType;
    private String panelVolume;
    private String paymentMethod;
    private int systemPrice;
    private boolean selected;
    private boolean deleted;

    public DOProjectQuotation() {
    } 

    public DOProjectQuotation(String id, String projectId, String systemVolume, String inventorType, String panelType, String panelVolume, String paymentMethod, int systemPrice, boolean selected, boolean deleted) {
        this.id = id;
        this.projectId = projectId;
        this.systemVolume = systemVolume;
        this.inventorType = inventorType;
        this.panelType = panelType;
        this.panelVolume = panelVolume;
        this.paymentMethod = paymentMethod;
        this.systemPrice = systemPrice;
        this.selected = selected;
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

    public String getSystemVolume() {
        return systemVolume;
    }

    public void setSystemVolume(String systemVolume) {
        this.systemVolume = systemVolume;
    }

    public String getInventerType() {
        return inventorType;
    }

    public void setInventorType(String inventorType) {
        this.inventorType = inventorType;
    }

    public String getPanelType() {
        return panelType;
    }

    public void setPanelType(String panelType) {
        this.panelType = panelType;
    }

    public String getPanelVolume() {
        return panelVolume;
    }

    public void setPanelVolume(String panelVolume) {
        this.panelVolume = panelVolume;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getSystemPrice() {
        return systemPrice;
    }

    public void setSystemPrice(int systemPrice) {
        this.systemPrice = systemPrice;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
}
