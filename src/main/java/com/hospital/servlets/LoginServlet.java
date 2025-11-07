package com.hospital.servlets;

import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Doctor;
import com.hospital.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private PatientDAO patientDAO;
    private DoctorDAO doctorDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
        doctorDAO = new DoctorDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        HttpSession session = request.getSession();

        if ("admin".equals(role)) {
            if ("admin".equals(id) && "admin".equals(password)) {
                session.setAttribute("user", "admin");
                session.setAttribute("role", "admin");
                response.sendRedirect("admin-panel.jsp");
            } else {
                request.setAttribute("error", "Invalid Admin credentials");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        } else if ("patient".equals(role)) {
            Patient patient = patientDAO.getPatientByContact(id);
            if (patient != null && patient.getPassword().equals(password)) {
                session.setAttribute("user", patient);
                session.setAttribute("role", "patient");
                response.sendRedirect("patient-dashboard.jsp"); // We will create this page next
            } else {
                request.setAttribute("error", "Invalid Patient contact or password");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        } else if ("doctor".equals(role)) {
            Doctor doctor = doctorDAO.getDoctorByName(id);
            if (doctor != null && doctor.getPassword().equals(password)) {
                session.setAttribute("user", doctor);
                session.setAttribute("role", "doctor");
                response.sendRedirect("doctor-dashboard.jsp");
            } else {
                request.setAttribute("error", "Invalid Doctor name or password");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        } else {
            request.setAttribute("error", "Invalid role selected");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}