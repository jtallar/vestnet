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

<%-- Set used vars --%>
<sec:authentication var="session_user_id" property="principal.id"/>

<%-- Set used URLs --%>
<c:url var="link_new_project" value="/newProject"/>
<c:url var="link_update" value="/message/update"/>
<c:url var="link_unread" value="/messages/unread"/>
<c:url var="link_user" value="/users/"/>

<body>

<%-- Dashboard view --%>
<strong class="tab-title2"><spring:message code="my_projects"/></strong>
<c:forEach var="project" items="${projects}" varStatus="status">
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
                            </div>
                            <div class="col-3">
                                <a href="<c:url value="/projects/${project.id}"/>" class="btn btn-dark btn-project pull-right">
                                    <spring:message code="preview_project"/>
                                </a>
                                <button onclick="fetchMessages(${project.id}, ${status.index})"
                                        class="btn btn-dark btn-project pull-right"  type="button" data-toggle="collapse"
                                        data-target="#collapse${project.id}" aria-expanded="false" aria-controls="collapse${project.id}">
<%--                                    <div class="notification-icon">--%>
                                        <span> <spring:message code="see_msgs"/></span>
                                            <%-- TODO fix this, make it work --%>
                                            <%--<c:if test="${project.notRead != 0}">--%>
                                            <%--<span class="badge bg-danger"><c:out value="${project.notRead}"/></span>--%>
                                            <%--</c:if>--%>
<%--                                    </div>--%>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="collapse" id="collapse${project.id}"></div>
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

    let state = new Array(${projects.size() + 1}).fill(0);

    function fetchMessages(project_id, index) {
        if(state[index] === 0) {
            state[index] = 1;
            fetch('${link_unread}' + '?p_id=' + project_id + '&u_id=' + '${session_user_id}')
                .then(response => response.json())
                .then(data => {
                    console.log("MATI: " + data);
                    let div = document.getElementById("collapse" + project_id);
                    for (let i = 0; i < data.length; i++) {
                        let g = document.createElement('div');
                        g.className = "card msg msg-collapse";
                        let h = document.createElement('div');
                        h.className = "card-body";

                        let bold1 = document.createElement("strong");
                        let bold2 = document.createElement("strong");
                        let bold3 = document.createElement("strong");

                        let textNode1 = document.createTextNode("<spring:message code="msg"/> ");
                        let textNode2 = document.createTextNode(data[i]["content"]["message"]);
                        let textNode3 = document.createTextNode("<spring:message code="offer"/> ");
                        let textNode4 = document.createTextNode(data[i]["content"]["offer"]);
                        let textNode5 = document.createTextNode("<spring:message code="request"/> ");
                        let textNode6= document.createTextNode(data[i]["content"]["interest"]);

                        bold1.append(textNode1);
                        bold2.append(textNode3);
                        bold3.append(textNode5);

                        let aux1 = document.createElement('div');
                        aux1.append(bold1);
                        aux1.append(textNode2);
                        let aux2 = document.createElement('div');
                        aux2.append(bold2);
                        aux2.append(textNode4);
                        let aux3 = document.createElement('div');
                        aux3.append(bold3);
                        aux3.append(textNode6);

                        h.append(aux1);
                        h.append(aux2);
                        h.append(aux3);

                        let profile = document.createElement('a');
                        profile.setAttribute('href', '${link_user}' + data[i]["sender_id"] + "?back=yes");
                        profile.innerText = '<spring:message code="view_inv_profile"/>';
                        profile.className = "btn btn-dark btn-md";

                        let refuse = document.createElement('button');
                        refuse.setAttribute('id', 'refuse-message-' + project_id + "-" + data[i]["sender_id"]);
                        refuse.addEventListener("click", function() { answer(project_id, data[i]["sender_id"], false) });
                        refuse.innerText = '<spring:message code="refuse"/>';
                        refuse.className = "btn btn-danger btn-md pull-right";

                        let accept = document.createElement('button');
                        accept.setAttribute('id', 'refuse-message-' + project_id + "-" + data[i]["sender_id"]);
                        accept.addEventListener("click", function () { answer(project_id, data[i]["sender_id"], true) });
                        accept.innerText = '<spring:message code="accept"/>';
                        accept.className = "btn btn-success btn-md pull-right";

                        h.append(profile);
                        h.append(refuse);
                        h.append(accept);
                        g.appendChild(h);
                        div.appendChild(g);
                    }

                    if (data.length === 0){
                        let g = document.createElement('div');
                        g.className = "card msg msg-collapse";
                        let h = document.createElement('div');
                        h.className = "card-body";
                        let textNode = document.createTextNode('<spring:message code="no_msg" javaScriptEscape="true"/>');
                        h.append(textNode);
                        g.appendChild(h);
                        div.appendChild(g);
                    }

                }).catch((function (reason) {
                state[index] = 0;
                console.error(reason)
            }));
        }

    }

    function answer(project, sender, value) {
        let url = '${link_update}' + '?p_id=' + project + '&s_id=' + sender + '&r_id=' + '${session_user_id}' + '&val=' + value;
        put(url);
    }

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