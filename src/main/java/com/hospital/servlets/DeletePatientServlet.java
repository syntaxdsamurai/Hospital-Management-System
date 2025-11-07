package com.hospital.servlets;

import com.hospital.dao.PatientDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/deletePatient")
public class DeletePatientServlet extends HttpServlet {

    private PatientDAO patientDAO;

    @Override
    public void init() {
        patientDAO = new PatientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patientId = request.getParameter("id");

        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");

        // Security check: ensure an admin is logged in
        if (!"admin".equals(role)) {
            response.sendRedirect("index.jsp");
            return;
        }

        if (patientId != null) {
            patientDAO.deletePatient(patientId);
        }

        // Redirect back to the admin panel
        response.sendRedirect("admin-panel.jsp");
    }
}