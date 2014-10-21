package com.dar.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dar.metier.apiHandlers.GmapsAPIHandler;

/**
 * Servlet implementation class GmapsServlet
 */
@WebServlet("/GmapsServlet")
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
		// examples of working requests :
		// http://localhost:8080/darserver/gmaps?origin=Toronto&destination=Montreal
		// http://localhost:8080/darserver/gmaps?origin=rue de madrid,gif-sur-yvette&destination=17 rue de liné,paris
		// http://localhost:8080/darserver/gmaps?origin=48.6917631,2.1018575&destination=paris
		
		String origin, destination, mode;
		PrintWriter out = response.getWriter();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("JSON");
		String jsonResp = "";
		if(request.getParameter("origin") != null
				&& request.getParameter("destination") != null){
			if(request.getParameter("mode") != null)
				mode = (String) request.getParameter("mode");
			else
				mode = "driving";
			
			destination = (String) request.getParameter("destination");
			origin = (String) request.getParameter("origin");
			System.out.println(origin+" "+destination+" "+mode);
			GmapsAPIHandler gmaps = new GmapsAPIHandler();
			response.setContentType("application/json");

			try{
				jsonResp = gmaps.doQuery(origin, destination, mode);
				jsonResp = "{success: true, result: "+jsonResp+"}";
				response.setStatus(HttpServletResponse.SC_OK);
			}
			catch(IOException e){
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				jsonResp = "{sucess: false, error: bad_request}";
			}			
		}
		else
			jsonResp = "{success: false, error: missing_parameter}";
		
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
