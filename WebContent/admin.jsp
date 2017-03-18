<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="socializer.core.Notification"%>
<%@ page import="socializer.core.AdminManager"%>
<%AdminManager m = (AdminManager)request.getAttribute("m");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container-fluid" style="max-width:800px;">
    <div class="navbar-header">
      <a class="navbar-brand" href="<%=request.getContextPath() + "/reports"%>" style="padding-right:80px">Socializer</a>
    </div>
    <ul class="nav navbar-nav navbar-right">
    <li><a href="#" onclick="document.getElementById('logoutform').submit();"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
    </ul>
  </div>
</nav>
  
<div class="container-fluid" style="max-width:800px; padding-top:100px; padding-bottom:20px; min-height:100%;height:100%;">
	<div class="row" style="min-height:100%;height:100%;">
  			<div class="col-sm-3">
              	<ul class="nav nav-stacked" data-spy="affix" data-offset-top="30">
                  <li><a href="<%=request.getContextPath() + "/reports"%>"><span class="glyphicon glyphicon-list"></span> Reports</a></li>
                  <li><a href="<%=request.getContextPath() + "/support"%>"><span class="glyphicon glyphicon-wrench"></span> Support</a></li>
              	</ul>
      		</div>  
      		<div class="col-sm-9" style="min-height:100%;height:100%;"><%
	            switch(request.getRequestURI().substring(request.getContextPath().length()).toString()){
						case "/reports":
							request.setAttribute("pageMode", "reports");
							%><jsp:include page="posts.jsp"/><%
							break;
						case "/support":
							%><jsp:include page="support.jsp"/><%
							break;
						default:
							%><jsp:include page="page.jsp"/><%
					}%>
      		</div>
  	</div>
</div>
<form id="logoutform" action="<%=request.getContextPath() + "/login"%>" method="post">
	<input type="hidden" name="action" value="logout">
</form>
<script>
	window.onload = function(){
		$('.textarea-autoresize').each(function () {
			  this.setAttribute('style', 'overflow-y:hidden;resize:none;'+this.getAttribute('style'));
			}).on('input', function () {
			  this.style.height = 'auto';
			  this.style.height = (this.scrollHeight) + 'px';
			});
		innerOnload();
	}
</script>
</body>
<head>
  <title><%=(String)request.getAttribute("title")%></title>
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
</html>
