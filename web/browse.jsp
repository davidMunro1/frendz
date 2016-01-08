<%--
  Created by IntelliJ IDEA.
  User: Juraj
  Date: 05.01.2016
  Time: 23:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title></title>
</head>
<body id="bgr" class="center">

    <nav><img id="mini_logo" src="images/logo.png"></nav>
    <div id="content_container">
        <div id="profile_picture"><img src="images/juraj.jpg"></div>
        <div id="dislike"></div>
        <div id="like"></div>
    </div>
    <div id="info_container">
        <%
            out.println("<div id='name'>" + request.getParameter("name") + "</div>");
            out.println("<div id='programme'>" + request.getParameter("programme") + "</div>");
        %>
        <%--<div id="name">Juraj</div>--%>
        <%--<div id="programme">Software Development</div>--%>
    </div>

</body>
</html>
