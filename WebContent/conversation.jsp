<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="socializer.core.Person"%>
<%@ page import="socializer.core.Friendship"%>
<%@ page import="socializer.core.Message"%>
<%@ page import="socializer.core.PersonManager"%>
<%PersonManager m = (PersonManager)request.getAttribute("m");
Person interlocutor = (Person)request.getAttribute("interlocutor");
if(interlocutor == null)
	request.setAttribute("title", "Support");
else{
	Friendship f = m.getFriendship(interlocutor);
	if(f == null || f.getStatus() == Friendship.FriendshipStatus.PENDING){
		request.setAttribute("redirect", m.getPerson().getPersonalLink());
		return;
	}
	request.setAttribute("title", "Conversation with " + interlocutor.getName());
}%>
<div id="conversationwell" class="well" style="min-height:100%;height:100%;margin-bottom:0;">
	<div id="conversationhistory" style="overflow-y:auto;height:500px"><%
		for(Message message : m.getConversation(interlocutor, null, null)){
			if(message.getSender() == null){%>
				<h4>Support team</h4><%
			} else{%>
				<h4><a href="<%=request.getContextPath() + "/" + message.getSender().getPersonalLink()%>"><%=message.getSender().getName()%></a></h4><%
			}%>
			<p class="text-muted small"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.getDate())%></p>
			<p style="white-space: pre-wrap; word-break: break-all"><%=message.getMessage()%></p><%
		}%>
	</div>
	<div id="conversationform" class="row">
		<hr>
		<form method="post"><%
			if(interlocutor!=null){%>
				<input type="hidden" name="receiver" value="<%=interlocutor.getPersonalLink()%>"><%
			}%>
			<div class="col-sm-10">
				<textarea name="message" id="messageform" class="form-control" placeholder="Type your message..." style="overflow-y:hidden;resize:none;"></textarea>
			</div>
			<div class="col-sm-2">
				<p style="align:rigth;"><button name="action" value="sendmessage" id="messagebutton" class="btn btn-primary btn-lg"><span class="glyphicon glyphicon-send"></span></button></p>
			</div>
		</form>
	</div>
</div>
<script>
function innerOnload(){
	var messageForm = document.getElementById("messageform");
	var messageButton = document.getElementById("messagebutton");
	messageForm.style.height = 'auto';
	messageForm.style.height = messageForm.scrollHeight + 'px';
	messageButton.style.height = 'auto';
	messageButton.style.height = messageForm.scrollHeight + 'px';
	messageform.addEventListener("input", function () {
		if(this.scrollHeight < 200){
			  this.style.overflowY="hidden";
			  this.style.height = 'auto';
			  this.style.height = (this.scrollHeight) + 'px';
			  document.getElementById('conversationhistory').style.height='auto';
			  resizeConversationHistory();
			}
			else
				this.style.overflowY="scroll";
		});
	$(window).on('resize', resizeConversationHistory);
	resizeConversationHistory();
	
}
function resizeConversationHistory(){
	var well = document.getElementById("conversationwell");
	document.getElementById('conversationhistory').style.height = (well.clientHeight - document.getElementById('conversationform').offsetHeight - 2*parseInt(window.getComputedStyle(well).paddingTop))+'px';
	var conversationHistory = document.getElementById("conversationhistory");
	conversationHistory.scrollTop = conversationHistory.scrollHeight;
}
</script>