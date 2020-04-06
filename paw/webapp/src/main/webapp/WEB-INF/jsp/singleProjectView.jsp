<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>${project.name}</title>
</head>
<body>
    <div class="container">
        <div>
            <div class="d-flex justify-content-start">
                <button class="btn btn-outline-primary" onclick="history.back()">Back</button>
            </div>
            <div class="d-flex justify-content-center">
                <blockquote class="blockquote text-center">
                    <h1>${project.name}</h1>
                    <footer class="blockquote-footer">by ${owner}</footer>
                </blockquote>
            </div>
        </div>
        <h6>ID: ${project.id}</h6>
        <h6>OWNER ID: ${project.ownerId}</h6>
        <p class="m-3 text-justify">${project.summary}</p>
    </div>
</body>
</html>
