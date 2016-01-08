<%--
  Created by IntelliJ IDEA.
  User: davidmunro
  Date: 30/12/2015
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>Create profile</title>
</head>
<body id="bgr" class="center">

<div class="container">
    <div class="form center-vertical">
        <img id="logo" src="images/logo.png">
        <form action="/FrendzServlet" method="post">
            <input name="age" placeholder="Age" type="text"><br>
            <input name="gender" placeholder="Gender" type="text"><br>
            <input name="programme" placeholder="Programme" type="text"><br>
            <select name="soughtGender">
                <option value="volvo">Men</option>
                <option value="saab">Women</option>
            </select><br>
            <input name="bio" placeholder="Bio" type="text"><br>
            <input id="button" type="submit" name="button" value="Create profile">
        </form>
    </div>
</div>


</body>
</html>
