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
<%--        TODO: COMO PERSISTO ROL PARA ESTE--%>
<c:choose>
    <c:when test="${roleNumber eq 0}">
        <c:set var="navbarClass" value="navbar navbar-light navbar-expand-sm navbar-custom3"/>
        <c:set var="searchButtonClass" value="btn logopurple"/>
        <c:url var="logo" value="/images/logo_bw.png"/>
        <c:url var="lupa" value="/images/lupa_v.png"/>
        <c:set var="options" value="${fn:split('/welcome,/login,/signUp', ',')}"/>
    </c:when>
    <c:when test="${roleNumber eq 1}">
        <c:set var="navbarClass" value="navbar navbar-light navbar-expand-sm navbar-custom2"/>
        <c:set var="searchButtonClass" value="btn btn-black"/>
        <c:url var="logo" value="/images/logo_wp.png"/>
        <c:url var="lupa" value="/images/lupa_bw.png"/>
        <c:set var="options" value="${fn:split('/projects,/newProject,/messages,/deals,/myProfile,/logout', ',')}"/>
    </c:when>
    <c:when test="${roleNumber eq 2}">
        <c:set var="navbarClass" value="navbar navbar-dark navbar-expand-sm navbar-custom"/>
        <c:set var="searchButtonClass" value="btn logopurple"/>
        <c:url var="logo" value="/images/logo_bp.png"/>
        <c:url var="lupa" value="/images/lupa_v.png"/>
        <c:set var="options" value="${fn:split('/projects,/requests,/myProfile,/logout', ',')}"/>
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
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav">
                <c:forEach var="option" items="${options}">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value="${option}"/>"><spring:message code="header.${option}"/></a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <c:url var="createUrl" value='/projects'/>
        <form class="form-inline mx-auto my-2 my-lg-0" action="${createUrl}" method="get">
            <spring:message var="search" code="search"></spring:message>
            <c:choose>
                <c:when test="${not empty keyword}">
                    <input class="form-control mx-1 my-auto col-5" name="keyword" value="${keyword}" type="text" placeholder="${search}" aria-label="Search"/>
                </c:when>
                <c:when test="${empty keyword}">
                    <input class="form-control mx-1 my-auto col-5" name="keyword" type="text" placeholder="${search}" aria-label="Search"/>
                </c:when>
            </c:choose>

            <select id="searchSelector" name="searchField" class="custom-select mx-1 col-4">
                <option value="default" <c:if test="${searchField == null or searchField eq 'default' }"> selected </c:if>><spring:message code="project_name"></spring:message> </option>
                <option value="project_info" <c:if test="${searchField eq 'project_info'}"> selected </c:if>><spring:message code="project_info"></spring:message> </option>
                <option value="owner_name" <c:if test="${searchField eq 'owner_name'}"> selected </c:if>><spring:message code="owner_name"></spring:message> </option>
                <option value="owner_email" <c:if test="${searchField eq 'owner_email'}"> selected </c:if>><spring:message code="owner_email"></spring:message> </option>
                <option value="project_location" <c:if test="${searchField eq 'project_location'}"> selected </c:if>><spring:message code="loc"></spring:message> </option>
            </select>
            <button type="submit" class="${searchButtonClass} col-1">
                <img src="${lupa}" height="29" alt="<spring:message code='search'/>"/>
            </button>
        </form>
    </nav>
</body>
</html>
