<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
</head>
<body>
<div class="container" style="margin-top: 20px">
    <div class="d-flex justify-content-start">
        <h5 class="card-title"><b> <spring:message code="contact"></spring:message> ${owner.firstName} ${owner.lastName}</b></h5>
    </div>
    <c:url value="/projects/${p_id}/contact" var="postPath"/>
        <form:form modelAttribute="mailForm" action="${postPath}" method="post">

            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="basic-addon1">@</span>
                </div>
                <spring:message code="enteremail" var="placeholderemail" />
                <form:input path="from" type="text" class="form-control" placeholder="${placeholderemail}" aria-describedby="basic-addon1"/>
            </div>

            <div class="input-group mb-3">
                <spring:message code="writemessage" var="placeholdermessage" />
                <form:textarea path="body" type="text" class="form-control" placeholder="${placeholdermessage}" aria-describedby="basic-addon2"/>
            </div>

            <form:input path="to" value="${owner.email}" type="hidden"/>    <%--TODO chequear si hay una mejor forma de hacerlo --%>

            <div class="text-right">
                <input type="submit" value="<spring:message code="send"></spring:message>" class="btn btn-dark"/>
            </div
        </form:form>
</div>
</body>
</html>