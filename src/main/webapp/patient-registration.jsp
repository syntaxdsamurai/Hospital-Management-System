<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Patient Registration</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="form-container">
    <h2>Patient Registration</h2>
    <form action="patientRegister" method="post">
        <div class="form-group">
            <label for="name">Full Name:</label>
            <input type="text" id="name" name="name" required>
        </div>
        <div class="form-group">
            <label for="age">Age:</label>
            <input type="number" id="age" name="age" required>
        </div>
        <div class="form-group">
            <label for="contact">Contact Number:</label>
            <input type="text" id="contact" name="contact" required>
        </div>
        <div class="form-group">
            <label for="history">Medical History (brief):</label>
            <input type="text" id="history" name="medicalHistory">
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit" class="btn">Register</button>
    </form>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
    <p class="message"><%= message %></p>
    <%
        }
    %>

    <p style="text-align: center; margin-top: 20px;">
        Already have an account? <a href="index.jsp">Login Here</a>
    </p>
</div>

</body>
</html>