import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by scott on 02/03/14.
 */
public class HttpCourseServer {
    public static void main(String[] args) throws IOException{
        InetSocketAddress address = new InetSocketAddress(8984);
        HttpServer server = HttpServer.create(address, 5);

        server.createContext("/favicon.ico", new FaviconHandler());
        server.createContext("/courses/", new CourseHandler());
        server.createContext("/rooms/", new RoomsHandler());
        server.createContext("/courses", new CourseHandler());
        server.createContext("/rooms", new RoomsHandler());
        server.createContext("/", new WelcomeHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is listening on port 8984");
    }
}
