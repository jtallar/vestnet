<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

<%--    TODO: VER SI NECESITAMOS FONT AWESOME ENTERA--%>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <!-- Google Fonts -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
    <!-- Bootstrap core CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.4.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- Material Design Bootstrap -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.16.0/css/mdb.min.css" rel="stylesheet">

    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/images/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/images/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/images/favicon-16x16.png"/>">
<%--    <link rel="manifest" href="<c:url value="/images/site.webmanifest"/>">--%>

    <link rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<c:choose>
    <c:when test="${roleNumber eq 0}">
        <c:set var="navbarClass" value="navbar navbar-dark navbar-expand-sm navbar-custom"/>
        <c:set var="searchButtonClass" value="btn btn-black"/>
        <c:url var="logo" value="/images/logo_bp.png"/>
        <c:url var="lupa" value="/images/lupa_bw.png"/>
        <c:set var="options" value="${fn:split('/welcome,/login,/signUp', ',')}"/>
        <c:set var="icons" value="${fn:split('home-icon,login-icon,signup-icon', ',')}"/>
    </c:when>
    <c:when test="${roleNumber eq 1}">
        <c:set var="navbarClass" value="navbar navbar-dark navbar-expand-sm navbar-custom"/>
        <c:set var="searchButtonClass" value="btn btn-black"/>
        <c:url var="logo" value="/images/logo_bp.png"/>
        <c:url var="lupa" value="/images/lupa_bw.png"/>
        <c:set var="options" value="${fn:split('/projects,/newProject,/messages,/deals,/myProfile', ',')}"/>
        <c:set var="icons" value="${fn:split('home-icon,new-icon,projects-icon,deals-icon,user-icon', ',')}"/>
    </c:when>
    <c:when test="${roleNumber eq 2}">
        <c:set var="navbarClass" value="navbar navbar-dark navbar-expand-sm navbar-custom"/>
        <c:set var="searchButtonClass" value="btn btn-black"/>
        <c:url var="logo" value="/images/logo_bp.png"/>
        <c:url var="lupa" value="/images/lupa_bw.png"/>
        <c:set var="options" value="${fn:split('/projects,/requests,/myProfile', ',')}"/>
        <c:set var="icons" value="${fn:split('home-icon,offer-icon,user-icon', ',')}"/>
    </c:when>
</c:choose>

<body>
    <nav class="${navbarClass}">
        <a class="navbar-brand" href="<c:url value='/'/>">
            <img src="${logo}" width="60" class="logo-img" alt="<spring:message code="logo"/>">
        </a>
        <a class="logo-text" href="<c:url value='/'/>">
            VestNet
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse topnav-right" id="navbarNavDropdown">
            <ul class="navbar-nav">
                <c:forEach var="option" items="${options}" varStatus="index">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="${option}"/>">
                            <c:url var="icon" value="/images/${icons[index.index]}.png"/>
                            <div class="row justify-content-center"> <img class="nav-icon" src="${icon}"></div>
                            <div class="row text-icon"><spring:message code="header.${option}"/></div>
                        </a>
                    </li>
                </c:forEach>
                <c:if test="${roleNumber != 0}">
                    <li class="nav-item">

                        <!-- Button trigger modal -->
                        <a type="button" class="nav-link" data-toggle="modal" data-target="#exampleModal">
                            <c:url var="logout" value="/images/logout-icon.png"/>
                            <div class="row justify-content-center"> <img class="nav-icon" src="${logout}"></div>
                            <div class="row text-icon"> <spring:message code="header./logout"/> </div>
                        </a>
                    </li>
                </c:if>
            </ul>
        </div>

    </nav>

    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog " role="document">
            <div class="modal-content mx-auto my-auto">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel"><spring:message code="conf_logout"/> </h5>
                </div>
                <div class="modal-body">
                    <spring:message code="conf_logout_body"/>
                </div>
                <div class="modal-footer">
                    <div class="row">
                        <button type="button" class="btn btn-danger" data-dismiss="modal"><spring:message code="cancel"/> </button>
                        <a href="<c:url value="/logout"/> " type="button" class="btn btn-success"><spring:message code="confirm"/></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
