package com.hospital.dao;

import com.hospital.model.MedicalRecord;
import com.hospital.model.MedicalRecords;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalRecordDAO {

    private static final String FILE_PATH = "data/medical_records.xml";
    private File file = new File(FILE_PATH);

    public List<MedicalRecord> getAllRecords() {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(MedicalRecords.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MedicalRecords recordsWrapper = (MedicalRecords) unmarshaller.unmarshal(file);
            return recordsWrapper.getRecordList() != null ? recordsWrapper.getRecordList() : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveAllRecords(List<MedicalRecord> recordList) {
        try {
            MedicalRecords recordsWrapper = new MedicalRecords();
            recordsWrapper.setRecordList(recordList);

            JAXBContext context = JAXBContext.newInstance(MedicalRecords.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            marshaller.marshal(recordsWrapper, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRecord(MedicalRecord record) {
        List<MedicalRecord> records = getAllRecords();
        records.add(record);
        saveAllRecords(records);
    }

    public MedicalRecord getRecordByAppointmentId(String appointmentId) {
        return getAllRecords().stream()
                .filter(record -> record.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElse(null);
    }

    public List<MedicalRecord> getRecordsByPatientId(String patientId) {
        return getAllRecords().stream()
                .filter(record -> record.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
}