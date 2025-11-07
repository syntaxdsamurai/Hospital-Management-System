package com.hospital.servlets;

import com.hospital.dao.DoctorDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/deleteDoctor")
public class DeleteDoctorServlet extends HttpServlet {

    private DoctorDAO doctorDAO;

    @Override
    public void init() {
        doctorDAO = new DoctorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String doctorId = request.getParameter("id");

        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");

        // Security check: ensure an admin is logged in
        if (!"admin".equals(role)) {
            response.sendRedirect("index.jsp");
            return;
        }

        if (doctorId != null) {
            doctorDAO.deleteDoctor(doctorId);
        }

        // Redirect back to the admin panel
        response.sendRedirect("admin-panel.jsp");
    }
}