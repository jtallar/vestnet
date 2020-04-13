<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
    <c:url var="order" value="/images/order.png"></c:url>
    <c:url var="filter" value="/images/filter.png"></c:url>
    <div>
        <div class="align-items-center" style="margin: 20px">
            <c:url var="createUrl" value="/projects "></c:url>

            <form:form modelAttribute="categoryForm" method="GET" action="${createUrl}">
                <div>
                    <img src="${filter}"width="30" class="logo-img">
                    <form:select class="custom-select mr-sm-2" path="categorySelector">
                        <form:option value="allCats"><spring:message code="nofilter"></spring:message> </form:option>
                        <c:forEach items="${cats}" var="category">
                        <%-- TODO: VER COMO MOSTRAMOS CATEGORIAS INTERNACIONALIZADAS --%>
                            <form:option value="${category.name}">${category.name}</form:option>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="row">
                    <img src="${order}" width="30" class="logo-img">
                    <form:select path="orderBy" class="custom-select mr-sm-2">
                        <form:option value="date"><spring:message code="date"></spring:message> </form:option>
                        <form:option value="cost-low-high"><spring:message code="cost_l_h"></spring:message></form:option>
                        <form:option value="cost-high-low"><spring:message code="cost_h_l"></spring:message></form:option>
                        <form:option value="alf"><spring:message code="alf"></spring:message></form:option>
                    </form:select>
                </div>

                <input type = "submit" class="btn btn-dark" value="<spring:message code='filter'/>">
            </form:form>
        </div>
    </div>

    <p id="test"></p>

    <c:if test="${!empty list}">
        <c:forEach items="${list}" var="project">
                <div class="card m-2">
                    <div class="card-header">
                        <h5 class="card-title"><c:out value="${project.name}"></c:out></h5>

                    </div>
                    <div class="card-body">
                        <c:out value="${project.publishDate}"></c:out>
                        <p class="card-text"><c:out value="${project.summary}"></c:out></p>
                        <strong><spring:message code="categories"/></strong>
                        <c:forEach items="${project.categories}" var="category">
                             <p class="card-text" id="category">-${category.name}</p>
                        </c:forEach>
                        <strong><spring:message code="price"/></strong>
                        <p>${project.cost}</p>
                        <a href="<c:url value='/projects/${project.id}'/>" class="btn btn-dark"><spring:message code="moreinfo"></spring:message></a>
                    </div>
                </div>
        </c:forEach>
    </c:if>
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
