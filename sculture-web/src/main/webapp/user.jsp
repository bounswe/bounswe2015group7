<%--
  Created by IntelliJ IDEA.
  User: bilal
  Date: 14/10/15
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"
        %>
<html lang="en" hola_ext_inject="disabled">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Sign up for Sculture!</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="js/bootstrap.min.js"></script>


    <!-- Bootstrap core CSS -->

    <!-- Custom styles for this template -->
</head>

<body>

<div class="container">

    <form action="" method="post" class="form-signin">
        <h2 class="form-signin-heading">Sign up</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" id="inputEmail" class="form-control" placeholder="Email address" required="" autofocus="">
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="Password" required="">

        <div class="checkbox">
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign up!</button>
    </form>

</div>
<!-- /container -->

<div class="container">
    <h2>Sculture database ^^</h2>

    <p>Table of users:</p>
    <table class="table table-hover table-bordered">
        <% ArrayList<String> username = (ArrayList<String>) request.getAttribute("username");
            ArrayList<String> password = (ArrayList<String>) request.getAttribute("password");
        %>
        <thead>
        <tr>
            <th>Email</th>
            <th>Password</th>
        </tr>
        </thead>
        <tbody>
        <% for (int i = 0; i < username.size(); i++) { %>
        <tr>
            <td><% out.println(username.get(i)); %></td>
            <td><% out.println(password.get(i));; %></td>
            <%

                } %>
        </tr>
        <tr>
            <td>Bilal</td>
            <td>Yasar</td>
        </tr>
        <tr>
            <td>Foo</td>
            <td>Bar</td>
        </tr>
        </tbody>
    </table>
</div>


</body>
<style id="stylish-1" class="stylish" type="text/css">
</style>
</html>


</body>
<style id="stylish-1" class="stylish"
       type="text/css">/* Created by PanosBabo on 9 March 2012, last updated on 15/May/2015 */
</style>
</html>