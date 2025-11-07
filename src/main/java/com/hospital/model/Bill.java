package com.hospital.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bill")
@XmlAccessorType(XmlAccessType.FIELD)
public class Bill {

    @XmlElement
    private String billId;

    @XmlElement
    private String appointmentId;

    @XmlElement
    private String patientId;

    @XmlElement
    private double amount;

    @XmlElement
    private String status; // e.g., "Pending", "Paid"

    public Bill() {
    }

    public Bill(String billId, String appointmentId, String patientId, double amount, String status) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.amount = amount;
        this.status = status;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}