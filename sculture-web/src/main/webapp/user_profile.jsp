<%--
  Created by IntelliJ IDEA.
  User: Atakan Arıkan
  Date: 07.12.2015
  Time: 23:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>

  <title>Sculture - share your culture!</title>
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
  <link rel="stylesheet" href="/public/css/bootstrap.min.css">
  <link rel="stylesheet" href="/public/css/font-awesome.css">
  <link rel="stylesheet" href="/public/css/form-elements.css">
  <link rel="stylesheet" href="/public/css/sweetalert.css">
  <link rel="stylesheet" href="/public/css/style.css">
  <link rel="stylesheet" href="/public/css/homepage_style.css">
  <link rel="stylesheet" href="/public/css/storystyle.css">


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
      <a href="/index"><img src="/public/images/logo.png" style="width:204px;height:58px"></a>
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


<div class="container">

  <hr class="">
  <div class="container target" align="left">
    <div class="row">
      <div class="col-sm-10">
        <% User user = request.getAttribute("relatedUser");%>
        <h1 class=""><% out.print(user.username);%></h1>
        <% out.print("<button href=\"" + user.followURL + "\"type=\"button\" class=\"btn btn-success\" style=\"height:50px;width:300px\"> Follow </button>");%>
        <br><br>


      </div>

      <div class="col-sm-2" align="right"><a href="#" class="pull-left">
        <% out.print("<img class=\"img-circle img-responsive\" src=\"" + user.mainPhotoUrl + "\" align=\"left\" alt=\"\">");%>
      </div>
    </div>
    <br>
    <div class="row">
      <div class="col-sm-3">
        <!--left col-->
        <div class="panel panel-default">
          <ul class="list-group">
            <div class="panel-heading">Profile</div>
            <li class="list-group-item text-right"><span class="pull-left"><strong class="">Joined</strong></span> <%out.print(user.createdAt)%></li>
            <li class="list-group-item text-right"><span class="pull-left"><strong class="">Name</strong></span> <%out.print(user.name)%></li>
            <li class="list-group-item text-right"><span class="pull-left"><strong class="">Username </strong></span> <%out.print(user.username)%>

            </li>
          </ul>
        </div>

        <div class="panel panel-default">
          <div class="panel-heading">Promoted?

          </div>
          <%if (user.isPromoted) {%>
          <div class="panel-body"><i style="color:green" class="fa fa-check-square"></i> Yes, I am a promoted user. </div>
          <% } else { %>
          <div class="panel-body"><i style="color:green" class="fa fa-xing"></i> No, I am not a promoted user.</div>
          <% } %>

        </div>

        <div class="panel panel-default">
          <div class="panel-heading">Activity</div>
          <ul class="list-group">
            <li class="list-group-item text-right"><span class="pull-left"><strong class="">Likes</strong></span> 123</li>
            <li class="list-group-item text-right"><span class="pull-left"><strong class="">Posts</strong></span> 44 </li>
            <li class="list-group-item text-right"><span class="pull-left"><strong class="">Followers</strong></span> <% out.print(user.stories.length);%> </li>
          </ul>
        </div>
      </div>
      <!--/col-3-->
      <div class="col-sm-9" contenteditable="false" style="">

        <div class="panel panel-default target">
          <div class="panel-heading" contenteditable="false">My Posts</div>
          <div class="panel-body">
            <div class="row">
              <% for (int i = 0; i < user.stories.length; i++) { %>
              <div class="col-md-4">
                <div class="thumbnail">
                  <%out.print("<img alt=\"300x200\" src=\"" + user.stories[i].mainPhotoUrl+ "\">");%>
                  <div class="caption">
                    <h3>
                      <% out.print(user.stories[i].title);%>
                    </h3>
                    <p>
                      <% out.print(user.stories[i].content);%>
                    </p>
                    <p>

                    </p>
                  </div>
                </div>
              </div>
              <% } %>
            </div>

          </div>

        </div>
      </div>


      <div id="push"></div>
    </div>


    <script src="/plugins/bootstrap-select.min.js"></script>
    <script src="/codemirror/jquery.codemirror.js"></script>
    <script src="/beautifier.js"></script>
    <script>
      jQuery.fn.shake = function(intShakes, intDistance, intDuration, foreColor) {
        this.each(function() {
          if (foreColor && foreColor!="null") {
            $(this).css("color",foreColor);
          }
          $(this).css("position","relative");
          for (var x=1; x<=intShakes; x++) {
            $(this).animate({left:(intDistance*-1)}, (((intDuration/intShakes)/4)))
                    .animate({left:intDistance}, ((intDuration/intShakes)/2))
                    .animate({left:0}, (((intDuration/intShakes)/4)));
            $(this).css("color","");
          }
        });
        return this;
      };
    </script>
    <script>
      $(document).ready(function() {

        $('.tw-btn').fadeIn(3000);
        $('.alert').delay(5000).fadeOut(1500);

        $('#btnLogin').click(function(){
          $(this).text("...");
          $.ajax({
            url: "/loginajax",
            type: "post",
            data: $('#formLogin').serialize(),
            success: function (data) {
              //console.log('data:'+data);
              if (data.status==1&&data.user) { //logged in
                $('#menuLogin').hide();
                $('#lblUsername').text(data.user.username);
                $('#menuUser').show();
                /*
                 $('#completeLoginModal').modal('show');
                 $('#btnYes').click(function() {
                 window.location.href="/";
                 });
                 */
              }
              else {
                $('#btnLogin').text("Login");
                prependAlert("#spacer",data.error);
                $('#btnLogin').shake(4,6,700,'#CC2222');
                $('#username').focus();
              }
            },
            error: function (e) {
              $('#btnLogin').text("Login");
              console.log('error:'+JSON.stringify(e));
            }
          });
        });
        $('#btnRegister').click(function(){
          $(this).text("Wait..");
          $.ajax({
            url: "/signup?format=json",
            type: "post",
            data: $('#formRegister').serialize(),
            success: function (data) {
              console.log('data:'+JSON.stringify(data));
              if (data.status==1) {
                $('#btnRegister').attr("disabled","disabled");
                $('#formRegister').text('Thanks. You can now login using the Login form.');
              }
              else {
                prependAlert("#spacer",data.error);
                $('#btnRegister').shake(4,6,700,'#CC2222');
                $('#btnRegister').text("Sign Up");
                $('#inputEmail').focus();
              }
            },
            error: function (e) {
              $('#btnRegister').text("Sign Up");
              console.log('error:'+e);
            }
          });
        });

        $('.loginFirst').click(function(){
          $('#navLogin').trigger('click');
          return false;
        });

        $('#btnForgotPassword').on('click',function(){
          $.ajax({
            url: "/resetPassword",
            type: "post",
            data: $('#formForgotPassword').serializeObject(),
            success: function (data) {
              if (data.status==1){
                prependAlert("#spacer",data.msg);
                return true;
              }
              else {
                prependAlert("#spacer","Your password could not be reset.");
                return false;
              }
            },
            error: function (e) {
              console.log('error:'+e);
            }
          });
        });

        $('#btnContact').click(function(){

          $.ajax({
            url: "/contact",
            type: "post",
            data: $('#formContact').serializeObject(),
            success: function (data) {
              if (data.status==1){
                prependAlert("#spacer","Thanks. We got your message and will get back to you shortly.");
                $('#contactModal').modal('hide');
                return true;
              }
              else {
                prependAlert("#spacer",data.error);
                return false;
              }
            },
            error: function (e) {
              console.log('error:'+e);
            }
          });
          return false;
        });

        /*
         $('.nav .dropdown-menu input').on('click touchstart',function(e) {
         e.stopPropagation();
         });
         */





      });
      $.fn.serializeObject = function()
      {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
          if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
              o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
          } else {
            o[this.name] = this.value || '';
          }
        });
        return o;
      };
      var prependAlert = function(appendSelector,msg){
        $(appendSelector).after('<div class="alert alert-info alert-block affix" id="msgBox" style="z-index:1300;margin:14px!important;">'+msg+'</div>');
        $('.alert').delay(3500).fadeOut(1000);
      }
    </script>





    <script src="/plugins/bootstrap-pager.js"></script>
  </div>

</div>






<div id="contact" class="container-fluid bg-grey">
  <p><span class="glyphicon glyphicon-map-marker"></span> Istanbul, TR</p>
  <p><span class="glyphicon glyphicon-phone"></span> +90 212 359 54 00</p>
  <p><span class="glyphicon glyphicon-envelope"></span> info@sculture.com</p>
</div>

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

        <form role="form" action="/login" method="post" class="login-form">
          <div class="form-group">
            <label class="sr-only" for="form-username">E-mail</label>
            <input type="text" name="form-username" placeholder="Username..." class="form-email form-control" id="form-username">
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

        <form role="form" action="/signup" method="post" class="register-form">
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
<!-- Javascript -->
<script src="public/js/jquery-1.11.1.min.js"></script>
<script src="public/js/bootstrap.min.js"></script>
<script src="public/js/jquery.backstretch.min.js"></script>
<script src="public/js/scripts.js"></script>
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
</body>

</html>