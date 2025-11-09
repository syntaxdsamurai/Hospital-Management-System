package com.hospital.servlets;

import com.hospital.dao.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "DataPathInitServlet", loadOnStartup = 1)
public class DataPathInitServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // Get the real path to the data directory
        String dataPath = getServletContext().getRealPath("/data");

        System.out.println("========================================");
        System.out.println("Setting data directory to: " + dataPath);
        System.out.println("========================================");

        // Set the data directory for all DAOs
        AppointmentDAO.setDataDirectory(dataPath);
        BillDAO.setDataDirectory(dataPath);
        DoctorDAO.setDataDirectory(dataPath);
        MedicalRecordDAO.setDataDirectory(dataPath);
        PatientDAO.setDataDirectory(dataPath);
    }
}