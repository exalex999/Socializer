package socializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import socializer.core.AdminManager;
import socializer.core.Message;
import socializer.core.PersonManager;
import socializer.core.Post;
import socializer.core.Report;
import socializer.core.ResearcherManager;
import socializer.core.UserManager;

/**
 * Servlet serving an entry point for the application
 * @author Oleksandr Grygorenko (a1601931)
 */
@WebServlet("/")
public class SocializerServlet extends HttpServlet {
	
	/**
	 * Serial version UID (for serialization)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Generates an html-webpage for HTTP GET request.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		try{
			PersonManager m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
			request.setAttribute("m", m);
			request.getRequestDispatcher("/person.jsp").include(request, response);
			m.close();
		} catch(Exception e1){
			e1.printStackTrace();
			try{
				AdminManager m = new AdminManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				request.setAttribute("m", m);
				request.getRequestDispatcher("/admin.jsp").include(request, response);
				m.close();
			}
			catch(Exception e2){
				try{
					ResearcherManager m = new ResearcherManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
					request.setAttribute("m", m);
					request.getRequestDispatcher("/researcher.jsp").include(request, response);
					m.close();
				} catch(Exception e3){
					session.invalidate();
					if(request.getRequestURI().substring(request.getContextPath().length()).equals("/login"))
						request.getRequestDispatcher("login.jsp").include(request, response);
					else
						request.setAttribute("redirect", "login");
				}
			}
		}
		if(request.getAttribute("redirect") != null)
			response.sendRedirect(request.getContextPath() + "/" + (String)request.getAttribute("redirect"));
	}

	/**
	 * Generates an html-webpage and persists corresponding changes for HTTP POST request.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try{
			switch(request.getParameter("action")){
			case "register":
				session.invalidate();
				session = request.getSession();
				try{
					PersonManager.register(request.getParameter("email").trim(), request.getParameter("password"));
				} catch(Exception e){
					request.setAttribute("error", e.getMessage());
					break;
				}
			case "login":
				session.invalidate();
				session = request.getSession();
				session.setAttribute("email", request.getParameter("email"));
				session.setAttribute("password", request.getParameter("password"));	
				request.setAttribute("error", "Invalid credentials");
				break;
			case "logout":
				session.invalidate();
				break;
			case "editprofile":
				PersonManager m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				if(!m.getPerson().isBlocked())
					try{
						m.editProfile(request.getParameter("birthday").equals("") ? null : new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("birthday")), request.getParameter("birthplace").trim(), request.getParameter("custompersonallink").trim(), request.getParameter("name").trim(), request.getParameter("residenceplace").trim());
						response.sendRedirect(request.getContextPath() + "/" + m.getPerson().getPersonalLink());
						m.close();
						return;
					} catch(IllegalArgumentException e){
						request.setAttribute("error", "Changes are not applied. " + e.getMessage());
					} catch(ParseException e){
						request.setAttribute("error", "Changes are not applied. Invalid birthday date format (must be 'yyyy-MM-dd')");
					}
				m.close();
				break;
			case "addfriend":
				m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				if(!m.getPerson().isBlocked())
					m.addFriend(m.getPerson(request.getParameter("friend")));
				m.close();
				break;
			case "removefriend":
				m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				if(!m.getPerson().isBlocked())
					m.removeFriend(m.getPerson(request.getParameter("friend")));
				m.close();
				break;
			case "addpost":
				m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				if(!m.getPerson().isBlocked())
					m.addPost(new Post(request.getParameter("newpost"), m.getPerson()));
				m.close();
				break;
			case "removepost":
				m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				if(!m.getPerson().isBlocked())
					m.removePost(m.getPost(Long.parseLong(request.getParameter("postid"))));
				m.close();
				return;
			case "reportpost":
				m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				if(!m.getPerson().isBlocked())
					m.reportPost(new Report(request.getParameter("reportreason"), m.getPost(Long.parseLong(request.getParameter("postid"))), m.getPerson()));
				m.close();
				return;
			case "resetnewnotificationscounter":
				m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				m.resetNewNotificationsNumber();
				m.close();
				return;
			case "sendmessage":
				m = new PersonManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				if(!m.getPerson().isBlocked() || request.getParameter("receiver") == null)
					m.sendMessage(new Message(request.getParameter("message"), request.getParameter("receiver") == null ? null : m.getPerson(request.getParameter("receiver")), m.getPerson(), ""));
				m.close();
				break;
			case "lockacc":
				AdminManager am = new AdminManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				am.lockAccount(am.getPerson(request.getParameter("account")), request.getParameter("blockreason"));
				am.close();
				break;
			case "unlockacc":
				am = new AdminManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				am.unlockAccount(am.getPerson(request.getParameter("account")));
				am.close();
				break;
			case "lockpost":
				am = new AdminManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				am.lockPost(am.getPost(Long.parseLong(request.getParameter("postid"))), request.getParameter("blockreason"));
				am.close();
				return;
			case "unlockpost":
				System.err.println("servl unl post");
				am = new AdminManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				am.unlockPost(am.getPost(Long.parseLong(request.getParameter("postid"))));
				am.close();
				return;
			case "ignorepost":
				am = new AdminManager((String)session.getAttribute("email"), (String)session.getAttribute("password"));
				am.ignorePost(am.getPost(Long.parseLong(request.getParameter("postid"))));
				am.close();
				return;
			}
		} catch(Exception e){e.printStackTrace();}
		doGet(request, response);
	}
	
	/**
	 * Initializes UserManager
	 */
	public void init() throws ServletException {
		UserManager.init(getServletContext().getRealPath("/"));
	}
	
	/**
	 * Finalizes UserManager
	 */
	public void destroy(){
		UserManager.destroy();
	}
}
