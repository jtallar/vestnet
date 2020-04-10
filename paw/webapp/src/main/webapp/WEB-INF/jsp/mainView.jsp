<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
    <div>
        <div class="form-row align-items-center">
                <select class="custom-select mr-sm-2" id = "category" >
                    <option value="null" selected = "selected"><spring:message code="nofilter"/></option>
                    <c:forEach items="${cat}" var="category">
                        <option value="${category}"><spring:message code="${category}"/></option>
                    </c:forEach>
                </select>
        </div>
        <div class="col-auto my-1">
            <button type="submit" class="btn btn-primary" onclick="filterList(this.id)"><spring:message code="filter"/></button>
        </div>
    </div>

    <p id="test"></p>

    <c:forEach items="${list}" var="project">
            <div class="card m-2">
                <div class="card-header">
                    <h5 class="card-title"><c:out value="${project.name}"></c:out></h5>

                </div>
                <div class="card-body">
                    <c:out value="${project.publishDate}"></c:out>
                    <p class="card-text"><c:out value="${project.summary}"></c:out></p>
<%--                    <p class="card-text" id="category"><spring:message code="${project.cat}"/></p>--%>
                    <a href="#" class="btn btn-primary">More info</a>
                </div>
            </div>
    </c:forEach>
</div>


    <script type="text/javascript">
        function filterList(clickedId)
        {
            var sel = document.getElementById("category")
            var cat = sel.options[sel.selectedIndex].value
            console.log(sel.options[sel.selectedIndex].value);
            document.getElementById("test").innerText = $("category");

            //console.log(list.get(0).getName());
            //list.filter(project => "${project.cat}" != cat);
        }
    </script>
</body>

</html>
