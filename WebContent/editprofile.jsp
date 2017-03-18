<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="socializer.core.PersonManager"%>
<%PersonManager m = (PersonManager)request.getAttribute("m");
request.setAttribute("title", "Edit profile");%>
<div class="well">
	<form method="post">
		<div class="form-group">
    		<label for="name">Name:</label>
    		<input id="name" class="form-control" type="text" name="name" value="<%=request.getParameter("name") == null ? (m.getPerson().getName()) : request.getParameter("name")%>">
  		</div>
		<div class="form-group">
    		<label for="birthday">Birthday:</label>
    		<input id="birthday" class="form-control" type="date" name="birthday" max="<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date())%>" value="<%=request.getParameter("birthday") == null ? (m.getPerson().getBirthday() == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(m.getPerson().getBirthday())) : request.getParameter("birthday")%>">
  		</div>
		<div class="form-group">
    		<label for="birthplace">Born in:</label>
    		<input id="birthplace" class="form-control" type="text" name="birthplace" value="<%=request.getParameter("birthplace") == null ? (m.getPerson().getBirthplace()) : request.getParameter("birthplace")%>">
  		</div>
		<div class="form-group">
    		<label for="residenceplace">Lives in:</label>
    		<input id="residenceplace" class="form-control" type="text" name="residenceplace" value="<%=request.getParameter("residenceplace") == null ? (m.getPerson().getResidencePlace()) : request.getParameter("residenceplace")%>">
  		</div>
		<div class="form-group">
    		<label for="custompersonallink">Custom personal link:</label>
    		<input id="custompersonallink" class="form-control" type="text" name="custompersonallink" value="<%=request.getParameter("custompersonallink") == null ? (m.getPerson().getCustomPersonalLink()) : request.getParameter("custompersonallink")%>">
  		</div>
  		<div align="center">
			<button class="btn btn-primary" name="action" value="editprofile" type="submit">Save changes</button>
		</div>
	</form><%
	if(request.getAttribute("error") != null){%>
		<hr>
		<div class="alert alert-danger alert-dismissable fade in">
			<strong>Error!</strong> <%=request.getAttribute("error").toString()%>
		</div><%
	}%>
</div>