<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<div id="app">
    <c:forEach items="${list}" var="project">
        <div class="card m-2">
            <div class="card-header">
                <c:out value="${project.date}"></c:out>
            </div>
            <div class="card-body">
                <h5 class="card-title"><c:out value="${project.name}"></c:out></h5>
                <p class="card-text"><c:out value="${project.summary}"></c:out></p>
                <a href="#" class="btn btn-primary">More info</a>
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>