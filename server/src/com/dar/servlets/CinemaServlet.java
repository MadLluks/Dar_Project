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
import com.dar.beans.User;

/**
 * Servlet implementation class CinemaServlet
 */
@WebServlet("/CinemaServlet")
public class CinemaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CinemaServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get cinemas visited by user
		User user = (User) request.getSession().getAttribute("user");
		if(user != null){
			String jsonResp = "";
			ArrayList<Cinema> cines = user.getCinemaVisited();
			for(Cinema c : cines){
				jsonResp += c.toJson()+", ";
			}
			jsonResp = "{success: true, result: "+jsonResp+"}";
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			out.print(jsonResp);
			out.flush();
			out.close();
		}
		else{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");			
			out.print("{success: false, error: not_logged_in}");
			out.flush();
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
