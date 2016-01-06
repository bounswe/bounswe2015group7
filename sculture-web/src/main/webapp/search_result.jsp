<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="com.sculture.model.response.StoriesResponse" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.sculture.model.response.StoryResponse" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sculture.Const" %>
<%
    String contextPath =request.getContextPath();
%>
<html lang="en">

<head>

    <title>Sculture - share your culture!</title>
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
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/font-awesome.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/form-elements.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/sweetalert.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/style.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/homepage_style.css">
    <link rel="stylesheet" href="<%out.print(contextPath);%>/public/css/searchstyle.css">


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
                <% boolean isLoggedIn = (Boolean) request.getAttribute("isLoggedIn"); %>
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
    <% String username = (String) request.getAttribute("username"); %>
    <h1>Sculture!</h1>
    <h3a>Looking good, <%out.print(username);%>!</h3a>
    <form class="form-inline" action="<%out.print(contextPath);%>/search" method="post">
        <br> <br>
        <input type="text" name="main-search" id="main-search" class="form-control" size="50"
               placeholder="Search stories" required>
    </form>
    <br>
    <a class="btn btn-link-2" href="<%out.print(contextPath);%>/search/all/1" data-modal-id="modal-create-story">All stories</a>
    <br>
    <br> <br>
</div>

<%-- here we assumed that there is a Story class with attributes title, content, tags and date created  --%>
<%
    List<StoryResponse> results = ((StoriesResponse) request.getAttribute("results")).getResult();

%>
<h1>We found <%out.print(results.size()); %> results for your search</h1>
<br>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="panel">
                <div class="panel-body">
                    <!--/stories-->
                    <% for (int i = 0; i < results.size(); i++) { %>

                    <div class="row">
                        <br>

                        <div class="col-md-2 col-sm-4 text-center">
                            <a class="story-title" href="#">
                                <% try { %>
                                <%if (results.get(i).getMedia() != null) { %>
                                <img style="width: 300px; height: 250px"
                                     src="<%out.print(Const.REST_BASE_URL + Const.Api.IMAGE_GET + results.get(i).getMedia().get(0));%>"
                                     alt="">
                                <% } %>
                                <%} catch (Exception e) {%>
                                <img style="width: 250px; height: 300px"
                                     src="http://en.mladinsko.com/images/emptyMME.gif" alt="">
                                <% }%>
                            </a>
                        </div>
                        <div class="col-md-10 col-sm-9">
                            <h3 align="left"><a href="#"></a> <% out.print(results.get(i).getTitle());%></h3>

                            <div class="row">
                                <div class="col-xs-9">
                                    <p><%

                                        String content = results.get(i).getContent();
                                        if (content.length() < 500) {
                                            out.print(content);
                                        } else {
                                            out.print(content.substring(0, 500) + "...");
                                        }
                                    %></p>
                                    <small style="font-family:courier,'new courier';" class="text-muted">
                                        <%
                                            Date storyCreationDate = results.get(i).getCreation_date();
                                            out.print(storyCreationDate);%> •
                                        <% String refUrl = contextPath + "/get/story/" + results.get(i).getId();%>
                                        <a href="<%out.print(refUrl);%>"> Read More</a></small>
                                </div>
                                <div class="col-xs-3"></div>
                            </div>
                            <br><br>
                        </div>
                    </div>
                    <hr>


                    <% } %>
                    <%String requrl = request.getAttribute("javax.servlet.forward.request_uri").toString();

                    if (requrl.contains("all")){%>
                    <div class="col-md-1">
                        <%  int pageNum = Integer.parseInt(requrl.substring(requrl.lastIndexOf('/')+1));
                            int left = pageNum - 1;
                            int right = pageNum + 1;
                            String leftUrl = contextPath + "/search/all/" + left;
                            String rightUrl = contextPath + "/search/all/" + right;
                        %>
                        <%if(left != 0) {%>
                        <a href="<%out.print(leftUrl);%>" class="btn btn-primary pull-right btnNext"><i
                                class="glyphicon glyphicon-chevron-left" style="align-self: flex-start"></i></a>
                        <%}%>
                    </div>

                    <!--/stories-->
                    <%if(results.size() != 0 && results.size() >= 10) {%>
                    <a href="<%out.print(rightUrl);%>" class="btn btn-primary pull-right btnNext"> <i
                            class="glyphicon glyphicon-chevron-right"></i></a>
                    <%} }%>
                </div>
            </div>
        </div>
        <!--/col-12-->
    </div>
</div>


<div id="contact" class="container-fluid bg-grey">
    <p><span class="glyphicon glyphicon-map-marker"></span> Istanbul, TR</p>

    <p><span class="glyphicon glyphicon-phone"></span> +90 212 359 54 00</p>

    <p><span class="glyphicon glyphicon-envelope"></span> info@sculture.com</p>
</div>

<!-- LOGIN -->
<div class="modal fade" id="modal-login" tabindex="-1" role="dialog" aria-labelledby="modal-login-label"
     aria-hidden="true">
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
                        <input type="password" name="form-password" placeholder="Password..."
                               class="form-password form-control" id="form-password">
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
<div class="modal fade" id="modal-register" tabindex="-1" role="dialog" aria-labelledby="modal-register-label"
     aria-hidden="true">
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
                        <input type="text" name="form-email" placeholder="Enter your email"
                               class="form-email form-control" id="form-email">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-username">Username</label>
                        <input type="text" name="form-username" placeholder="Enter your username"
                               class="form-bane form-control" id="form-username">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-password">Password</label>
                        <input type="password" name="form-password" placeholder="Enter your password"
                               class="form-password form-control" id="form-password">
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="form-retypedpassword">Password</label>
                        <input type="password" name="form-retypedpassword" placeholder="Retype your password..."
                               class="form-retypedpassword form-control" id="form-retypedpassword">
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
<!-- Javascript -->
<script src="public/js/jquery-1.11.1.min.js"></script>
<script src="public/js/bootstrap.min.js"></script>
<script src="public/js/jquery.backstretch.min.js"></script>
<script src="public/js/scripts.js"></script>
<script>
    $(document).ready(function () {
        // Add smooth scrolling to all links in navbar + footer link
        $(".navbar a, footer a[href='#myPage']").on('click', function (event) {

            // Prevent default anchor click behavior
            //   event.preventDefault();

            // Store hash
            var hash = this.hash;

            // Using jQuery's animate() method to add smooth page scroll
            // The optional number (900) specifies the number of milliseconds it takes to scroll to the specified area
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 900, function () {

                // Add hash (#) to URL when done scrolling (default click behavior)
                window.location.hash = hash;
            });
        });

        // Slide in elements on scroll
        $(window).scroll(function () {
            $(".slideanim").each(function () {
                var pos = $(this).offset().top;

                var winTop = $(window).scrollTop();
                if (pos < winTop + 600) {
                    $(this).addClass("slide");
                }
            });
        });
    })
</script>
</body>

</html>