
<%--
  Created by IntelliJ IDEA.
  User: njtallar
  Date: 15/4/20
  Time: 17:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:url value="/login" var="loginUrl"></c:url>
<form method="post" action="${loginUrl}" >
    <div>
        <label>
            <spring:message code = "username"></spring:message>
            <input name="username" placeholder="<spring:message code = "username"/>"/>
        </label>
    </div>
    <div>
        <label>
            <spring:message code = "password"></spring:message>
            <input name="password" type="password" placeholder="<spring:message code = "password"/>"/>
        </label>
    </div>
    <div>
        <label>
            <input name="remember_me" type="checkbox"/>
            <spring:message code = "rememberMe"></spring:message>
        </label>
    </div>

    <input type="submit"  value="<spring:message code = "submit"></spring:message>">

</form>


</body>
</html>
