package com.hospital.servlets;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updatePatientProfile")
public class UpdatePatientProfileServlet extends HttpServlet {

    private PatientDAO patientDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get all form data
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String contact = request.getParameter("contact");
        String medicalHistory = request.getParameter("medicalHistory");
        String password = request.getParameter("password"); // This might be blank

        // Get the existing patient from the session
        HttpSession session = request.getSession(false);
        Patient patient = (Patient) session.getAttribute("user");

        // Create an updated patient object
        Patient updatedPatient = new Patient(id, name, age, contact, medicalHistory, password);

        // Call the DAO to update the XML file
        patientDAO.updatePatient(updatedPatient);

        // Update the patient object in the session
        // This is important so the user sees the new data on their dashboard
        session.setAttribute("user", patientDAO.getPatientById(id));

        // Redirect back to the dashboard
        response.sendRedirect("patient-dashboard.jsp");
    }
}