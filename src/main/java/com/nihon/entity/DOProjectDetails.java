/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.entity;

import java.util.List;

/**
 *
 * @author yohan
 */
public class DOProjectDetails {

    private DOProject project;

    private DOUser customer;

    private DOBankLoan bankLoan;
    private DOClearance clearance;
    private List<DOComplain> complains;
    private List<DOCustomerFeedback> customerFeedbacks;
    private DOInsurance insurance;
    private List<DOPayment> payments;
    private DOProjectElectricityBoard projectElectricityBoard;
    private List<DOQuotation> quotations;
    private List<DOService> services;
    private DOSiteVisit siteVisit;
    private List<DOStatusCheck> statusChecks;

    public DOProjectDetails() {
    }

    public DOProject getProject() {
        return project;
    }

    public void setProject(DOProject project) {
        this.project = project;
    }

    public DOUser getCustomer() {
        return customer;
    }

    public void setCustomer(DOUser customer) {
        this.customer = customer;
    }

    public DOBankLoan getBankLoan() {
        return bankLoan;
    }

    public void setBankLoan(DOBankLoan bankLoan) {
        this.bankLoan = bankLoan;
    }

    public DOClearance getClearance() {
        return clearance;
    }

    public void setClearance(DOClearance clearance) {
        this.clearance = clearance;
    }
    public List<DOComplain> getComplains() {
        return complains;
    }

    public void setComplains(List<DOComplain> complains) {
        this.complains = complains;
    }

    public List<DOCustomerFeedback> getCustomerFeedbacks() {
        return customerFeedbacks;
    }

    public void setCustomerFeedbacks(List<DOCustomerFeedback> customerFeedbacks) {
        this.customerFeedbacks = customerFeedbacks;
    }

    public DOInsurance getInsurance() {
        return insurance;
    }

    public void setInsurance(DOInsurance insurance) {
        this.insurance = insurance;
    }

    public List<DOPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<DOPayment> payments) {
        this.payments = payments;
    }

    public DOProjectElectricityBoard getProjectElectricityBoard() {
        return projectElectricityBoard;
    }

    public void setProjectElectricityBoard(DOProjectElectricityBoard projectElectricityBoard) {
        this.projectElectricityBoard = projectElectricityBoard;
    }

    public List<DOQuotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<DOQuotation> quotations) {
        this.quotations = quotations;
    }

    public List<DOService> getServices() {
        return services;
    }

    public void setServices(List<DOService> services) {
        this.services = services;
    }

    public DOSiteVisit getSiteVisit() {
        return siteVisit;
    }

    public void setSiteVisit(DOSiteVisit siteVisit) {
        this.siteVisit = siteVisit;
    }

    public List<DOStatusCheck> getStatusChecks() {
        return statusChecks;
    }

    public void setStatusChecks(List<DOStatusCheck> statusChecks) {
        this.statusChecks = statusChecks;
    }

    
}
