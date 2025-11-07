package com.hospital.servlets;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.MedicalRecordDAO;
import com.hospital.dao.BillDAO;
import com.hospital.model.MedicalRecord;
import com.hospital.model.Bill;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/addMedicalRecord")
public class AddMedicalRecordServlet extends HttpServlet {

    private MedicalRecordDAO recordDAO;
    private AppointmentDAO appointmentDAO;
    private BillDAO billDAO;

    private static final double CONSULTATION_FEE = 150.00; // Example fee

    @Override
    public void init() {
        recordDAO = new MedicalRecordDAO();
        appointmentDAO = new AppointmentDAO();
        billDAO = new BillDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appointmentId = request.getParameter("appointmentId");
        String patientId = request.getParameter("patientId");
        String doctorId = request.getParameter("doctorId");
        String diagnosis = request.getParameter("diagnosis");
        String prescription = request.getParameter("prescription");

        String recordId = "rec_" + UUID.randomUUID().toString().substring(0, 8);
        String billId = "bill_" + UUID.randomUUID().toString().substring(0, 8);

        // 1. Create and save the new medical record
        MedicalRecord record = new MedicalRecord(recordId, appointmentId, patientId, doctorId, diagnosis, prescription);
        recordDAO.addRecord(record);

        // 2. Update the appointment status to "Completed"
        appointmentDAO.updateAppointmentStatus(appointmentId, "Completed");

        // 3. Create and save a new bill (NEW STEP)
        Bill bill = new Bill(billId, appointmentId, patientId, CONSULTATION_FEE, "Pending");
        billDAO.addBill(bill);

        // 4. Send the doctor back to their dashboard
        response.sendRedirect("doctor-dashboard.jsp");
    }
}