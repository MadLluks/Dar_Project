package com.dar.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dar.beans.User;

/**
 * Servlet implementation class LoginServlet
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
		if(request.getSession().getAttribute("user") != null){
			request.setAttribute("message", "Vous êtes déjà connecté");
			this.getServletContext().getRequestDispatcher( "/WEB-INF/index.jsp" ).forward( request, response );
		}
		else
			this.getServletContext().getRequestDispatcher( "/WEB-INF/login.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//BufferedReader buffer = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String login,password;
		PrintWriter out = response.getWriter();
		try{
			login = request.getParameter("login");
			password = request.getParameter("password");
		}catch(Exception e)
	    {
			out.println("<html>");
		    out.println("<head><title>Error</title></head>");
		    out.println("<body>");
		    out.println("<p>Fatal error : cannot retrieve credentials.</p>");
		    out.println("</body></html>");
		    out.close();
		    return;
	    }
		User user = new User();
		user.setLogin(login);
		request.getSession().setAttribute("user", user);
		// validate credentials
		
	    
	    out.println("<html>");
	    out.println("<head><title>demolet</title></head>");
	    out.println("<body>");
	    out.println("<p>Hello, "+login+" with password "+password+".</p>");
	    out.println("</body></html>");
	    out.close();
	}

}
