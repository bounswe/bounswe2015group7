<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath =request.getContextPath();
%>
<html lang="en">

<head>

    <title>Sculture - Add a new story</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="http://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/sweetalert.min.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/scripts.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/bootstrap.min.js"></script>
    <script src="<%out.print(contextPath);%>/public/js/jquery.backstretch.min.js"></script>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/sweetalert.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/style.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/homepage_style.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/font-awesome.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/form-elements.css">

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
            <a href="<%out.print(contextPath);%>/index"><img src="<%out.print(contextPath);%>/public/images/logo.png" style="width:204px;height:58px" ;></a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <% boolean isLoggedIn;
                    try {
                        isLoggedIn = (Boolean) request.getAttribute("isLoggedIn");
                    } catch(Exception ex) {
                        isLoggedIn = false;
                    }
                %>
                <% if (isLoggedIn) { %>
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="<%out.print(contextPath);%>/addstory">Add Story</a>
                    </div>
                </li>
                <li>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="<%out.print(contextPath);%>/logout">Log Out</a>
                    </div>
                </li>
                <li>
                    <%String refUrl = contextPath + "/get/user/" + request.getSession().getAttribute("userid");%>
                    <div class="top-big-link">
                        <a class="btn btn-link-2" href="<%out.print(refUrl);%>" data-modal-id="modal-logout">My
                            Profile</a>
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
    <h1>Sculture!</h1> <br>
    <h3a>Whoops :(</h3a>
</div>

<div class="container">
    <div class="row">
        <%String errorMessage = (String) request.getAttribute("errormsg");%>
        <img src="http://images.clipartpanda.com/moderation-clipart-zyToK4GiE.jpeg" width="100" height="100">
        <div><p><%out.print(errorMessage);%> </p></div> <br>
        <a href="<%out.print(contextPath);%>/index" type="button" class="btn btn-link-1" style="height:50px;width:300px"> Home </a>

    </div>
</div>
<br>
<br><br>
<!-- LOGIN -->
<div class="modal fade" id="modal-login" tabindex="-1" role="dialog" aria-labelledby="modal-login-label" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h3 class="modal-title" id="modal-login-label">Sign in to Sculture</h3>
                <p>Enter your username and password to sign in:</p>
            </div>

            <div class="modal-body">

                <form role="form" action="<%out.print(contextPath);%>/login" method="post" class="login-form">
                    <div class="form-group">
                        <label class="sr-only" for="form-email">E-mail</label>
                        <input type="text" name="form-email" placeholder="Email..." class="form-email form-control" id="form-email">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-password">Password</label>
                        <input type="password" name="form-password" placeholder="Password..." class="form-password form-control" id="form-password">
                    </div>
                    <button type="submit" class="btn">Sign in!</button>
                    <div style="text-align: center;">
                        <br> Or sign in using: <br>
                        <a class="btn btn-link-1" href="#">
                            <i class="fa fa-facebook"></i> Facebook
                        </a>
                    </div>
                </form>

            </div>

        </div>
    </div>
</div>

<!-- REGISTER -->
<div class="modal fade" id="modal-register" tabindex="-1" role="dialog" aria-labelledby="modal-register-label" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h3 class="modal-title" id="modal-register-label">Register to Sculture</h3>
                <p>Fill out the fields below:</p>
            </div>

            <div class="modal-body">

                <form role="form" action="<%out.print(contextPath);%>/signup" method="post" class="register-form">
                    <div class="form-group">
                        <label class="sr-only" for="form-email">E-mail</label>
                        <input type="text" name="form-email" placeholder="Enter your email" class="form-email form-control" id="form-email">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-username">Username</label>
                        <input type="text" name="form-username" placeholder="Enter your username" class="form-bane form-control" id="form-username">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-password">Password</label>
                        <input type="password" name="form-password" placeholder="Enter your password" class="form-password form-control" id="form-password">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-retypedpassword">Password</label>
                        <input type="password" name="form-retypedpassword" placeholder="Retype your password..." class="form-retypedpassword form-control" id="form-retypedpassword">
                    </div>
                    <button type="submit" class="btn">Sign up!</button>
                    <div style="text-align: center;">
                        <br> Or sign up using: <br>
                        <a class="btn btn-link-1" href="#">
                            <span><i class="fa fa-facebook"></i> Facebook</span>
                        </a>
                    </div>
                </form>

            </div>

        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        // Add smooth scrolling to all links in navbar + footer link
        $(".navbar a, footer a[href='#myPage']").on('click', function(event) {

            // Prevent default anchor click behavior
            //   event.preventDefault();

            // Store hash
            var hash = this.hash;

            // Using jQuery's animate() method to add smooth page scroll
            // The optional number (900) specifies the number of milliseconds it takes to scroll to the specified area
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 900, function(){

                // Add hash (#) to URL when done scrolling (default click behavior)
                window.location.hash = hash;
            });
        });

        // Slide in elements on scroll
        $(window).scroll(function() {
            $(".slideanim").each(function(){
                var pos = $(this).offset().top;

                var winTop = $(window).scrollTop();
                if (pos < winTop + 600) {
                    $(this).addClass("slide");
                }
            });
        });
    })
</script>
<div id="contact" class="container-fluid bg-grey">
    <p><span class="glyphicon glyphicon-map-marker"></span> Istanbul, TR</p>

    <p><span class="glyphicon glyphicon-phone"></span> +90 212 359 54 00</p>

    <p><span class="glyphicon glyphicon-envelope"></span> info@sculture.com</p>
</div>

