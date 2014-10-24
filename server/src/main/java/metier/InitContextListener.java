package main.java.metier;

import javax.servlet.ServletContextEvent;

import main.java.metier.DBHandler;

public class InitContextListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {		
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		DBHandler.setPath("src/main/resources/dar.db");
	}

}
