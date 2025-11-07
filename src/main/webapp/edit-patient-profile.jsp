<%@ page import="com.hospital.model.Patient" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Security Check
    Patient patient = (Patient) session.getAttribute("user");
    String role = (String) session.getAttribute("role");

    if (patient == null || !"patient".equals(role)) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<html>
<head>
    <title>Edit Your Profile</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="form-container">
    <h2>Edit Your Profile</h2>
    <form action="updatePatientProfile" method="post">
        <input type="hidden" name="id" value="<%= patient.getId() %>">

        <div class="form-group">
            <label for="name">Full Name:</label>
            <input type="text" id="name" name="name" value="<%= patient.getName() %>" required>
        </div>
        <div class="form-group">
            <label for="age">Age:</label>
            <input type="number" id="age" name="age" value="<%= patient.getAge() %>" required>
        </div>
        <div class="form-group">
            <label for="contact">Contact Number:</label>
            <input type="text" id="contact" name="contact" value="<%= patient.getContact() %>" required>
        </div>
        <div class="form-group">
            <label for="history">Medical History (brief):</label>
            <input type="text" id="history" name="medicalHistory" value="<%= patient.getMedicalHistory() %>">
        </div>
        <div class="form-group">
            <label for="password">New Password (leave blank to keep old password):</label>
            <input type="password" id="password" name="password">
        </div>
        <button type="submit" class="btn">Update Profile</button>
    </form>

    <p style="text-align: center; margin-top: 20px;">
        <a href="patient-dashboard.jsp">Back to Dashboard</a>
    </p>
</div>

</body>
</html>