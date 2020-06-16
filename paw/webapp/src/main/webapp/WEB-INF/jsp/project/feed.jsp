<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ include file="../components/header.jsp" %>

<html>
<head>
    <link rel="stylesheet"
          href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value="/css/feed.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/form.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><spring:message code="page.title.feed"/></title>
</head>

<%-- Set used variables --%>
<c:set var="searchButtonClass" value="btn btn-black"/>
<sec:authorize access="isAuthenticated()">
    <sec:authentication var="session_user_id" property="principal.id"/>
</sec:authorize>
<c:set var="projects" value="${projectPage.content}"/>
<c:set var="page" value="${projectPage.currentPage}"/>
<c:set var="startPage" value="${projectPage.startPage}"/>
<c:set var="endPage" value="${projectPage.endPage}"/>

<%-- Set used URLs --%>
<c:url var="icon_fav_off" value="/images/bookmarkOffB.png"/>
<c:url var="icon_fav_on" value="/images/bookmarkOnB.png"/>
<c:url var="icon_order" value="/images/order.png"/>
<c:url var="icon_filter" value="/images/filter.png"/>
<c:url var="icon_search" value="/images/lupa_bv.png"/>
<c:url var="link_projects" value='/projects'/>
<c:url var="link_delete_fav" value="/deleteFavorite"/>
<c:url var="link_add_fav" value="/addFavorite"/>

<body>

<%-- Project pagniation --%>
<div class="row">
    <div class="col-1"></div>
    <div class="col-11">
        <ul class="pagination justify-content-center">
            <li id="li-previous" class="page-item">
                <a id="li-a-previous" class="page-link" onclick="modHref(${page-1})" aria-label="<spring:message code="previous"/>">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <c:forEach var="pageNumber" begin="${startPage}" end="${endPage}">
                <li class="page-item <c:if test="${pageNumber == page }"> active-item </c:if>">
                    <a class="page-link" onclick="modHref(${pageNumber})">${pageNumber}</a>
                </li>
            </c:forEach>
            <li id="li-next" class="page-item">
                <a id="li-a-next" class="page-link" onclick="modHref(${page+1})" aria-label="<spring:message code="next"/>">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </div>
<%--    <div class="col-1"></div>--%>
</div>

<%-- Side Navigation Bar --%>
<div class="sidenav">
    <div class="form-row align-items-center">
                <%-- Filter Form --%>
        <form:form modelAttribute="filter" method="GET" action="${link_projects}">
            <div class="container">

                <%-- Search --%>
                <div class="row field">
                    <div class="col-">
                        <img src="${icon_search}" class="icon-img">
                    </div>
                    <div class="col-md">
                        <spring:message var="search" code="search"/>
                        <form:input path="keyword" class="form-control mr-sm-2" type="text" placeholder="${search}" aria-label="Search"/>
                        <form:errors path="keyword" element="p" cssClass="formError"/>
                    </div>
                </div>
                <div class="row field">
                    <div class="col- icon-img"></div>
                    <div class="col-md">
                        <form:select path="field" class="custom-select mr-sm-2">
                            <c:forEach items="${fieldValues}" var="item">
                                <form:option value="${item.value}"><spring:message code="feed.${item.message}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>

                <div class="dropdown-divider"></div>

                <div class="row field">
                    <div class="col-">
                        <img src="${icon_filter}" class="icon-img">
                    </div>
                    <div class="col-md">
                        <form:select class="custom-select mr-sm-2" path="category">
                            <form:option value=""><spring:message code="noFilter"/> </form:option>
                            <c:forEach items="${categories}" var="category">
                                <form:option value="${category.id}">
                                    <spring:message code="${category.name}"/>
                                </form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="row field">
                    <div class="col-">
                        <img src="${icon_order}" class="icon-img">
                    </div>
                    <div class="col-md">
                        <form:select path="order" class="custom-select mr-sm-2">
                            <c:forEach items="${orderValues}" var="item">
                                <form:option value="${item.value}"><spring:message code="feed.${item.message}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="row field">
                    <label class="range"><spring:message code="range"/> </label>
                    <div>
                        <div class="row range-box col-md">
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><spring:message code="min"/></span>
                                </div>
                                <form:input path="minCost" type="number" class="form-control mx-auto mx-auto" placeholder="100" id="filter-form-min"/>
                            </div>
                            <form:errors path="minCost" element="p" cssClass="formError"/>
                        </div>
                        <div class="row range-box col-md" >
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><spring:message code="max"/></span>
                                </div>
                                <form:input path="maxCost" type="number" class="form-control mx-auto mx-auto" placeholder="100000"
                                            id="filter-form-max"/>
                            </div>
                            <form:errors path="maxCost" element="p" cssClass="formError"/>
                            <form:errors cssClass="formError" element="p"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="apply-btn">
                        <input type="submit" class="btn btn-dark pull-right" value="<spring:message code='apply'/>" onclick="adjustInputs()">
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>

