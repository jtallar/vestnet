<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/signin.css"/>"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>

    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/images/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/images/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/images/favicon-16x16.png"/>">
    <title><spring:message code="page.title.requestPassword"/></title>
</head>

<%-- Set used URLs --%>
<c:url var="icon_logo" value="/images/logo_bp.png"/>
<c:url var="link_request_pass" value="/requestPassword"/>
<c:url var="link_login" value="/login"/>

<body>
<%-- Side navigation --%>
<div class="sidenav">
    <div class="text-center mt-5">
        <img class="logo-img" src=${icon_logo}>
    </div>
</div>

<div class="main">
    <div class="col-md-8">
        <div class="login-form">
            <div class="row back-req">
                <a href="${link_login}" class="btn btn-outline-dark pull-left"><spring:message code="back"/></a>
            </div>
            <form method="post" action="${link_request_pass}">
                <h4><spring:message code="requestPasswordTitle"/></h4>
                <p><spring:message code="requestPasswordSubtitle"/></p>
                <div class="form-group">
                    <label><spring:message code="email"/></label>
                    <input name="username" class="form-control" placeholder="<spring:message code = "enter_email"/>"/>
                </div>
                <div class="row justify-content-center">
                    <div class="col-2 text-left">
                        <input type="submit" class="btn btn-dark" value="<spring:message code = "send"/>">
                    </div>
                    <c:if test="${error}">
                        <div class="col mailError">
                            <p class="mailError"><spring:message code="emailError"/></p>
                        </div>
                    </c:if>
                    <div class="col"></div>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
