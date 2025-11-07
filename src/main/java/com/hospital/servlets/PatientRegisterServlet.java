package com.hospital.servlets;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/patientRegister")
public class PatientRegisterServlet extends HttpServlet {

    private PatientDAO patientDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String contact = request.getParameter("contact");
        String medicalHistory = request.getParameter("medicalHistory");
        String password = request.getParameter("password");

        String id = "p_" + UUID.randomUUID().toString().substring(0, 8);

        Patient existingPatient = patientDAO.getPatientByContact(contact);

        if (existingPatient != null) {
            request.setAttribute("message", "A patient with this contact number already exists.");
        } else {
            Patient patient = new Patient(id, name, age, contact, medicalHistory, password);
            patientDAO.addPatient(patient);
            request.setAttribute("message", "Registration successful! You can now login.");
        }

        request.getRequestDispatcher("patient-registration.jsp").forward(request, response);
    }
}