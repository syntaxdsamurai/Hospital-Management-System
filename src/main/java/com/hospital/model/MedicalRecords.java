package com.hospital.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "medicalRecords")
@XmlAccessorType(XmlAccessType.FIELD)
public class MedicalRecords {

    @XmlElement(name = "medicalRecord")
    private List<MedicalRecord> recordList;

    public List<MedicalRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<MedicalRecord> recordList) {
        this.recordList = recordList;
    }
}