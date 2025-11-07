<%@ page import="com.hospital.model.Patient" %>
<%@ page import="com.hospital.dao.AppointmentDAO" %>
<%@ page import="com.hospital.model.Appointment" %>
<%@ page import="com.hospital.dao.DoctorDAO" %>
<%@ page import="com.hospital.model.Doctor" %>
<%@ page import="com.hospital.dao.MedicalRecordDAO" %>
<%@ page import="com.hospital.model.MedicalRecord" %>
<%@ page import="com.hospital.dao.BillDAO" %>
<%@ page import="com.hospital.model.Bill" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Security Check
  Patient patient = (Patient) session.getAttribute("user");
  String role = (String) session.getAttribute("role");

  if (patient == null || !"patient".equals(role)) {
    response.sendRedirect("index.jsp");
    return;
  }

  // Get Data for Page
  DoctorDAO doctorDAO = new DoctorDAO();
  AppointmentDAO appointmentDAO = new AppointmentDAO();
  MedicalRecordDAO recordDAO = new MedicalRecordDAO();
  BillDAO billDAO = new BillDAO();

  List<Doctor> allDoctors = doctorDAO.getAllDoctors();
  List<Appointment> appointmentList = appointmentDAO.getAppointmentsByPatientId(patient.getId());
  List<MedicalRecord> recordList = recordDAO.getRecordsByPatientId(patient.getId());
  List<Bill> billList = billDAO.getBillsByPatientId(patient.getId());
%>
<html>
<head>
  <title>Patient Dashboard</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="container">
  <div class="header">
    <h1>Welcome, <%= patient.getName() %>!</h1>
    <div class="header-links">
      <a href="edit-patient-profile.jsp">Edit Profile</a>
      <a href="logout" class="logout">Logout</a>
    </div>
  </div>

  <div class="content">
    <div class="content-grid">
      <div class="section">
        <h2>Book New Appointment</h2>
        <form action="addAppointment" method="post">
          <div class="form-group">
            <label for="doctor">Select Doctor:</label>
            <select id="doctor" name="doctorId" required>
              <option value="">-- Choose a doctor --</option>
              <% for (Doctor doc : allDoctors) { %>
              <option value="<%= doc.getId() %>"><%= doc.getName() %> (<%= doc.getSpecialization() %>)</option>
              <% } %>
            </select>
          </div>
          <div class="form-group">
            <label for="date">Preferred Date:</label>
            <input type="date" id="date" name="date" required>
          </div>
          <button type="submit" class="btn">Book Appointment</button>
          <%
            String appMessage = (String) request.getAttribute("appMessage");
            if (appMessage != null) {
          %>
          <p class="message"><%= appMessage %></p>
          <%
            }
          %>
        </form>
      </div>

      <div class="section">
        <h2>Your Appointments</h2>
        <table>
          <thead> <tr> <th>Doctor</th> <th>Date</th> <th>Status</th> <th>Action</th> </tr> </thead>
          <tbody>
          <% for (Appointment app : appointmentList) { %>
          <tr>
            <td><%= doctorDAO.getDoctorById(app.getDoctorId()).getName() %></td>
            <td><%= app.getAppointmentDate() %></td>
            <td><%= app.getStatus() %></td>
            <td>
              <% if (app.getStatus().equals("Booked")) { %>
              <a class="action-link-cancel" href="patientCancelAppointment?id=<%= app.getId() %>">Cancel</a>
              <% } else { %>
              -
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
    </div> <div class="section">
    <h2>Your Medical Records</h2>
    <table>
      <thead> <tr> <th>Date</th> <th>Doctor</th> <th>Diagnosis</th> <th>Prescription</th> </tr> </thead>
      <tbody>
      <% for (MedicalRecord rec : recordList) { %>
      <tr>
        <td><%= appointmentDAO.getAppointmentById(rec.getAppointmentId()).getAppointmentDate() %></td>
        <td><%= doctorDAO.getDoctorById(rec.getDoctorId()).getName() %></td>
        <td><%= rec.getDiagnosis() %></td>
        <td><%= rec.getPrescription() %></td>
      </tr>
      <% } %>
      <% if (recordList.isEmpty()) { %>
      <tr>
        <td colspan="4">You have no medical records.</td>
      </tr>
      <% } %>
      </tbody>
    </table>
  </div>

    <div class="section">
      <h2>Your Bills</h2>
      <table>
        <thead> <tr> <th>Date</th> <th>Amount</th> <th>Status</th> <th>Action</th> </tr> </thead>
        <tbody>
        <% for (Bill bill : billList) { %>
        <tr>
          <td><%= appointmentDAO.getAppointmentById(bill.getAppointmentId()).getAppointmentDate() %></td>
          <td>$<%= String.format("%.2f", bill.getAmount()) %></td>
          <td><%= bill.getStatus() %></td>
          <td>
            <% if (bill.getStatus().equals("Pending")) { %>
            <a href="payBill?id=<%= bill.getBillId() %>">Pay Now</a>
            <% } else { %>
            Paid
            <% } %>
          </td>
        </tr>
        <% } %>
        <% if (billList.isEmpty()) { %>
        <tr>
          <td colspan="4">You have no bills.</td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </div>
  </div> </div> </body>
</html>