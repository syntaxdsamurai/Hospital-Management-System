<%@ page import="com.hospital.dao.DoctorDAO" %>
<%@ page import="com.hospital.model.Doctor" %>
<%@ page import="com.hospital.dao.PatientDAO" %>
<%@ page import="com.hospital.model.Patient" %>
<%@ page import="com.hospital.dao.AppointmentDAO" %>
<%@ page import="com.hospital.model.Appointment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Security Check
    String role = (String) session.getAttribute("role");
    if (!"admin".equals(role)) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Get All Data for Admin
    DoctorDAO doctorDAO = new DoctorDAO();
    PatientDAO patientDAO = new PatientDAO();
    AppointmentDAO appointmentDAO = new AppointmentDAO();

    List<Doctor> doctorList = doctorDAO.getAllDoctors();
    List<Appointment> appointmentList = appointmentDAO.getAllAppointments();

    // Search Logic
    String searchQuery = request.getParameter("searchName");
    List<Patient> patientList;
    if (searchQuery != null && !searchQuery.trim().isEmpty()) {
        patientList = patientDAO.getAllPatients().stream()
                .filter(p -> p.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());
    } else {
        patientList = patientDAO.getAllPatients();
    }
%>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="container">
    <div class="header">
        <h1>Admin Management Panel</h1>
        <div class="header-links">
            <a href="logout" class="logout">Logout</a>
        </div>
    </div>

    <div class="content">
        <div class="content-grid">
            <div class="section">
                <h2>Add New Doctor</h2>
                <form action="addDoctor" method="post">
                    <div class="form-group">
                        <label for="name">Doctor Name:</label>
                        <input type="text" id="name" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="specialization">Specialization:</label>
                        <input type="text" id="specialization" name="specialization" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn">Add Doctor</button>
                    <%
                        String docMessage = (String) request.getAttribute("docMessage");
                        if (docMessage != null) {
                    %>
                    <p class="message"><%= docMessage %></p>
                    <%
                        }
                    %>
                </form>
            </div>

            <div class="section">
                <h2>Registered Doctors</h2>
                <table>
                    <thead> <tr> <th>ID</th> <th>Name</th> <th>Specialization</th> <th>Action</th> </tr> </thead>
                    <tbody>
                    <% for (Doctor doc : doctorList) { %>
                    <tr>
                        <td><%= doc.getId() %></td>
                        <td><%= doc.getName() %></td>
                        <td><%= doc.getSpecialization() %></td>
                        V<td><a class="action-link-delete" href="deleteDoctor?id=<%= doc.getId() %>">Delete</a></td>
                    </tr>
                    <% } %>
                    <% if (doctorList.isEmpty()) { %>
                    <tr>
                        <td colspan="4">No doctors registered.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div> <div class="section">
        <h2>Registered Patients</h2>

        <form class="search-form" action="admin-panel.jsp" method="get">
            <input type="text" name="searchName" placeholder="Search by patient name..." value="<%= searchQuery != null ? searchQuery : "" %>">
            <button type="submit" class="btn" style="width: auto;">Search</button>
            <a href="admin-panel.jsp">Clear</a>
        </form>

        <table>
            <thead> <tr> <th>ID</th> <th>Name</th> <th>Contact</th> <th>Action</th> </tr> </thead>
            <tbody>
            <% for (Patient p : patientList) { %>
            <tr>
                <td><%= p.getId() %></td>
                <td><%= p.getName() %></td>
                <td><%= p.getContact() %></td>
                <td><a class="action-link-delete" href="deletePatient?id=<%= p.getId() %>">Delete</a></td>
            </tr>
            <% } %>
            <% if (patientList.isEmpty()) { %>
            <tr> <td colspan="4">No patients found.</td> </tr>
            <% } %>
            </tbody>
        </table>
    </div>

        <div class="section">
            <h2>All Appointments</h2>
            <table>
                <thead> <tr> <th>ID</th> <th>Patient</th> <th>Doctor</th> <th>Date</th> <th>Status</th> </tr> </thead>
                <tbody>
                <% for (Appointment app : appointmentList) { %>
                <%
                    Patient p = patientDAO.getPatientById(app.getPatientId());
                    Doctor d = doctorDAO.getDoctorById(app.getDoctorId());

                    String patientName = (p != null) ? p.getName() : "Patient Deleted";
                    String doctorName = (d != null) ? d.getName() : "Doctor Deleted";
                %>
                <tr>
                    <td><%= app.getId() %></td>
                    <td><%= patientName %></td>
                    <td><%= doctorName %></td>
                    <td><%= app.getAppointmentDate() %></td>
                    <td><%= app.getStatus() %></td>
                </tr>
                <% } %>
                <% if (appointmentList.isEmpty()) { %>
                <tr> <td colspan="5">No appointments found.</td> </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div> </div> </body>
</html>