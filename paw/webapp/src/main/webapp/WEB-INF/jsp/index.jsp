<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file = "header.jsp" %>

<html>
<body>
    <h2>Hello ${user.firstName}!</h2>
    <h5>Your id is ${user.id}</h5>
</body>
<c:forEach items="${cats}" var="category">
    <p><c:out value="${category.name}"/></p>
</c:forEach>
</html>