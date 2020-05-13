<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "../components/header.jsp" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/userprofile.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><spring:message code="header./requests"/> | VestNet</title>
</head>
<body>
<c:if test="${!empty messages}">
    <div class="flex-row-reverse mt-4">
        <nav aria-label="Page navigation example">
            <div class="row">
                <div class="col-4 text-center tab-title">
                    <strong> <spring:message code="req_title"/> </strong>
                </div>
                <div class="col-4">
                    <ul class="pagination justify-content-center">
                    <c:set value="${page + 1}" var="nextOne"/>
                    <c:set value="${page - 1}" var="previous"/>
                    <c:if test="${page != 1}">
                        <li class="page-item mx-2">
                            <a href="<c:url value='/requests?page=${previous}'/>" class="page-link2"><spring:message code="previous"/></a>
                        </li>
                        <li class="page-item mx-2"><a href="<c:url value='/requests?page=${previous}'/>" class="page-link2">${previous}</a></li>
                    </c:if>
                    <li class="page-item mx-2"><a href="<c:url  value='/requests?page=${page}'/>" class="page-link2-active">${page}</a></li>
                    <c:if test="${hasNext eq true}">
                        <li class="page-item mx-2"><a href="<c:url  value='/requests?page=${nextOne}'/>" class="page-link2">${nextOne}</a></li>
                        <li class="page-item mx-2">
                            <a href="<c:url value='/requests?page=${nextOne}'/>" class="page-link2"><spring:message code="next"/> </a>
                        </li>
                    </c:if>
                    </ul>
                </div>
                <div class="col-"></div>
            </div>
        </nav>
    </div>
</c:if>


<c:forEach var="message" items="${messages}" varStatus="theCount">
    <span class="anchor-header" id="dashboard-project-${project.id}"></span>
    <div class="container-deal py-3">
        <div class="card">
            <div class="card-deal">
                <strong><spring:message code="msg"/> </strong>
                <p>${message.content.message}</p>
                <div class="row ">
                    <div class="col-5">
                        <strong><spring:message code="offer"/> </strong>
                        <p>${message.content.offer}</p>
                    </div>
                    <div class="col-6">
                        <strong><spring:message code="request"/> </strong>
                        <p>${message.content.interest}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-8 text-centered">
                        <strong><spring:message code="state"/> </strong>
                        <c:set var="accepted" value="${message.isAccepted()}"/>
                        <c:choose>
                            <c:when test="${accepted == null}">
                                <h3 class="text-secondary"><spring:message code="pending"/> </h3>
                            </c:when>
                            <c:when test="${accepted eq true}">
                                <h3 class="text-success"><spring:message code="accepted"/> </h3>
                            </c:when>
                            <c:when test="${accepted eq false}">
                                <h3 class="text-danger"><spring:message code="rejected"/> </h3>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="card-footer">
                <div class="row ">
                    <div class="col-8">
                        <strong><spring:message code="published_date"/> </strong>
                        <p>${message.publishDate}</p>
                    </div>
                    <div class="col-4">
                        <c:url value="/users/${message.receiverId}?back=yes" var="profileURL"/>
                        <a href="${profileURL}" class="btn btn-dark btn-md pull-right"><spring:message code="view_profile"/></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</c:forEach>
<c:if test="${empty messages}">
    <div class="col-4 text-center tab-title">
        <strong> <spring:message code="req_title"/> </strong>
    </div>
    <div class="card no-proj-mine">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="no_offers" arguments=""/> </h5>
        </div>
    </div>
</c:if>

</body>
</html>
