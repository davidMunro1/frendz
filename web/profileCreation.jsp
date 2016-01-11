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
    <title>Create profile</title>
</head>
<body id="bgr" class="center">


<div class="container">
    <div class="form center-vertical">
        <img id="logo" src="images/logo.png">
        <form action="<%= blobstoreService.createUploadUrl("/FrendzServlet", UploadOptions.Builder.withGoogleStorageBucketName("images_frendz/user_images")) %>" method="post" enctype="multipart/form-data">
            <input name="age" placeholder="Age" type="text"><br>
            <select name="gender">
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
            </select><br>
            <input name="programme" placeholder="Programme" type="text"><br>
            <select name="soughtGender">
                <option value="MALE">Men</option>
                <option value="FEMALE">Women</option>
                <option value="BOTH">Both</option>
            </select><br>
            <input name="bio" placeholder="Bio" type="text"><br>

            <input name="image1" type="file">
            <input name="image2" type="file">

            <input id="button" type="submit" name="button" value="Create profile">
        </form>
    </div>
</div>


</body>
</html>
