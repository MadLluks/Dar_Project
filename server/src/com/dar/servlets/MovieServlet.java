package com.dar.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dar.beans.Cinema;
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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		// Get movies saved by current user
		User user = (User) request.getSession().getAttribute("user");
		if(user != null){
			String jsonResp = "";
			ArrayList<Movie> movies = user.getSeenMovies();
			for(Movie m : movies){
				jsonResp += m.toJson()+", ";
			}
			jsonResp = "{\"success\": true, \"result\": "+jsonResp+"}";
			PrintWriter out = response.getWriter();
			out.print(jsonResp);
			out.flush();
			out.close();
		}
		else{
			PrintWriter out = response.getWriter();
			out.print("{\"success\": false, \"error\": \"not_logged_in\"}");
			out.flush();
			out.close();
		}
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
			out.print("{\"success\" : false, \"error\" : \"login_required\"}");
			out.flush();
			out.close();
			return;
		}
		String title;
		Float cine_lon, cine_lat;
		String movie_id;
		String name;
		try{
			title = request.getParameter("title");
			movie_id = request.getParameter("movie_id");
			cine_lon = Float.valueOf(request.getParameter("cine_lon"));
			cine_lat = Float.valueOf(request.getParameter("cine_lat"));
			name = request.getParameter("cine_name");
			if(name == null || cine_lat == null || cine_lon == null || title == null || movie_id == null)
				throw new Exception(); 
		}
		catch(Exception e){
			out.print("{\"success\" : false, \"error\" : \"missing_parameter\"}");
			out.flush();
			out.close();
			return;
	    }
		
		Cinema cine = new Cinema(name, cine_lat, cine_lon);
		Movie m = new Movie(movie_id, title);
		
		if(!user.addSeenMovie(m,cine) || !user.save()){
			out.print("{\"success\": false, \"error\": \"user_save_error\"}");
			out.flush();
		}
		out.print("{\"success\" : true}");
		out.flush();
		out.close();
		
	}

}
