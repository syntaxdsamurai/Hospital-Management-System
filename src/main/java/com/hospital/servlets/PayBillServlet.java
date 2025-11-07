package com.hospital.servlets;

import com.hospital.dao.BillDAO;
import com.hospital.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/payBill")
public class PayBillServlet extends HttpServlet {

    private BillDAO billDAO;

    @Override
    public void init() {
        billDAO = new BillDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String billId = request.getParameter("id");

        HttpSession session = request.getSession(false);
        Patient patient = (Patient) session.getAttribute("user");

        // Security check: ensure a patient is logged in
        if (patient == null || !"patient".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.jsp");
            return;
        }

        if (billId != null) {
            billDAO.updateBillStatus(billId, "Paid");
        }

        // Redirect back to the patient's dashboard
        response.sendRedirect("patient-dashboard.jsp");
    }
}