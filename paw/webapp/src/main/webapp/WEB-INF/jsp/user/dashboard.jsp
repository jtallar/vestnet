<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../components/header.jsp" %>

<html>
<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/userprofile.css"/>"/>
    <title><spring:message code="page.title.dashboard"/></title>
</head>

<%-- Set used URLs --%>
<c:url var="link_new_project" value="/newProject"/>
<c:url var="link_refuse" value="/message/refuse/"/>
<c:url var="link_accept" value="/message/accept/"/>
<c:url var="link_user" value="/users/"/>

<body>

<%-- Dashboard view --%>
<strong class="tab-title2"><spring:message code="my_projects"/></strong>
<c:forEach var="project" items="${projects}">
    <span class="anchor-header" id="dashboard-project-${project.id}"></span>
    <div class="container py-3">
        <div class="card msg">
            <div class="row ">
                <div class="col-2">
                    <img src="<c:url value="/imageController/project/${project.id}"/>" class="p-img w-100"/>
                </div>
                <div class="col-md-10 px-3">
                    <div class="card-block px-3">
                        <h4 class="card-title msg-title"><c:out value="${project.name}"/></h4>
                        <div class="row msg-content">
                            <div class="col-9">
                                <div class="row msg-content">
                                    <div class="col-"><h5><spring:message code="cost"/></h5></div>
                                    <div class="col-5 msg-content"><p class="card-text"><c:out value="${project.cost}"/></p></div>
                                </div>
                                <div class="row msg-content">
                                    <div class="col-"><h5><spring:message code="hits"/></h5></div>
                                    <div class="col-5 msg-content"><p class="card-text"><c:out value="${project.hits}"/></p></div>
                                </div>
                                <div class="row msg-content">
                                    <div class="col-"><h5><spring:message code="favs"/></h5></div>
                                    <div class="col-5 msg-content"><p class="card-text"><c:out value="${project.favoriteBy.size()}"/></p></div>
                                </div>
                            </div>
                            <div class="col-3">
                                <a href="<c:url value="/projects/${project.id}"/>" class="btn btn-dark btn-project pull-right">
                                    <spring:message code="preview_project"/>
                                </a>
                                <button class="btn btn-dark btn-project pull-right" type="button" data-toggle="collapse"
                                        data-target="#collapse${project.id}" aria-expanded="false"
                                        aria-controls="collapse${project.id}">
                                    <div class="notification-icon">
                                        <span> <spring:message code="see_msgs"/></span>
                                            <%-- TODO fix this, make it work --%>
                                            <%--<c:if test="${project.notRead != 0}">--%>
                                            <%--<span class="badge bg-danger"><c:out value="${project.notRead}"/></span>--%>
                                            <%--</c:if>--%>
                                    </div>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="collapse" id="collapse${project.id}">
            <c:forEach var="message" items="${project.messageList}">
            <div class="card msg msg-collapse">
                <div class="card-body">
                    <div class="row">
                        <div class="col-9">
                            <div>
                                <strong><spring:message code="msg"/> </strong>
                                    ${message.content.message}
                            </div>
                            <div>
                                <strong><spring:message code="offer"/> </strong>
                                    ${message.content.offer}
                            </div>
                            <div>
                                <strong><spring:message code="request"/> </strong>
                                    ${message.content.interest}
                            </div>
                        </div>
                        <div class="col-3">
                            <div class="row-">
                                <a href="${link_user}${message.sender.id}?back=yes" class="btn btn-dark btn-md pull-right">
                                    <spring:message code="view_inv_profile"/>
                                </a>
                            </div>
                            <div class="row-">
                                <button onclick="put('${link_refuse}${project.id}/${message.sender.id}')"
                                        class="btn btn-danger btn-md pull-right" id="refuse-message-${project.id}-${message.sender.id}">
                                    <spring:message code="refuse"/>
                                </button>
                                <button onclick="put('${link_accept}${project.id}/${message.sender.id}')"
                                        class="btn btn-success btn-md pull-right" id="accept-message-${project.id}-${message.sender.id}">
                                    <spring:message code="accept"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${empty project.messageList}">
                <div class=" card msg msg-collapse">
                    <div class="card-body">
                        <p><spring:message code="no_msg" javaScriptEscape="true"/></p>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</c:forEach>

<%-- If there is no projects --%>
<c:if test="${empty projects}">
    <div class="card no-proj-mine">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="noProjOwned" arguments=""/></h5>
        </div>
    </div>
</c:if>

<%-- Add a new project link --%>
<div class="text-center mt-5">
    <a href="${link_new_project}" class="btn btn-white btn-lg"> <spring:message code="add_project"/> </a>
</div>

<script>
    let options = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    function put(url) {
        fetch(url, options)
            .then(window.location.reload())
            .catch((reason => {
                console.error(reason)
            }));
    }
</script>

</body>
</html>