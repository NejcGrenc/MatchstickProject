package grenc.masters.servlets.setup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import grenc.simpleton.processor.BeanProcessor;


/**
 * Executes code on server startup - before servlet initialization
 */
public class ServletSetup implements ServletContextListener {

	@Override
    public void contextInitialized(ServletContextEvent event) {
        // Webapp startup.
    	System.out.println("ServletSetup");
    	BeanProcessor.processPath("grenc.masters");
    	System.out.println("ServletSetup--------");
    	System.out.println();
    	System.out.println();
    	System.out.println("ServletSetup--------");
    }

	@Override
    public void contextDestroyed(ServletContextEvent event) {
        // Webapp shutdown.
    	System.out.println("Servlet shutdown --------");
    }

}