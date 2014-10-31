package main.java.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
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
		if(user != null){
			//this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );
			// TODO REMOVE : test only
//			Cinema c = new Cinema("Premier cinéma", 1.429504f, 2.504950f);
//			user.addSeenMovie(new Movie("P111", "Le dernier des mohicans"), c);
//			Movie m = new Movie("P222");
//			Movie m2 = new Movie("P111");
//			System.out.println(m.toJson());
//			System.out.println(m2.toJson());
//			Cinema c2 = new Cinema("Premier cinéma", 1.429504f, 2.504950f);
//			System.out.println("C2 : "+c2.getExists());
		}
		// not logged in, can load login page
		else
			this.getServletContext().getRequestDispatcher( "/WEB-INF/login.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = "unknown";
		response.setContentType("application/json");
		System.out.println("#########################################");
		Enumeration params = request.getParameterNames(); 
		while(params.hasMoreElements()){
		    String paramName = (String)params.nextElement();
		    System.out.println("Attribute Name - "+paramName+", Value - "+request.getParameter(paramName));
		}
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
		System.out.println("-----------------------");
		System.out.println("ACTION : "+action);
		System.out.println("-----------------------");
		switch(action){
			case "login":
			    System.out.println("HERE IN LOGIN");
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
