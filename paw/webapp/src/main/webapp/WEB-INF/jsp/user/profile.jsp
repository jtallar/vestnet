<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="../components/header.jsp" %>
<html>

<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value='/css/userprofile.css'/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><spring:message code="page.title.profile" arguments="${user.firstName},${user.lastName}"/></title>
</head>

<%-- Used variables --%>
<c:url var="user_image" value="/imageController/user/${user.id}"/>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="session_user_id" property="principal.id"/>
</sec:authorize>

<body>
<%-- Back button logic --%>
    <c:if test="${back}">
        <div class="back">
            <a onclick="history.back()" class="btn btn-dark"><spring:message code="back"/></a>
        </div>
    </c:if>

<%-- User profile --%>
    <div class="container emp-profile">
        <div class="row">
            <div class="col-md-4">

                <%-- User profile picture --%>
                <div class="profile-img ">
                    <img src="${user_image}" alt="<spring:message code="userPicture"/>" aria-placeholder="<spring:message code="userPicture"/>"/>
                </div>

                <%-- User linkedin button --%>
                <c:if test="${not empty user.linkedin}">
                    <div class="text-center my-2">
                        <button onclick="goToLinkedin('<c:out value="${user.linkedin}"/>')" class="btn btn-linkedin">
                            <i class="fab fa-linkedin-in"></i> <spring:message code="linkedin_profile"/>
                        </button>
                    </div>
                </c:if>
            </div>
            <div class="col-md-6">
                <div class="profile-head">
                    <h2 class="bold">
                        <c:out value="${user.firstName} " escapeXml="false"/>
                        <c:out value="${user.lastName}" escapeXml="false"/>
                    </h2>
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <li class="nav-item">
                            <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab"
                               aria-controls="home" aria-selected="true"><spring:message code="information"/></a>
                        </li>

                        <%-- Check if can show favorites --%>
                        <sec:authorize access="hasRole('ROLE_ENTREPRENEUR')">
                            <c:if test="${session_user_id eq user.id}">
                                <li class="nav-item">
                                    <a class="nav-link" id="favorites-tab" data-toggle="tab" href="#favorites" role="tab"
                                       aria-controls="favorites" aria-selected="false"><spring:message code="favorites"/></a>
                                </li>
                            </c:if>
                        </sec:authorize>
                    </ul>
                </div>

                <%-- User information card --%>
                <div class="tab-content profile-tab" id="myTabContent">
                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="name"/></label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.firstName}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="last_name"/></label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.lastName}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="email"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.email}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="phone"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.phone}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="birthdate"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.birthDate}</p>
                            </div>
                        </div>
                        <div class="row mt-2 mb-2">
                            <h4><spring:message code="location"/></h4>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label class="font-weight-bold"><spring:message code="country"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.location.country.name}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="city"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.location.city.name}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="state"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.location.state.name}</p>
                            </div>
                        </div>
                    </div>

                    <%-- User favorites pane --%>
                    <div class="tab-pane fade" id="favorites" role="tabpanel" aria-labelledby="profile-tab">
                        <c:if test="${!empty favs}">
                            <c:forEach items="${favs}" var="project">
                                <div class="card m-2">
                                    <div class="card-header">
                                        <h5 class="card-title"><c:out value="${project.name}"/></h5>
                                    </div>
                                    <div class="card-body">
                                        <p class="card-text"><c:out value="${project.summary}"/></p>
                                        <strong><spring:message code="owner"/></strong>
                                        <p><c:out value="${project.owner.firstName}"/> <c:out value="${project.owner.lastName}"/></p>
                                        <strong><spring:message code="price"/></strong>
                                        <p><c:out value="${project.cost}"/></p>
                                        <a href="<c:url value='/projects/${project.id}'/>" class="btn btn-dark"><spring:message code="moreinfo"/></a>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>

                        <%-- If there is no favorites --%>
                        <c:if test="${empty favs}">
                            <div class="card m-2">
                                <div class="card-header">
                                    <h5 class="card-title centered"><spring:message code="noProjFound"/></h5>
                                </div>
                            </div>
                        </c:if>
                    </div>

                </div>
            </div>
        </div>
    </div>

<%-- TODO check if can change on signup // Format linkedin link --%>
    <script>
        var aux = '${user.linkedin}';

        function goToLinkedin(url) {
            if (!(url.indexOf('http') === 0)) {
                url = '//' + url;
            }
            window.open(url, '_blank');
        }
    </script>
</body>
</html>
