<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ include file="header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <!-- Google font -->
    <link href="https://fonts.googleapis.com/css?family=Poppins:400,700" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value="/css/404.css"/>"/>
    <title>Error <c:out value="${errorCode}"/> | VestNet</title>
</head>
<body>
    <div id="notfound">
        <div class="notfound">
            <div class="notfound-404">
                <h1><c:out value="${errorCode}"/></h1>
            </div>
            <h2><spring:message code="errorTitle"/></h2>
            <h2><spring:message code="error${errorCode}"/> </h2>
            <div><a href="#" onclick="history.back()"><span class="arrow"></span><spring:message code="goBack"/></a></div>
            <div><a href="<c:url value='/'/>"><span class="arrow"></span><spring:message code="goHome"/></a></div>
        </div>
    </div>

</body>
</html>