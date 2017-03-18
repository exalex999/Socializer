<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="socializer.core.UserManager"%>
<%@ page import="socializer.core.PersonManager"%>
<%@ page import="socializer.core.AdminManager"%>
<%@ page import="socializer.core.Person"%>
<%@ page import="socializer.core.Post"%>
<%@ page import="socializer.core.Report"%>
<%@ page import="java.lang.System"%>
<%UserManager m = (UserManager)request.getAttribute("m");%><%
Post posts[] = null;
String pageMode = request.getAttribute("pageMode").toString();
switch(pageMode){
case "own":
	posts = ((PersonManager)m).getPosts(((PersonManager)m).getPerson(), null, null);
	break;
case "another":
	posts = ((PersonManager)m).getPosts((Person)request.getAttribute("p"), null, null);
	break;
case "feed":
	request.setAttribute("title", "News");
	posts = ((PersonManager)m).getFeed(null, null);
	break;
case "reports":
	request.setAttribute("title", "Reports");
	posts = ((AdminManager)m).getPendingReportedPosts(null, null);
	break;
case "unlock":
	posts = ((AdminManager)m).getBlockedPosts((Person)request.getAttribute("p"), null, null);
	break;
}
for(int i = 0; i < posts.length; ++i){
	if(posts[i].getBlockingReason() != null && (pageMode.equals("another") || pageMode.equals("feed")))
		continue;%>
	<div class="well collapse in" id="postwell<%=posts[i].getId()%>">
		<div class="row">
			<div class="col-sm-6 text-left"><%
				if(pageMode.equals("feed") || pageMode.equals("reports")){%>
					<h4><a href="<%=request.getContextPath() + "/" + posts[i].getPublisher().getPersonalLink()%>"><%=posts[i].getPublisher().getName()%></a></h4><%
				}%>
				<p class="text-muted small"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(posts[i].getDate())%></p>
			</div>
			<div class="col-sm-6 text-right">   
			    <button id="removebutton<%=posts[i].getId()%>" class="btn-transparent" data-toggle="collapse" data-target="#<%=pageMode.equals("own") || pageMode.equals("reports") || pageMode.equals("unlock") ? "postwell"+posts[i].getId()+"\" onclick=\"removePost("+posts[i].getId()+")" : (((PersonManager)m).isPostReported(posts[i]) ? "reportedarea"+posts[i].getId() : "reportarea"+posts[i].getId())%>"><span class="<%=pageMode.equals("unlock") ? "glyphicon glyphicon-ok" : "glyphicon glyphicon-remove"%>"></span></button>
			</div>
		</div><%
		if(posts[i].getBlockingReason() != null)
		switch(pageMode){
		case "own":%>
			<div class="alert alert-danger">This post has been blocked by administrator.<br><b>Reason</b>: "<%=posts[i].getBlockingReason()%>".<br>Please contact support team for more details (include the post ID in your message: <b>#<%=posts[i].getId()%></b>).</div><%
			break;
		case "unlock":%>
			<div class="alert alert-danger"><%=posts[i].getBlockingReason()%></div><%
			break;
		}%>
		<p style="white-space: pre-wrap; word-break: break-all"><%=posts[i].getContent()%></p><%
		if(pageMode.equals("reports") || pageMode.equals("unlock")){
			if(pageMode.equals("reports")){%>
				<hr>
				<div class="row">
					<div class="col-sm-9 text-left">
						<textarea id="blockreason<%=posts[i].getId()%>" class="form-control textarea-autoresize" placeholder="Blocking reason..." value="<%=posts[i].getBlockingReason() != null ? posts[i].getBlockingReason() : ""%>" <%=posts[i].getBlockingReason() != null ? "disabled" : ""%>></textarea>
					</div>
					<div class="col-sm-3 text-right">
						<button id="postlckbtn<%=posts[i].getId()%>" onclick="togglePostLock('<%=posts[i].getId()%>')" class="<%=posts[i].getBlockingReason() == null ? "btn btn-danger" : "btn btn-success"%>" style="width:100%;"><%=posts[i].getBlockingReason() == null ? "Lock" : "Unlock"%></button>
					</div>
				</div><%
			}%>
			<p align="right"><button id="reportsbtn<%=posts[i].getId()%>" class="btn-transparent" data-toggle="collapse" data-target="#reportsarea<%=posts[i].getId()%>"><span class="glyphicon glyphicon-chevron-down"></span></button></p>
			<div id="reportsarea<%=posts[i].getId()%>" class="collapse">
				<hr><%
				Report[] reports = ((AdminManager)m).getReports(posts[i]);
				System.err.println(reports.length);
				for(Report r : reports){%>
					<p class="small" style="white-space: pre-wrap; word-break: break-all"><a href="<%=request.getContextPath() + "/" + r.getReporter().getPersonalLink()%>"><%=r.getReporter().getName()%></a><br><%=r.getComment()%></p><%
				}%>
			</div><%
		}%>
		<div id="reportarea<%=posts[i].getId()%>" class="collapse">
			<hr>
			<p><textarea id="reportreason<%=posts[i].getId()%>" class="form-control textarea-autoresize" placeholder="Report reason..."></textarea></p>
			<div align="right">
				<button class="btn btn-danger" onclick="reportPost('<%=posts[i].getId()%>')">Report</button>
			</div>
		</div>
		<div id="reportedarea<%=posts[i].getId()%>" class="collapse">
			<hr>
			<div class="alert alert-success">Thank you for your watchfulness. Your report has been successfully registered.</div>
		</div>
	</div><%
}%>
<script>
function innerOnload(){<%
	for(int i = 0; i < posts.length; ++i){%>
		$("#reportsarea<%=posts[i].getId()%>").on("hide.bs.collapse", function(){
		    $("#reportsbtn<%=posts[i].getId()%>").html('<span class="glyphicon glyphicon-chevron-down"></span>');
		});
		$("#reportsarea<%=posts[i].getId()%>").on("show.bs.collapse", function(){
		    $("#reportsbtn<%=posts[i].getId()%>").html('<span class="glyphicon glyphicon-chevron-up"></span>');
		});<%
	}%>
}
function removePost(id){
	$.post('socializer',{"action": "<%=pageMode.equals("own") ? "removepost" : pageMode.equals("reports") ? "ignorepost" : "unlockpost"%>", "postid": id});
}
function reportPost(id){
	var reportReason = document.getElementById('reportreason'+id).value;
	if(reportReason != "")
		$.post('socializer',{"action": "reportpost", "postid": id, "reportreason": reportReason},
			function(){
						$('#reportarea'+id).collapse('hide');
						$('#reportedarea'+id).collapse('show');
						var removeButton = document.getElementById('removebutton'+id);
						removeButton.removeAttribute('onclick');
						removeButton.setAttribute('data-target', '#reportedarea' + id);
		            });
}
function togglePostLock(id){
	var lockBtn = document.getElementById('postlckbtn'+id);
	var lockTextarea = document.getElementById('blockreason'+id);
	if(lockTextarea.value == "")
		return;
	if(lockBtn.innerHTML == "Lock"){
		$.post('socializer',{"action": "lockpost", "postid": id, "blockreason" : lockTextarea.value});
		lockBtn.className = "btn btn-success";
		lockBtn.innerHTML = "Unlock";
		lockTextarea.disabled = "disabled";
	}
	else{
		$.post('socializer',{"action": "unlockpost", "postid": id});
		lockBtn.className = "btn btn-danger";
		lockBtn.innerHTML = "Lock";
		lockTextarea.disabled = "";
	}
}
function toggleReportsCollapse(id){
	var lockBtn = document.getElementById('reportsbtn'+id);
	if(lockBtn.innerHTML == '<span class="glyphicon glyphicon-chevron-down"></span>')
		lockBtn.innerHTML = '<span class="glyphicon glyphicon-chevron-up"></span>';
	else
		lockBtn.innerHTML = '<span class="glyphicon glyphicon-chevron-down"></span>';
}
</script>