<%-- PROJECTS --%>
<c:if test="${!empty projects}">
    <div class="body">
        <div class="card-deck">
            <c:forEach items="${projects}" var="project" varStatus="projectIndex">
                <div class="card mb-3">
                    <div class="card-header text-white">
                        <div class="row icon-fav">
                            <div class="col-10">
                                <div class="card-title">
                                    <h5><c:out value="${project.name}"/></h5>
                                </div>
                            </div>
                            <div class="col-2">
                                <sec:authorize access="hasRole('ROLE_INVESTOR')">
                                    <button onclick="favTap(${project.id})"
                                            class="btn-transp">
                                        <c:choose>
                                            <c:when test="${user.favorites.contains(project)}">
                                                <c:set var="icon_fav" value="${icon_fav_on}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="icon_fav" value="${icon_fav_off}"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <img id="favImg_${project.id}" src="${icon_fav}" class="icon-img" alt="${icon_fav}"/>
                                    </button>
                                </sec:authorize>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-5">
                                <img src="<c:url value="/imageController/project/${project.id}"/>" class="proj-img"
                                     alt="<spring:message code="projectImage"/>"
                                     aria-placeholder="<spring:message code="projectImage"/>"/>
                            </div>
                            <div class="col-7 card-content">
                                <p class="card-text"><c:out value="${project.summary}"/></p>
<%--                                <div class="card-secondary">--%>
<%--                                    <strong><spring:message code="price"/></strong>--%>
<%--                                    <spring:message code="project.cost" arguments="${project.cost}" var="costVar"/>--%>
<%--                                    <p><c:out value="${costVar}"/></p>--%>
<%--                                </div>--%>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer">
                        <div class="row">
                            <div class="price-footer col-6">
                                <strong><spring:message code="price"/></strong>
                                <spring:message code="project.cost" arguments="${project.cost}" var="costVar"/>
                                <p><c:out value="${costVar}"/></p>
                            </div>
                            <div class="col-6">
                                <a href="<c:url value='/projects/${project.id}'/>" class="btn btn-dark pull-right"><spring:message code="moreinfo"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</c:if>

<%-- EMPTY PROJECT CARD --%>
<c:if test="${empty projects}">
    <div class="card m-2 no-proj">
        <div class="card-header">
            <h5 class="card-title text-white centered"><spring:message code="noProjFound"/></h5>
        </div>
    </div>
</c:if>

<script>


    let options = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    function addFav(p_id) {
        let path = '${link_add_fav}' + "?u_id=" + ${session_user_id} + "&p_id=" + p_id;
        fetch(path, options).catch((function (reason) {
            console.error(reason)
        }));
    }

    function delFav(p_id) {
        let path = '${link_delete_fav}' + "?u_id=" + ${session_user_id} + "&p_id=" + p_id;
        fetch(path, options).catch((function (reason) {
            console.error(reason)
        }));
    }

    function favTap(p_id) {
        let pid = "favImg_" + p_id;
        let favImage = document.getElementById(pid);
        if (favImage.getAttribute("src") === "${icon_fav_on}") {
            favImage.setAttribute("src", "${icon_fav_off}");
            delFav(p_id);
        } else {
            favImage.setAttribute("src", "${icon_fav_on}");
            addFav(p_id);
        }
    }

</script>

<script>
    window.onload = function () {
        disableArrows(${page}, ${startPage}, "previous");
        disableArrows(${page}, ${endPage}, "next");
    }

    function disableArrows(page, limitPage, name) {
        if (page === limitPage) {
            document.getElementById("li-" + name).className = "page-item disabled";
            document.getElementById("li-a-" + name).setAttribute("tabindex", "-1");
            document.getElementById("li-a-" + name).setAttribute("aria-disabled", "true");
        }
    }

    function modHref(page) {
        let url = window.location.href;
        if (!window.location.search.includes("?")) window.location.href = url + "?page=" + page;
        else if (window.location.search.includes("page")) window.location.href = url.replace(/page=[0-9]*/, "page=" + page);
        else window.location.href = url + "&page=" + page;
    }

    function adjustInputs() {
        let minTag = document.getElementById('filter-form-min');
        if (minTag.value < 0 || minTag.value > 9999999) {
            minTag.value = '';
        }
        if (minTag.value.length) minTag.value = Math.round(minTag.value);
        let maxTag = document.getElementById('filter-form-max');
        if (maxTag.value < 0 || maxTag.value > 9999999) {
            maxTag.value = '';
        }
        if (maxTag.value.length) maxTag.value = Math.round(maxTag.value);
    }
</script>
</body>
</html>
