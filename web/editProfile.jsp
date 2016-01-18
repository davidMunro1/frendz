<%@ page import="com.google.appengine.api.blobstore.UploadOptions" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="beans.UserBeanBean" %>
<%@ page import="hibernate.UserProfileEntity" %>
<%@ page import="hibernate.UserEntity" %>
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
<html id="edit_profile_page">
<head>
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>Edit profile</title>

    <%
        UserBeanBean bean = (UserBeanBean)request.getSession().getAttribute("bean");
//        UserBeanBean bean = new UserBeanBean();
//        bean.setUSER_ID(53);
        UserEntity user = bean.getUser();
        UserProfileEntity profile = bean.getProfile();
    %>

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
        <form action="<%= blobstoreService.createUploadUrl("/FrendzServlet", UploadOptions.Builder.withGoogleStorageBucketName("images_frendz/user_images")) %>" method="post" enctype="multipart/form-data">
            <div style="position: relative; left: 0px; width: 50%; margin-top: 120px; margin-bottom: 80px; z-index: 99;">
                <input id="second_name" name="secondName" placeholder="<%=user.getSecondName()%>" type="text"><br>
                <input id="password" name="password" placeholder="Password" type="password"><br>
                <input id="programme_input" name="programme" placeholder="<%=profile.getProgramme()%>" type="password"><br>
                <input id="bio_input" name="bio" placeholder="<%=profile.getBio()%>" type="text" maxlength="150"><br>
            </div>

            <div style="position: absolute; left: 50%; width: 50%; top: 140px;">
            <div style="position:relative;">
                <div id="image1" class="file">
                    <%
                        if(profile.getImage1() != null && !profile.getImage1().isEmpty()) {
                            out.println("<div class='edit_profile_picture'><img src='" + bean.getServingURL(profile.getImage1()) + "'></div>");
                            out.println("Change image");
                        } else {
                            out.println("Select an image");
                        }
                    %>
                    <%--<div class="edit_profile_picture"><img src="images/juraj.jpg"></div>--%>
                </div>
                <input id="img1cntrlr" name="image1controller" type="text" hidden="true">
                <input type="file" name="image1" class="file_input" onchange="document.getElementById('image1').innerHTML = this.value; document.getElementById('img1cntrlr').value = this.value;" />
            </div>
            <div style="position:relative;">
                <div id="image2" class="file">
                    <%
                        if(profile.getImage2() != null && !profile.getImage2().isEmpty()) {
                            out.println("<div class='edit_profile_picture'><img src='" + bean.getServingURL(profile.getImage2()) + "'></div>");
                            out.println("Change image");
                        } else {
                            out.println("Select an image");
                        }
                    %>
                        <%--<img class="edit_profile_picture" src="images/juraj.jpg">--%>
                </div>
                <input id="img2cntrlr" name="image2controller" type="text" hidden="true">
                <input type="file" name="image2" class="file_input" onchange="document.getElementById('image2').innerHTML = this.value; document.getElementById('img2cntrlr').value = this.value;" />
            </div>
            <div style="position:relative;">
                <div id="image3" class="file">
                    <%
                        if(profile.getImage3() != null && !profile.getImage3().isEmpty()) {
                            out.println("<div class='edit_profile_picture'><img src='" + bean.getServingURL(profile.getImage3()) + "'></div>");
                            out.println("Change image");
                        } else {
                            out.println("Select an image");
                        }
                    %>
                </div>
                <input id="img3cntrlr" name="image3controller" type="text" hidden="true">
                <input type="file" name="image3" class="file_input" onchange="document.getElementById('image3').innerHTML = this.value; document.getElementById('img3cntrlr').value = this.value;" />
            </div>
            <div style="position:relative;">
                <div id="image4" class="file">
                    <%
                        if(profile.getImage4() != null && !profile.getImage4().isEmpty()) {
                            out.println("<div class='edit_profile_picture'><img src='" + bean.getServingURL(profile.getImage4()) + "'></div>");
                            out.println("Change image");
                        } else {
                            out.println("Select an image");
                        }
                    %>
                </div>
                <input id="img4cntrlr" name="image4controller" type="text" hidden="true">
                <input type="file" name="image4" class="file_input" onchange="document.getElementById('image4').innerHTML = this.value; document.getElementById('img4cntrlr').value = this.value;" />
            </div>
            <div style="position:relative;">
                <div id="image5" class="file">
                    <%
                        if(profile.getImage5() != null && !profile.getImage5().isEmpty()) {
                            out.println("<div class='edit_profile_picture'><img src='" + bean.getServingURL(profile.getImage5()) + "'></div>");
                            out.println("Change image");
                        } else {
                            out.println("Select an image");
                        }
                    %>
                </div>
                <input id="img5cntrlr" name="image5controller" type="text" hidden="true">
                <input type="file" name="image5" class="file_input" onchange="document.getElementById('image5').innerHTML = this.value; document.getElementById('img5cntrlr').value = this.value;" />
            </div>
            </div>

            <input id="button" type="submit" name="button" value="Save">
        </form>
    </div>
    </div>

</body>
</html>
