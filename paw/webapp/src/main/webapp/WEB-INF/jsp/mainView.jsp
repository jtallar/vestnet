<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/form.css"/>"/>

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>VestNet | Projects</title>
</head>
<body>
    <c:url var="order" value="/images/order.png"/>
    <c:url var="filter" value="/images/filter.png"/>
    <div>
        <div class="form-row align-items-center" style="margin: 20px">
            <c:url var="createUrl" value='/projects'/>
            <form:form modelAttribute="categoryForm" method="GET" action="${createUrl}">
                <div class="container">
                    <div class="row">
                        <div class="col-">
                            <img src="${filter}"width="40" class="logo-img">
                        </div>
                        <div class="col-md">
                            <form:select class="custom-select mr-sm-2" path="categorySelector">
                                <form:option value="allCats"><spring:message code="nofilter"/> </form:option>
                                <c:forEach items="${cats}" var="category">
                                    <%-- TODO: VER COMO MOSTRAMOS CATEGORIAS INTERNACIONALIZADAS --%>
                                    <form:option value="${category.name}">${category.name}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                        <div class="col-">
                            <img src="${order}" width="40" class="logo-img">
                        </div>
                        <div class="col-md">
                            <form:select path="orderBy" class="custom-select mr-sm-2">
                                <form:option value="date"><spring:message code="date"/> </form:option>
                                <form:option value="cost-low-high"><spring:message code="cost_l_h"/></form:option>

                                <form:option value="cost-high-low"><spring:message code="cost_h_l"/></form:option>
                                <form:option value="alf"><spring:message code="alf"/></form:option>

                            </form:select>
                        </div>
                        <div class="col-5">
                            <div class="row">
                                <div class="col-2 text-center text-justify">
                                    <label class="font-weight-bold"><spring:message code="range"></spring:message> </label>
                                </div>
                                <div class="col-7">
                                    <spring:message var="min" code="min"></spring:message>
                                    <form:input path="min" class="form-control mx-auto mx-auto" placeholder="${min}"></form:input>
                                    <form:errors path="min" cssClass="formError"></form:errors>
                                    <spring:message var="max" code="max"></spring:message>
                                    <form:input path="max" class="form-control mx-auto mx-auto"  placeholder="${max}"></form:input>
                                    <form:errors path="max" cssClass="formError"></form:errors>
                                    <form:errors cssClass="formError"></form:errors>
                                </div>
                            </div>
                        </div>
                        <div class="col-">
                            <input type = "submit" class="btn btn-dark" value="<spring:message code='apply'/>">
                        </div>


                    </div>
                </div>
            </form:form>
        </div>
    </div>
    <div class="col">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-end">


                <c:set value="${page + 1}" var="nextOne"></c:set>
                <c:set value="${page - 1}" var="previous"></c:set>

                <c:if test="${empty param.categorySelector and empty param.orderBy}">
                    <c:if test="${page != 1}">
                        <li class="page-item">
                            <a href="<c:url value='/projects?page=${previous}'></c:url>" class="page-link">Previous</a>
                        </li>
                        <li class="page-item"><a href="<c:url value='/projects?page=${previous}'></c:url>" >${previous}</a></li>
                    </c:if>
                    <li class="page-item"><a href="<c:url  value='/projects?page=${page}'></c:url>" class="page-link">${page}</a></li>
                    <c:if test="${hasNext eq true}">
                        <li class="page-item"><a href="<c:url  value='/projects?page=${nextOne}'></c:url>" class="page-link">${nextOne}</a></li
                        <li class="page-item">
                            <a href="<c:url value='/projects?page=${nextOne}'></c:url>" class="page-link">Next</a>
                        </li>
                    </c:if>
                </c:if>
                <c:if test="${not empty param.categorySelector and not empty param.orderBy}">
                    <c:set var="parameters" value="categorySelector=${param.categorySelector}&orderBy=${param.orderBy}"></c:set>
                    <c:if test="${page != 1}">
                    <li class="page-item">
                        <a href="<c:url value='/projects?${parameters}&page=${previous}'></c:url>" class="page-link"><spring:message code="previous"></a>
                    </li>
                    <li class="page-item"><a href="<c:url value='/projects?${parameters}&page=${previous}'></c:url>" >${previous}</a></li>
                    </c:if>
                <li class="page-item"><a href="<c:url  value='/projects?${parameters}&page=${page}'></c:url>" class="page-link">${page}</a></li>
                <c:if test="${hasNext eq true}">
                    <li class="page-item"><a href="<c:url  value='/projects?${parameters}&page=${nextOne}'></c:url>" class="page-link">${nextOne}</a></li
                    <li class="page-item">
                        <a href="<c:url value='/projects?${parameters}&page=${nextOne}'></c:url>" class="page-link"><spring:message code="next"></spring:message> </a>
                    </li>
                </c:if>
                </c:if>
            </ul>
        </nav>
    </div>

    <p id="test"></p>

<%--    <div class="row grid">--%>

        <c:if test="${!empty list}">
         <div class="card-deck">
        <c:forEach items="${list}" var="project">
<%--                <div class="col-sm-3 my-card">--%>
            <div class="card mb-3">
                <div class="card-header text-white">
                    <h5 class="card-title"><c:out value="${project.name}"></c:out></h5>
                </div>
                <div class="card-body">
<%--                        <c:out value="${project.publishDate}"></c:out>--%>
                    <p class="card-text"><c:out value="${project.summary}"></c:out></p>
<%--                        <strong><spring:message code="categories"/></strong>--%>
<%--                        <c:forEach items="${project.categories}" var="category">--%>
<%--                             <p class="card-text" id="category">-${category.name}</p>--%>
<%--                        </c:forEach>--%>
                    <strong><spring:message code="owner"/></strong>
                    <p><c:out value="${project.owner.firstName}"/> <c:out value="${project.owner.lastName}"/></p>
                    <strong><spring:message code="price"/></strong>
                    <p><c:out value="${project.cost}"/></p>
                </div>
                <div class="card-footer">
                    <a href="<c:url value='/projects/${project.id}'/>" class="btn btn-dark pull-right"><spring:message code="moreinfo"></spring:message></a>
                </div>
            </div>

        </c:forEach>
         </div>
            <div class="grid">
                    <div class="row">
                        <div class="col-4  d-flex justify-content-center">
                        <c:url value="/projects" var="projectPage"></c:url>
                        <form action="${projectPage}" method="get">


                        </form>
                        </div>
                     </div>
            </div>

    </c:if>


    <c:if test="${empty list}">
    <div class="card m-2">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="noProjFound" arguments=""></spring:message> </h5>
        </div>
    </div>
    </c:if>
</div>


    <script type="text/javascript">
        function filterList(clickedId)
        {
            var sel = document.getElementById("category")
            var cat = sel.options[sel.selectedIndex].value
            console.log(sel.options[sel.selectedIndex].value);
            document.getElementById("test").innerText = $("category");

            //console.log(list.get(0).getName());
            //
            //list.filter(project => "${project.cat}" != cat);
        }

    </script>
</body>

</html>
