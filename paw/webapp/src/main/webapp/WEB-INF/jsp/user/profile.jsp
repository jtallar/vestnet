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
<c:choose>
    <c:when test="${user.image_id eq null}">
        <c:url var="user_image" value="/imageController/user/0"/>
    </c:when>
    <c:otherwise>
        <c:url var="user_image" value="/imageController/user/${user.image_id}"/>
    </c:otherwise>
</c:choose>
<c:if test="">

</c:if>

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
                    <spring:message code="profile.title" arguments="${user.firstName},${user.lastName}" var="titleVar"/>
                    <h2 class="bold"><c:out value="${titleVar}"/></h2>
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <li class="nav-item">
                            <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab"
                               aria-controls="home" aria-selected="true"><spring:message code="information"/></a>
                        </li>

                        <%-- Check if can show favorites --%>
                        <sec:authorize access="hasRole('ROLE_INVESTOR')">
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
                                <p><c:out value="${user.firstName}"/></p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="last_name"/></label>
                            </div>
                            <div class="col-md-6">
                                <p><c:out value="${user.lastName}"/></p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="email"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p><c:out value="${user.email}"/></p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="phone"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p><c:out value="${user.phone}"/></p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="birthdate"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p><c:out value="${user.birthDate}"/></p>
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
                                <p><c:out value="${user.location.country.name}"/></p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="city"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p><c:out value="${user.location.city.name}"/></p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="state"/> </label>
                            </div>
                            <div class="col-md-6">
                                <p><c:out value="${user.location.state.name}"/></p>
                            </div>
                        </div>
                    </div>

                    <%-- User favorites pane --%>
                    <sec:authorize access="hasRole('ROLE_INVESTOR')">
                        <c:if test="${session_user_id eq user.id}">
                            <div class="tab-pane fade" id="favorites" role="tabpanel" aria-labelledby="profile-tab">
                                <c:if test="${!empty user.favorites}">
                                    <c:forEach items="${user.favorites}" var="project">
                                        <div class="card m-2">
                                            <div class="card-header">
                                                <h5 class="card-title"><c:out value="${project.name}"/></h5>
                                            </div>
                                            <div class="card-body">
                                                <p class="card-text"><c:out value="${project.summary}"/></p>
                                                <div class="row">
                                                    <div class="col">
                                                        <strong><spring:message code="price"/></strong>
                                                        <spring:message code="project.cost" arguments="${project.cost}" var="costVar"/>
                                                        <p><c:out value="${costVar}"/></p>
                                                    </div>
                                                    <div class="col">
                                                        <a href="<c:url value='/projects/${project.id}'/>" class="btn btn-dark pull-right"><spring:message code="moreinfo"/></a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:if>

                                <%-- If there is no favorites --%>
                                <c:if test="${empty user.favorites}">
                                    <div class="card m-2">
                                        <div class="card-header">
                                            <h5 class="card-title centered"><spring:message code="noProjFound"/></h5>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </div>

<%-- TODO check if can change on signup // Format linkedin link --%>
    <script>
        let aux = '${user.linkedin}';

        function goToLinkedin(url) {
            if (!(url.indexOf('http') === 0)) {
                url = '//' + url;
            }
            window.open(url, '_blank');
        }
    </script>
</body>
<footer>
    <%@ include file="../components/footer.jsp" %>
</footer>
</html>
