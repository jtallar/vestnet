
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
<%--    <link href="https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/css/bootstrap4-toggle.min.css" rel="stylesheet">--%>
<%--    <script src="https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js"></script>--%>
    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/images/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/images/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/images/favicon-16x16.png"/>">
    <title>VestNet | Login</title>
</head>
<body>

<div class="sidenav">
    <c:url var="logo" value="/images/logo_bp.png"/>
    <div class="text-center mt-5">
        <img class="logo-img" src=${logo} >
    </div>
</div>

<div class="main">
    <div class="col-md-8">
        <div class="login-form">
            <c:url value="/login" var="loginUrl"></c:url>
            <form method="post" action="${loginUrl}" >
<%--                <div class="form-group">--%>
<%--                    <div><label><spring:message code = "role"></spring:message></label></div>--%>
<%--                    <input name="user_type" type="checkbox" checked data-toggle="toggle" data-on="<spring:message code="investor"/>" data-off="<spring:message code="entrepreneur"/>" data-onstyle="dark" data-offstyle="dark">--%>
<%--                </div>--%>
                <div class="form-group">
                    <label><spring:message code = "email"></spring:message></label>
                    <input name="username" class="form-control" placeholder="<spring:message code = "enter_email"/>"/>
                </div>
                <div class="form-group">
                    <label><spring:message code = "password"></spring:message></label>
                    <input name="password" class="form-control" type="password" placeholder="<spring:message code = "enter_password"/>"/>
                </div>
                <div class="form-group">
                    <label>
                        <input name="remember_me" type="checkbox"/>
                        <spring:message code = "rememberMe"></spring:message>
                    </label>
                </div>
                <div class="row justify-content-center">
                    <div class="col-2 text-left">
                        <input type="submit"   class="btn btn-dark" value="<spring:message code = "submit"></spring:message>">
                    </div>
                    <div class="col mailError">
                        <c:if test="${param.error != null}">
                        <p class="mailError"><spring:message code="loginError"/></p>
                        </c:if>
                    </div>
                </div>
            </form>
            <c:url var="register" value="/signUp"></c:url>
            <form action="${register}">
                <label><spring:message code = "noReg"></spring:message></label>
                <input type="submit" class="btn btn-outline-dark" value="<spring:message code='sign_up'></spring:message>" />
            </form>
        </div>
    </div>
</div>


</body>
</html>
