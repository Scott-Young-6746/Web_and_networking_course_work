import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/**
 * Created by scott on 18/02/14.
 */
public class MeetingServer implements Runnable{
    private Socket socket;
    private ArrayList<Course> courses;
    static private Logger logger =
            Logger.getLogger("MeetingServer");
    static {
        try {
            // append to log file
            FileHandler fh = new FileHandler("meeting.log", true);
            fh.setFormatter( new SimpleFormatter() );
            logger.addHandler(fh);
            // remove standard err
            logger.setUseParentHandlers(false);
        }
        catch( Exception e) {
        }
    }

    public MeetingServer(Socket socket, ArrayList<Course> courses)throws IOException{
        this.socket=socket;
        this.courses=courses;
    }
    public void run(){
        while(true){
            try{
                try{
                    ObjectOutputStream outputStream = new ObjectOutputStream(
                            new BufferedOutputStream( socket.getOutputStream() ));
                    outputStream.flush();
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Course course = (Course)inputStream.readObject();
                    logger.info("Received request for " + course.getName());
                    for(Course c: courses){
                        if(c.getName().equals(course.getName())){
                            logger.info("Sending info for " + c.getName());
                            outputStream.writeObject(c);
                            outputStream.flush();
                        }
                    }
                    inputStream.close();
                    outputStream.close();
                }
                catch(ClassNotFoundException ex){
                    logger.warning(ex.getMessage());
                }
                logger.info("Closing: " + socket);
                socket.close();
                break;
            }
            catch(IOException ex){
                logger.severe("IOException: " + ex.getMessage());
            }
        }
    }
    private static Course createCourse(Node node){
        Element element = (Element)node;
        String name = element.getAttribute("subject") + "-" + element.getAttribute("number");
        try{
            NodeList meetTimes = element.getElementsByTagName("meeting");
            ArrayList<String> buildings = new ArrayList<String>();
            ArrayList<String> rooms = new ArrayList<String>();
            ArrayList<String> starts = new ArrayList<String>();
            ArrayList<String> ends = new ArrayList<String>();
            ArrayList<String> days = new ArrayList<String>();
            for(int i=0; i<meetTimes.getLength(); i++){
                Element meetTime = (Element)meetTimes.item(i);
                buildings.add(meetTime.getAttribute("building"));
                rooms.add(meetTime.getAttribute("room"));
                starts.add(meetTime.getAttribute("start"));
                ends.add(meetTime.getAttribute("end"));
                days.add(meetTime.getAttribute("day"));
            }
            return new Course(buildings, rooms, starts, ends, days, name);
        }
        //This Catch statement allows courses with no meet times to be added
        catch(NullPointerException ex){}
        return new Course(name);
    }
    private static void dumpThreads() {
        int count = Thread.activeCount();
        Thread[] threads = new Thread[count];
        int num = Thread.enumerate( threads );
        for( int i = 0 ; i < num; i++ ) {
            System.out.println( threads[i] );
        }
    }
    public static void main(String[] args){
        ExecutorService tpes = Executors.newFixedThreadPool(5);
        try{
            ArrayList<Course> courses = new ArrayList<Course>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("slot-book.xml");
            Element root = doc.getDocumentElement();
            NodeList list = root.getElementsByTagName("course");
            for(int i=0; i<list.getLength(); i++){
                Course course = createCourse(list.item(i));
                courses.add(course);
            }

            int port = Integer.parseInt(args[0]);
            ServerSocket serverSocket = new ServerSocket(port);
            logger.info("Listening on "+ serverSocket.getLocalPort());
            while(true){
                Socket socket = serverSocket.accept();
                logger.info("Accepted " +socket);
                tpes.execute(new MeetingServer(socket, courses));
                dumpThreads();
            }

        }
        catch(NumberFormatException ex){
            logger.severe(ex.getMessage());
        }
        catch(IOException ex){
            logger.severe(ex.getMessage());
        }
        catch(SAXException ex){
            logger.severe(ex.getMessage());
        }
        catch(ParserConfigurationException ex){
            logger.severe(ex.getMessage());
        }
        tpes.shutdown();
    }

}
