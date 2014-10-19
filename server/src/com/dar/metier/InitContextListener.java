package com.dar.metier;

import javax.servlet.ServletContextEvent;

public class InitContextListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		// TODO Auto-generated method stub
		DBHandler.setPath(contextEvent.getServletContext().getRealPath("/") + "/WEB-INF/dar.db");
	}

}
