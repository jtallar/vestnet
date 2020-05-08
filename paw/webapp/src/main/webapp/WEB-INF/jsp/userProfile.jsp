<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file = "header.jsp" %>
<html>
<head>

    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="<c:url value='/css/userprofile.css'/>"/>

    <title>VestNet | <spring:message code="my_profile"></spring:message></title>
</head>
<body>
<div class="container emp-profile">
    <form method="post">
        <div class="row">
            <div class="col-md-4">
                <div class="profile-img">
                    <img src="<c:url value="/imageController/user/${user.id}"/>" alt="<spring:message code="userPicture"/>"
                         aria-placeholder="<spring:message code="userPicture"/>"/>
<%--                    <div class="file btn btn-lg btn-primary">--%>
<%--                        Change Photo--%>
<%--                        <input type="file" name="file"/>--%>
<%--                    </div>--%>
                </div>
            </div>
            <div class="col-md-6">
                <div class="profile-head">
                    <h2 class="bold">
                        <c:out value="${user.firstName}"></c:out><c:out value=" "></c:out><c:out value="${user.lastName}"></c:out>
                    </h2>
                    <div>
                        <h6>
                            <spring:message code="trust_index"></spring:message>
                        </h6>
                        <c:forEach var = "i" begin = "1" end = "${user.trustIndex}">
                            <span class="fa fa-star checked"></span>
                        </c:forEach>
                        <c:forEach var = "i" begin = "${user.trustIndex}" end = "4">
                            <span class="fa fa-star"></span>
                        </c:forEach>
                    </div>
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <li class="nav-item">
                            <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true"><spring:message code="information"/></a>
                        </li>
                        <c:if test="${user.role eq 1}">
                        <li class="nav-item">
                            <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false"><spring:message code="projects"/></a>
                        </li>
                        </c:if>
                        <c:if test="${user.role eq 2}">
                        <li class="nav-item">
                            <a class="nav-link" id="favorites-tab" data-toggle="tab" href="#favorites" role="tab" aria-controls="favorites" aria-selected="false"><spring:message code="favorites"/></a>
                        </li>
                        </c:if>
                    </ul>
                </div>
            </div>
            <!--
            <div class="col-md-2">
                <input type="submit" class="profile-edit-btn" name="btnAddMore" value="Edit Profile"/>
            </div>
            -->
        </div>

        <div class="row">
            <div class="col-md-4">
                <div class="profile-work">
                    <!--
                    <p>WORK LINK</p>
                    <a href="">Website Link</a><br/>
                    <a href="">Bootsnipp Profile</a><br/>
                    <a href="">Bootply Profile</a>
                    <p>SKILLS</p>
                    <a href="">Web Designer</a><br/>
                    <a href="">Web Developer</a><br/>
                    <a href="">WordPress</a><br/>
                    <a href="">WooCommerce</a><br/>
                    <a href="">PHP, .Net</a><br/>
                    -->
                </div>
            </div>

            <div class="col-md-8">
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
                                <label><spring:message code="email"></spring:message> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.email}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="phone"></spring:message> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.phone}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="birthdate"></spring:message> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.birthDate}</p>
                            </div>
                        </div>
                        <div class="row mt-2 mb-2">
                            <h4><spring:message code="location"></spring:message> </h4>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label class="font-weight-bold"><spring:message code="country"></spring:message> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.location.country.name}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="city"></spring:message> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.location.city.name}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label><spring:message code="state"></spring:message> </label>
                            </div>
                            <div class="col-md-6">
                                <p>${user.location.state.name}</p>
                            </div>
                        </div>



                    </div>

                    <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">

                        <c:if test="${!empty list}">
                            <c:forEach items="${list}" var="project">
                                <div class="card m-2">
                                    <div class="card-header">
                                        <h5 class="card-title"><c:out value="${project.name}"></c:out></h5>

                                    </div>
                                    <div class="card-body">
                                            <%--                        <c:out value="${project.publishDate}"></c:out>--%>
                                        <p class="card-text"><c:out value="${project.summary}"></c:out></p>
                                            <%--                        <strong><spring:message code="categories"/></strong>--%>
                                            <%--                        <c:forEach items="${project.categories}" var="category">--%>
                                            <%--                             <p class="card-text" id="category">-${category.name}</p>--%>
                                            <%--                        </c:forEach>--%>
                                        <strong><spring:message code="owner"/></strong>
                                        <p><c:out value="${project.owner.firstName}"/> <c:out value="${project.owner.lastName}"/></p>
                                        <strong><spring:message code="price"/></strong>
                                        <p><c:out value="${project.cost}"/></p>
                                        <a href="<c:url value='/users/${user.id}/${project.id}'/>" class="btn btn-dark"><spring:message code="moreinfo"></spring:message></a>
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

                    <div class="tab-pane fade" id="favorites" role="tabpanel" aria-labelledby="profile-tab">
                        <c:if test="${!empty favs}">
                            <c:forEach items="${favs}" var="project">
                                <div class="card m-2">
                                    <div class="card-header">
                                        <h5 class="card-title"><c:out value="${project.name}"></c:out></h5>
                                    </div>
                                    <div class="card-body">
                                        <p class="card-text"><c:out value="${project.summary}"></c:out></p>
                                        <strong><spring:message code="owner"/></strong>
                                        <p><c:out value="${project.owner.firstName}"/> <c:out value="${project.owner.lastName}"/></p>
                                        <strong><spring:message code="price"/></strong>
                                        <p><c:out value="${project.cost}"/></p>
                                        <a href="<c:url value='/users/${user.id}/${project.id}'/>" class="btn btn-dark"><spring:message code="moreinfo"></spring:message></a>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty favs}">
                            <div class="card m-2">
                                <div class="card-header">
                                    <h5 class="card-title centered"><spring:message code="noProjFound"></spring:message> </h5>
                                </div>
                            </div>
                        </c:if>
                    </div>

                </div>
            </div>
        </div>
    </form>
</div>

</body>
</html>
