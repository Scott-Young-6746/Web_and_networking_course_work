import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;

/**
 * Created by scott on 02/03/14.
 */
public class WelcomeHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException{
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Context-Type", "text/plain");
        exchange.sendResponseHeaders(200,0);
        PrintWriter responseBody = new PrintWriter( exchange.getResponseBody());
        URI uri = exchange.getRequestURI();
        System.out.println("Handling Welcome: " + (new Date() + ":" + uri ));
        String path = uri.getPath();
        //Read the welcome.html file and print it response body
        responseBody.println(new Scanner(new File("./welcome.html")).useDelimiter("//A").next());
        responseBody.close();
    }
}
