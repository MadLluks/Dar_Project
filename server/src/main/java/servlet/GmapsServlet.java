package main.java.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.metier.apiHandlers.GmapsAPIHandler;

/**
 * Servlet implementation class GmapsServlet
 */

public class GmapsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GmapsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String origin, destination, mode;
		Integer arrival_time = null;
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String jsonResp = "";
		if(request.getParameter("origin") != null
				&& request.getParameter("destination") != null){
			// if no mode was given by the user we'll find the best mode for him
			if(request.getParameter("mode") != null)
				mode = (String) request.getParameter("mode");
			else
				mode = "none";

			if(request.getParameter("arrival_time") != null)
				arrival_time = Integer.valueOf(request.getParameter("arrival_time"));

			destination = (String) request.getParameter("destination");
			origin = (String) request.getParameter("origin");
			GmapsAPIHandler gmaps = GmapsAPIHandler.getInstance();	
			try{
				if(mode == "none")
					jsonResp = gmaps.findFastestDirection(origin, destination, arrival_time);
				else
					jsonResp = gmaps.doQuery(origin, destination, mode, arrival_time);
				jsonResp = "{\"success\": true, \"result\": "+jsonResp+"}";
				response.setStatus(HttpServletResponse.SC_OK);
			}
			catch(IOException e){
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				jsonResp = "{\"success\": false, \"error\": \"bad_request\"}";
			}			
		}
		else
			jsonResp = "{\"success\": false, \"error\": \"missing_parameter\"}";

		out.write(jsonResp);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
