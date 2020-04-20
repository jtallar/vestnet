<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Projects</title>
</head>
<body>
    <c:url var="order" value="/images/order.png"></c:url>
    <c:url var="filter" value="/images/filter.png"></c:url>
    <div>
        <div class="form-row align-items-center" style="margin: 20px">
            <c:url var="createUrl" value='/projects'></c:url>
            <form:form modelAttribute="categoryForm" method="GET" action="${createUrl}">
                <div class="container">
                    <div class="row">
                        <div class="col-">
                            <img src="${filter}"width="50" class="logo-img">
                        </div>
                        <div class="col-md">
                            <form:select class="custom-select mr-sm-2" path="categorySelector">
                                <form:option value="allCats"><spring:message code="nofilter"></spring:message> </form:option>
                                <c:forEach items="${cats}" var="category">
                                    <%-- TODO: VER COMO MOSTRAMOS CATEGORIAS INTERNACIONALIZADAS --%>
                                    <form:option value="${category.name}">${category.name}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                        <div class="col-">
                            <img src="${order}" width="50" class="logo-img">
                        </div>
                        <div class="col-md">
                            <form:select path="orderBy" class="custom-select mr-sm-2">
                                <form:option value="date"><spring:message code="date"></spring:message> </form:option>
                                <form:option value="cost-low-high"><spring:message code="cost_l_h"></spring:message></form:option>
                                <form:option value="cost-high-low"><spring:message code="cost_h_l"></spring:message></form:option>
                                <form:option value="alf"><spring:message code="alf"></spring:message></form:option>
                            </form:select>
                        </div>
                        <div class="col-sm">
                            <input type = "submit" class="btn btn-dark" value="<spring:message code='apply'/>">
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </div>

    <p id="test"></p>

<%--    <div class="row grid">--%>
    <div class="card-deck">
        <c:if test="${!empty list}">
        <c:forEach items="${list}" var="project">
<%--                <div class="col-sm-3 my-card">--%>
            <div class="card ">
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
    </c:if>
    </div>

    <c:if test="${empty list}">
    <div class="card m-2">
        <div class="card-header">
            <h5 class="card-title centered"><spring:message code="noProjFound"></spring:message> </h5>
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
