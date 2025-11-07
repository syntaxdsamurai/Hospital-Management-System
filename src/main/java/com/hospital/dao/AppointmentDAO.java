package com.hospital.dao;

import com.hospital.model.Appointment;
import com.hospital.model.Appointments;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppointmentDAO {

    private static final String FILE_PATH = "data/appointments.xml";
    private File file = new File(FILE_PATH);

    public List<Appointment> getAllAppointments() {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(Appointments.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Appointments appointmentsWrapper = (Appointments) unmarshaller.unmarshal(file);
            return appointmentsWrapper.getAppointmentList() != null ? appointmentsWrapper.getAppointmentList() : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveAllAppointments(List<Appointment> appointmentList) {
        try {
            Appointments appointmentsWrapper = new Appointments();
            appointmentsWrapper.setAppointmentList(appointmentList);

            JAXBContext context = JAXBContext.newInstance(Appointments.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File directory = file.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            marshaller.marshal(appointmentsWrapper, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAppointment(Appointment appointment) {
        List<Appointment> appointments = getAllAppointments();
        appointments.add(appointment);
        saveAllAppointments(appointments);
    }

    public Appointment getAppointmentById(String id) {
        return getAllAppointments().stream()
                .filter(app -> app.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Appointment> getAppointmentsByPatientId(String patientId) {
        return getAllAppointments().stream()
                .filter(app -> app.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        return getAllAppointments().stream()
                .filter(app -> app.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public void updateAppointmentStatus(String id, String newStatus) {
        List<Appointment> appointments = getAllAppointments();
        Optional<Appointment> existingAppointment = appointments.stream()
                .filter(app -> app.getId().equals(id))
                .findFirst();

        if (existingAppointment.isPresent()) {
            existingAppointment.get().setStatus(newStatus);
            saveAllAppointments(appointments);
        }
    }

    public void deleteAppointment(String id) {
        List<Appointment> appointments = getAllAppointments();
        appointments.removeIf(app -> app.getId().equals(id));
        saveAllAppointments(appointments);
    }
}