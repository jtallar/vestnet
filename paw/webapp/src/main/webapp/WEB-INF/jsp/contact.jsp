<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link rel="stylesheet" href="<c:url value = 'https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css' />" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
</head>
<body>
<h2>Contact Entrepreneur</h2>
    <c:url value="/contact" var="postPath"/>
    <form:form modelAttribute="mailForm" action="${postPath}" method="post">
        <div>
            <form:label path="from"> Your Email: </form:label>
            <form:input path="from"/>
        </div>
        <div>
            <form:label path="subject"> Subject: </form:label>
            <form:input path="subject"/>
        </div>
        <div>
            <form:label path="body">Body: </form:label>
            <form:input path="body" />
        </div>

        <div>
            <input type="submit" value="Send"/>
        </div
    </form:form>
</body>
</html>