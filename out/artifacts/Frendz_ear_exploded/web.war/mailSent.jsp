<%--
  Created by IntelliJ IDEA.
  User: davidmunro
  Date: 05/01/2016
  Time: 14:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="style/style.css">
  <title>Mail confirmation sent!</title>
</head>
<body id="bgr" class="center">

<div class="container">

  <div class="center-vertical">
    <img id="logo" src="images/logo.png">
    <h1 class="center">An email has been sent to <%=request.getParameter("email")%>, please go to your email and click on the confirmation link!</h1>
    <p>&nbsp;</p>
    <h2>- The frendz team -</h2>
  </div>

</div>

</body>
</html>
