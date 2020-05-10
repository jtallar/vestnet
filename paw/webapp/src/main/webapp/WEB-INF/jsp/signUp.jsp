<%--
  Created by IntelliJ IDEA.
  User: njtallar
  Date: 22/4/20
  Time: 18:31
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>
<head>
    <link rel="stylesheet"
          href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <%--    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>--%>
    <link rel="stylesheet" href="<c:url value="/css/form.css"/>"/>
    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/images/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/images/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/images/favicon-16x16.png"/>">
    <link rel="manifest" href="<c:url value="/images/site.webmanifest"/>">
    <title>Sign up | VestNet</title>
</head>

<body background="<c:url value ="/images/signupBack.png"/>">
<c:set var="val"><spring:message code="select_country"/></c:set>
<input id="select_country_msg" type="hidden" value="${val}"/>
<c:set var="val"><spring:message code="select_state"/></c:set>
<input id="select_state_msg" type="hidden" value="${val}"/>
<c:set var="val"><spring:message code="select_city"/></c:set>
<input id="select_city_msg" type="hidden" value="${val}"/>


<spring:message code="enter_first_name" var="enter_first_name"/>
<spring:message code="enter_password" var="enter_password"/>
<spring:message code="enter_repeat_password" var="enter_repeat_password"/>
<spring:message code="enter_real_id" var="enter_real_id"/>
<spring:message code="enter_birthdate" var="enter_birthdate"/>
<spring:message code="enter_phone" var="enter_phone"/>
<spring:message code="enter_linkedin" var="enter_linkedin"/>
<spring:message code="enter_email" var="enter_email"/>
<spring:message code="enter_last_name" var="enter_last_name"/>

<c:url var="createUrl" value='/signUp'/>

<div class="container">
    <div class="row h-100">
        <div class="col-sm-12 my-auto">
            <div class="card rounded-lg px-4 py-3">
                <form:form modelAttribute="userForm" method="POST" action="${createUrl}" enctype="multipart/form-data">
                    <div class="row justify-content-center">
                        <div class="col-2 text-left">
                            <a href="<c:url value="/login"/>" class="btn btn-dark pull-left"><spring:message code="back"/></a>
                        </div>
                        <div class="col-md text-left">
                            <h2 class="bold" style="margin-right: 20%"><spring:message code="sign_up_title"/></h2>
                        </div>
                    </div>
                    <div class="dropdown-divider"></div>

                    <div class="container">
                        <div class="row">
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="first_name_required"/> </label>
                                    <form:input type="text" class="form-control" path="firstName"
                                                placeholder="${enter_first_name}" id="sign-up-first-name"/>
                                    <form:errors path="firstName" element="p" cssClass="formError"/>
                                </div>
                            </div>
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="last_name_required"/> </label>
                                    <form:input type="text" class="form-control" path="lastName"
                                                placeholder="${enter_last_name}" id="sign-up-last-name"/>
                                    <form:errors path="lastName" cssClass="formError" element="p"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="container">
                        <div class="row">
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="real_id_required"/> </label>
                                    <form:input type="text" class="form-control" path="realId"
                                                placeholder="${enter_real_id}" id="sign-up-real-id"/>
                                    <form:errors path="realId" cssClass="formError" element="p"/>
                                </div>
                            </div>
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="phone"/> </label>
                                    <form:input type="text" class="form-control" path="phone"
                                                placeholder="${enter_phone}"/>
                                    <form:errors path="phone" cssClass="formError" element="p"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="dropdown-divider"></div>

                    <div class="container">
                        <div class="row">
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="email_required"/> </label>
                                    <form:input type="text" class="form-control" path="email"
                                                placeholder="${enter_email}"/>
                                    <form:errors path="email" cssClass="formError" element="p"/>
                                </div>
                            </div>
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="role"/> </label>
                                    <form:select path="role" class="custom-select mr-sm-2">
                                        <form:option value="Investor"><spring:message code="investor"/></form:option>
                                        <form:option value="Entrepreneur"><spring:message code="entrepreneur"/></form:option>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="container">
                        <div class="row">
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="password_required"/> </label>
                                    <form:input type="password" class="form-control" path="password"
                                                placeholder="${enter_password}" id="sign-up-password"/>
                                    <form:errors path="password" element="p" cssClass="formError"/>
                                </div>
                            </div>
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="repeat_password_required"/> </label>
                                    <form:input type="password" class="form-control" path="repeatPassword"
                                                placeholder="${enter_repeat_password}" id="sign-up-repeat-password"/>
                                    <form:errors cssClass="formError" element="p"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="dropdown-divider"></div>

                    <div class="container">
                        <div class="row">
                            <div class="col-md">
                                <div class="form-group">
                                    <label><spring:message code="linkedin"/> </label>
                                    <p class="form-requirement"><spring:message code="linkedinRequirement"/></p>
                                    <form:input type="text" class="form-control" path="linkedin"
                                                placeholder="${enter_linkedin}"/>
                                    <form:errors path="linkedin" cssClass="formError" element="p"/>
                                </div>
                            </div>
                            <div class="col-md">
                                <label><spring:message code="userPicture"/> </label>
                                <p class="form-requirement"><spring:message code="userPictureRequirement"/></p>
                                <div class="custom-file">
                                    <form:input path="profilePicture" type="file" class="custom-file-input" id="customFileProfilePic"/>
                                    <label class="custom-file-label" for="customFileProfilePic" id="customFileProfilePicLabel"><spring:message code="chooseFile"/></label><br>
                                    <form:errors path="profilePicture" cssClass="formError" element="p" id="fileErrorFormTag"/>
                                </div>
                                <label class="formError" id="maxSizeErrorMsg" hidden><spring:message code="imageMaxSize"/></label>
                            </div>
                        </div>
                    </div>

                    <div class="container">
                        <div class="form-group">
                            <label><spring:message code="birthdate"/> </label>
                            <div class="container">
                                <div class="row">
                                    <div class="col-1">
                                        <label><spring:message code="day"/></label>
                                    </div>
                                    <div class="col-md">
                                        <form:select class="custom-select mr-sm-2" path="day">
                                            <c:forEach var="i" begin="1" end="31">
                                                <form:option value="${i}">${i}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                    <div class="col-1">
                                        <label><spring:message code="month"/> </label>
                                    </div>
                                    <div class="col-md">
                                        <form:select path="month" class="custom-select mr-sm-2">
                                            <c:forEach var="i" begin="1" end="12">
                                                <form:option value="${i}">${i}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                    <div class="col-1">
                                        <label><spring:message code="year"/> </label>
                                    </div>
                                    <div class="col-md">
                                        <form:select path="year" class="custom-select mr-sm-2">
                                            <c:forEach begin="0" end="70" varStatus="loop">
                                                <c:set var="currentYear" value="${maxYear - loop.index}"/>
                                                <form:option value="${currentYear}">${currentYear}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="container">
                        <div class="form-group">
                            <div class="container">
                                <div class="row">
                                    <div class="col-1">
                                        <label><spring:message code="country"/></label>
                                    </div>
                                    <div class="col-md">
                                        <form:select id="country-select" path="country" class="custom-select mr-sm-2" onchange="fetchData('country', 'state')"/>
                                    </div>
                                    <div class="col-1">
                                        <label><spring:message code="state"/> </label>
                                    </div>
                                    <div class="col-md">
                                        <form:select id="state-select" path="state" class="custom-select mr-sm-2" onchange="fetchData('state', 'city')"/>
                                    </div>
                                    <div class="col-1">
                                        <label><spring:message code="city"/> </label>
                                    </div>
                                    <div class="col-md">
                                        <form:select id="city-select" path="city" class="custom-select mr-sm-2"/>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>


                        <div class="text-right">
                            <c:if test="${invalidUser}">
                                <h4 class="big-error"><spring:message code="userAlreadyExists"/></h4>
                            </c:if>
                            <input type="submit" class="btn btn-dark pull-right"
                                   value="<spring:message code="sign_up"/>" onclick="adjustInputs()"/>
                        </div>


                </form:form>
            </div>
        </div>
    </div>
