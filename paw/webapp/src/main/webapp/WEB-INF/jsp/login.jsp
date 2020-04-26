
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
    <link rel="stylesheet" href="<c:url value="/css/signin.css"/>"/>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/images/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/images/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/images/favicon-16x16.png"/>">
    <link rel="manifest" href="<c:url value="/images/site.webmanifest"/>">
    <title>Sign in</title>
</head>
<body>

<div class="sidenav">
    <c:url var="logo" value="/images/logo_bp.png"/>
    <div class="text-center mt-5">
        <img src=${logo} class="rounded">
    </div>


</div>
<div class="main">
    <div class="col-md-6 col-sm-12">
        <div class="login-form">
            <c:url value="/login" var="loginUrl"></c:url>
            <form method="post" action="${loginUrl}" >
                <div class="form-group">
                    <label><spring:message code = "username"></spring:message></label>
                    <input name="username" class="form-control" placeholder="<spring:message code = "username"/>"/>
                </div>
                <div class="form-group">
                    <label><spring:message code = "password"></spring:message></label>
                    <input name="password" class="form-control" type="password" placeholder="<spring:message code = "password"/>"/>
                </div>
                <div class="form-group">
                    <label>
                        <input name="remember_me" type="checkbox"/>
                        <spring:message code = "rememberMe"></spring:message>
                    </label>
                </div>
                <input type="submit"   class="btn btn-black" value="<spring:message code = "submit"></spring:message>">
                <c:url var="register" value="/signUp"></c:url>
            </form>

            <form action="${register}">
                <input type="submit" class="btn btn-black" value="<spring:message code='sign_up'></spring:message>" />
            </form>
        </div>
    </div>
</div>


</body>
</html>
