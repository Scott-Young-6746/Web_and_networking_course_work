import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by scott on 02/03/14.
 */
public class FaviconHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException{
        // setup the reply
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(404, 0);
        PrintWriter responseBody = new PrintWriter( exchange.getResponseBody());
        responseBody.println( "Not Found" );
        responseBody.close();

        // log request
        URI uri = exchange.getRequestURI();
        System.out.println("Handling Favicon: " + (new Date()) + ":" + uri );

        System.out.println("Headers:" );
        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            List values = requestHeaders.get(key);
            System.out.println( key + " = " + values.toString() );
        }
    }
}
