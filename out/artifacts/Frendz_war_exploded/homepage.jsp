<%--
  Created by IntelliJ IDEA.
  User: davidmunro
  Date: 30/12/2015
  Time: 20:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <link href='https://fonts.googleapis.com/css?family=Poiret+One' rel='stylesheet' type='text/css'>
    <title>Frendz</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/sinch.min.js"></script>
</head>
<body id="hkr" class="center">

    <div id="school_title" class="title">Högskolan Kristianstad</div>
    <div id="browse_button">Browse</div>
    <%--<div id="popular_title">Popular users</div>--%>
    <div id="popular_container">
        <div class="popular_user">
            <img class="popular_user_picture" src="images/juraj.jpg">
            Ron
        </div>
        <div class="popular_user">
            <img class="popular_user_picture" src="images/juraj.jpg">
            David
        </div>
        <div class="popular_user">
            <img class="popular_user_picture" src="images/juraj.jpg">
            John
        </div>
        <div class="popular_user">
            <img class="popular_user_picture" src="images/juraj.jpg">
            Juraj
        </div>
    </div>

    <nav><img id="mini_logo" src="images/logo.png"></nav>

    <%--<h1>This is the homepage for the user</h1>--%>

  <div id="chatArea"></div>

</body>

<script src="js/index.js"></script>

</html>
