<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../components/header.jsp" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/userprofile.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><spring:message code="page.title.deals"/></title>
</head>

<%-- Set used variables --%>

<%-- Set used URLs--%>


<body>

<%-- Deals display --%>
<c:forEach var="message" items="${messages}">
    <span class="anchor-header" id="dashboard-project-${message.projectId}"></span>
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
            </div>
            <div class="card-footer">
                <div class="row ">
                    <div class="col-8">
                        <strong><spring:message code="published_date"/> </strong>
                        <p>${message.publishDate}</p>
                    </div>
                    <div class="col-4">
                        <c:url value="/users/${message.senderId}?back=yes" var="profileURL"/>
                        <a href="${profileURL}" class="btn btn-dark btn-md pull-right"><spring:message code="view_inv_profile"/></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:forEach>

<%-- If there are no deals --%>
<c:if test="${empty messages}">
    <div class="col-4 text-center tab-title">
        <strong> <spring:message code="deals_title"/> </strong>
    </div>
    <div class="card no-proj-mine">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="no_msg" arguments=""/></h5>
        </div>
    </div>
</c:if>

</body>
</html>
