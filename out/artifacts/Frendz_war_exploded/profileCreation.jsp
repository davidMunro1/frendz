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
<html class="bgr">
<head>
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>Create profile</title>
</head>
<body class="bgr center" style="background-color: #5a6b8c;">

<%--<div style="position: absolute; background-color: rgba(255, 255, 255, 0.5); top: 0px; left:calc(50% - 350px);border-radius: 20px; width: 700px; height: 1000px;"></div>--%>

<div class="container">
    <div class="form center-vertical">
        <img id="logo" src="images/logo.png">
        <form action="<%= blobstoreService.createUploadUrl("/FrendzServlet", UploadOptions.Builder.withGoogleStorageBucketName("images_frendz/user_images")) %>" method="post" enctype="multipart/form-data">
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
            <textarea id="bio_input" name="bio" placeholder="Bio" type="text" aria-multiline="true"></textarea><br>

            <div style="position:relative;">
                <div id="image1" class="file">Select an image</div>
                <input type="file" name="image1" class="file_input" onchange="document.getElementById('image1').innerHTML = this.value;" />

            </div>
            <div style="position:relative;">
                <div id="image2" class="file">Select an image</div>
                <input type="file" name="image1" class="file_input" onchange="document.getElementById('image2').innerHTML = this.value;" />
            </div>

            <input id="button" type="submit" name="button" value="Create profile">
        </form>
    </div>
</div>


</body>
</html>
