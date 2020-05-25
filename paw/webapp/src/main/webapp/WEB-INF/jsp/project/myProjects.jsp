<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "../components/header.jsp" %>

<html>
<head>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <%--<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>--%>
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/userprofile.css"/>"/>
    <title><spring:message code="my_projects"/> | VestNet</title>
</head>
<body>

<strong class="tab-title2"><spring:message code="my_projects"/></strong>

<c:forEach var="project" items="${projects}" varStatus="theCount">
    <span class="anchor-header" id="dashboard-project-${project.id}"></span>
    <div class="container py-3">
        <div class="card msg">
            <div class="row ">
                <div class="col-2">
                    <img src="<c:url value="/imageController/project/${project.id}"/>" class="p-img w-100">
                </div>
                <div class="col-md-10 px-3">
                    <div class="card-block px-3">
                        <h4 class="card-title msg-title"><c:out value="${project.name}"/></h4>
<%--                        <p class="card-text">${project.summary} </p>--%>
                        <div class="row msg-content">
                            <div class="col-"><h5><spring:message code="cost"/> </h5></div>
                            <div class="col-5 msg-content"><p class="card-text"> <c:out value="${project.cost}"/></p></div>
                        </div>
                        <div class="row msg-content">
                            <div class="col-3">
                                <strong><spring:message code="hits"/> </strong>
                            </div>
                            <div class="col-7">
                                <p>  <c:out value="${project.hits}"/></p>
                            </div>
                            <div class="col-">
                                <a href="<c:url value="/messages/${project.id}"/>" class="btn btn-dark btn-project pull-right"> <spring:message code="preview_project"/> </a>
                            </div>
                        </div>
                        <div class="row msg-content">
                            <div class="col-3">
                                <strong><spring:message code="favs"/> </strong>
                            </div>
                            <div class="col-7">
                                <c:set var="favorites" value="${project.name}favs"/>
                                <p>  <c:out value="${requestScope[favorites]}"/></p>
                            </div>
                            <div class="col-">



                                    <button onclick="fetchMsgs(${project.id}, ${theCount.index})" class="btn btn-dark btn-project pull-right"  type="button" data-toggle="collapse" data-target="#collapse${project.id}" aria-expanded="false" aria-controls="collapse${project.id}">
                                        <div class="notification-icon">
                                            <span> <spring:message code="see_msgs"></spring:message></span>
                                            <c:if test="${project.notRead != 0}">
                                                <span class="badge bg-danger"><c:out value="${project.notRead}"/></span>
                                            </c:if>
                                        </div>

                                    </button>



                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <div class="collapse" id="collapse${project.id}">
        </div>
    </div>

</c:forEach>
<c:if test="${empty projects}">
    <div class="card no-proj-mine">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="noProjOwned" arguments=""/> </h5>
        </div>
    </div>
</c:if>
<c:url var="addProject" value="/newProject"/>

<div class="text-center mt-5">
    <a href="${addProject}" class="btn btn-white btn-lg"> <spring:message code="add_project"/> </a>
</div>

<script>
    var state = new Array(${projects.size() + 1}).fill(0);

    let options = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    };
</script>

<script>
    function fetchMsgs(project_id, index){
        console.log('')
        if(state[index] === 0) {
            state[index] = 1;
            fetch(window.location.origin + '${pageContext.request.contextPath}' + "/message/" + project_id)
                .then(response => response.json())
                .then(data => {
                    let div = document.getElementById("collapse" + project_id);
                    for (let i = 0; i < data.length; i++) {
                        var g = document.createElement('div')
                        g.className = "card msg msg-collapse"
                        var h = document.createElement('div')
                        h.className = "card-body"

                        var bold1 = document.createElement("strong")
                        var bold2 = document.createElement("strong")
                        var bold3 = document.createElement("strong")

                        var textNode1 = document.createTextNode("<spring:message code="msg"/> ")
                        var textNode2 = document.createTextNode(data[i]["content"]["message"])
                        var textNode3 = document.createTextNode("<spring:message code="offer"/> ")
                        var textNode4 = document.createTextNode(data[i]["content"]["offer"])
                        var textNode5 = document.createTextNode("<spring:message code="request"/> ")
                        var textNode6= document.createTextNode(data[i]["content"]["interest"])

                        bold1.append(textNode1)
                        bold2.append(textNode3)
                        bold3.append(textNode5)

                        var aux1 = document.createElement('div');
                        aux1.append(bold1)
                        aux1.append(textNode2)
                        var aux2 = document.createElement('div');
                        aux2.append(bold2)
                        aux2.append(textNode4)
                        var aux3 = document.createElement('div');
                        aux3.append(bold3)
                        aux3.append(textNode6)

                        h.append(aux1);
                        h.append(aux2);
                        h.append(aux3);

                        let profileUrl = '<c:url value="/users/"/>' + data[i]["senderId"] + "?back=yes"
                        let profile = document.createElement('a')
                        profile.setAttribute('href', profileUrl)
                        profile.innerText = '<spring:message code="view_inv_profile"/>'
                        profile.className = "btn btn-dark btn-md"

                        let refuseUrl = window.location.origin + '<c:url value="/message/refuse/"/>' + data[i]["projectId"] + "/" + data[i]["senderId"]
                        let refuse = document.createElement('button')
                        refuse.setAttribute('id', 'refuse-message-' + data[i]["projectId"] + "-" + data[i]["senderId"]);
                        // refuse.setAttribute('href', refuseUrl)
                        refuse.addEventListener("click", function () {
                            fetch(refuseUrl, options)
                                .then(response => {
                                    // div.removeChild(g);
                                    // window.location.href = window.location.origin + window.location.pathname + '#dashboard-project-' + data[i]["projectId"];
                                    window.location.reload();
                            }).catch((function (reason) { console.error(reason) }));
                        });
                        refuse.innerText = '<spring:message code="refuse"/>'
                        refuse.className = "btn btn-danger btn-md pull-right"

                        let acceptUrl = window.location.origin + '<c:url value="/message/accept/"/>' + data[i]["projectId"] + "/" + data[i]["senderId"]
                        let accept = document.createElement('button')
                        accept.setAttribute('id', 'refuse-message-' + data[i]["projectId"] + "-" + data[i]["senderId"]);
                        // accept.setAttribute('href', acceptUrl)
                        accept.addEventListener("click", function () {
                            fetch(acceptUrl, options)
                                .then(response => {
                                    // div.removeChild(g);
                                    // window.location.href = window.location.origin + window.location.pathname + '#dashboard-project-' + data[i]["projectId"];
                                    window.location.reload();
                            }).catch((function (reason) { console.error(reason) }));
                        });
                        accept.innerText = '<spring:message code="accept"/>'
                        accept.className = "btn btn-success btn-md pull-right"

                        h.append(profile)
                        h.append(refuse)
                        h.append(accept)
                        g.appendChild(h)
                        div.appendChild(g)
                    }


                    if (data.length === 0){
                        console.log("hola")
                        var g = document.createElement('div')
                        g.className = "card msg msg-collapse"
                        var h = document.createElement('div')
                        h.className = "card-body"
                        var textNode = document.createTextNode('<spring:message code="no_msg" javaScriptEscape="true"/>')
                        h.append(textNode)
                        g.appendChild(h)
                        div.appendChild(g)
                    }

                }).catch((function (reason) {
                    state[index] = 0;
                    console.error(reason)
                }));
        }

    }


</script>

</body>
</html>
<!--
-->