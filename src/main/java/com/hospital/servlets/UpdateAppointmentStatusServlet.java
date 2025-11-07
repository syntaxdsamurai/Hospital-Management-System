package com.hospital.servlets;

import com.hospital.dao.AppointmentDAO;
import com.hospital.model.Doctor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updateAppointmentStatus")
public class UpdateAppointmentStatusServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appointmentId = request.getParameter("id");
        String newStatus = request.getParameter("status");

        HttpSession session = request.getSession(false);
        Doctor doctor = (Doctor) session.getAttribute("user");

        // Security check: ensure a doctor is logged in and the parameters are valid
        if (doctor == null || !"doctor".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        if (appointmentId != null && newStatus != null) {
            if (newStatus.equals("Completed") || newStatus.equals("Cancelled")) {
                appointmentDAO.updateAppointmentStatus(appointmentId, newStatus);
            }
        }

        // Redirect back to the doctor's dashboard
        response.sendRedirect("doctor-dashboard.jsp");
    }
}