</div>
<script>
    var fileBox = document.getElementById('customFileProfilePic');
    var maxSizeMsg = document.getElementById('maxSizeErrorMsg');
    var errorTag = document.getElementById('fileErrorFormTag');
    fileBox.addEventListener("change", function () {
        var label = document.getElementById('customFileProfilePicLabel');
        if (fileBox.files[0].size >= ${maxSize}) {
            fileBox.value = null;
            if (errorTag != null) {
                errorTag.hidden = true;
            }
            maxSizeMsg.hidden = false;
        } else {
            label.innerText = fileBox.files[0].name;
            maxSizeMsg.hidden = true;
        }
    });
</script>
<script>
    function fetchData(source, receiver) {
        let id = "";
        if (source !== '') {
            let aux = document.getElementById(source + "-select");
            id = "/" + aux.options[aux.selectedIndex].value;
        }

        // Fetch data depending on arguments
        fetch(window.location.href.slice(0, window.location.href.lastIndexOf('/')) + "/location/" + receiver + id)
            .then(response => response.json())
            .then(data => {
                let select = document.getElementById(receiver + "-select");
                select.options.length = 0;
                for (let i = 0; i < data.length; i++)
                    select.appendChild(new Option(data[i]["name"], data[i]["id"]));

                //If there are no options
                if (data.length === 0)
                    select.appendChild(new Option("-", "0"));

                // Recursive update
                if (receiver === 'country') {
                    fetchData('country', 'state');
                } else if (receiver === 'state') {
                    fetchData('state', 'city');
                }
            })
    }
    window.onload = function () {
        fetchData('', 'country');
    };
    function adjustInputs() {
        var firstNameTag = document.getElementById('sign-up-first-name');
        firstNameTag .value = firstNameTag .value.trim();
        var lastNameTag = document.getElementById('sign-up-last-name');
        lastNameTag .value = lastNameTag .value.trim();
        var realIdTag = document.getElementById('sign-up-real-id');
        realIdTag .value = realIdTag .value.trim();
        var passwordTag = document.getElementById('sign-up-password');
        passwordTag .value = passwordTag .value.trim();
        var repeatPasswordTag = document.getElementById('sign-up-repeat-password');
        repeatPasswordTag .value = repeatPasswordTag .value.trim();
    }
</script>
</body>
</html>
