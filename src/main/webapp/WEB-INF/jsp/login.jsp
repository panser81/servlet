<%--
  Created by IntelliJ IDEA.
  User: spanteleyko
  Date: 4/7/2023
  Time: 3:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
<h1>Login</h1>

<form action="login" method="post">
    <label for="username">User Name</label>
    <input type="text" id="username" name="username">

    <br/>
    <label for="password">Password</label>
    <input type="password" id="password" name="password">
    <br/>

    <input type="submit" value="Login">
</form>

</body>
</html>
