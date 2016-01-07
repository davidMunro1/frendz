<%@ page import="beans.UserBeanBean" %><%--
  Created by IntelliJ IDEA.
  User: davidmunro
  Date: 30/12/2015
  Time: 17:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>Confirm account</title>
</head>
<body id="bgr" class="center">

<div class="container">
    <div class="form center-vertical">
        <img id="logo" src="images/logo.png">
        <%
            UserBeanBean bean = new UserBeanBean();
            boolean verified = bean.handleConfirmation(request.getParameter("token"), request.getParameter("email"));
            if(verified)
                out.println("<div class='info_message'>Congratulations! Your account has been verified!</div>");
            else {
                out.println("<div class='info_message'>Your account could not be verified!</div>");
                response.setHeader("Refresh", "5;url=index.html");
            }
        %>
        <form action="/FrendzServlet" method="post">
            <input name="password" placeholder="Password" type="password"><br>
            <input name="confirmPassword" placeholder="Confirm password" type="password"><br>
            <input id="button" type="submit" name="button" value="Confirm">
        </form>
    </div>
</div>

</body>
</html>
