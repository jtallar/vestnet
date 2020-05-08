<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "header.jsp" %>

<html>
<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <%--<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>--%>
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>

    <title>My Projects | Vestnet</title>
</head>
<body>
<c:forEach var="project" items="${projects}">
    <div class="container py-3">
        <div class="card">
            <div class="row ">
                <div class="col-md-4">
                    <img src="<c:url value="/imageController/project/${project.id}"/>" class="w-100">
                </div>
                <div class="col-md-8 px-3">
                    <div class="card-block px-3">
                        <h4 class="card-title">${project.name}{</h4>
                        <p class="card-text">${project.summary} </p>
                        <h5><spring:message code="cost"></spring:message> </h5>
                        <p class="card-text"> ${project.cost}</p>
                    </div>
                </div>

            </div>
        </div>
    </div>
    </div>
</c:forEach>
<c:if test="${empty projects}">
    <div class="card m-2">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="noProjOwned" arguments=""></spring:message> </h5>
        </div>
    </div>
</c:if>
<c:url var="addProject" value="/newProject"></c:url>

<div class="text-center mt-5">
    <a href="${addProject}" class="btn btn-secondary btn-lg"> <spring:message code="add_project"></spring:message> </a>
</div>
</body>
</html>
<!--
-->