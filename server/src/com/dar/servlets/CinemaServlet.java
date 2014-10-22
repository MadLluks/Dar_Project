package com.dar.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dar.beans.Cinema;
import com.dar.beans.User;
import com.dar.metier.apiHandlers.AlloCineAPIHandler;

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
		String action = (String) request.getSession().getAttribute("action");
		response.setHeader("Access-Control-Allow-Origin", "*");
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
			response.setContentType("application/json");			
			out.print("{\"success\": false, \"error\": \"unknown_action\"}");
			out.flush();
			out.close();
			break;
		}
	}
	
	private void listCinemasByUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		User user = (User) request.getSession().getAttribute("user");
		if(user != null){
			String jsonResp = "";
			ArrayList<Cinema> cines = user.getCinemaVisited();
			for(Cinema c : cines){
				jsonResp += c.toJson()+", ";
			}
			jsonResp = "{\"success\": true, \"result: "+jsonResp+"}";
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
			out.print("{\"success\": false, \"error\": \"not_logged_in\"}");
			out.flush();
			out.close();
		}
	}

	private void alloCineRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
		request.removeAttribute("action");		
		String type = (String) request.getAttribute("type");
		String query = type+"?";
		request.removeAttribute("type");
		Enumeration<String> names = request.getAttributeNames();
		while(names.hasMoreElements()){
			String e = names.nextElement();
			query += e+"="+request.getAttribute(e)+"&";
		}
		String jsonResp = AlloCineAPIHandler.getInstance().doQuery(query);
		PrintWriter out = response.getWriter();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		out.write(jsonResp);
		out.close();
		out.flush();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
