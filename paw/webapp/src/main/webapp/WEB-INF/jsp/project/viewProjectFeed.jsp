<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../components/header.jsp" %>

<html>
<head>
    <link rel="stylesheet"
          href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/form.css"/>"/>

    <%--<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>--%>

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Projects | VestNet</title>
</head>
<body>
<c:url var="favOff" value="/images/bookmarkOffB.png"/>
<c:url var="favOn" value="/images/bookmarkOnB.png"/>
<c:url var="order" value="/images/order.png"/>
<c:url var="filter" value="/images/filter.png"/>
<div>

</div>
<div class="col">
        <ul class="pagination justify-content-center">
            <li id="li-previous" class="page-item">
                <a id="li-a-previous" class="page-link" onclick="" href="#" aria-label="<spring:message code="previous"/>">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <c:forEach var="pageNumber" begin="${startPage}" end="${endPage}">
                <li class="page-item <c:if test="${pageNumber == page }"> active </c:if>"><a class="page-link" href="#">${pageNumber}</a></li>
            </c:forEach>
            <li id="li-next" class="page-item">
                <a id="li-a-next" class="page-link" href="#" aria-label="<spring:message code="next"/>">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
</div>
<%--    <div class="row grid">--%>

<div class="sidenav">
    <div class="form-row align-items-center" style="margin: 20px">
        <c:url var="createUrl" value='/projects'/>
        <form:form modelAttribute="categoryForm" method="GET" action="${createUrl}">
            <input type="hidden" name="keyword" value="${keyword}" />
            <input type="hidden" name="searchField" value="${searchField}" />
            <div class="container">
                <div class="row"><h5><spring:message code="filter"/></h5></div>
                <div class="row">
                    <div class="col-">
                        <img src="${filter}" width="40" class="logo-img">
                    </div>
                    <div class="col-md">
                        <form:select class="custom-select mr-sm-2" path="categoryId">
                            <form:option value="0"><spring:message code="noFilter"/> </form:option>
                            <c:forEach items="${categories}" var="category">
                                <form:option value="${category.id}">${category.name}</form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="row field">
                    <div class="col-">
                        <img src="${order}" width="40" class="logo-img">
                    </div>
                    <div class="col-md">
                        <form:select path="orderBy" class="custom-select mr-sm-2">
                            <form:option value="default"><spring:message code="noOrder"/> </form:option>
                            <form:option value="date"><spring:message code="date"/> </form:option>
                            <form:option value="cost_ascending"><spring:message code="cost_l_h"/></form:option>
                            <form:option value="cost_descending"><spring:message code="cost_h_l"/></form:option>
                            <form:option value="alphabetical"><spring:message code="alf"/></form:option>
                        </form:select>
                    </div>
                </div>
                <div class="field">
                    <label class="font-weight-bold"><spring:message code="range"/> </label>
                    <div class="row">
                        <div class="col-sm">
                            <spring:message var="min" code="min"/>
                            <form:input path="minCost" type="number" class="form-control mx-auto mx-auto"
                                        placeholder="${min}"/>
                            <form:errors path="minCost" cssClass="formError"/>
                        </div>
                        <p>-</p>
                        <div class="col-sm">
                            <spring:message var="max" code="max"/>
                            <form:input path="maxCost" type="number" class="form-control mx-auto mx-auto"
                                        placeholder="${max}"/>
                            <form:errors path="maxCost" cssClass="formError"/>
                            <form:errors cssClass="formError"/>
                        </div>
                    </div>
                </div>
                <div class="row field">
                    <div class="col-md">
                        <input type="submit" class="btn btn-dark pull-right" value="<spring:message code='apply'/>">
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>

<c:if test="${!empty projects}">
    <div class="body">
        <div class="card-deck">
            <c:forEach items="${projects}" var="project" varStatus="projectIndex">
                <%--                <div class="col-sm-3 my-card">--%>
                <div class="card mb-3">
                    <div class="card-header text-white">
                        <div class="row">
                            <div class="col-">
                                <div class="card-title">
                                    <h5><c:out value="${project.name}"/></h5>
                                </div>
                            </div>
                            <div class="col-md">
                                <c:if test="${sessionUser.role eq 2}">
                                <button onclick="favTap(${project.id}, ${projectIndex.index})" class="btn-transp pull-right">
