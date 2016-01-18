<%@ page import="com.google.appengine.api.blobstore.UploadOptions" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="beans.UserBeanBean" %>
<%--
  Created by IntelliJ IDEA.
  User: davidmunro
  Date: 30/12/2015
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>
<html id="create_profile_page">
<head>
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>Create profile</title>

    <%
        UserBeanBean bean = (UserBeanBean)request.getSession().getAttribute("bean");
    %>

    <script>
        function validateSelect(){
            var genderSelection = document.getElementById("gender");
            var selection = genderSelection.options[genderSelection.selectedIndex].text;
            var soughtGenderSelection = document.getElementById("soughtGender");
            var selection1 = soughtGenderSelection.options[soughtGenderSelection.selectedIndex].text;

            if ($.inArray(selection, ['Male', 'Female']) == -1) {
            $('#error_message').text('Gender must be chosen');
            return false;
            }
            else if($.inArray(selection1, ['Men', 'Women', 'Both']) == -1){
            $('#error_message').text('Sought gender must be chosen');
            return false;
            }
            else{
            return true;
            }
        }
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
        <div id="logout" class="menu_item">Logout</div>
    </nav>

    <div style="width: 80%; height: 510px; background-color: rgba(255, 255, 255, 0.9); position:absolute; top: 110px; left: 10%; border-radius: 10px;"></div>

    <div class="container">
        <div class="form">
            <%--<img id="logo" src="images/logo.png">--%>
            <form action="<%= blobstoreService.createUploadUrl("/FrendzServlet", UploadOptions.Builder.withGoogleStorageBucketName("images_frendz/user_images")) %>" method="post" enctype="multipart/form-data" onsubmit="return validateSelect()">
                <div style="position: relative; left: 0px; width: 50%; margin-top: 80px; margin-bottom: 40px; z-index: 99;">
                    <input name="age" placeholder="Age" type="text"><br>
                    <select name="gender">
                        <option value="" disabled selected>Your gender</option>
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select><br>
                    <input name="programme" placeholder="Programme" type="text"><br>
                    <select name="soughtGender">
                        <option value="" disabled selected>Sought gender</option>
                        <option value="MALE">Men</option>
                        <option value="FEMALE">Women</option>
                        <option value="BOTH">Both</option>
                    </select><br>
                </div>

                <div style="position: absolute; left: 50%; width: 50%; top: 200px;">
                    <div style="position: relative; left: 0px;">
                        <textarea id="bio_input" name="bio" placeholder="Bio" type="text" aria-multiline="true"></textarea><br>
                    </div>
                    <div style="position:relative;">
                        <div id="image1" class="file">Select an image</div>
                        <input type="file" name="image1" class="file_input" onchange="document.getElementById('image1').innerHTML = this.value;" />
                    </div>
                    <div style="position:relative;">
                        <div id="image2" class="file">Select an image</div>
                        <input type="file" name="image2" class="file_input" onchange="document.getElementById('image2').innerHTML = this.value;" />
                    </div>
                </div>

                <input id="button" type="submit" name="button" value="Create profile">
            </form>
        </div>
    </div>

</body>
</html>

