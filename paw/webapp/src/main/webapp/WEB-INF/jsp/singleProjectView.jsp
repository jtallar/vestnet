<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file = "header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <%--<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>--%>
    <link rel="stylesheet" href="<c:url value="/css/detail.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>${project.name} | VestNet</title>
</head>
<body>
<c:url var="favOff" value="/images/bookmarkOff.png"/>
<c:url var="favOn" value="/images/bookmarkOn.png"/>
<div class="container" style="margin-top: 20px">
    <div>
<%--        <div class="row">--%>
            <div class="d-flex justify-content-between align-self-center">
                <div class="p-2">
                    <a href="<c:url value="${back}"/>" class="btn btn-dark"><spring:message code="back"/></a>
                </div>
                <c:if test="${mailSent}">
                <div class="p-2 ml-8">
                    <h5 class="card-title mr-4" style="color: blueviolet"><spring:message code="mailSent"/> <c:out value="${project.owner.email}"/></h5>
                </div>
                </c:if>
            </div>
        <div class="row" style="margin: 20px">
            <div class="col">
                <div class="container-img">
                <img src="<c:url value="/imageController/project/${project.id}"/>" class="proj-img" alt="<spring:message code="projectImage"/>"
                     aria-placeholder="<spring:message code="projectImage"/>"/>
<%--                        <div class="centered-txt">${project.name}</div>--%>
<%--                        <div class="bottom-txt"><spring:message code="noImage"/></div>--%>
                </div>
<%--                <div id="carouselExampleCaptions" class="carousel slide" data-ride="carousel"--%>
<%--                     style="width: 450px; height:500px; margin: 0 auto">--%>
<%--                    <ol class="carousel-indicators">--%>
<%--                        <li data-target="#carouselExampleCaptions" data-slide-to="0" class="active"></li>--%>
<%--                        &lt;%&ndash;<li data-target="#carouselExampleCaptions" data-slide-to="1"></li>--%>
<%--                        <li data-target="#carouselExampleCaptions" data-slide-to="2"></li>&ndash;%&gt;--%>
<%--                    </ol>--%>
<%--                    <div class="carousel-inner">--%>
<%--                        <div class="carousel-item active">--%>
<%--                            &lt;%&ndash;<c:url var="first_pic" value="/images/purple.png"/>--%>
<%--                            <img src="${first_pic}" class="d-block w-100" alt="" style="width: 100%; height:100% ">&ndash;%&gt;--%>
<%--                            <img src="<c:url value="/imageController/project/${project.id}"/>" class="d-block w-100" alt="<spring:message code="projectImage"/>"--%>
<%--                                 style="width: 100%; height:100% " aria-placeholder="<spring:message code="projectImage"/>"/>--%>
<%--&lt;%&ndash;                            <div class="carousel-caption d-none d-md-block">--%>
<%--                                <p>This is a first view of the web prototype</p>--%>
<%--                            </div>&ndash;%&gt;--%>
<%--                        </div>--%>
<%--                        &lt;%&ndash;<div class="carousel-item">--%>
<%--                            <c:url var="second_pic" value="/images/orange.png"/>--%>
<%--                            <img src="${second_pic}" class="d-block w-100" alt="" style="width: 100%; height:100% ">--%>
<%--                            <div class="carousel-caption d-none d-md-block">--%>
<%--                                <p>Another view of the web prototype</p>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                        <div class="carousel-item">--%>
<%--                            <c:url var="third_pic" value="/images/green.png"/>--%>
<%--                            <img src="${third_pic}" class="d-block w-100" alt="" style="width: 100%; height:100% ">--%>
<%--                            <div class="carousel-caption d-none d-md-block">--%>
<%--                                <p>This is how we design de database</p>--%>
<%--                            </div>--%>
<%--                        </div>&ndash;%&gt;--%>
<%--                    </div>--%>
<%--                    <a class="carousel-control-prev" href="#carouselExampleCaptions" role="button" data-slide="prev">--%>
<%--                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>--%>
<%--                        <span class="sr-only">Previous</span>--%>
<%--                    </a>--%>
<%--                    <a class="carousel-control-next" href="#carouselExampleCaptions" role="button" data-slide="next">--%>
<%--                        <span class="carousel-control-next-icon" aria-hidden="true"></span>--%>
<%--                        <span class="sr-only">Next</span>--%>
<%--                    </a>--%>
<%--                </div>--%>
            </div>
            <div class="col">
                <div class="d-flex justify-content-center">
                    <div class="card mb-3">
                        <%--                    <img src="" class="card-img-top" alt="..." >--%>
                        <div class="card-header header-white">
                            <button onclick="favTap()" class="btn-transp pull-right">
                                <c:set var="fav" value="${isFav}"/>
                                <c:choose>
                                    <c:when test="${isFav==true}" >
                                        <c:set var="favSrc" value="${favOn}"/>
                                    </c:when>
                                    <c:when test="${isFav==false}">
                                        <c:set var="favSrc" value="${favOff}"/>
                                    </c:when>
                                </c:choose>
                                <img id="favImg" src="${favSrc}" height="40">

                            </button>
                        </div>
                        <div class="card-body">
                            <h5 class="card-title"><b><c:out value="${project.name}"/></b></h5>
                            <footer class="blockquote-footer">by <c:out value="${project.owner.firstName}"/>
                                <c:out value="${project.owner.lastName}"/></footer>
                            <p class="card-text"><c:out value="${project.summary}"/></p>

                            <h5 class="card-title"><b><spring:message code="categories"/></b></h5>
                            <c:forEach var="category" items="${project.categories}">
                                <li><c:out value="${category.name}"/></li>
                            </c:forEach>
                            <br/>
                            <h5 class="card-title"><b><spring:message code="totalCost"/></b></h5>
                            <p>U$D<c:out value="${project.cost}"/></p>

                            <h5 class="card-title"><b><spring:message code="contactMail"/></b></h5>
                            <p><c:out value="${project.owner.email}"/></p>

                            <c:if test="${sessionUser.id != project.owner.id}">
                                <h5><a href="<c:url value='/users/${project.owner.id}?back=yes'/>" class="btn btn-dark btn-sm"><spring:message code="view_profile"/></a></h5>
                            </c:if>

                            <p class="card-text"><small class="text-muted"><spring:message code="lastUpdated"/> <c:out value="${project.updateDate}"/></small></p>
                        </div>
                    </div>
                </div>
                <div class="d-flex justify-content-end">
                    <c:if test="${sessionUser.id == project.owner.id}">
