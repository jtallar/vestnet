<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../components/header.jsp" %>

<html>
<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css" rel="stylesheet">
    <script src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
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
<c:url var="link_dashboard" value="/dashboard"/>
<c:url var="link_stop_funding" value="/stopFunding"/>

<body>
<%-- Dashboard view --%>
<div class="row">
    <div class="col-2"></div>
    <strong class="tab-title2"><spring:message code="my_projects"/></strong>
</div>
<div class="row">
    <div class="col-8"></div>
    <input type="checkbox" onchange="changeFunded()" data-toggle="toggle"
           data-on="<spring:message code="show_acc_proj"/> " data-off="<spring:message code="show_curr_proj"/> "
           data-onstyle="dark" data-offstyle="white" id="funded-toggle"
           <c:if test="${funded}">checked</c:if>>
</div>
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
                                <div>
                                    <c:if test="${!project.funded}">
                                        <button type="button" class="btn btn-success" data-toggle="modal" data-target="#expModal-p${project.id}">
                                            <spring:message code='stopFunding'/>
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                            <!-- Show stop funding confirmation -->

                            <div class="modal fade" id="expModal-p${project.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog " role="document">
                                    <div class="modal-content mx-auto my-auto">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="exampleModalLabel">
                                                <spring:message code="confirm_stop_funding"/>
                                            </h5>
                                        </div>
                                        <div class="modal-body">
                                            <spring:message code="stop_funding_result"/>
                                        </div>
                                        <div class="modal-footer">
                                            <div class="row">
                                                <button type="button" class="btn btn-danger" data-dismiss="modal">
                                                    <spring:message code="cancel"/>
                                                </button>
                                                <button onclick="stopFunding(${project.id})" type="button" class="btn btn-success">
                                                    <spring:message code="confirm"/>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-3">
                                <a href="<c:url value="/projects/${project.id}"/>" class="btn btn-dark btn-project pull-right">
                                    <spring:message code="preview_project"/>
                                </a>
                                <c:if test="${!project.funded}">
                                    <button onclick="fetchMessages(${project.id}, ${status.index})"
                                            class="btn btn-dark btn-project pull-right"  type="button" data-toggle="collapse"
                                            data-target="#collapse${project.id}" aria-expanded="false" aria-controls="collapse${project.id}">
                                            <%--                                    <div class="notification-icon">--%>
                                        <span> <spring:message code="see_msgs"/></span>
                                            <%-- TODO fix this, make it work --%>
                                            <c:if test="${project.msgCount != 0}">
                                                <span class="badge bg-danger"><c:out value="${project.notRead}"/></span>
                                            </c:if>
                                            <%--                                    </div>--%>
                                    </button>
                                </c:if>
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
            <c:choose>
                <c:when test="${funded}">
                    <h5 class="card-title text-white centered"><spring:message code="noProjOwned"/></h5>
                </c:when>
                <c:otherwise>
                    <h5 class="card-title text-white centered"><spring:message code="noProjFunded"/></h5>
                </c:otherwise>
            </c:choose>
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

    function changeFunded() {
        location.href = '${link_dashboard}' + '?funded=' + '${!funded}';
    }

    function stopFunding(project) {
        let url = '${link_stop_funding}' + '?p_id=' + project;
        put(url);
    }

    function put(url) {
        fetch(url, options)
            .catch((reason => {
                console.error(reason)
            }))
            .finally(function() {
                window.location.reload(true)
            });
    }
</script>

<script>

</script>
</body>
</html>