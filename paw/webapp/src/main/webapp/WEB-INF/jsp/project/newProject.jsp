<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="../components/header.jsp" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="<c:url value="/css/form.css"/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><spring:message code="page.title.newProject"/></title>
</head>

<%-- Set used URLs --%>
<c:url var="link_post_project" value="/newProject"/>

<body>

<%-- New project form completition --%>
<div class="container" style="margin-top: 20px">
    <div class="text-center">
        <h1><spring:message code="createProjectTitle"/></h1>
        <h6><spring:message code="createProjectSubtitle"/></h6>
    </div>
    <div class="dropdown-divider"></div>
    <form:form modelAttribute="newProjectForm" action="${link_post_project}" method="post" enctype="multipart/form-data">
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
                <div class="row justify-content-center">
                    <div class="col-2">
                        <label><spring:message code="currency"/></label>
                    </div>
                    <div class="col">
                        <form:input path="cost" type="number" class="form-control" cssClass="custom-form-number" id="new-project-cost"/>
                    </div>
                </div>
                <form:errors path="cost" cssClass="formError" element="p"/>
            </div>
            <div class="d-flex flex-column">
            </div>
        </div>

        <%-- Categories Selection --%>
        <h4 class="mt-3"><spring:message code="categories"/></h4>
        <h7><spring:message code="projectCategoriesSubtitle"/></h7>
        <div class="dropdown-divider"></div>
        <div class="d-flex justify-content-around flex-row">
            <div class="d-flex flex-column form-group">
                <label><spring:message code="all-categories"/></label>
                <select id="all-categories" class="custom-form-select mr-sm-2" size="10">
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.id}" selected="selected">
                            <spring:message code="${category.name}"/>
                        </option>
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

        <h5 class="project-image-header"><spring:message code="projectImage"/></h5>
        <p class="form-requirement"><spring:message code="pictureRequirement"/></p>
        <form:input path="portraitImage" type="file" class="form-control" cssClass="custom-form-file" id="customFileProjectPic"/>
        <form:errors path="portraitImage" cssClass="formError" element="p" id="fileErrorFormTag"/>
        <label class="formError" id="maxSizeErrorMsg" hidden><spring:message code="imageMaxSize"/></label>

        <h5 class="project-image-header"><spring:message code="project.slideshow"/></h5>
        <p class="form-requirement"><spring:message code="picturesRequirement"/></p>
        <form:input path="slideshowImages" type="file" class="form-control" cssClass="custom-form-file" id="customMultipleFileProjectPic" multiple="true"/>
        <form:errors path="slideshowImages" cssClass="formError" element="p" id="multipleFileErrorFormTag"/>
        <label class="formError" id="maxSizeErrorMsgMultiple" hidden><spring:message code="imageMaxSize"/></label>
        <label class="formError" id="maxCountErrorMsgMultiple" hidden><spring:message code="imageMaxCount"/></label>

        <div class="text-right">
            <input type="submit" value="<spring:message code="create"/>" class="btn btn-dark" onclick="adjustInputs()"/>
        </div
    </form:form>
</div>
</body>
<script>
    let fileBox = document.getElementById('customFileProjectPic');
    let maxSizeMsg = document.getElementById('maxSizeErrorMsg');
    let errorTag = document.getElementById('fileErrorFormTag');
    fileBox.addEventListener("change", function () {
        if (!(fileBox.files.length === 0) && fileBox.files[0].size >= ${maxSize}) {
            fileBox.value = null;
            if (errorTag != null) {
                errorTag.hidden = true;
            }
            maxSizeMsg.hidden = false;
        } else {
            maxSizeMsg.hidden = true;
        }
    });

    let multipleFileBox = document.getElementById('customMultipleFileProjectPic');
    let maxSizeMsgMultiple = document.getElementById('maxSizeErrorMsgMultiple');
    let maxCountMsgMultiple = document.getElementById('maxCountErrorMsgMultiple');
    let errorTagMultiple = document.getElementById('multipleFileErrorFormTag');
    multipleFileBox.addEventListener("change", function () {
        let index = 0, error = 0;
        if (multipleFileBox.files.length > ${maxSlideshowCount}) {
            error = 1;
            maxSizeMsgMultiple.hidden = true;
            maxCountMsgMultiple.hidden = false;
        } else if (!(multipleFileBox.files.length === 0)) {
            for (index = 0; index < multipleFileBox.files.length; index++) {
                if (multipleFileBox.files[index].size >= ${maxSize}) {
                    error = 1;
                    maxSizeMsgMultiple.hidden = false;
                    maxCountMsgMultiple.hidden = true;
                    break;
                }
            }
        }
        if (error === 0) {
            maxSizeMsgMultiple.hidden = true;
            maxCountMsgMultiple.hidden = true;
        } else {
            multipleFileBox.value = null;
            if (errorTagMultiple != null) {
                errorTagMultiple.hidden = true;
            }
        }
    });

    let costTag = document.getElementById('new-project-cost');
    costTag.addEventListener("keypress", function () {
        if (costTag.value.length > 6) {
            costTag.value = costTag.value.slice(0, 6);
        }
    });

    function addCategory() {
        let cat = document.getElementById("all-categories");
        if (cat.selectedIndex !== -1)
            document.getElementById("final-categories").appendChild(cat.options[cat.selectedIndex]);
    }

    function delCategory() {
        let cat = document.getElementById("final-categories");
        if (cat.selectedIndex !== -1)
            document.getElementById("all-categories").appendChild(cat.options[cat.selectedIndex]);
    }

    function addCategories() {
        let cat = document.getElementById("final-categories");
        for (let i = 0; i < cat.options.length; i++)
            cat[i].selected = true;
    }

    function adjustInputs() {
        addCategories();
        let titleTag = document.getElementById('new-project-title');
        titleTag.value = titleTag.value.trim();
        let summaryTag = document.getElementById('new-project-summary');
        summaryTag.value = summaryTag.value.trim();
        if (costTag.value.length === 0 || costTag.value < 0) {
            costTag.value = 0;
        }
        costTag.value = Math.round(costTag.value);
    }
</script>
</html>
