package com.hospital.servlets;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/addDoctor")
public class AddDoctorServlet extends HttpServlet {

    private DoctorDAO doctorDAO;

    @Override
    public void init() {
        doctorDAO = new DoctorDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String specialization = request.getParameter("specialization");
        String password = request.getParameter("password");
        String id = "d_" + UUID.randomUUID().toString().substring(0, 8);

        Doctor existingDoctor = doctorDAO.getDoctorByName(name);

        if (existingDoctor != null) {
            request.setAttribute("docMessage", "A doctor with this name already exists.");
        } else {
            Doctor doctor = new Doctor(id, name, specialization, password);
            doctorDAO.addDoctor(doctor);
            request.setAttribute("docMessage", "Doctor added successfully!");
        }

        request.getRequestDispatcher("admin-panel.jsp").forward(request, response);
    }
}