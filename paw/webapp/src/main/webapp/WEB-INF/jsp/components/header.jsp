<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
    <link rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<%-- Set used variables --%>
<sec:authorize access="!isAuthenticated()">
    <c:set var="options" value="${fn:split('/welcome,/login,/signUp', ',')}"/>
    <c:set var="icons" value="${fn:split('home-icon,login-icon,signup-icon', ',')}"/>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_ENTREPRENEUR')">
    <c:set var="dropdownpages" value="${fn:split('/dashboard,/deals,/profile', ',')}"/>
    <c:set var="icons" value="${fn:split('home-icon,user-icon', ',')}"/>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_INVESTOR')">
    <c:set var="dropdownpages" value="${fn:split('/requests,/profile', ',')}"/>
    <c:set var="icons" value="${fn:split('home-icon,user-icon', ',')}"/>
</sec:authorize>

<%-- Set used URLs --%>
<c:url var="link_home" value='/'/>
<c:url var="icon_logo" value="/images/logo_bp.png"/>
<c:url var="icon_logout" value="/images/logout-icon.png"/>
<c:url var="icon_home" value="/images/home-icon.png"/>
<c:url var="icon_user" value="/images/user-icon.png"/>

<body>
    <nav class="navbar navbar-dark navbar-expand-sm navbar-custom">
        <%-- Logo and name --%>
        <a class="navbar-brand" href="${link_home}">
            <img src="${icon_logo}" width="60" class="logo-img" alt="<spring:message code="logo"/>">
        </a>
        <a class="logo-text" href="${link_home}">
            VestNet
        </a>

        <%-- Align --%>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <%-- Icons --%>
        <div class="collapse navbar-collapse topnav-right" id="navbarNavDropdown">
            <ul class="navbar-nav">

                <sec:authorize access="isAuthenticated()">
                    <li class="nav-item">
                        <a class="btn btn-header btn-transp nav-link" href="<c:url value="/projects"/>">
                            <div class="row justify-content-center"><img class="nav-icon" src="${icon_home}"></div>
                        </a>
                    </li>
                    <li class="nav-item">
                        <div class="dropdown">
                            <a class="btn btn-header btn-transp dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <div class="row justify-content-center"><img class="nav-icon" src="${icon_user}"></div>
                            </a>
                            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
                                <c:forEach var="page" items="${dropdownpages}" varStatus="index">
                                    <a class="dropdown-item" href="<c:url value='${page}'/>"><spring:message code="header.${page}"/></a>
                                </c:forEach>
                                <div class="dropdown-divider"></div>
                                <sec:authorize access="hasRole('ROLE_ENTREPRENEUR')">
                                    <a class="dropdown-item" href="<c:url value="/newProject"/>"><spring:message code="header./newProject"/></a>
                                    <div class="dropdown-divider"></div>
                                </sec:authorize>

                                <a class="dropdown-item" data-toggle="modal" data-target="#exampleModal"><spring:message code="header./logout"/></a>
                            </div>
                        </div>
                    </li>
                </sec:authorize>

                <sec:authorize access="!isAuthenticated()">
                    <c:forEach var="option" items="${options}" varStatus="index">
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="${option}"/>">
                                <c:url var="icon_generic" value="/images/${icons[index.index]}.png"/>
                                <div class="row justify-content-center"><img class="nav-icon" src="${icon_generic}"></div>
                                <div class="row text-icon"><spring:message code="header.${option}"/></div>
                            </a>
                        </li>
                    </c:forEach>
                </sec:authorize>

            </ul>
        </div>
    </nav>

    <!-- Show logout confirmation -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog " role="document">
            <div class="modal-content mx-auto my-auto">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">
                        <spring:message code="conf_logout"/>
                    </h5>
                </div>
                <div class="modal-body">
                    <spring:message code="conf_logout_body"/>
                </div>
                <div class="modal-footer">
                    <div class="row">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">
                            <spring:message code="cancel"/>
                        </button>
                        <a href="<c:url value="/logout"/> " type="button" class="btn btn-success">
                            <spring:message code="confirm"/>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
