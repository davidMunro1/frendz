<%@ page import="beans.UserBeanBean" %>
<%@ page import="hibernate.NextUser" %>
<%@ page import="java.util.ArrayList" %><%--
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

    <%
                UserBeanBean bean = (UserBeanBean)request.getSession().getAttribute("bean");
//        UserBeanBean bean = new UserBeanBean();
//        bean.setUSER_ID(39);
        ArrayList<NextUser> users = (ArrayList)bean.getRandomUsers();
//        String school = get 'my' school
    %>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/sinch.min.js"></script>
</head>
<body id="hkr" class="center">

    <nav>
        <a href="homepage.jsp"><img id="mini_logo" src="images/logo.png"></a>
        <a href="editProfile.jsp">
            <div id="edit_profile" class="menu_item">
                <div class="small_profile_picture"><img src="<%=bean.getServingURL(bean.getProfile().getImage1())%>"></div>
                <%=bean.getUser().getFirstName()%>
            </div>
        </a>
        <div id="logout" class="menu_item">Logout</div>
    </nav>
    <%--show school name--%>
    <div id="school_title" class="title">Högskolan Kristianstad</div>
    <a href="browse.jsp"><div id="browse_button">Connect</div></a>
    <div id="popular_container">
        <div class="popular_user">
            <div class="popular_user_picture">
            <img src="<%= bean.getServingURL(users.get(0).getPictureString()) %>">
            </div>
            <%= users.get(0).getFirstName() %>
        </div>
        <div class="popular_user">
            <div class="popular_user_picture">
            <img src="<%= bean.getServingURL(users.get(1).getPictureString()) %>">
            </div>
            <%= users.get(1).getFirstName() %>
        </div>
        <div class="popular_user">
            <div class="popular_user_picture">
            <img src="<%= bean.getServingURL(users.get(2).getPictureString()) %>">
            </div>
            <%= users.get(2).getFirstName() %>
        </div>
        <div class="popular_user">
            <div class="popular_user_picture">
            <img src="<%= bean.getServingURL(users.get(3).getPictureString()) %>">
            </div>
            <%= users.get(3).getFirstName() %>
        </div>
    </div>

  <div id="chatArea"></div>

</body>

<script src="js/index.js"></script>

</html>
