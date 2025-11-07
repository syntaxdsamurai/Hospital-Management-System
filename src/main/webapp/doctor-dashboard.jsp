<%@ page import="com.hospital.model.Doctor" %>
<%@ page import="com.hospital.dao.AppointmentDAO" %>
<%@ page import="com.hospital.model.Appointment" %>
<%@ page import="com.hospital.dao.PatientDAO" %>
<%@ page import="com.hospital.model.Patient" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Security Check
    Doctor doctor = (Doctor) session.getAttribute("user");
    String role = (String) session.getAttribute("role");

    if (doctor == null || !"doctor".equals(role)) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Get doctor's appointments
    AppointmentDAO appointmentDAO = new AppointmentDAO();
    PatientDAO patientDAO = new PatientDAO();
    List<Appointment> appointmentList = appointmentDAO.getAppointmentsByDoctorId(doctor.getId());
%>
<html>
<head>
    <title>Doctor Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="container">
    <div class="header">
        <h1>Welcome, Dr. <%= doctor.getName() %>!</h1>
        <div class="header-links">
            <a href="logout" class="logout">Logout</a>
        </div>
    </div>

    <div class="content">
        <div class="section">
            <h2>Your Appointments</h2>
            <table>
                <thead>
                <tr>
                    <th>Patient Name</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <% for (Appointment app : appointmentList) { %>
                <tr>
                    <td>
                        <%
                            Patient p = patientDAO.getPatientById(app.getPatientId());
                            out.print(p != null ? p.getName() : "Unknown");
                        %>
                    </td>
                    <td><%= app.getAppointmentDate() %></td>
                    <td><%= app.getStatus() %></td>
                    <td>
                        <% if (app.getStatus().equals("Booked")) { %>
                        <a class="action-link-complete" href="add-record.jsp?id=<%= app.getId() %>">Add Record</a> |
                        <a class="action-link-cancel" href="updateAppointmentStatus?id=<%= app.getId() %>&status=Cancelled">Cancel</a>
                        <% } else { %>
                        <%= app.getStatus() %>
                        <% } %>
                    </td>
                </tr>
                <% } %>
                <% if (appointmentList.isEmpty()) { %>
                <tr>
                    <td colspan="4">You have no appointments.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>