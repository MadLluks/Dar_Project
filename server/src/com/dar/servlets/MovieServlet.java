package com.dar.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dar.beans.Movie;
import com.dar.beans.User;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Get movies saved by current user
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Save movie for current user
		
		
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		
		// user must be logged in
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			out.print("{success : false, error : login_required}");
			out.flush();
			return;
		}
		String title;
		float cine_lon, cine_lat;
		String movie_id;
		
		try{
			title = request.getParameter("title");
			movie_id = request.getParameter("movie_id");
			cine_lon = Float.valueOf(request.getParameter("cine_lon"));
			cine_lat = Float.valueOf(request.getParameter("cine_lat"));
		}
		catch(Exception e){
			out.print("{success : false, error : missing_parameter}");
			out.flush();
			return;
	    }
		
		Movie m = new Movie(movie_id, title);
		m.setCine(cine_lat, cine_lon);
		user.addSeenMovie(m);
		if(!user.save()){
			out.print("{success : false, error : user_save_error}");
			out.flush();
		}
		out.print("{success : true}");
		out.flush();
		out.close();
		
	}

}
