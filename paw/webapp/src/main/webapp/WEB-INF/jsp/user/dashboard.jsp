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
                    <img src="<c:url value="/imageController/project/${project.id}"/>" class="p-img w-200"/>
                </div>
                <div class="col-md-10 px-3">
                    <div class="card-block px-3">
                        <h4 class="card-title msg-title"><c:out value="${project.name}"/></h4>
                        <div class="row msg-content">
                            <div class="col-9">
                                <div class="row msg-content">
                                    <div class="col-"><h5><spring:message code="cost"/></h5></div>
                                    <div class="col-5 msg-content">
                                        <spring:message code="project.cost" arguments="${project.cost}" var="costVar"/>
                                        <p class="card-text dash-text"><c:out value="${costVar}"/></p>
                                    </div>
                                </div>
                                <div class="row msg-content">
                                    <div class="col-"><h5><spring:message code="hits"/></h5></div>
                                    <div class="col-5 msg-content"><p class="card-text dash-text"><c:out value="${project.hits}"/></p></div>
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
            <h5 class="card-title text-white centered"><spring:message code="noProjOwned"/></h5>
        </div>
    </div>
</c:if>

<%-- Add a new project link --%>
<div class="text-center mt-5 mb-5">
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
                    let div = document.getElementById("collapse" + project_id);
                    for (let i = 0; i < data.length; i++) {
                        let message = {   body: htmlEscape(data[i]['content']['message']),
                            offer: htmlEscape(data[i]['content']['offer']),
                            request: htmlEscape(data[i]['content']['interest']),
                            investorUrl: '${link_user}' + data[i]["sender_id"] + "?back=yes",
                            projectId: project_id,
                            senderId: data[i]["sender_id"],
                        };
                        const element = messageTemplate(message);
                        div.innerHTML = div.innerHTML + element;
                    }

                    if (data.length === 0){
                        let g = document.createElement('div');
                        g.className = "card msg msg-collapse";
                        let h = document.createElement('div');
                        h.className = "card-body";
                        let textNode = document.createTextNode('<spring:message code="no_msg"/>');
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

    function htmlEscape(str) {
        return str.replace(/&/g, '&amp;')
            .replace(/>/g, '&gt;')
            .replace(/</g, '&lt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;')
            .replace(/`/g, '&#96;');
    }

    function messageTemplate(message) {
        return `
            <div class="card msg msg-collapse">
                <div class="card-body">
                    <div>
                        <strong><spring:message code="msg.title.body"/></strong><br>
                        <p>\${message.body}</p>
                        <strong><spring:message code="msg.title.offer"/></strong><br>
                        <p>\${message.offer}</p>
                        <strong><spring:message code="msg.title.request"/></strong><br>
                        <p>\${message.request}</p>
                        <a href="\${message.investorUrl}" class="btn btn-dark btn-md"><spring:message code="view_inv_profile"/></a>
                        <button id="refuse-message-\${message.projectId}-\${message.senderId}" class="btn btn-danger btn-md pull-right" onclick="answer(\${message.projectId}, \${message.senderId}, false)"><spring:message code="dashboard.msg.refuse"/></button>
                        <button id="accept-message-\${message.projectId}-\${message.senderId}" class="btn btn-success btn-md pull-right" onclick="answer(\${message.projectId}, \${message.senderId}, true)"><spring:message code="dashboard.msg.accept"/></button>
                    </div>
                </div>
            </div>
        `;
    }

    function answer(project, sender, value) {
        let url = '${link_update}' + '?p_id=' + project + '&s_id=' + '${session_user_id}' + '&r_id=' + sender + '&val=' + value;
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