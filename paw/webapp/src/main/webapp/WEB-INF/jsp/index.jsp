<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<body>
    <h2>Hello ${user.firstName}!</h2>
    <h5>Your id is ${user.id}</h5>
</body>
<c:forEach items="${cats}" var="category">
    <p>fewqf</p>
</c:forEach>
</html>