<%--                                    <c:set var="fav" value="${isFav[projectIndex]}"/>--%>
                                    <c:choose>
                                        <c:when test="${isFav[projectIndex.index]}" >
                                            <c:set var="favSrc" value="${favOn}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="favSrc" value="${favOff}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <img id="favImg_${project.id}" src="${favSrc}" class="fav-img" alt="${favSrc}"/>
                                </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-">
                                <img src="<c:url value="/imageController/project/${project.id}"/>" class="proj-img"
                                     alt="<spring:message code="projectImage"/>"
                                     aria-placeholder="<spring:message code="projectImage"/>"/>
                            </div>
                            <div class="col-md">
                                <p class="card-text"><c:out value="${project.summary}"/></p>
                                    <%--                        <strong><spring:message code="categories"/></strong>--%>
                                    <%--                        <c:forEach items="${project.categories}" var="category">--%>
                                    <%--                             <p class="card-text" id="category">-${category.name}</p>--%>
                                    <%--                        </c:forEach>--%>
                                <div class="card-secondary">
                                    <strong><spring:message code="owner"/></strong>
                                    <p><c:out value="${project.owner.firstName}"/> <c:out
                                            value="${project.owner.lastName}"/></p>
                                    <strong><spring:message code="price"/></strong>
                                    <p><c:out value="${project.cost}"/></p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <c:if test="${sessionUser.role != 1}">
                    <div class="card-footer">
                        <a href="<c:url value='/projects/${project.id}'/>"
                           class="btn btn-dark pull-right"><spring:message code="moreinfo"/></a>
                    </div>
                    </c:if>
                </div>

            </c:forEach>
        </div>
    </div>
    <div class="grid">
        <div class="row">
            <div class="col-4  d-flex justify-content-center">
                <c:url value="/projects" var="projectPage"/>
                <form action="${projectPage}" method="get">


                </form>
            </div>
        </div>
    </div>
</c:if>


<c:if test="${empty projects}">
    <div class="card m-2 no-proj">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="noProjFound" arguments=""/></h5>
        </div>
    </div>
</c:if>
</div>


<script>

    var fav = ${isFav};
    // let fav = true;
    <%--            ${isFav};--%>

    function filterList(clickedId) {
        var sel = document.getElementById("category");
        var cat = sel.options[sel.selectedIndex].value;
        console.log(sel.options[sel.selectedIndex].value);
        document.getElementById("test").innerText = $("category");

        //console.log(list.get(0).getName());
        //
        //list.filter(project => "${project.cat}" != cat);
    }

    let options = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    function addFav(p_id) {
        <%--let path = window.location.href.slice(0, window.location.href.lastIndexOf('/')) + "/addFavorite?u_id=" + ${sessionUser.id}+"&p_id="+${project.id};--%>
        <%--let path2 = window.location.href.split('/')[0] + window.location.pathname.split('/')[0] + "/addFavorite?u_id=" + ${sessionUser.id}+"&p_id="+${project.id};--%>
        let path_aux = "${pageContext.request.contextPath}";
        let path = window.location.origin + path_aux + "/addFavorite?u_id=" + ${sessionUser.id}+"&p_id=" + p_id;
        fetch(path, options).catch((function (reason) {
            console.error(reason)
        }));
    }

    function delFav(p_id) {
        let path_aux = "${pageContext.request.contextPath}";
        let path = window.location.origin + path_aux + "/deleteFavorite?u_id=" + ${sessionUser.id}+"&p_id=" + p_id;
        fetch(path, options).catch((function (reason) {
            console.error(reason)
        }));
    }

    function favTap(p_id, index) {
        let pid = "favImg_" + p_id;
        let favImage = document.getElementById(pid);
        if (fav[index]) {
            favImage.setAttribute("src", "${favOff}");
            fav[index] = false;
            delFav(p_id);
        } else {
            favImage.setAttribute("src", "${favOn}");
            fav[index] = true;
            addFav(p_id);
        }
    }

</script>

<script>
    window.onload = function () {
        if (${page} === ${startPage}) {
            document.getElementById("li-previous").className = "page-item disabled";
            document.getElementById("li-a-previous").setAttribute("tabindex", "-1");
            document.getElementById("li-a-previous").setAttribute("aria-disabled", "true");
        } else if (${page} === ${endPage}) {
            document.getElementById("li-next").className = "page-item disabled";
            document.getElementById("li-a-next").setAttribute("tabindex", "-1");
            document.getElementById("li-a-next").setAttribute("aria-disabled", "true");
        }
    }
</script>
</body>
</html>
