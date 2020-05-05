<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/signin.css"/>"/>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

<%--    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>--%>
    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/images/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/images/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/images/favicon-16x16.png"/>">
    <title>VestNet | Welcome</title>
</head>
<body>

<div class="sidenav">
    <c:url var="logo" value="/images/logo_bp.png"/>
    <div class="text-center mt-5">
        <img class="logo-img" src=${logo}>
    </div>
</div>

<div class="main">
    <div class="col-md-8">
        <div class="welcome">
            <h1><spring:message code='w_welcome'/><b> VestNet</b></h1>
                <h2><spring:message code='w_subtitle'/></h2>
                <ul class="nav nav-tabs" id="myTab" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" id="inv-tab" data-toggle="tab" href="#inv" role="tab" aria-controls="inv" aria-selected="true"><spring:message code="invest_tab"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="entrep-tab" data-toggle="tab" href="#entrep" role="tab" aria-controls="entrep" aria-selected="false"><spring:message code="entrep_tab"/></a>
                    </li>
                </ul>
                <div class="tab-content inv-tab" id="myTabContent">
                    <div class="tab-pane fade show active" id="inv" role="tabpanel" aria-labelledby="inv-tab">
                        <h4><spring:message code='w_i_ask'/></h4>
                        <p><spring:message code='w_i_p1'/></p>
                        <p><spring:message code='w_i_p2'/></p>
                        <p><spring:message code='w_i_p3'/></p>
                        <h5><spring:message code='w_i_register'/></h5>
                        <h4><spring:message code='w_bepart'/></h4>
                        <a href="<c:url value='/signUp'/>" class="btn btn-dark"><spring:message code='sign_up'/></a>
                        <a href="<c:url value='/projects'/>" class="btn btn-outline-dark distance-btn" ><spring:message code='visit_web'/></a>
                    </div>
                    <div class="tab-pane fade" id="entrep" role="tabpanel" aria-labelledby="entrep-tab">
                        <h4><spring:message code='w_e_ask'/></h4>
                        <p><spring:message code='w_e_p1'/></p>
                        <p><spring:message code='w_e_p2'/></p>
                        <p><spring:message code='w_e_p3'/></p>
                        <h5><spring:message code='w_e_register'/></h5>
                        <h4><spring:message code='w_bepart'/></h4>
                        <a href="<c:url value='/signUp'/>" class="btn btn-dark"><spring:message code='sign_up'/></a>
                    </div>
                </div>
        </div>
    </div>
</div>


</body>
</html>
