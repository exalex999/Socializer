<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="socializer.core.UserManager"%>
<%@ page import="socializer.core.PersonManager"%>
<%@ page import="socializer.core.AdminManager"%>
<%@ page import="socializer.core.Person"%>
<%@ page import="socializer.core.Friendship"%><%
UserManager m = (UserManager)request.getAttribute("m");
Person p = null;
try{
	p = m.getPerson(request.getRequestURI().substring(request.getContextPath().length() + 1));
	
} catch(Exception e){
	e.printStackTrace();
	request.setAttribute("redirect", m instanceof PersonManager ? ((PersonManager)m).getPerson().getPersonalLink() : "reports");
	return;
}
request.setAttribute("title", p.getName());
if(m instanceof PersonManager && p.isBlocked() && !request.getRequestURI().substring(request.getContextPath().length()).toString().equals("/support")){%>
	<div class="alert alert-danger">The account of <b><%=p.getName()%></b> has been blocked by administrator.</div><%
	return;
}%>
<form method="post">
	<div class="well">
		<h1><%=p.getName()%></h1><br/>
		<p><%
			if(p.getBirthday() != null){%>
				Birthday: <%=new SimpleDateFormat("yyyy-MM-dd").format(p.getBirthday())%><br/><%
			}
			if(!p.getBirthplace().equals("")){%>
				Born in: <%=p.getBirthplace()%><br/><%
			}
			if(!p.getResidencePlace().equals("")){%>
				Lives in: <%=p.getResidencePlace()%><br/><%
			}%>
		</p><%
		if(m instanceof AdminManager){%>
			<hr><%
			request.setAttribute("pageMode", "unlock");
			request.setAttribute("p", p);%>
			<input type="hidden" name="account" value="<%=p.getPersonalLink()%>">
			<div class="row">
				<div class="col-sm-9 text-left"><%
					if(p.isBlocked()){%>
						<div class="alert alert-danger"><%=((AdminManager)m).getBlockingReason(p)%></div><%
					} else{%>
						<textarea name="blockreason" class="form-control textarea-autoresize" placeholder="Blocking reason..."></textarea><%	
					}%>
				</div>
				<div class="col-sm-3 text-right">   
				    <button name="action" value="<%=p.isBlocked() ? "unlockacc" : "lockacc"%>" type="submit" class="<%=p.isBlocked() ? "btn btn-success" : "btn btn-danger"%>" style="width:100%;"><%=p.isBlocked() ? "Unlock" : "Lock"%></button>
				</div>
			</div>
	</div><%
		}
		else if(((PersonManager)m).getPerson().getId() == p.getId()){
			request.setAttribute("pageMode", "own");%>
	</div>
			<div class="well">
				<p>
					<textarea name="newpost" class="form-control textarea-autoresize" placeholder="What's new?"></textarea>
				</p>
				<div align="right">
					<button name="action" value="addpost" type="submit" class="btn btn-primary">Add post</button>
				</div>
			</div><%
		}
		else{%>
			<hr>
			<input type="hidden" name="friend" value="<%=p.getPersonalLink()%>"><%
			request.setAttribute("pageMode", "another");
			request.setAttribute("p", p);
			Friendship f = ((PersonManager)m).getFriendship(p);
			if(f == null){%>
				<p align="right">
					<button name="action" value="addfriend" type="submit" class="btn btn-success" style="width:230px;">Add friend</button>
				</p><%
			}
			else
				switch(f.getStatus()){
				case ACCEPTED:%>
				<div class="row">
					<div class="col-sm-6 text-left">
						<a class="btn btn-primary" href="<%= "conversations/" + p.getPersonalLink()%>" style="width:230px;">Send message</a>
					</div>
					<div class="col-sm-6 text-right">   
					    <button class="btn btn-danger" name="action" value="removefriend" type="submit" style="width:230px;">Exclude from friends</button>
					</div>
				</div><%
					break;
				case PENDING:
					if(f.getInviter().getId() == ((PersonManager)m).getPerson().getId()){%>
						<p align="right">
							<button class="btn btn-danger" name="action" value="removefriend" type="submit" style="width:230px;">Cancel friendship invitation</button>
						</p><%
					}
					else{%>
						<div class="row">
							<div class="col-sm-6 text-left">
								<button class="btn btn-success" name="action" value="addfriend" type="submit" style="width:230px;">Accept friendship</button>
							</div>
							<div class="col-sm-6 text-right">   
							    <button class="btn btn-danger" name="action" value="removefriend" type="submit" style="width:230px;">Decline friendship</button>
							</div>
						</div><%
					}
					break;
				}%>
	</div><%
		}%>
</form>
<jsp:include page="posts.jsp"/>