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

<%-- Set used URLs --%>
<c:url var="icon_logo" value="/images/logo_bp.png"/>
<c:url var="link_sign_up" value="/signUp"/>
<c:url var="link_request_pass" value='/requestPassword'/>

<body>

<div class="sidenav">
    <div class="text-center mt-5">
        <img class="logo-img" src=${icon_logo} >
    </div>
</div>

<div class="main">
    <div class="col-md-8">
        <div class="login-form">
            <c:url value="/login" var="loginUrl"/>
            <form method="post" action="${loginUrl}" >
                <div class="form-group">
                    <label><spring:message code = "email"/></label>
                    <input name="username" class="form-control" placeholder="<spring:message code = "enter_email"/>"/>
                </div>
                <div class="form-group">
                    <label><spring:message code = "password"/></label>
                    <input name="password" class="form-control" type="password" placeholder="<spring:message code = "enter_password"/>"/>
                    <a href="${link_request_pass}"><spring:message code="forgotPassword"/></a>
                </div>
                <div class="form-group">
                    <label>
                        <input name="remember_me" type="checkbox"/>
                        <spring:message code = "rememberMe"/>
                    </label>
                </div>
                <div class="row justify-content-center">
                    <div class="col-2 text-left">
                        <input type="submit"   class="btn btn-dark" value="<spring:message code = "submit"/>">
                    </div>
                    <div class="col mailError text-right">
                        <c:if test="${param.error != null}">
                            <c:choose>
                                <c:when test="${sessionScope[\"SPRING_SECURITY_LAST_EXCEPTION\"].message eq 'Bad credentials'}"><p class="mailError"><spring:message code = "loginError"/></p></c:when>
                                <c:when test="${sessionScope[\"SPRING_SECURITY_LAST_EXCEPTION\"].message eq 'User is disabled'}"><p class="mailError"><spring:message code = "loginVerificationError"/></p></c:when>
                            </c:choose>
                        </c:if>
                        <c:choose>
                            <c:when test="${message eq 10}"><p class="mailError"><spring:message code = "verificationInvalid"/></p></c:when>
                            <c:when test="${message eq 11}"><p class="mailError"><spring:message code = "verificationTokenExpired"/></p></c:when>
                            <c:when test="${message eq 12}"><p class="mailError"><spring:message code="passwordInvalidToken"/></p></c:when>
                            <c:when test="${message eq 13}"><p class="mailError"><spring:message code="passwordExpiredToken"/></p></c:when>
                        </c:choose>
                    </div>
                    <div class="col mailSent text-right">
                        <c:choose>
                            <c:when test="${message eq 1}"><p class="mailSent"><spring:message code = "verificationMessageSent"/></p></c:when>
                            <c:when test="${message eq 2}"><p class="mailSent"><spring:message code = "verificationMade"/></p></c:when>
                            <c:when test="${message eq 3}"><p class="mailSent"><spring:message code="passwordRecoverySent"/></p></c:when>
                            <c:when test="${message eq 4}"><p class="mailSent"><spring:message code="passwordRecoveryMade"/></p></c:when>
                        </c:choose>

                    </div>


                    </div>
                </div>
            </form>

            <div>
                <label><spring:message code = "noReg"/></label>
                <a href="${link_sign_up}" class="btn btn-outline-dark"><spring:message code='sign_up'/></a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
