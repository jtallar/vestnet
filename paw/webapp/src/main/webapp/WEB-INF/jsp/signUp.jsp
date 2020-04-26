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
    <link rel="stylesheet" href="<c:url value="/css/form.css"/>"/>
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

<div class="container">
    <div class="text-center">
        <h1 class="bold"><spring:message code="sign_up_title"></spring:message> </h1>
    </div>

<form:form modelAttribute="userForm" method="POST" action="${createUrl}">

    <div class="form-group">
        <label><spring:message code="first_name"></spring:message> </label>
        <form:input type="text" class="form-control" path="firstName" placeholder="${enter_first_name}"/>
        <form:errors path="firstName" element="p" cssClass="formError"></form:errors>
    </div>
    <div class="form-group">
        <label><spring:message code="last_name"></spring:message> </label>
        <form:input type="text" class="form-control" path="lastName" placeholder="${enter_last_name}"/>
        <form:errors path="lastName" cssClass="formError" element="p"></form:errors>
    </div>
    <div class="form-group">
        <label><spring:message code="real_id"></spring:message> </label>
        <form:input type="text" class="form-control" path="realId" placeholder="${enter_real_id}"/>
        <form:errors path="realId" cssClass="formError" element="p"></form:errors>
    </div>

    <div class="form-group">
        <label><spring:message code="password"></spring:message> </label>
        <form:input type="password" class="form-control" path="password" placeholder="${enter_password}"/>
        <form:errors path="password" cssClass="formError" element="p"></form:errors>
    </div>
    <div class="form-group">
        <label><spring:message code="repeat_password"></spring:message> </label>
        <form:input type="password" class="form-control" path="repeatPassword" placeholder="${enter_repeat_password}"/>
        <form:errors path="repeatPassword" element="p" cssClass="formError"></form:errors>
    </div>

    <div class="form-group">
        <label><spring:message code="birthdate"></spring:message> </label>
        <div class="row">
            <div class="col-">
                <label><spring:message code="day"></spring:message></label>
            </div>
            <div class="col-md">
                <form:select class="custom-select mr-sm-2" path="day">
                    <c:forEach  var="i" begin="1" end="31">
                        <form:option value="${i}">${i}</form:option>
                    </c:forEach>
                </form:select>
            </div>
            <div class="col-">
                <label><spring:message code="month"></spring:message> </label>
            </div>
            <div class="col-md">
                <form:select path="month" class="custom-select mr-sm-2">
                    <c:forEach var="i" begin="1" end="12">
                        <form:option value="${i}">${i}</form:option>
                    </c:forEach>
                </form:select>
            </div>
            <div class="col-">
                <label><spring:message code="year"></spring:message> </label>
            </div>
<%--            TODO: CAMBIAR PARA QUE NO SEA DESDE 2010--%>
            <div class="col-md">
                <form:select path="year" class="custom-select mr-sm-2">
                    <c:forEach begin="0" end="70" varStatus="loop">
                        <c:set var="currentYear" value="${2010 - loop.index}" />
                        <option value="${currentYear}">${currentYear}</option>
                    </c:forEach>
                </form:select>
            </div>
        </div>


    </div>
    <div class="form-group">
        <label><spring:message code="location"></spring:message> </label>
        <div class="row">
            <div class="col-">
                <label><spring:message code="country"/></label>
            </div>
            <div class="col-md">
<%--                TODO: COMO HAGO PARA QUE SE EJECUTE ESE URL tipo SRC, no que sea ese el valor? --%>
                <c:url var="countryUrl" value='/location/countries'/>
                <form:select class="custom-select mr-sm-2" path="country">
                    <c:forEach  var="country" items="${countryUrl}">
                        <form:option value="1" label="${country}"/>
                    </c:forEach>
                </form:select>
            </div>
            <div class="col-">
                <label><spring:message code="state"/> </label>
            </div>
            <div class="col-md">
                <c:url var="stateUrl" value='/location/states/${userForm.country}'/>
                <form:select path="state" class="custom-select mr-sm-2">
                    <c:forEach  var="state" items="${stateUrl}">
                        <form:option value="1" label="${state}"/>
                    </c:forEach>
                </form:select>
            </div>
            <div class="col-">
                <label><spring:message code="city"/> </label>
            </div>
            <div class="col-md">
                <c:url var="cityUrl" value='/location/cities/${userForm.state}'/>
                <form:select path="city" class="custom-select mr-sm-2">
                    <c:forEach  var="city" items="${cityUrl}">
                        <form:option value="1" label="${city}"/>
                    </c:forEach>
                </form:select>
            </div>
        </div>


    </div>
    <div class="form-group">
        <label><spring:message code="email"></spring:message> </label>
        <form:input type="text" class="form-control" path="email" placeholder="${enter_email}"/>
        <form:errors path="email" cssClass="formError" element="p"></form:errors>
    </div>
    <div class="form-group">
        <label><spring:message code="phone"></spring:message> </label>
        <form:input type="text" class="form-control" path="phone" placeholder="${enter_phone}"/>
        <form:errors path="phone" cssClass="formError" element="p"></form:errors>

    </div>
    <div class="form-group">
        <label><spring:message code="linkedin"/> </label>
        <form:input type="text" class="form-control" path="linkedin" placeholder="${enter_linkedin}"/>
        <form:errors path="linkedin" cssClass="formError" element="p"/>
    </div>
    <div class="form-group">
        <label><spring:message code="picture"/> </label>
    </div>

    <div class="form-group">
        <label><spring:message code="role"/> </label>
        <form:select path="role" class="custom-select mr-sm-2">
            <form:option value="Investor"><spring:message code="investor"/></form:option>
            <form:option value="Entrepreneur"><spring:message code="entrepreneur"/></form:option>
        </form:select>
    </div>
    <input type="submit" class="btn btn-dark" value="<spring:message code="sign_up"></spring:message> "/>

</form:form>


</div>



</body>
</html>
