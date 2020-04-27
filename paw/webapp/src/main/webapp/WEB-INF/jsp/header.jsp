<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

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
<body>
<c:url var="logo" value="/images/logo_bp.png"/>
<c:url var="lupa" value="/images/lupa_v.png"/>
<nav class="navbar navbar-dark navbar-expand-sm navbar-custom">
    <a class="navbar-brand" href="<c:url value='/'/>">
        <img src=${logo} width="60" class="logo-img">
    </a>
    <a class="logo-text" href="<c:url value='/'/>">
        VestNet
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="<c:url value='/projects'/>"><spring:message code="feed"></spring:message></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value='/myProfile'/>"><spring:message code="my_profile"></spring:message></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value='/logout'/>"><spring:message code="logout"></spring:message></a>
            </li>

<%--            <li class="nav-item">--%>
<%--                <a class="nav-link" href="#"><spring:message code="myprojects"></spring:message></a>--%>
<%--            </li>--%>
<%--            <li class="nav-item dropdown">--%>
<%--                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">--%>
<%--                    <spring:message code="more"></spring:message>--%>
<%--                </a>--%>
<%--                <div class="dropdown-menu" aria-labelledby="navbarDropdown">--%>
<%--                    <a class="dropdown-item" href="#"><spring:message code="editprofile"></spring:message></a>--%>
<%--                    <a class="dropdown-item" href="#"><spring:message code="help"></spring:message></a>--%>
<%--                    <div class="dropdown-divider"></div>--%>
<%--                    <a class="dropdown-item" href="#"><spring:message code="logout"></spring:message></a>--%>
<%--                </div>--%>
<%--            </li>--%>
        </ul>
    </div>
    <c:url var="searchURL" value="/search"/>
    <form class="form-inline mx-auto my-2 my-lg-0" action="${searchURL}" method="get">
        <input class="form-control mx-auto mx-auto" id="searching" type="text" placeholder="<spring:message code='search'></spring:message>" aria-label="Search" name="searching" />
        <button type="submit" class="btn btn-purple">
            <img src=${lupa} height="29">
        </button>
    </form>
</nav>
</body>
</html>
