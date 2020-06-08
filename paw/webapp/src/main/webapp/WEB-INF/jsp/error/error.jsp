<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ include file="../components/header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <!-- Google font -->
    <link href="https://fonts.googleapis.com/css?family=Poppins:400,700" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value="/css/404.css"/>"/>
    <title><spring:message code="page.title.error" arguments="${errorCode}"/></title>
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