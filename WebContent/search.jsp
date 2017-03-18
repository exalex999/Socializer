<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Map.Entry"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="socializer.core.Person"%>
<%@ page import="socializer.core.PersonManager"%>
<%PersonManager m = (PersonManager)request.getAttribute("m");%>
<div class="well" style="min-height:100%;margin-bottom:0">
	<form action="<%=request.getContextPath() + (request.getAttribute("searchtype").equals("friends") ? "/friends" : "/search")%>" class="form-inline" style="width: 50%; margin: 0 auto;">
      <div class="input-group">
        <input type="text" name="query" value="<%=request.getParameter("query") == null ? "" : request.getParameter("query")%>" class="form-control" placeholder="Search...">
        <span class="input-group-btn">
          <button class="btn btn-default" type="submit">&nbsp;<span class="glyphicon glyphicon-search"></span>&nbsp;</button>
        </span>
      </div>
    </form><%
    request.setAttribute("title", "Search");
    HashMap<Person,String> results = new HashMap<>();
    if(request.getParameter("query") != null && !request.getParameter("query").trim().equals(""))
    	results = m.search(request.getParameter("query"), request.getAttribute("searchtype").equals("friends"));
    else if(request.getAttribute("searchtype").equals("friends"))
    	results = m.search("", true);
    for(Iterator<Entry<Person, String>> it = results.entrySet().iterator(); it.hasNext();){
    	Entry<Person, String> pair = it.next();%>
    	<hr>
	    <div class="row vertical-align">
	    	<div class="col-sm-8 text-left">
	    		<h4><a href="<%=request.getContextPath() + "/" + pair.getKey().getPersonalLink()%>"><%=pair.getValue()%></a></h4>
				<p><%
					if(pair.getKey().getBirthday() != null){%>
						Birthday: <%=new SimpleDateFormat("yyyy-MM-dd").format(pair.getKey().getBirthday())%><br><%
					}
					if(!pair.getKey().getBirthplace().equals("")){%>
						Born in: <%=pair.getKey().getBirthplace()%><br><%
					}
					if(!pair.getKey().getResidencePlace().equals("")){%>
						Lives in: <%=pair.getKey().getResidencePlace()%><br><%
					}%>
				</p>
	    	</div>
    	<div class="col-sm-4 text-right">
    			<br><br><br><%
	    		if(request.getAttribute("searchtype").equals("friends")){
    			request.setAttribute("title", "Friends");%>
	    			<a class="btn btn-primary" href="<%= "conversations/" + pair.getKey().getPersonalLink()%>">Send message</a><br><%
	    		}
	    		else{%>
	    			<br><%
	    		}%>
	    		<br><br><br>
    		</div>
	    </div><%
    }%>
    <hr>
</div>