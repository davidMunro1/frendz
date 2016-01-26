<%@ page import="beans.UserBeanBean" %>
<%--
  Created by IntelliJ IDEA.
  User: davidmunro
  Date: 12/01/2016
  Time: 12:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en" id="login_page">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" media="(min-width:1001px)" type="text/css" href="style/style.css">
  <link rel="stylesheet" media="(max-width:1000px)" type="text/css" href="style/style_small.css">
  <title>frendz</title>
</head>
<body id="bgr" class="center">

<%--<div class="container">--%>
    <%
      String error = "";
      if(request.getParameter("error") != null){
        error = request.getParameter("error").isEmpty() ? "" : request.getParameter("error");
      }
      request.getSession().invalidate();
    %>
  <div class="form center-vertical">
    <img id="logo" src="images/logo.png">
    <div class="error_message">
      <%= error%>
    </div>
    <form action="/FrendzServlet" method="post">
      <input name="email" placeholder="E-mail" type="text"><br>
      <input name="password" placeholder="Password" type="password"><br>
      <input id="button" type="submit" name="button" value="Login">
    </form>
    <a href="signup.html"><div>Don't have an account yet? Sign up here!</div></a>
  </div>
<%--</div>--%>

</body>
</html>
