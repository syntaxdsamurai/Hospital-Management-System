package com.hospital.dao;

import com.hospital.model.Doctor;
import com.hospital.model.Doctors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDAO {

    private static final String FILE_PATH = "data/doctors.xml";
    private File file = new File(FILE_PATH);

    public List<Doctor> getAllDoctors() {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Doctors.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Doctors doctorsWrapper = (Doctors) unmarshaller.unmarshal(file);
            return doctorsWrapper.getDoctorList() != null ? doctorsWrapper.getDoctorList() : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveAllDoctors(List<Doctor> doctorList) {
        try {
            Doctors doctorsWrapper = new Doctors();
            doctorsWrapper.setDoctorList(doctorList);

            JAXBContext context = JAXBContext.newInstance(Doctors.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            marshaller.marshal(doctorsWrapper, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDoctor(Doctor doctor) {
        List<Doctor> doctors = getAllDoctors();
        doctors.add(doctor);
        saveAllDoctors(doctors);
    }

    public Doctor getDoctorById(String id) {
        List<Doctor> doctors = getAllDoctors();
        for (Doctor doctor : doctors) {
            if (doctor.getId().equals(id)) {
                return doctor;
            }
        }
        return null;
    }

    public void updateDoctor(Doctor updatedDoctor) {
        List<Doctor> doctors = getAllDoctors();
        Optional<Doctor> existingDoctor = doctors.stream()
                .filter(d -> d.getId().equals(updatedDoctor.getId()))
                .findFirst();

        if (existingDoctor.isPresent()) {
            Doctor d = existingDoctor.get();
            d.setName(updatedDoctor.getName());
            d.setSpecialization(updatedDoctor.getSpecialization());
            if (updatedDoctor.getPassword() != null && !updatedDoctor.getPassword().isEmpty()) {
                d.setPassword(updatedDoctor.getPassword());
            }
            saveAllDoctors(doctors);
        }
    }

    public void deleteDoctor(String id) {
        List<Doctor> doctors = getAllDoctors();
        doctors.removeIf(doctor -> doctor.getId().equals(id));
        saveAllDoctors(doctors);
    }

    public Doctor getDoctorByName(String name) {
        List<Doctor> doctors = getAllDoctors();
        for (Doctor doctor : doctors) {
            if (doctor.getName().equals(name)) {
                return doctor;
            }
        }
        return null;
    }
}