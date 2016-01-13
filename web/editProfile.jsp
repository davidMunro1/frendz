<%@ page import="com.google.appengine.api.blobstore.UploadOptions" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
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
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>Edit profile</title>
</head>
<body id="bgr" class="center">

    <nav>
        <a href="homepage.jsp"><img id="mini_logo" src="images/logo.png"></a>
        <a href="editProfile.jsp"><div id="edit_profile" class="menu_item">Juraj</div></a>
        <div id="logout" class="menu_item">Logout</div>
    </nav>

    <div style="width: 80%; height: 510px; background-color: rgba(255, 255, 255, 0.9); position:absolute; top: 110px; left: 10%; border-radius: 10px;"></div>

    <div class="container">
    <div class="form">
        <%--<img id="logo" src="images/logo.png">--%>
        <form action="<%= blobstoreService.createUploadUrl("/FrendzServlet", UploadOptions.Builder.withGoogleStorageBucketName("images_frendz/user_images")) %>" method="post" enctype="multipart/form-data">
            <div style="position: relative; left: 0px; width: 50%; margin-top: 120px; margin-bottom: 80px; z-index: 99;">
            <input name="secondName" placeholder="Second name" type="text"><br>
            <input name="password" placeholder="Password" type="password"><br>
            <input id="bio_input" name="bio" placeholder="Bio" type="text" maxlength="150"><br>
            </div>

            <div style="position: absolute; left: 50%; width: 50%; top: 140px;">
            <div style="position:relative;">
                <div id="image1" class="file">Select an image</div>
                <input type="file" name="image1" class="file_input" onchange="document.getElementById('image1').innerHTML = this.value;" />
            </div>
            <div style="position:relative;">
                <div id="image2" class="file">Select an image</div>
                <input type="file" name="image1" class="file_input" onchange="document.getElementById('image2').innerHTML = this.value;" />
            </div>
            <div style="position:relative;">
                <div id="image3" class="file">Select an image</div>
                <input type="file" name="image1" class="file_input" onchange="document.getElementById('image3').innerHTML = this.value;" />
            </div>
            <div style="position:relative;">
                <div id="image4" class="file">Select an image</div>
                <input type="file" name="image1" class="file_input" onchange="document.getElementById('image4').innerHTML = this.value;" />
            </div>
            <div style="position:relative;">
                <div id="image5" class="file">Select an image</div>
                <input type="file" name="image1" class="file_input" onchange="document.getElementById('image5').innerHTML = this.value;" />
            </div>
            </div>

            <input id="button" type="submit" name="button" value="Save">
        </form>
    </div>
    </div>

</body>
</html>
