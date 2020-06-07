<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="../components/header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="<c:url value="/css/detail.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><spring:message code="page.title.singleProject" arguments="${project.name}"/></title>
</head>

<%-- Set used variables --%>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="session_user_id" property="principal.id"/>
    <sec:authentication var="session_user_mail" property="principal.username"/>
</sec:authorize>

<sec:authorize access="!isAuthenticated()">
    <c:set var="session_user_id" value="0"/>
</sec:authorize>

<%-- Set used URLs --%>
<c:url var="icon_fav_off" value="/images/bookmarkOff.png"/>
<c:url var="icon_fav_on" value="/images/bookmarkOn.png"/>

<body>

<%-- Message and Back logic --%>
<div class="container" style="margin-top: 20px">
    <div>
        <div class="d-flex justify-content-between align-self-center">
            <div class="p-2">
                <a onclick="getBackAction()" class="btn btn-dark"><spring:message code="back"/></a>
            </div>
            <c:if test="${contactStatus == 1}">
                <div class="p-2 ml-8">
                    <h5 class="card-title mr-4" style="margin-top: 15px; color: #750096"><spring:message
                            code="successfulContact"/> <c:out value="${project.owner.firstName}"/></h5>
                </div>
            </c:if>
            <c:if test="${contactStatus == 2}">
                <div class="p-2 ml-8">
                    <h5 class="card-title mr-4" style="margin-top: 15px; color: #750096"><spring:message
                            code="waitReply"/></h5>
                </div>
            </c:if>
        </div>
        <div class="row" style="margin: 20px">
            <div class="col-5">
                <div class="container-img">
                    <img src="<c:url value="/imageController/project/${project.id}"/>" class="proj-img"
                         alt="<spring:message code="projectImage"/>"
                         aria-placeholder="<spring:message code="projectImage"/>"/>
                </div>
            </div>

            <%-- Favorite icon logic --%>
            <div class="col-6">
                <div class="d-flex justify-content-center">
                    <div class="card mb-3">
                        <sec:authorize access="hasRole('ROLE_INVESTOR')">
                            <div class="card-header header-white">
                                <button onclick="favTap()" class="btn-transp pull-right">
                                    <c:choose>
                                        <c:when test="${user.favorites.contains(project)}">
                                            <c:set var="icon_fav" value="${icon_fav_on}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="icon_fav" value="${icon_fav_off}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <img id="favImg" src="${icon_fav}" height="40">
                                </button>
                            </div>
                        </sec:authorize>

                        <%-- Project card --%>
                        <div class="card-body">
                            <h5 class="card-title"><b><c:out value="${project.name}"/></b></h5>
                            <footer class="blockquote-footer">by <c:out value="${project.owner.firstName}"/>
                                <c:out value="${project.owner.lastName}"/></footer>
                            <p class="card-text"><c:out value="${project.summary}"/></p>

                            <h5 class="card-title"><b><spring:message code="categories"/></b></h5>
                            <c:forEach var="category" items="${project.categories}">
                                <li><spring:message code="${category.name}"/></li>
                            </c:forEach>
                            <br/>
                            <h5 class="card-title"><b><spring:message code="totalCost"/></b></h5>
                            <p>U$D<c:out value="${project.cost}"/></p>

                            <h5 class="card-title"><b><spring:message code="contactMail"/></b></h5>
                            <p><c:out value="${project.owner.email}"/></p>

                            <c:if test="${session_user_id != project.owner.id}">
                                <h5><a href="<c:url value='/users/${project.owner.id}?back=yes'/>"
                                       class="btn btn-dark btn-sm"><spring:message code="view_profile"/></a></h5>
                            </c:if>

                            <div class="dropdown-divider"></div>

                            <p class="card-text"><small class="text-muted"><spring:message code="lastUpdated"/> <c:out
                                    value="${project.updateDate}"/></small></p>
                        </div>
                    </div>
                </div>

                <%-- Contact button --%>
                <div class="d-flex justify-content-end">
                    <sec:authorize access="isAnonymous()">
                        <%-- TODO ask if wants to login/sign up --%>
                        <button class="btn btn-dark btn-lg btn-block" aria-controls="contact" id="contact-login-button">
                            <spring:message code="singleView.button.createToContactOwner"/>
                        </button>
                    </sec:authorize>

                    <sec:authorize access="hasRole('ROLE_INVESTOR')">
                            <c:choose>
                                <c:when test="${lastMessage.isPresent() and lastMessage.get().accepted eq null}">
                                    <button class="btn btn-dark btn-lg btn-block" id="contact-already-sent-button" disabled>
                                        <spring:message code="singleView.button.alreadySent"/>
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-dark btn-lg btn-block" data-toggle="collapse"
                                    data-target="#contact" aria-expanded="false" aria-controls="contact" id="contact-expand-button">
                                        <spring:message code="contactowner"/>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                    </sec:authorize>
                </div>
            </div>
        </div>

        <sec:authorize access="hasRole('ROLE_INVESTOR')">
            <div class="collapse" id="contact">
                <div class="card contact">
                    <div class="card-header">
                        <label class="label-header"> <spring:message
                                code="contact.header"/> ${project.owner.firstName} ${project.owner.lastName}</label>
                        <button class="btn btn-dark pull-right" type="button" data-toggle="collapse"
                                data-target="#contact" aria-expanded="false" aria-controls="contact">X
                        </button>
                    </div>
                    <div class="card-body">
                        <c:url value="/projects/${project.id}" var="postPath"/>
                        <form:form modelAttribute="mailForm" action="${postPath}" method="post">

                            <div class="form-group">
                                <label><spring:message code="contact.bodyMessage"/></label>
                                <div class="input-group mb-3">
                                    <spring:message code="writemessage" var="placeholdermessage"/>
                                    <form:textarea path="body" type="text" class="form-control"
                                                   placeholder="${placeholdermessage}" aria-describedby="basic-addon2"/>
                                </div>
                            </div>

                            <div class="container-contact">
                                <div class="row">
                                    <div class="col-2">
                                        <label><spring:message code="contact.offerMessage"/></label>
                                    </div>
                                    <div class="col-md-5">
                                        <div class="row justify-content-center">
                                            <div class="col-2">
                                                <label><spring:message code="currency"/></label>
                                            </div>
                                            <div class="col">
                                                <spring:message code="writemessage" var="placeholderoffers"/>
                                                <form:input path="offers" type="number" class="form-control"
                                                            placeholder="${placeholderoffers}"
                                                            aria-describedby="basic-addon2" id="contact-offer"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <form:errors path="offers" cssClass="formError"/>
                            </div>

                            <div class="container-contact">
                                <div class="row">
                                    <div class="col-2">
                                        <label><spring:message code="contact.exchangeMessage"/></label>
                                    </div>
                                    <div class="col-md-5">
                                        <spring:message code="writemessage" var="placeholderexchange"/>
                                        <form:textarea path="exchange" type="text" class="form-control"
                                                       placeholder="${placeholderexchange}"
                                                       aria-describedby="basic-addon2"/>
                                    </div>
                                </div>
                            </div>

                            <form:input path="receiverId" value="${project.owner.id}" type="hidden"/>

                            <div class="text-right">
                                <input type="submit" value="<spring:message code="send"/>" class="btn btn-dark"
                                       onclick="adjustInputs()" id="contact-send"/>
                            </div
                        </form:form>
                    </div>
                </div>
            </div>
        </sec:authorize>
    </div>
