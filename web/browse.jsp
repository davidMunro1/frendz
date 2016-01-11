<%@ page import="beans.UserBeanBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="hibernate.NextUser" %>
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

    <%
        UserBeanBean bean = (UserBeanBean)request.getSession().getAttribute("bean");
        ArrayList<NextUser> users = (ArrayList)bean.browseAllUsers();
    %>

    <script>
        var index = 0;
        var usersArray = new Array();
        <%
            for (int i=0; i < users.size(); i++) {
        %>
        var user = {
            id: "<%= users.get(i).getUserID() %>",
            firstName: "<%= users.get(i).getFirstName() %>",
            age: <%= users.get(i).getAge() %>,
            programme: "<%= users.get(i).getProgramme() %>",
            picture: "<%= users.get(i).getPictureString() %>"
        };
        usersArray[<%= i %>] = user;
        <%}%>
    </script>

</head>
<body id="bgr" class="center">

<nav><img id="mini_logo" src="images/logo.png"></nav>
<div id="content_container">
    <div id="profile_picture">
        <%--<% out.println("<img src=" + users.get(0).getPictureString() + ">"); %>--%>
        <img id="picture">
    </div>
    <div id="bio">
        This is my awesome bio. Everyone loves me because I'm a guy called Anna and I'm 78. Call me ;)
    </div>
    <div id="dislike" onclick="sendDislike();"></div>
    <div id="like" onclick="sendLike();"></div>
</div>
<div id="info_container">
    <div id="name"></div>
    <div id="age"></div>
    <div id="programme"></div>
</div>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
    function sendLike() {
        $.get("FrendzServlet", {action: "like", userId: usersArray[index].id});
        showNextUser();
    }
    function sendDislike() {
        $.get("FrendzServlet", {action: "dislike", userId: usersArray[index].id});
        showNextUser();
    }
    function showNextUser() {
        index++;
        document.getElementById("name").innerHTML = usersArray[index].firstName;
        document.getElementById("age").innerHTML = usersArray[index].age;
        document.getElementById("programme").innerHTML = usersArray[index].programme;
        document.getElementById("picture").src = usersArray[index].picture;
    }
    document.getElementById("name").innerHTML = usersArray[0].firstName;
    document.getElementById("age").innerHTML = usersArray[0].age;
    document.getElementById("programme").innerHTML = usersArray[0].programme;
    document.getElementById("picture").src = usersArray[0].picture;
</script>

</body>
</html>