<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
</head>
<body>
<div class="container" style="margin-top: 20px">
    <div class="d-flex justify-content-start">
        <h5 class="card-title"><b> Contact ${owner.firstName} ${owner.lastName}</b></h5>
    </div>
    <c:url value="/projects/${p_id}/contact" var="postPath"/>
        <form:form modelAttribute="mailForm" action="${postPath}" method="post">

            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="basic-addon1">@</span>
                </div>
                <form:input path="from" type="text" class="form-control" placeholder="Enter your email" aria-describedby="basic-addon1"/>
            </div>

            <div class="input-group mb-3">
                <form:textarea path="body" type="text" class="form-control" placeholder="Write your message..." aria-describedby="basic-addon2"/>
            </div>

            <form:input path="to" value="${owner.email}" type="hidden"/>    <%--TODO chequear si hay una mejor forma de hacerlo --%>

            <div class="text-right">
                <input type="submit" value="Send" class="btn btn-dark"/>
            </div
        </form:form>
</div>
</body>
</html>