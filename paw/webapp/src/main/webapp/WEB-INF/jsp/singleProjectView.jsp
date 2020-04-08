<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>${project.name}</title>
</head>
<body>
    <div class="container" style="margin-top: 20px">
        <div>
            <div class="d-flex justify-content-start">
                <button class="btn btn-dark" onclick="history.back()">Back</button>
            </div>

        <div class="row" style="margin: 20px">
            <div class="col">
                <div id="carouselExampleCaptions" class="carousel slide" data-ride="carousel" style="width: 450px; height:500px; margin: 0 auto">
                    <ol class="carousel-indicators">
                        <li data-target="#carouselExampleCaptions" data-slide-to="0" class="active"></li>
                        <li data-target="#carouselExampleCaptions" data-slide-to="1"></li>
                        <li data-target="#carouselExampleCaptions" data-slide-to="2"></li>
                    </ol>
                    <div class="carousel-inner">
                        <div class="carousel-item active" >
                            <img src="https://www.creativefabrica.com/wp-content/uploads/2020/02/13/Green-Gradient-Background-Graphics-1-1-580x387.jpg" class="d-block w-100" alt="" style="width: 100%; height:100% ">
                            <div class="carousel-caption d-none d-md-block">
                                <p>This is a first view of the web prototype</p>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <img src="https://nesa.com.au/wp-content/uploads/2016/11/Orange-gradient-1.jpg" class="d-block w-100" alt="" style="width: 100%; height:100% ">
                            <div class="carousel-caption d-none d-md-block">
                                <p>Another view of the web prototype</p>
                            </div>
                        </div>
                        <div class="carousel-item">
                            <img src="https://i.pinimg.com/originals/03/6d/a0/036da04d634aadb37d3226f849770712.jpg" class="d-block w-100" alt="" style="width: 100%; height:100% ">
                            <div class="carousel-caption d-none d-md-block">
                                <p>This is how we design de database</p>
                            </div>
                        </div>
                    </div>
                    <a class="carousel-control-prev" href="#carouselExampleCaptions" role="button" data-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="carousel-control-next" href="#carouselExampleCaptions" role="button" data-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>
            </div>
            <div class="col">
                <div class="d-flex justify-content-center">
                    <div class="card mb-3">
                        <%--                    <img src="" class="card-img-top" alt="..." >--%>
                        <div class="card-body">
                            <h5 class="card-title" ><b>${project.name}</b></h5>
                            <footer class="blockquote-footer">by ${owner}</footer>
                            <p class="card-text">${project.summary}</p>
                            <p class="card-text"><small class="text-muted">Last updated 3 mins ago</small></p>
                        </div>
                    </div>
                </div>
            </div>

<%--                <blockquote class="blockquote text-center">--%>
<%--                    <h1>${project.name}</h1>--%>
<%--                    <footer class="blockquote-footer">by ${owner}</footer>--%>
<%--                </blockquote>--%>
            </div>
        </div>
<%--        <h6>ID: ${project.id}</h6>--%>
<%--        <h6>OWNER ID: ${project.ownerId}</h6>--%>
<%--        <p class="m-3 text-justify">${project.summary}</p>--%>
    </div>
</body>
</html>
