package main.java.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import main.java.beans.User;

/**
 * Servlet implementation class UserServlet
 */

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// check if user is logged in
		User user = (User) request.getSession().getAttribute("user");
		if(user == null)
			this.getServletContext().getRequestDispatcher( "/WEB-INF/login.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = "unknown";
		response.setContentType("application/json");			
		try{
			action = request.getParameter("action");
		}
		catch(Exception e){
			PrintWriter out = response.getWriter();					
			out.print("{\"success\" : false, \"error\" : \"no_action_set\"}");
			out.flush();
			out.close();
			return;
		}

		switch(action){
		case "login":
			doLogin(request, response);
			break;
		case "register":
			doRegister(request, response);
			break;
		default:
			PrintWriter out = response.getWriter();
			out.print("{\"success\" : false, \"error\" : \"unknown_action\"}");
			out.flush();
			out.close();
			break;
		}		
	}

	private void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login,password;
		String jsonResponse = "";
		PrintWriter out = response.getWriter();
		try{
			login = request.getParameter("login");
			password = request.getParameter("password");
		}
		catch(Exception e){
			out.print("{\"success\": false, \"error\": \"missing_parameter\"}");
			out.flush();
			out.close();
			return;
		}
		User user = new User();
		if(user.load(login, password)){
			request.getSession().setAttribute("user", user);
			jsonResponse = "{\"success\": true}";
			Cookie c = new Cookie("user", user.getLogin());
			response.addCookie(c);
		}
		else{
			jsonResponse = "{\"success\": false, \"error\" : \"wrong_credentials\"}";
		}
		out.write(jsonResponse);
		out.flush();
		out.close();
	}

	private void doRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login,password;
		String jsonResponse = "";
		PrintWriter out = response.getWriter();
		try{
			login = request.getParameter("login");
			password = request.getParameter("password");
		}
		catch(Exception e){
			out.print("{\"success\" : false, \"error\" : \"missing_parameter\"}");
			out.flush();
			return;
		}
		User user = new User(login, password);
		if(user.load())
			jsonResponse = "{\"success\" : false, error : \"login_exists\"}";		
		else{
			if(user.save()){				
				request.getSession().setAttribute("user", user);
				jsonResponse = "{\"success\" : true}";
			}
			else
				jsonResponse = "{\"success\" : false, \"error\": \"insert_error\"}";
		}
		out.print(jsonResponse);
		out.flush();
		out.close();
	}
}
