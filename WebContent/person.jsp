<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="socializer.core.Notification"%>
<%@ page import="socializer.core.PersonManager"%>
<%PersonManager m = (PersonManager)request.getAttribute("m");
Notification[] notifications = m.getNotifications(null, null);
int newNotificationsNumber = m.getNewNotificationsNumber();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container-fluid" style="max-width:800px;">
    <div class="navbar-header">
      <a class="navbar-brand" href="<%=request.getContextPath() + "/" + m.getPerson().getPersonalLink()%>" style="padding-right:80px">Socializer</a>
    </div>
    <form action="<%=request.getContextPath() + "/" + "search"%>" class="navbar-form navbar-left">
      <div class="input-group">
        <input type="text" name="query" class="form-control" placeholder="Search...">
        <span class="input-group-btn">
          <button class="btn btn-default" type="submit">&nbsp;<span class="glyphicon glyphicon-search"></span>&nbsp;</button>
        </span>
      </div>
    </form>
    <ul class="nav navbar-nav">
      <li class="dropdown"><a  class="btn-lg dropdown-toggle" data-toggle="dropdown" href="#" id="notificationdropdown"><span class="glyphicon glyphicon-bell"></span><%=newNotificationsNumber == 0 ? "" : "<span id='notificationnumberbadge' class=\"badge\" style=\"background-color:#ecf0f1; color: #45668e\">" + newNotificationsNumber + "</span>"%></a>
        <ul class="dropdown-menu scrollable-menu" style="width:300px; max-height: 200px;overflow-y: auto;"><%
        	for(int i = 0; i < notifications.length; ++i){
        		if(i < newNotificationsNumber){%>
        			<li><div style="width=300px;"><a class="list-group-item list-group-item-warning" href="<%=request.getContextPath() + "/" + notifications[i].getReference()%>"><%=notifications[i].getNotification()%></a></div></li><%
        		}
        		else{%>
    				<li><div style="width=300px;"><a class="list-group-item" href="<%=request.getContextPath() + "/" + notifications[i].getReference()%>"><%=notifications[i].getNotification()%></a></div></li><%
        		}
        	}
        	if(notifications.length == 0){%>
				<li><div style="text-align:center;width=300px;"><i>No notifications yet :(</i></div></li><%
        	}%>
        </ul>
      </li>
    </ul>
    <ul class="nav navbar-nav navbar-right">
      <li class="dropdown"><a  class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-user"></span><b> <%=m.getPerson().getName().substring(0, m.getPerson().getName().contains(" ") ? m.getPerson().getName().indexOf(" ") : m.getPerson().getName().length())%></b><span class="caret"></span></a>
        <ul class="dropdown-menu">
          <li><a href="<%=request.getContextPath() + "/editprofile"%>"><span class="glyphicon glyphicon-edit"></span> Edit profile</a></li>
          <li><a href="<%=request.getContextPath() + "/support"%>"><span class="glyphicon glyphicon-wrench"></span> Support</a></li>
          <li class="divider"></li>
          <li><a href="#" onclick="document.getElementById('logoutform').submit();"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
        </ul>
      </li>
    </ul>
  </div>
</nav>
  
<div class="container-fluid" style="max-width:800px; padding-top:100px; padding-bottom:20px; min-height:100%;height:100%;">
	<div class="row" style="min-height:100%;height:100%;">
  			<div class="col-sm-3">
              	<ul class="nav nav-stacked" data-spy="affix" data-offset-top="30">
                  <li><a href="<%= request.getContextPath() + "/" + m.getPerson().getPersonalLink()%>"><span class="glyphicon glyphicon-home"></span> My profile</a></li>
                  <li><a href="<%=request.getContextPath() + "/feed"%>"><span class="glyphicon glyphicon-calendar"></span> News</a></li>
                  <li><a href="<%=request.getContextPath() + "/friends"%>"><span class="glyphicon glyphicon-list"></span> Friends</a></li>
              	</ul>
      		</div>  
      		<div class="col-sm-9" style="min-height:100%;height:100%;"><%
      			if(m.getPerson().isBlocked() && !request.getRequestURI().substring(request.getContextPath().length()).toString().equals("/support")){
      				try{
						if(!m.getPerson().getPersonalLink().equals(request.getRequestURI().substring(request.getContextPath().length() + 1)))
							throw new Exception();
						request.setAttribute("title", m.getPerson().getName());
						
					} catch(Exception e){
						e.printStackTrace();
						request.setAttribute("redirect", m.getPerson().getPersonalLink());
						return;
					}%>
      				<div class="alert alert-danger">Your account has been blocked by administrator.<br><b>Reason</b>: "<%=m.getBlockingReason()%>".<br>Please contact support team for more details</div><%
      			}
      			else
		            switch(request.getRequestURI().substring(request.getContextPath().length()).toString()){
							case "/editprofile":
								%><jsp:include page="editprofile.jsp"/><%
								break;
							case "/search":
								request.setAttribute("searchtype", "general");
								%><jsp:include page="search.jsp"/><%
								break;
							case "/friends":
								request.setAttribute("searchtype", "friends");
								%><jsp:include page="search.jsp"/><%
								break;
							case "/feed":
								request.setAttribute("pageMode", "feed");
								%><jsp:include page="posts.jsp"/><%
								break;
							case "/support":
								%><jsp:include page="conversation.jsp"/><%
								break;
							case "/login":
								if("register".equals(request.getParameter("action"))){
									request.setAttribute("redirect", "editprofile");
									break;
								}
							default:
								if(request.getRequestURI().substring(request.getContextPath().length()).startsWith("/conversations/"))
									try{
										request.setAttribute("interlocutor", m.getPerson(request.getRequestURI().substring(request.getContextPath().length() + "/conversations/".length())));
										%><jsp:include page="conversation.jsp"/><%
										break;
									} catch(Exception e){}
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
		var myElem = document.getElementById("notificationdropdown");
		myElem.addEventListener("click", resetNewNotificationsCounter);
		$('.textarea-autoresize').each(function () {
			  this.setAttribute('style', 'overflow-y:hidden;resize:none;'+this.getAttribute('style'));
			}).on('input', function () {
			  this.style.height = 'auto';
			  this.style.height = (this.scrollHeight) + 'px';
			});
		innerOnload();
	}
	function resetNewNotificationsCounter() {
		document.getElementById('notificationnumberbadge').style.display='none';
		$.post('socializer',{"action": "resetnewnotificationscounter"});
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