<%--                        TODO: ADD EDIT PROJECT--%>
                    </c:if>
                    <c:if test="${investor}">
                        <a href="<c:url value='/projects/${project.id}/contact'/>" class="btn btn-dark btn-lg btn-block"><spring:message code="contactowner"/></a>
                    </c:if>
                </div>
            </div>
        </div>
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
        fetch(window.location.origin + "${pageContext.request.contextPath}" + "/addHit/" + "${project.id}", options)
        .catch((function (reason) { console.error(reason) }))
    };
</script>
<script>


    function addFav() {
        <%--let path = window.location.href.slice(0, window.location.href.lastIndexOf('/')) + "/addFavorite?u_id=" + ${sessionUser.id}+"&p_id="+${project.id};--%>
        <%--let path2 = window.location.href.split('/')[0] + window.location.pathname.split('/')[0] + "/addFavorite?u_id=" + ${sessionUser.id}+"&p_id="+${project.id};--%>
        let path_aux = "${pageContext.request.contextPath}";
        let path = window.location.origin + path_aux +"/addFavorite?u_id=" + ${sessionUser.id}+"&p_id="+${project.id};
        fetch(path, options).catch((function (reason) { console.error(reason) }));
    }
    function delFav() {
        // let path_aux = window.location.pathname.split('/')[1];
        let path_aux = "${pageContext.request.contextPath}";
        let path = window.location.origin + path_aux +"/deleteFavorite?u_id=" + ${sessionUser.id}+"&p_id="+${project.id};
        fetch(path, options).catch((function (reason) { console.error(reason) }));
    }

    var favImage = document.getElementById('favImg');
    var fav = ${isFav};

    function favTap() {
        if (fav) {
            favImage.setAttribute("src","${favOff}");
            fav = false;
            delFav();
        } else {
            favImage.setAttribute("src","${favOn}");
            fav = true;
            addFav();
        }
    }
</script>
</body>
</html>
