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
    <title><spring:message code="my_projects"/> | VestNet</title>
</head>
<body>
<c:forEach var="project" items="${projects}" varStatus="theCount">
    <span class="anchor-header" id="dashboard-project-${project.id}"></span>
    <div class="container py-3">
        <div class="card">
            <div class="row ">
                <div class="col-md-4">
                    <img src="<c:url value="/imageController/project/${project.id}"/>" class="w-100">
                </div>
                <div class="col-md-8 px-3">
                    <div class="card-block px-3">
                        <h4 class="card-title">${project.name}</h4>
                        <p class="card-text">${project.summary} </p>
                        <h5><spring:message code="cost"></spring:message> </h5>
                        <p class="card-text"> ${project.cost}</p>
                        <div class="row">
                            <div class="col">
                                <strong><spring:message code="hits"></spring:message> </strong>
                                <p>  ${project.hits}</p>
                            </div>
                            <div class="col">
                                <strong><spring:message code="favs"></spring:message> </strong>
                                <c:set var="favorites" value="${project.name}favs"></c:set>
                                <p>  ${requestScope[favorites]}</p>
                            </div>
                        </div>
                        <a href="<c:url value="/messages/${project.id}"/>" class="btn btn-dark pull-right"> <spring:message code="preview_project"/> </a>
                        <button onclick="fetchMsgs(${project.id}, ${theCount.index})" class="btn btn-dark pull-right"  type="button" data-toggle="collapse" data-target="#collapse${project.id}" aria-expanded="false" aria-controls="collapse${project.id}"><spring:message code="see_msgs"></spring:message> </button>

                    </div>
                </div>

            </div>
        </div>
        <div class="collapse" id="collapse${project.id}">
        </div>
    </div>
    </div>



</c:forEach>
<c:if test="${empty projects}">
    <div class="card m-2">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="noProjOwned" arguments=""></spring:message> </h5>
        </div>
    </div>
</c:if>
<c:url var="addProject" value="/newProject"></c:url>

<div class="text-center mt-5">
    <a href="${addProject}" class="btn btn-secondary btn-lg"> <spring:message code="add_project"></spring:message> </a>
</div>


<script>
    var state = new Array(${projects.size() + 1}).fill(0);
</script>

<script>
    function fetchMsgs(project_id, index){
        console.log('')
        if(state[index] === 0) {
            fetch(window.location.origin + '${pageContext.request.contextPath}' + "/message/" + project_id)
                .then(response => response.json())
                .then(data => {
                    let div = document.getElementById("collapse" + project_id);
                    for (let i = 0; i < data.length; i++) {
                        var g = document.createElement('div')
                        g.className = "card"
                        var h = document.createElement('div')
                        h.className = "card-body"

                        var bold1 = document.createElement("strong")
                        var bold2 = document.createElement("strong")
                        var bold3 = document.createElement("strong")

                        var textNode1 = document.createTextNode("<spring:message code="msg"></spring:message> ")
                        var textNode2 = document.createTextNode(data[i]["content"]["message"])
                        var textNode3 = document.createTextNode("<spring:message code="offer"></spring:message> ")
                        var textNode4 = document.createTextNode(data[i]["content"]["offer"])
                        var textNode5 = document.createTextNode("<spring:message code="request"></spring:message> ")
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
                        profile.innerText = '<spring:message code="view_inv_profile"></spring:message>'
                        profile.className = "btn btn-dark btn-sm"

                        let refuseUrl = '<c:url value="/message/refuse/"/>' + data[i]["projectId"] + "/" + data[i]["senderId"]
                        let refuse = document.createElement('a')
                        refuse.setAttribute('href', refuseUrl)
                        refuse.innerText = '<spring:message code="refuse"></spring:message>'
                        refuse.className = "btn btn-danger"

                        let acceptUrl = '<c:url value="/message/accept/"/>' + data[i]["projectId"] + "/" + data[i]["senderId"]
                        let accept = document.createElement('a')
                        accept.setAttribute('href', acceptUrl)
                        accept.innerText = '<spring:message code="accept"></spring:message>'
                        accept.className = "btn btn-success"




                        h.append(profile)
                        h.append(refuse)
                        h.append(accept)
                        g.appendChild(h)
                        div.appendChild(g)
                    }


                    if (data.length === 0){
                        console.log("hola")
                        var g = document.createElement('div')
                        g.className = "card"
                        var h = document.createElement('div')
                        h.className = "card-body"
                        var textNode = document.createTextNode('<spring:message code="no_msg" javaScriptEscape="true"/>')
                        h.append(textNode)
                        g.appendChild(h)
                        div.appendChild(g)
                    }


                    state[index] = 1;

                })
        }

    }


</script>

</body>
</html>
<!--
-->