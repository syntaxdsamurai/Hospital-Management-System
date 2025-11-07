<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login - Hospital Management</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="form-container">
    <h2>Hospital Login</h2>
    <form action="login" method="post">
        <div class="form-group">
            <label for="id">Login ID (Contact # or Name):</label>
            <input type="text" id="id" name="id" required>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="form-group">
            <label for="role">Login As:</label>
            <select id="role" name="role">
                <option value="patient">Patient</option>
                <option value="doctor">Doctor</option>
                <option value="admin">Admin</option>
            </select>
        </div>
        <button type="submit" class="btn">Login</button>
    </form>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <p class="error-message"><%= error %></p>
    <%
        }
    %>

    <p style="text-align: center; margin-top: 20px;">
        Don't have an account? <a href="patient-registration.jsp">Register as Patient</a>
    </p>
</div>

</body>
</html>