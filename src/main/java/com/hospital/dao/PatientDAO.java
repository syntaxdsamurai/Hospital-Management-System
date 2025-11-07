package com.hospital.dao;

import com.hospital.model.Patient;
import com.hospital.model.Patients;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDAO {

    private static final String FILE_PATH = "data/patients.xml";
    private File file = new File(FILE_PATH);

    public List<Patient> getAllPatients() {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Patients.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Patients patientsWrapper = (Patients) unmarshaller.unmarshal(file);
            return patientsWrapper.getPatientList() != null ? patientsWrapper.getPatientList() : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveAllPatients(List<Patient> patientList) {
        try {
            Patients patientsWrapper = new Patients();
            patientsWrapper.setPatientList(patientList);

            JAXBContext context = JAXBContext.newInstance(Patients.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            marshaller.marshal(patientsWrapper, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPatient(Patient patient) {
        List<Patient> patients = getAllPatients();
        patients.add(patient);
        saveAllPatients(patients);
    }

    public Patient getPatientById(String id) {
        List<Patient> patients = getAllPatients();
        for (Patient patient : patients) {
            if (patient.getId().equals(id)) {
                return patient;
            }
        }
        return null;
    }

    public void updatePatient(Patient updatedPatient) {
        List<Patient> patients = getAllPatients();
        Optional<Patient> existingPatient = patients.stream()
                .filter(p -> p.getId().equals(updatedPatient.getId()))
                .findFirst();

        if (existingPatient.isPresent()) {
            Patient p = existingPatient.get();
            p.setName(updatedPatient.getName());
            p.setAge(updatedPatient.getAge());
            p.setContact(updatedPatient.getContact());
            p.setMedicalHistory(updatedPatient.getMedicalHistory());
            if (updatedPatient.getPassword() != null && !updatedPatient.getPassword().isEmpty()) {
                p.setPassword(updatedPatient.getPassword());
            }
            saveAllPatients(patients);
        }
    }

    public void deletePatient(String id) {
        List<Patient> patients = getAllPatients();
        patients.removeIf(patient -> patient.getId().equals(id));
        saveAllPatients(patients);
    }

    public Patient getPatientByContact(String contact) {
        List<Patient> patients = getAllPatients();
        for (Patient patient : patients) {
            if (patient.getContact().equals(contact)) {
                return patient;
            }
        }
        return null;
    }
}