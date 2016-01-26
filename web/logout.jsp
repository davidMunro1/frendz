<%--
  Created by IntelliJ IDEA.
  User: Juraj
  Date: 20.01.2016
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        request.getSession().invalidate();
        response.sendRedirect("index.jsp");
    %>
    <title></title>
</head>
<body>

</body>
</html>
