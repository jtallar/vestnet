<%--
  Created by IntelliJ IDEA.
  User: njtallar
  Date: 22/4/20
  Time: 18:31
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <title>Sign up</title>
</head>
<body>

<spring:message code="enter_first_name" var="enter_first_name"></spring:message>
<spring:message code="enter_password" var="enter_password"></spring:message>
<spring:message code="enter_repeat_password" var="enter_repeat_password"></spring:message>
<spring:message code="enter_real_id" var="enter_real_id"></spring:message>
<spring:message code="enter_birthdate" var="enter_birthdate"></spring:message>
<spring:message code="enter_phone" var="enter_phone"></spring:message>
<spring:message code="enter_linkedin" var="enter_linkedin"></spring:message>
<spring:message code="enter_email" var="enter_email"></spring:message>
<spring:message code="enter_last_name" var="enter_last_name"></spring:message>

<c:url var="createUrl" value='/signUp'></c:url>
<form:form modelAttribute="userForm" method="POST" action="${createUrl}">


    <div class="form-group">
        <label><spring:message code="last_name"></spring:message> </label>
        <form:input type="text" class="form-control" path="lastName" placeholder="${enter_last_name}"/>
    </div>
    <div class="form-group">
        <label><spring:message code="first_name"></spring:message> </label>
        <form:input type="text" class="form-control" path="firstName" placeholder="${enter_last_name}"/>
    </div>
    <div class="form-group">
        <label><spring:message code="real_id"></spring:message> </label>
        <form:input type="text" class="form-control" path="realId" placeholder="${enter_real_id}"/>
    </div>

    <div class="form-group">
        <label><spring:message code="password"></spring:message> </label>
        <form:input type="password" class="form-control" path="password" placeholder="${enter_password}"/>
    </div>
    <div class="form-group">
        <label><spring:message code="repeat_password"></spring:message> </label>
        <form:input type="password" class="form-control" path="repeatPassword" placeholder="${enter_repeat_password}"/>
    </div>
    <div class="form-group">
        <label><spring:message code="birthdate"></spring:message> </label>
        <form:input type="text" class="form-control" path="birthDate" placeholder="${enter_birthdate}"/>
    </div>
    <div class="form-group">
        <label><spring:message code="email"></spring:message> </label>
        <form:input type="text" class="form-control" path="email" placeholder="${enter_email}"/>
    </div>
    <div class="form-group">
        <label><spring:message code="phone"></spring:message> </label>
        <form:input type="text" class="form-control" path="phone" placeholder="${enter_phone}"/>
    </div>
    <div class="form-group">
        <label><spring:message code="linkedin"></spring:message> </label>
        <form:input type="text" class="form-control" path="linkedin" placeholder="${enter_linkedin}"/>
    </div>
    <div class="form-group">
        <label><spring:message code="picture"></spring:message> </label>
        <form:input type="text" class="form-control" path="profilePicture" placeholder="<spring:message code='first_name'></spring:message> "/>
    </div>

</form:form>




</body>
</html>
