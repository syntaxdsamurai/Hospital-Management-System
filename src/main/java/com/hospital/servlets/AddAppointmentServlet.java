package com.hospital.servlets;

import com.hospital.dao.AppointmentDAO;
import com.hospital.model.Appointment;
import com.hospital.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/addAppointment")
public class AddAppointmentServlet extends HttpServlet {

    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String doctorId = request.getParameter("doctorId");
        String date = request.getParameter("date");

        HttpSession session = request.getSession(false);
        Patient patient = (Patient) session.getAttribute("user");

        if (patient == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String patientId = patient.getId();
        String appointmentId = "appt_" + UUID.randomUUID().toString().substring(0, 8);
        String status = "Booked";

        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, date, status);
        appointmentDAO.addAppointment(appointment);

        request.setAttribute("appMessage", "Appointment booked successfully!");
        request.getRequestDispatcher("patient-dashboard.jsp").forward(request, response);
    }
}