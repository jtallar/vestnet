<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/signin.css"/>"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/images/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/images/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/images/favicon-16x16.png"/>">
    <title><spring:message code="page.title.resetPassword"/></title>
</head>

<%-- Set used URLs --%>
<c:url var="icon_logo" value="/images/logo_bp.png"/>
<c:url var="link_home" value="/"/>
<c:url var="link_reset_pass" value="/resetPassword"/>

<body>
<div class="sidenav">
    <div class="text-center mt-5">
        <img class="logo-img" src=${icon_logo}>
    </div>
</div>

<div class="main">
    <div class="col-md-8">
        <div class="login-form">
            <div class="text-left my-2">
                <a href="${link_home}" class="btn btn-outline-dark pull-left"><spring:message code="home"/></a>
            </div>
            <form:form modelAttribute="passwordForm" method="POST" action="${link_reset_pass}">
                <h4><spring:message code="resetPasswordTitle"/></h4>
                <p><spring:message code="resetPasswordSubtitle" arguments="${email}"/></p>
                <div class="form-group">
                    <label><spring:message code="password"/> </label>
                    <spring:message code="enter_password" var="enter_password"/>
                    <form:input type="password" class="form-control" path="password"
                                placeholder="${enter_password}" id="reset-password"/>
                    <form:errors path="password" element="p" cssClass="formError"/>
                </div>
                <div class="form-group">
                    <label><spring:message code="repeat_password"/> </label>
                    <spring:message code="enter_repeat_password" var="enter_repeat_password"/>
                    <form:input type="password" class="form-control" path="repeatPassword"
                                placeholder="${enter_repeat_password}" id="reset-repeat-password"/>
                    <form:errors cssClass="formError" element="p"/>
                </div>

                <form:input path="email" value="${email}" type="hidden"/>
                <form:input path="token" value="${token}" type="hidden"/>

                <input type="submit"   class="btn btn-dark" value="<spring:message code = "changePassword"/>"
                       onclick="adjustInputs()"/>
            </form:form>
        </div>
    </div>
</div>

<script>
    function adjustInputs() {
        let passwordTag = document.getElementById('reset-password');
        passwordTag .value = passwordTag .value.trim();
        let repeatPasswordTag = document.getElementById('reset-repeat-password');
        repeatPasswordTag .value = repeatPasswordTag .value.trim();
    }
</script>
</body>
</html>
