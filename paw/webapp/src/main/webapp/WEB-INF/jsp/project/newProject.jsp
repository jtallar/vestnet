<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file = "../components/header.jsp" %>

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
    <link rel="stylesheet" href="<c:url value="/css/form.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>New Project | VestNet</title>
</head>
<body>
<div class="container" style="margin-top: 20px">
    <div class="text-center">
        <h1><spring:message code="createProjectTitle"/></h1>
        <h6><spring:message code="createProjectSubtitle"/></h6>
    </div>
    <div class="dropdown-divider"></div>
    <c:url value="/newProject" var="postPath"/>
    <form:form modelAttribute="newProjectForm" action="${postPath}" method="post" enctype="multipart/form-data">
        <div class="d-flex justify-content-between flex-row mt-1">
            <div class="d-flex flex-column">
                <h4><spring:message code="projectOverviewTitle"/></h4>
                <h7><spring:message code="projectOverviewSubtitle"/></h7>
                <div class="dropdown-divider"></div>

                <h6 class="form-label"><spring:message code="title_required"/></h6>
                <p class="form-requirement"><spring:message code="titleRequirement"/></p>
                <spring:message code="ProjectTitlePlaceholder" var="titlePlaceholder"/>
                <form:input path="title" type="text" class="form-control" placeholder="${titlePlaceholder}" cssClass="custom-form-input" id="new-project-title"/>
                <form:errors path="title" cssClass="formError" element="p"/>

                <h6 class="form-label"><spring:message code="summary_required"/></h6>
                <p class="form-requirement"><spring:message code="descriptionRequirement"/></p>
                <spring:message code="ProjectSummaryPlaceholder" var="summaryPlaceholder"/>
                <form:textarea path="summary" type="text" class="form-control" placeholder="${summaryPlaceholder}" cssClass="custom-form-input" rows="4" id="new-project-summary"/>
                <form:errors path="summary" cssClass="formError" element="p"/>

                <h6 class="form-label"><spring:message code="cost_required"/></h6>
                <p class="form-requirement"><spring:message code="costRequirement"/></p>
<%--                TODO: ALIGN $USD WITH INPUT--%>
                <div class="row justify-content-center">
                    <div class="col-1">
                        <label><spring:message code="currency"/></label>
                    </div>
                    <div class="col">
                        <form:input path="cost" type="number" class="form-control" cssClass="custom-form-number" id="new-project-cost"/>
                    </div>
                </div>
                <form:errors path="cost" cssClass="formError" element="p"/>
            </div>
            <div class="d-flex flex-column">
                <%--TODO Project Showcase (images and videos)--%>
            </div>

        </div>

        <%-- CATEGORIES --%>
        <h4 class="mt-3"><spring:message code="categories"/></h4>
        <h7><spring:message code="projectCategoriesSubtitle"/></h7>
        <div class="dropdown-divider"></div>
        <div class="d-flex justify-content-around flex-row">
            <div class="d-flex flex-column form-group">
                <label><spring:message code="all-categories"/></label>
                <select id="all-categories" class="custom-form-select mr-sm-2" size="10">
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.id}" selected="selected"><spring:message code="${category.name}"/> </option>
                    </c:forEach>
                </select>
            </div>
            <div class="d-flex justify-content-around flex-column ">
                <button type="button" class="btn btn-dark" onclick="addCategory()">>></button>
                <button type="button" class="btn btn-dark" onclick="delCategory()"><<</button>
            </div>
            <div class="d-flex flex-column form-group">
                <label><spring:message code="my-categories"/> </label>
                <form:select path="categories" id="final-categories" class="custom-form-select mr-sm-2" size="10"/>
                <form:errors path="categories" cssClass="formError" element="p"/>
            </div>
        </div>

        <h5><spring:message code="projectImage"/></h5>
        <form:input path="image" type="file" class="form-control" cssClass="custom-form-file" id="customFileProjectPic"/>
        <form:errors path="image" cssClass="formError" element="p" id="fileErrorFormTag"/>
        <label class="formError" id="maxSizeErrorMsg" hidden><spring:message code="imageMaxSize"/></label>
        <div class="text-right">
            <input type="submit" value="<spring:message code="create"/>" class="btn btn-dark" onclick="adjustInputs()"/>
        </div
    </form:form>
</div>
</body>
<script>
    var fileBox = document.getElementById('customFileProjectPic');
    var maxSizeMsg = document.getElementById('maxSizeErrorMsg');
    var errorTag = document.getElementById('fileErrorFormTag');
    fileBox.addEventListener("change", function () {
        if (fileBox.files[0].size >= ${maxSize}) {
            fileBox.value = null;
            if (errorTag != null) {
                errorTag.hidden = true;
            }
            maxSizeMsg.hidden = false;
        } else {
            maxSizeMsg.hidden = true;
        }
    });

    var costTag = document.getElementById('new-project-cost');
    costTag.addEventListener("keypress", function () {
        if (costTag.value.length > 6) {
            costTag.value = costTag.value.slice(0, 6);
        }
    });
    function addCategory() {
        var cat = document.getElementById("all-categories");
        if (cat.selectedIndex !== -1)
            document.getElementById("final-categories").appendChild(cat.options[cat.selectedIndex]);
    }
    function delCategory() {
        var cat = document.getElementById("final-categories");
        if (cat.selectedIndex !== -1)
            document.getElementById("all-categories").appendChild(cat.options[cat.selectedIndex]);
    }
    function addCategories() {
        var cat = document.getElementById("final-categories");
        for (var i = 0; i < cat.options.length; i++)
            cat[i].selected = true;
    }
    function adjustInputs() {
        addCategories();
        var titleTag = document.getElementById('new-project-title');
        titleTag.value = titleTag.value.trim();
        var summaryTag = document.getElementById('new-project-summary');
        summaryTag.value = summaryTag.value.trim();
        if (costTag.value.length === 0 || costTag.value < 0) {
            costTag.value = 0;
        }
        costTag.value = Math.round(costTag.value);
    }
</script>
</html>
