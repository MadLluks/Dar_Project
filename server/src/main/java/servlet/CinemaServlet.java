package main.java.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.beans.Cinema;
import main.java.beans.User;
import main.java.metier.apiHandlers.AlloCineAPIHandler;

/**
 * Servlet implementation class CinemaServlet
 */

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
		response.setContentType("application/json");
		String action = (String) request.getParameter("action");
		switch(action){
		// execute request to AlloCine API
		case "api_request":
			alloCineRequest(request, response);
			break;
			// get cinemas visited by user
		case "user_list":
			listCinemasByUser(request, response);
			break;
		default:
			PrintWriter out = response.getWriter();		
			out.print("{\"success\": false, \"error\": \"unknown_action\"}");
			out.flush();
			out.close();
			break;
		}
	}

	/*
	 * Returns the cinemas saved by current user in JSON format
	 */
	private void listCinemasByUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		User user = (User) request.getSession().getAttribute("user");
		if(user != null){
			String jsonResp = "";
			ArrayList<Cinema> cines = user.getCinemaVisited();
			boolean first = true;
			for(Cinema c : cines){
				if(!first) jsonResp += ", ";
				else first = false;
				jsonResp += c.toJson();
			}
			jsonResp = "{\"success\": true, \"result\": ["+jsonResp+"]}";
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter out = response.getWriter();
			out.print(jsonResp);
			out.flush();
			out.close();
		}
		else{
			PrintWriter out = response.getWriter();		
			out.print("{\"success\": false, \"error\": \"login_required\"}");
			out.flush();
			out.close();
		}
	}

	private void alloCineRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
		request.removeAttribute("action");		
		String type = (String) request.getParameter("type");
		String query = type+"?";
		request.removeAttribute("type");
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String e = names.nextElement();
			query += e+"="+request.getParameter(e).replace("%2C", ",")+"&"; // prevents urlencoding of "," by client
		}
		String jsonResp = AlloCineAPIHandler.getInstance().doQuery(query);
		PrintWriter out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
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
