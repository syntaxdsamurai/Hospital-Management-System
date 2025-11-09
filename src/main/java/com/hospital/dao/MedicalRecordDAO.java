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

    private static String DATA_DIR = "data"; // Default for local testing
    private File file;

    // Static method to set the data directory (called from a servlet at startup)
    public static void setDataDirectory(String dataDir) {
        DATA_DIR = dataDir;
    }

    public MedicalRecordDAO() {
        this.file = new File(DATA_DIR + File.separator + "medical_records.xml");
    }

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