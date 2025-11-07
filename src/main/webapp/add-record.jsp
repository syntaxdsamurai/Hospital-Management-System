<%@ page import="com.hospital.model.Doctor" %>
<%@ page import="com.hospital.dao.AppointmentDAO" %>
<%@ page import="com.hospital.model.Appointment" %>
<%@ page import="com.hospital.dao.PatientDAO" %>
<%@ page import="com.hospital.model.Patient" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Security Check
  Doctor doctor = (Doctor) session.getAttribute("user");
  String role = (String) session.getAttribute("role");
  if (doctor == null || !"doctor".equals(role)) {
    response.sendRedirect("index.jsp");
    return;
  }

  // Get the appointment to be updated
  String appointmentId = request.getParameter("id");
  if (appointmentId == null) {
    response.sendRedirect("doctor-dashboard.jsp");
    return;
  }

  AppointmentDAO appointmentDAO = new AppointmentDAO();
  PatientDAO patientDAO = new PatientDAO();

  Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
  Patient patient = patientDAO.getPatientById(appointment.getPatientId());
%>
<html>
<head>
  <title>Add Medical Record</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="form-container" style="width: 600px;"> <div style="text-align: center; margin-bottom: 20px;">
  <h2>Add Medical Record</h2>
  <p style="margin: 0;"><strong>Patient:</strong> <%= patient.getName() %></p>
  <p style="margin: 0;"><strong>Appointment Date:</strong> <%= appointment.getAppointmentDate() %></p>
</div>

  <form action="addMedicalRecord" method="post">
    <input type="hidden" name="appointmentId" value="<%= appointment.getId() %>">
    <input type="hidden" name="patientId" value="<%= patient.getId() %>">
    <input type="hidden" name="doctorId" value="<%= doctor.getId() %>">

    <div class="form-group">
      <label for="diagnosis">Diagnosis:</label>
      <textarea id="diagnosis" name="diagnosis" required></textarea>
    </div>

    <div class="form-group">
      <label for="prescription">Prescription:</label>
      <textarea id="prescription" name="prescription" required></textarea>
    </div>

    <button type="submit" class="btn">Save Record & Complete Appointment</button>
  </form>

  <p style="text-align: center; margin-top: 20px;">
    <a href="doctor-dashboard.jsp">Back to Dashboard</a>
  </p>
</div>

</body>
</html>