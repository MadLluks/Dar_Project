package com.dar.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dar.beans.User;

/**
 * Servlet implementation class LoginServlet
 * @deprecated
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// check if user is logged in
		if(request.getSession().getAttribute("user") != null){
			this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );
		}
		// not logged in, can load login page
		else
			this.getServletContext().getRequestDispatcher( "/WEB-INF/login.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String login,password;
		String jsonResponse = "";
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		try{
			login = request.getParameter("login");
			password = request.getParameter("password");
		}
		catch(Exception e){
			out.print("{success: false, error: missing_parameter}");
			out.flush();
			return;
	    }

		User user = new User();
		if(user.load(login, password)){
			request.getSession().setAttribute("user", user);
			response.setContentType("JSON");
			response.setStatus(HttpServletResponse.SC_OK);
			jsonResponse = "{success: true}";
		}
		else{
			jsonResponse = "{success: false, error : \"wrong_credentials\"}";
		}

		out.write(jsonResponse);
		out.close();
		out.flush();
	}
}
