<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
    <div>
        <div class="form-row align-items-center">
            <c:url var="createUrl" value="/projects "></c:url>
            <form:form modelAttribute="categoryForm" method="GET" action="${createUrl}">
                <form:select class="custom-select mr-sm-2" path="categorySelector">
                    <form:option value="allCats"><spring:message code="nofilter"></spring:message> </form:option>
                    <c:forEach items="${cats}" var="category">
                        <form:option value="${category.name}"><spring:message code="${category.name}"></spring:message> </form:option>
                    </c:forEach>
                </form:select>
                <input type = "submit" class="btn btn-primary" value="<spring:message code='filter'/>"></input>
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
                             <p class="card-text" id="category">- <spring:message code="${category.name}"/></p>
                        </c:forEach>
                        <a href="<c:url value='./${project.id}'/>" class="btn btn-primary">More info</a>
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
