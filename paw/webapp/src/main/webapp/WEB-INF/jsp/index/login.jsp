<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/signin.css"/>"/>
    <link rel="stylesheet"
          href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>
    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/images/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/images/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/images/favicon-16x16.png"/>">
    <title><spring:message code="page.title.login"/></title>
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
                <div class="form-group">
                    <label><spring:message code = "email"></spring:message></label>
                    <input name="username" class="form-control" placeholder="<spring:message code = "enter_email"/>"/>
                </div>
                <div class="form-group">
                    <label><spring:message code = "password"></spring:message></label>
                    <input name="password" class="form-control" type="password" placeholder="<spring:message code = "enter_password"/>"/>
                    <a href="<c:url value='/requestPassword'/>"><spring:message code="forgotPassword"/></a>
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
                            <c:choose>
                                <c:when test="${sessionScope[\"SPRING_SECURITY_LAST_EXCEPTION\"].message eq 'Bad credentials'}"><p class="mailError"><spring:message code = "loginError"></spring:message></p></c:when>
                                <c:when test="${sessionScope[\"SPRING_SECURITY_LAST_EXCEPTION\"].message eq 'User is disabled'}"><p class="mailError"><spring:message code = "loginVerificationError"></spring:message></p></c:when>
                            </c:choose>
                        </c:if>

                        <c:choose>
                            <c:when test="${message eq 1}"><p class="noError"><spring:message code = "verificationMessageSent"></spring:message></p></c:when>
                            <c:when test="${message eq 2}"><p class="mailError"><spring:message code = "verificationInvalid"></spring:message></p></c:when>
                            <c:when test="${message eq 3}"><p class="mailError"><spring:message code = "verificationTokenExpired"></spring:message></p></c:when>
                            <c:when test="${message eq 4}"><p class="noError"><spring:message code = "verificationMade"></spring:message></p></c:when>
                        </c:choose>
                    </div>
                </div>
            </form>
            <c:url var="register" value="/signUp"></c:url>
            <div>
                <label><spring:message code = "noReg"></spring:message></label>
                <a href="<c:url value='/signUp'/>" class="btn btn-outline-dark"><spring:message code='sign_up'/></a>
            </div>
        </div>
    </div>
</div>


</body>
</html>
