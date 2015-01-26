import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by scott on 02/03/14.
 */
public class CourseHandler implements HttpHandler{

    public void handle(HttpExchange exchange)throws IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;

        try{
            builder = factory.newDocumentBuilder();
            doc = builder.parse("./winter2014.xml");
            Element root = doc.getDocumentElement();
            NodeList list = root.getElementsByTagName("course");
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Context-Type", "text/plain");
            exchange.sendResponseHeaders(200,0);
            PrintWriter responseBody = new PrintWriter( exchange.getResponseBody());
            URI uri = exchange.getRequestURI();
            System.out.println("Handling Courses: " + (new Date() + ":" + uri ));
            String path = uri.getPath();

            String[] pathArguments = path.split("/");
            if(pathArguments.length>1 && pathArguments[2].matches("[A-Z]{4}")){
                System.out.println("Handling course: " + pathArguments[2]);
                responseBody.print("<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<title>Courses of the subject " + pathArguments[2] + "</title>\n" +
                        "</head>\n" +
                        "<body>\n");
                for(int i=0; i<list.getLength(); i++){

                    Element element = (Element)list.item(i);
                    responseBody.println("<ul>");
                    if(element.getAttribute("subject").equals(pathArguments[2])){

                        NodeList sections = element.getElementsByTagName("section");
                        for(int j=0; j<sections.getLength(); j++){

                            Element e = (Element)sections.item(j);
                            responseBody.print("<li>\nSection " + e.getAttribute("seq") + " of " + element.getAttribute("subject")
                                    + "-" + element.getAttribute("cnum") + " meets at: </br>");
                            NodeList meetings = e.getElementsByTagName("meeting");
                            for(int k=0; k<meetings.getLength(); k++){

                                Element m = (Element)meetings.item(k);
                                responseBody.print("meeting: slot=" + m.getAttribute("slot") +
                                        " days=" + m.getAttribute("days") +
                                        " starts at=" + m.getAttribute("start") +
                                        " ends at=" + m.getAttribute("end") +
                                        " in=" + m.getAttribute("bc") + "-" + m.getAttribute("room")+"</br>");
                            }
                            responseBody.println("</li>");

                        }
                    }
                    responseBody.println("</ul>");
                }


                responseBody.println("</body>");
                responseBody.println("</html>");


            }
            responseBody.close();
        }
        catch(Exception exception){
            //well this sucks, lets not get here
            System.exit(1);
        }
    }
}
