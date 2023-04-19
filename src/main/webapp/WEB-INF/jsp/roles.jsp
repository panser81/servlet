<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.gmail.spanteleyko.web.services.RoleService" %>
<%@ page import="com.gmail.spanteleyko.web.services.impl.RoleServiceImpl" %>
<%@ page import="com.gmail.spanteleyko.web.models.RoleDTO" %>
<html>
<body>
<h2>List of roles</h2>


<%
    RoleService roleService = RoleServiceImpl.getInstance();
    List<RoleDTO> roles = roleService.get();

    long rolesCount = roles.stream().count();
%>

<p>
    Count: <%=rolesCount%>
</p>
<table style="text-align: center;">
    <tr>

    </tr>
    <tr style="background-color: #d05a5a">
        <td><b>id</b></td>
        <td><b>name</b></td>
        <td><b>description</b></td>
    </tr>

    <%for (int i = 0; i < rolesCount; i++) { %>
    <tr>
        <td><%=roles.get(i).getId() %>
        </td>
        <td><%=roles.get(i).getName() %>
        </td>
        <td><%=roles.get(i).getDescription() %>
        </td>
    </tr>
    <%}%>
</table>
</body>
</html>
