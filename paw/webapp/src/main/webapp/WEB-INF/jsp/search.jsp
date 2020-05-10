<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "header.jsp" %>
<html>
<head>

    <%--<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>--%>
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <title>Search | VestNet</title>
</head>


<body>

<div class="container">

    <%--    <div class="row grid">--%>
    <div class="flex-row-reverse mt-4">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-end">
        <c:if test="${!empty projectsList}">
            <c:set value="${page + 1}" var="nextOne"/>
            <c:set value="${page - 1}" var="previous"/>
            <c:set var="parameters" value="searching=${param.searching}&selection=${param.selection}"/>
            <c:if test="${page != 1}">
                <li class="page-item">
                    <a href="<c:url value='/search?${parameters}&page=${previous}'/>" class="page-link"><spring:message code="previous"/></a>
                </li>
                <li class="page-item"><a href="<c:url value='/search?${parameters}&page=${previous}'/>" class="page-link">${previous}</a></li>
            </c:if>
            <li class="page-item"><a href="<c:url  value='/search?${parameters}&page=${page}'/>" class="page-link">${page}</a></li>
            <c:if test="${hasNext eq true}">
                <li class="page-item"><a href="<c:url  value='/search?${parameters}&page=${nextOne}'/>" class="page-link">${nextOne}</a></li>
                <li class="page-item">
                    <a href="<c:url value='/search?${parameters}&page=${nextOne}'/>" class="page-link"><spring:message code="next"/> </a>
                </li>
            </c:if>
            </ul>
        </nav>
    </div>
            <div class="row my-2">
                <div class="col-md">
                    <h1><spring:message code="projects"/> </h1>
                </div>
            </div>
        <div class="card-deck">

            <c:forEach items="${projectsList}" var="project">
                <%--                <div class="col-sm-3 my-card">--%>
                <div class="card ">
                    <div class="card-header text-white">
                        <h5 class="card-title"><c:out value="${project.name}"/></h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-">
                                <img src="<c:url value="/imageController/project/${project.id}"/>" class="proj-img"
                                     alt="<spring:message code="projectImage"/>"
                                     aria-placeholder="<spring:message code="projectImage"/>"/>
                            </div>
                            <div class="col-md">
                                <p class="card-text"><c:out value="${project.summary}"></c:out></p>
                                    <%--                        <strong><spring:message code="categories"/></strong>--%>
                                    <%--                        <c:forEach items="${project.categories}" var="category">--%>
                                    <%--                             <p class="card-text" id="category">-${category.name}</p>--%>
                                    <%--                        </c:forEach>--%>
                                <div class="card-secondary">
                                    <strong><spring:message code="owner"/></strong>
                                    <p><c:out value="${project.owner.firstName}"/> <c:out
                                            value="${project.owner.lastName}"/></p>
                                    <strong><spring:message code="price"/></strong>
                                    <p><c:out value="${project.cost}"/></p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="<c:url value='/projects/${project.id}'/>" class="btn btn-dark pull-right"><spring:message code="moreinfo"/></a>
                    </div>
                </div>

            </c:forEach>
        </div>
        </c:if>






    <c:if test="${empty projectsList}">
        <div class="card m-2">
            <div class="card-header">
                <h5 class="card-title centered text-light"><spring:message code="no_search_found" arguments="${string}"/> </h5>
            </div>
        </div>
    </c:if>
    </div>
</div>
</body>
</html>
