<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "../components/header.jsp" %>
<html>
<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <title>Title</title>
</head>
<body>
<c:if test="${!empty messages}">
    <div class="flex-row-reverse mt-4">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-end">
                <c:set value="${page + 1}" var="nextOne"/>
                <c:set value="${page - 1}" var="previous"/>
                <c:if test="${page != 1}">
                    <li class="page-item">
                        <a href="<c:url value='/requests?page=${previous}'/>" class="page-link"><spring:message code="previous"/></a>
                    </li>
                    <li class="page-item"><a href="<c:url value='/requests?page=${previous}'/>" class="page-link">${previous}</a></li>
                </c:if>
                <li class="page-item"><a href="<c:url  value='/requests?page=${page}'/>" class="page-link">${page}</a></li>
                <c:if test="${hasNext eq true}">
                    <li class="page-item"><a href="<c:url  value='/requests?page=${nextOne}'/>" class="page-link">${nextOne}</a></li>
                    <li class="page-item">
                        <a href="<c:url value='/requests?page=${nextOne}'/>" class="page-link"><spring:message code="next"/> </a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </div>
</c:if>


<c:forEach var="message" items="${messages}" varStatus="theCount">
    <span class="anchor-header" id="dashboard-project-${project.id}"></span>
    <div class="container py-3">
        <div class="card">
            <div class="row ">
                <div class="col-4">
                    <strong><spring:message code="msg"></spring:message> </strong>
                    <p>${message.content.message}</p>
                </div>
                <div class="col-4">
                    <strong><spring:message code="published_date"></spring:message> </strong>
                    <p>${message.publishDate}</p>
                </div>

            </div>
            <div class="row ">
                <div class="col-4">
                    <strong><spring:message code="offer"></spring:message> </strong>
                    <p>${message.content.offer}</p>
                </div>
                <div class="col-4">
                    <strong><spring:message code="request"></spring:message> </strong>
                    <p>${message.content.interest}</p>

                </div>
            </div>
            <div class="row ">
                <div class="col-5">
                    <c:url value="/users/${message.receiverId}?back=yes" var="profileURL"></c:url>
                    <a href="${profileURL}" class="btn btn-dark btn-sm"><spring:message code="view_profile"/></a>
                </div>
            </div>
            <div class="row ">
                <div class="col-5 text-centered">
                    <strong><spring:message code="state"></spring:message> </strong>
                    <c:set var="accepted" value="${message.isAccepted()}"></c:set>
                    <c:choose>
                        <c:when test="${accepted == null}">
                            <h3 class="text-secondary"><spring:message code="pending"></spring:message> </h3>
                        </c:when>
                        <c:when test="${accepted eq true}">
                            <h3 class="text-success"><spring:message code="accepted"></spring:message> </h3>

                        </c:when>
                        <c:when test="${accepted eq false}">
                            <h3 class="text-danger"><spring:message code="rejected"></spring:message> </h3>

                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    </div>



</c:forEach>
<c:if test="${empty messages}">
    <div class="card m-2">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="no_offers" arguments=""></spring:message> </h5>
        </div>
    </div>
</c:if>

</body>
</html>
