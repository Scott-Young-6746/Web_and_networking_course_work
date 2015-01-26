import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Date;

/**
 * Created by scott on 02/03/14.
 */
public class RoomsHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;

        try{
            builder = factory.newDocumentBuilder();
            doc = builder.parse("./winter2014.xml");
            Element root = doc.getDocumentElement();
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Context-Type", "text/plain");
            exchange.sendResponseHeaders(200,0);
            NodeList list = root.getElementsByTagName("course");
            PrintWriter responseBody = new PrintWriter( exchange.getResponseBody());
            URI uri = exchange.getRequestURI();
            System.out.println("Handling Room: " + (new Date() + ":" + uri ));
            String path = uri.getPath();
            String[] pathArguments = path.split("/");
            responseBody.print("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>Courses in the room " + pathArguments[2] +"-"+pathArguments[3]+ "</title>\n" +
                    "</head>\n" +
                    "<body>\n");
            boolean isBuilding = pathArguments[2].matches("[A-Z]{1,2}") || pathArguments[2].matches("[A-Z]{3}");
            boolean isRoom = pathArguments[3].matches("[0-9]{4}");
            if(pathArguments.length>2 && isBuilding && isRoom){
                System.out.println("Handling room: " + pathArguments[2] + "-" + pathArguments[3]);
                for(int i=0; i<list.getLength(); i++){
                    Element element = (Element)list.item(i);
                    responseBody.println("<ul>");
                    NodeList sections = element.getElementsByTagName("section");
                    for(int j=0; j<sections.getLength(); j++){
                        Element e = (Element)sections.item(j);
                        NodeList meetings = e.getElementsByTagName("meeting");
                        for(int k=0; k<meetings.getLength(); k++){
                            Element m = (Element)meetings.item(k);
                            if(m.getAttribute("bc").equals(pathArguments[2]) && m.getAttribute("room").equals(pathArguments[3])){
                                responseBody.print("<li>\nSection " + e.getAttribute("seq") + " of " + element.getAttribute("subject")
                                        + "-" + element.getAttribute("cnum") + " meets at: </br>");
                                responseBody.print("meeting: slot=" + m.getAttribute("slot") +
                                        " days=" + m.getAttribute("days") +
                                        " starts at=" + m.getAttribute("start") +
                                        " ends at=" + m.getAttribute("end") +
                                        " in=" + m.getAttribute("bc") + "-" + m.getAttribute("room")+"</br>");
                                responseBody.println("</li>");
                            }
                        }


                    }
                    responseBody.println("</ul>");
                }

            }



            responseBody.println("</body>");
            responseBody.println("</html>");
            responseBody.close();

        }
        catch(Exception e){
            //Lets hope this never happens
            System.exit(1);
        }
    }
}
