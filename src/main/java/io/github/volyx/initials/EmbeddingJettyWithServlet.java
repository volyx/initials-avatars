package io.github.volyx.initials;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.Map;

public class EmbeddingJettyWithServlet {
    public static void main(String[] args) throws Exception {

        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        final int port = Integer.parseInt(System.getenv("PORT"));

        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setContextPath("/");

        server.setHandler(context);

        context.addServlet(new ServletHolder(new InitialsAvatarsServlet()), "/*");
        server.start();

    }
}
