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

    <link rel="stylesheet" media="(min-width:1001px)" type="text/css" href="style/style.css">
    <link rel="stylesheet" media="(max-width:1000px)" type="text/css" href="style/style_small.css">
    <script src="js/jquery-1.10.2.js"></script>
    <title>frendz</title>

    <%
        UserBeanBean bean = (UserBeanBean)request.getSession().getAttribute("bean");
//        UserBeanBean bean = new UserBeanBean();
//        bean.setUSER_ID(39);
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
            bio: "<%= users.get(i).getBio() %>",
            picture: "<%= bean.getServingURL(users.get(i).getPictureString()) %>"
        };
        usersArray[<%= i %>] = user;
        <%}%>
    </script>

</head>
<body id="bgr" class="center">

    <nav>
        <a href="homepage.jsp"><img id="mini_logo" src="images/logo.png"></a>
        <a href="editProfile.jsp">
            <div id="edit_profile" class="menu_item">
                <div class="small_profile_picture"><img src="<%=bean.getServingURL(bean.getProfile().getImage1())%>"></div>
                <%=bean.getUser().getFirstName()%>
            </div>
        </a>
        <a href="logout.jsp"><div id="logout" class="menu_item">Logout</div></a>
    </nav>

    <div id="content_container">
        <div id="profile_picture">
            <img id="picture">
        </div>
        <div id="bio"></div>
        <div id="dislike" onclick="sendDislike();"></div>
        <div id="like" onclick="sendLike();"></div>

        <div id="invitation">
            <%
                if(request.getParameter("invited") != null && request.getParameter("invited").equals("true"))
                    out.println("<div><p>Invitation has been sent successfully</p><p>Invite more frendz!</p></div>");
                else
                    out.println("<div>There are no more matches for you at the moment. It's time to invite your frendz!</div>");
            %>
            <form action="/FrendzServlet" method="post" id="signUp" name="signUp" onsubmit="return validateInput();">
                <input id="email" name="email" placeholder="E-mail" type="text"><br><span id="emailError" class="error_message"></span><br>
                <input id="button" type="submit" name="button" value="Invite">
            </form>
        </div>
    </div>
    <div id="info_container">
        <div id="name"></div>
        <div id="age"></div>
        <div id="programme"></div>
    </div>

    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script>
        if(!checkNoMoreUsers()) {
            document.getElementById("name").innerHTML = usersArray[0].firstName;
            document.getElementById("age").innerHTML = usersArray[0].age;
            document.getElementById("programme").innerHTML = usersArray[0].programme;
            document.getElementById("bio").innerHTML = usersArray[0].bio;
            document.getElementById("picture").src = usersArray[0].picture;
        }

        function sendLike() {
            $.get("FrendzServlet", {action: "like", userId: usersArray[index].id});
            $("#info_container").fadeOut();
            $("#content_container").fadeOut();
            $("#bio").fadeOut(function() {
                showNextUser();
            });
        }
        function sendDislike() {
            $("#info_container").fadeOut();
            $("#content_container").fadeOut();
            $("#bio").fadeOut();
            $.get("FrendzServlet", {action: "dislike", userId: usersArray[index].id});
            showNextUser();
        }
        function showNextUser() {
            index++;
            if (!checkNoMoreUsers()) {
                document.getElementById("name").innerHTML = usersArray[index].firstName;
                document.getElementById("age").innerHTML = usersArray[index].age;
                document.getElementById("programme").innerHTML = usersArray[index].programme;
                document.getElementById("bio").innerHTML = usersArray[index].bio;
                document.getElementById("picture").src = usersArray[index].picture;
                $("#info_container").animate({width:'toggle'}, 350);
                $("#content_container").animate({width:'toggle'}, 350, function() {
                    $("#bio").fadeIn(100);
                });
            }
        }
        function checkNoMoreUsers() {
            if(index == usersArray.length) {
                document.getElementById("name").style.display = 'none';
                document.getElementById("age").style.display = 'none';
                document.getElementById("programme").style.display = 'none';
                document.getElementById("bio").style.display = 'none';
                document.getElementById("picture").style.display = 'none';
                document.getElementById("info_container").style.display = 'none';
                document.getElementById("like").style.display = 'none';
                document.getElementById("dislike").style.display = 'none';
                document.getElementById("invitation").style.display = 'block';
                $("#content_container").fadeIn();
                return true;
            } else
                return false;
        }

        function validateInput() {
            var email = document.getElementById("email").value;
            if (document.getElementById("email").value === "") {
                document.getElementById("emailError").innerHTML = 'This field can not be empty';
                return false;
            } else if (validateEmail(email) == false) {
                document.getElementById("emailError").innerHTML = 'This is not a valid email';
                return false;
            }
            function validateEmail(email){
                var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }
        }

        $(document).ready(function(){
            $("#info_container").hide(0).delay(200).fadeIn(300);
            $("#content_container").hide(0).delay(200).fadeIn(300);
        });
    </script>

</body>
</html>