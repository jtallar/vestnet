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
<c:set var="messages" value="${messagePage.content}"/>
<c:set var="page" value="${messagePage.currentPage}"/>
<c:set var="startPage" value="${messagePage.startPage}"/>
<c:set var="endPage" value="${messagePage.endPage}"/>

<%-- Set used URLs--%>


<body>

<strong class="tab-title"> <spring:message code="deals_title"/> </strong>

<%-- Message pagniation --%>
<div class="row">
    <div class="col">
        <ul class="pagination justify-content-center">
            <li id="li-previous" class="page-item">
                <a id="li-a-previous" class="page-link" onclick="modHref(${page-1})" aria-label="<spring:message code="previous"/>">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <c:forEach var="pageNumber" begin="${startPage}" end="${endPage}">
                <li class="page-item <c:if test="${pageNumber == page }"> active-item </c:if>">
                    <a class="page-link" onclick="modHref(${pageNumber})">${pageNumber}</a>
                </li>
            </c:forEach>
            <li id="li-next" class="page-item">
                <a id="li-a-next" class="page-link" onclick="modHref(${page+1})" aria-label="<spring:message code="next"/>">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </div>
</div>

<%-- Deals display --%>
<c:forEach var="message" items="${messages}">
    <span class="anchor-header" id="dashboard-project-${message.project_id}"></span>
    <div class="container-deal py-3">
        <div class="card">
            <div class="card-deal">
                <strong><spring:message code="msg.title.body"/> </strong>
                <p><c:out value="${message.content.message}"/></p>
                <div class="row ">
                    <div class="col-5">
                        <strong><spring:message code="msg.title.offer"/> </strong>
                        <p><c:out value="${message.content.offer}"/></p>
                    </div>
                    <div class="col-6">
                        <strong><spring:message code="msg.title.request"/> </strong>
                        <p><c:out value="${message.content.interest}"/></p>
                    </div>
                </div>
            </div>
            <div class="card-footer">
                <div class="row ">
                    <div class="col-8">
                        <strong><spring:message code="published_date"/> </strong>
                        <p><c:out value="${message.publishDate}"/></p>
                    </div>
                    <div class="col-4">
                        <c:url value="/users/${message.sender_id}?back=yes" var="profileURL"/>
                        <a href="${profileURL}" class="btn btn-dark btn-deal pull-right"><spring:message code="view_inv_profile"/></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:forEach>

<%-- If there are no deals --%>
<c:if test="${empty messages}">
    <div class="card no-proj-mine">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="no_msg"/></h5>
        </div>
    </div>
</c:if>

<script>
    window.onload = function () {
        disableArrows(${page}, ${startPage}, "previous");
        disableArrows(${page}, ${endPage}, "next");
    }

    function disableArrows(page, limitPage, name) {
        if (page === limitPage) {
            document.getElementById("li-" + name).className = "page-item disabled";
            document.getElementById("li-a-" + name).setAttribute("tabindex", "-1");
            document.getElementById("li-a-" + name).setAttribute("aria-disabled", "true");
        }
    }

    function modHref(page) {
        let url = window.location.href;
        if (!window.location.search.includes("?")) window.location.href = url + "?page=" + page;
        else if (window.location.search.includes("page")) window.location.href = url.replace(/page=[0-9]*/, "page=" + page);
        else window.location.href = url + "&page=" + page;
    }
</script>

</body>
</html>
