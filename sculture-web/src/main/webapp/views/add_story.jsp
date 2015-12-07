<!DOCTYPE html>
 <%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
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
                           <% boolean isLoggedIn = request.getAttribute("isLoggedIn"); %>
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
 <% String username = request.getAttribute("username"); %>
    <h1>Sculture!</h1>
    <h3a>Looking good, <%out.print(username);%>!</h3a>
</div>

<div class="container">
    <div class="row">
        <form role="form" method="post" action="/{{credentials.objectId}}/addstory">
            <div class="col-md-8 col-md-offset-2">
                <div class="form-group">
                    <label for="story-title">Title</label>
                    <div class="input-group">
                        <input type="text" class="form-control" name="story-title" id="story-title" placeholder="Enter title" required>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="story-content">Content</label>
                    <div class="input-group">
                        <textarea name="story-content" id="story-content" class="form-control" placeholder="Enter your story" rows="5" required></textarea>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
                    </div>
                </div>

                <div class="form-group">
                    <label for="story-tags">Tags</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="story-tags" name="story-tags" placeholder="Seperate tags by whitespace">
                        <span class="input-group-addon"></span>
                    </div>
                </div>

                <div container>
                    Fields with * are required
                    <input type="submit" name="submit" id="submit" value="Submit" class="btn btn-info pull-right">
                </div>

            </div>
        </form>
    </div>
</div>
<br>
<br><br>


<div id="contact" class="container-fluid bg-grey">
    <p><span class="glyphicon glyphicon-map-marker"></span> Istanbul, TR</p>
    <p><span class="glyphicon glyphicon-phone"></span> +90 212 359 54 00</p>
    <p><span class="glyphicon glyphicon-envelope"></span> info@sculture.com</p>
</div>

