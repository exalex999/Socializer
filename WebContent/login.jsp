<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Socializer</title>
		<meta charset="utf-8">
		 <meta name="viewport" content="width=device-width, initial-scale=1">
		 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
		 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		 <style>
		 	html{
		   	height: 100%;
		}
		 	body{
		   	height: 100%;
			background-color: #DFE9F5;
		}
		.navbar-default {
		   background-color: #45668e;
		   border-color: #36507c;
		 }
		 .navbar-default .navbar-brand {
		   color: #ecf0f1;
		 }
		 .navbar-default .navbar-brand:hover,
		 .navbar-default .navbar-brand:focus {
		   color: #6d9ed9;
		 }
		 .navbar-default .navbar-text {
		   color: #ecf0f1;
		 }
		 .navbar-default .navbar-nav > li > a {
		   color: #ecf0f1;
		 }
		 .navbar-default .navbar-nav > li > a:hover,
		 .navbar-default .navbar-nav > li > a:focus {
		   color: #6d9ed9;
		 }
		 .navbar-default .navbar-nav > .active > a,
		 .navbar-default .navbar-nav > .active > a:hover,
		 .navbar-default .navbar-nav > .active > a:focus {
		   color: #6d9ed9;
		   background-color: #36507c;
		 }
		 .navbar-default .navbar-nav > .open > a,
		 .navbar-default .navbar-nav > .open > a:hover,
		 .navbar-default .navbar-nav > .open > a:focus {
		   color: #6d9ed9;
		   background-color: #36507c;
		 }
		 .navbar-default .navbar-toggle {
		   border-color: #36507c;
		 }
		 .navbar-default .navbar-toggle:hover,
		 .navbar-default .navbar-toggle:focus {
		   background-color: #36507c;
		 }
		 .navbar-default .navbar-toggle .icon-bar {
		   background-color: #ecf0f1;
		 }
		 .navbar-default .navbar-collapse,
		 .navbar-default .navbar-form {
		   border-color: #ecf0f1;
		 }
		 .navbar-default .navbar-link {
		   color: #ecf0f1;
		 }
		 .navbar-default .navbar-link:hover {
		   color: #6d9ed9;
		 }
		
		 @media (max-width: 767px) {
		   .navbar-default .navbar-nav .open .dropdown-menu > li > a {
		     color: #ecf0f1;
		   }
		   .navbar-default .navbar-nav .open .dropdown-menu > li > a:hover,
		   .navbar-default .navbar-nav .open .dropdown-menu > li > a:focus {
		     color: #6d9ed9;
		   }
		   .navbar-default .navbar-nav .open .dropdown-menu > .active > a,
		   .navbar-default .navbar-nav .open .dropdown-menu > .active > a:hover,
		   .navbar-default .navbar-nav .open .dropdown-menu > .active > a:focus {
		     color: #6d9ed9;
		     background-color: #36507c;
		   }
		 }
		 .affix {
		   top: 70px;
		 }
		 .well{
		 	background-color: #ffffff;
		 }
		 .btn-transparent {
		 	background-color: Transparent;
		 	border: none;
		 	outline:none;
		 	color: #DFE9F5;
		 }
		 .btn-transparent:hover {
		 	color: #45668e;
		 }
		.vertical-align {
		    display: flex;
		    align-items: center;
		}
		 </style>
	</head>
	<body>
	<nav class="navbar navbar-default navbar-fixed-top">
	  <div class="container-fluid" style="max-width:800px;">
	    <div class="navbar-header">
	      <a class="navbar-brand" href="" style="padding-right:80px">Socializer</a>
	    </div>
	</nav>
	<div class="container-fluid" style="max-width:800px; padding-top:100px; padding-bottom:20px; min-height:100%;height:100%;">
		<div class="row" style="min-height:100%;height:100%;">
	  			<div class="col-sm-3">
	      		</div>
	      		<div class="col-sm-6">
	      			<div class="well">
						  <form method="post">
						    <div class="input-group">
						      <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
						      <input id="email" type="text" class="form-control" name="email" placeholder="Email">
						    </div>
						    <br>
						    <div class="input-group">
						      <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
						      <input id="password" type="password" class="form-control" name="password" placeholder="Password">
						    </div>
						    <br>
						    <div class="row">
								<div class="col-sm-6 text-left">
									<button class="btn btn-primary" name="action" value="login" type="submit" style="width:100px;"><span class="glyphicon glyphicon-log-in"></span> Log in</button>
								</div>
								<div class="col-sm-6 text-right">   
								    <button class="btn btn-primary" name="action" value="register" type="submit" style="width:100px;"><span class="glyphicon glyphicon-pencil"></span> Sign up</button>
								</div>
							</div>
						  </form><%
						if(request.getAttribute("error") != null){%>
							<hr>
							<div class="alert alert-danger alert-dismissable fade in">
								<strong>Error!</strong> <%=request.getAttribute("error").toString()%>
							</div><%
						}%>
					</div>
	      		</div>
	  			<div class="col-sm-3">
	      		</div>
	  	</div>
	</div>
	</body>
</html>