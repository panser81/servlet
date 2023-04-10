<%@ page import="com.gmail.spanteleyko.web.services.UserService" %>
<%@ page import="com.gmail.spanteleyko.web.services.impl.UserServiceImpl" %>
<%@ page import="com.gmail.spanteleyko.web.models.UserDTO" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: spanteleyko
  Date: 4/7/2023
  Time: 11:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>List of users</h2>


<%
    UserService userService = UserServiceImpl.getInstance();
    List<UserDTO> users = userService.get();

    long usersCount = users.stream().count();
%>

<p>
    Count: <%=usersCount%>
</p>
<table style="text-align: center;">
    <tr>

    </tr>
    <tr style="background-color: #d05a5a">
        <td><b>id</b></td>
        <td><b>username</b></td>
        <td><b>created</b></td>
    </tr>

    <%for (int i = 0; i < usersCount; i++) { %>
    <tr>
        <td><%=users.get(i).getId() %>
        </td>
        <td><%=users.get(i).getUsername() %>
        </td>
        <td><%=users.get(i).getCreatedBy() %>
        </td>
    </tr>
    <%}%>
</table>
</body>
</html>