</div>
<script>
    let options = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    // Every time window loads its a hit
    window.onload = function () {
        if (${session_user_id != project.owner.id}) {
            fetch(window.location.origin + "${pageContext.request.contextPath}" + "/addHit/" + "${project.id}", options)
                .catch((function (reason) {
                    console.error(reason)
                }))
        }
    };
</script>

<script>
    $('.collapse').on('shown.bs.collapse', function () {
        let element = document.getElementById('contact');
        element.scrollIntoView({
            block: 'start',
            behavior: 'smooth'
        });
    });

    function addFav() {
        let path_aux = "${pageContext.request.contextPath}";
        let path = window.location.origin + path_aux + "/addFavorite?u_id=" + ${session_user_id}+"&p_id=" +${project.id};
        fetch(path, options).catch((function (reason) {
            console.error(reason)
        }));
    }

    function delFav() {
        let path_aux = "${pageContext.request.contextPath}";
        let path = window.location.origin + path_aux + "/deleteFavorite?u_id=" + ${session_user_id}+"&p_id=" +${project.id};
        fetch(path, options).catch((function (reason) {
            console.error(reason)
        }));
    }

    let favImage = document.getElementById('favImg');
    let fav = ${isFav};

    function favTap() {
        if (fav) {
            favImage.setAttribute("src", "${icon_fav_off}");
            fav = false;
            delFav();
        } else {
            favImage.setAttribute("src", "${icon_fav_on}");
            fav = true;
            addFav();
        }
    }

    // TODO: VER SI SE PUEDE HACER DE OTRA FORMA
    function getBackAction() {
        if (${contactStatus == 0}) {
            history.back();
        } else {
            history.back();
            history.back();
        }
    }

    let offerTag = document.getElementById('contact-offer');
    if (offerTag != null) {
        offerTag.addEventListener("keypress", function (key) {
            if (offerTag.value.length > 6) {
                offerTag.value = offerTag.value.slice(0, 6);
            }
        });
    }

    function adjustInputs() {
        if (offerTag.value.length === 0 || offerTag.value < 0) {
            offerTag.value = 0;
        }
        offerTag.value = Math.round(offerTag.value);
    }
</script>
</body>
</html>
