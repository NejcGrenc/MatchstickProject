
package grenc.masters.servlets.setup;

import java.nio.charset.Charset;

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
    	System.out.println("MatchstickApplication startup!");
    	System.out.println(Charset.defaultCharset().displayName());

    	BeanProcessor.processPath("grenc.masters");
    }

	@Override
    public void contextDestroyed(ServletContextEvent event) {
        // Webapp shutdown.
    	System.out.println("MatchstickApplication shutdown!");
    }

}