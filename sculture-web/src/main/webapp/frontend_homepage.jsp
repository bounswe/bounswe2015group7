!DOCTYPE html>
<html lang="en">

<head>

    <title>Sculture - Add a new story</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="public/js/sweetalert.min.js"></script>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="public/css/bootstrap.min.css">
    <link rel="stylesheet" href="public/css/font-awesome.css">
    <!--<link rel="stylesheet" href="public/css/form-elements.css">-->
    <link rel="stylesheet" href="public/css/sweetalert.css">
    <link rel="stylesheet" href="public/css/style.css">
    <link rel="stylesheet" href="public/css/homepage_style.css">

</head>

<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="/"><img src="public/images/logo.png" style="width:204px;height:58px";></a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="/addstory" data-modal-id="modal-create-story">Add Story</a>
                    </div>
                </li>
                <% boolean isLoggedIn = request.getAttribute(isLoggedIn); %>
                <% if (isLoggedIn) { %>
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="/logout" data-modal-id="modal-logout">Log Out</a>
                    </div>
                </li>
                <% } else { %>
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2 launch-modal" href="#" data-modal-id="modal-login">Sign in</a>
                    </div>
                </li>
                <li>
                    <div class="top-link">
                        <a class="btn btn-link-2 launch-modal" href="#" data-modal-id="modal-register">Sign up</a>
                    </div>
                </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>

<div class="jumbotron text-center">
    <br>
    <% String username = (String)request.getAttribute("username"); %>
    <h1>Sculture!</h1>
    <h3a>Looking good, <%out.print(username);%>!</h3a>
    <form class="form-inline" action="/search" method="post">
        <br> <br>
        <input type="text" name="main-search" id="main-search" class="form-control" size="50" placeholder="Search stories" required>
    </form>
    <br>
    <a class="btn btn-link-2" href="/search/all" data-modal-id="modal-create-story">All stories</a>
    <br>
</div>






<!-- Page Content -->
<div class="container">

    <!-- Portfolio Item Heading -->
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header" align="left">Story Of The Day
            </h1>
        </div>
    </div>
    <!-- /.row -->

    <!-- Portfolio Item Row -->
    <div class="row">

        <div class="col-md-8">
            <% Story topStory = request.getAttribute("topStory");%>

            <img class="img-responsive" src="<%out.print(topStory.mainPhotoUrl);%>" alt="">
        </div>

        <div class="col-md-4">
            <% String title = topStory.title; %>
            <% String content = topStory.content; %>
            <h3><% out.print(title); %></h3>
            <p><% out.print(content); %></p>
        </div>

    </div>
    <!-- /.row -->
    <hr>



    <div class="row">
        <div class="col-lg-12">
            <h3>Popular Stories</h3>
        </div>
    </div>
    <!-- /.row -->

        <% Story[] popular = request.getAttribute("popularStories"); %>

    <!-- Page Features -->
    <div class="row text-center">
        <% for (int i = 0; i < popular.length; i++) { %>
        <div class="col-md-3 col-sm-6 hero-feature">
            <div class="thumbnail">

                <img src="<%out.print(popular[i].mainPhotoUrl);%>" alt="">
                <div class="caption">
                    <% String popularTitle = popular[i].title; %>
                    <h3><% out.print(popularTitle); %></h3>
                    <% String popularContent = popular[i].content; %>
                    <p><% out.print(popularContent); %></p>
                    <p>
                        <a href="#" class="btn btn-primary">Read More</a>
                    </p>
                </div>
            </div>
        </div>
        <% } %>
    </div>













    <div id="contact" class="container-fluid bg-grey">
        <p><span class="glyphicon glyphicon-map-marker"></span> Istanbul, TR</p>
        <p><span class="glyphicon glyphicon-phone"></span> +90 212 359 54 00</p>
        <p><span class="glyphicon glyphicon-envelope"></span> info@sculture.com</p>
    </div>
