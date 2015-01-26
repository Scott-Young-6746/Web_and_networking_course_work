import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.util.ArrayList;
import java.util.TreeSet;
/**
 * This program creates a list of buildings on a campus from two XML files called "slot-book.xml"
 * and "building.xml". It then uses "slot-book.xml" to create a list of rooms for each building
 * and a list of courses for each room, and print them to the screen in an html format to be read
 * using an internet browser.
 * @author Scott Young
 * @since 2014-1-30
 * @version 1.1
 */
public class CoursesInRooms {
    /**
     * The main method of this program, which creates and prints the html file of buildings, rooms
     * and courses.
     * @param args the command line arguments, of which there should be none, and if there are
     *             nothing is done with them anyway.
     */
    public static void main(String[] args){
        try {
            TreeSet<Building> buildingTreeSet = new TreeSet<Building>();
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder =
                    factory.newDocumentBuilder();
            /* doc is the tree document created from slot-book.xml */
            Document doc = builder.parse( "slot-book.xml" );
            Element root = doc.getDocumentElement();
            /* gets a list of all the course elements */
            NodeList list = root.getElementsByTagName("course");
            // doc2 is the tree document for all buildings
            Document doc2 = builder.parse( "building.xml" );
            Element root2 = doc2.getDocumentElement();
            //get a list of all buildings
            NodeList list2 = root2.getElementsByTagName("building");
            for(int i=0; i<list2.getLength(); i++){
                Building building = createBuilding(list2.item(i));
                buildingTreeSet.add(building);
            }
            for(int i=0; i<list.getLength(); i++){
                Course course = createCourse(list.item(i));
                for(Building b : buildingTreeSet){
                    for(int j=0; j<course.getBuildingCodes().size(); j++){
                        if(course.getBuildingCodes().get(j).equals(b.getbCode())){
                            b.addCourse(course, j);
                        }
                    }
                }
            }
            for(Building building : buildingTreeSet){
                System.out.print("<html>\n<head>\n<title>Buildings, rooms, and courses</title>\n</head>\n<body>");
                building.printBuilding();
                System.out.print("</body>");
            }


        }
        catch( java.io.IOException ex ) {
            System.out.println(ex.getMessage() );
        }
        catch( SAXException ex ) {
            System.out.println(ex.getMessage() );
        }
        catch( ParserConfigurationException ex ) {
            System.out.println(ex.getMessage() );
        }
    }

    /**
     * A helper method which aids in creating a course object from a Course element node in the xml file
     * @param node a Course node from the xml file "slot-book.xml"
     * @return A course object created from the data in the Course element node
     */
    private static Course createCourse(Node node){
        //System.err.println("Attempting to make the course class");
        Element element = (Element)node;
        String name = element.getAttribute("subject") + "-" + element.getAttribute("number");
        try{
            NodeList meetTimes = element.getElementsByTagName("meeting");
            ArrayList<String> buildings = new ArrayList<String>();
            ArrayList<String> rooms = new ArrayList<String>();
            for(int i=0; i<meetTimes.getLength(); i++){
                Element meetTime = (Element)meetTimes.item(i);
                buildings.add(meetTime.getAttribute("building"));
                rooms.add(meetTime.getAttribute("room"));
            }
            //System.err.println("We're done!");
            return new Course(buildings, rooms, name);
        }
        catch(NullPointerException ex){
            System.err.println("No meeting time for "+ name +", i'll work around that for you.");
        }

        //System.err.println("We're done!");
        return new Course(null, null, name);
    }

    /**
     * A helper method which helps create a building object from a building element node in "buildings.xml"
     * @param node A building Element node from "building.xml"
     * @return A building object created from data in the building element node
     */
    private static Building createBuilding(Node node){
        //System.err.println("Attempting to make the building class");
        Element element = (Element)node;
        String bCode = element.getAttribute("code");
        String name = element.getTextContent();
        //System.err.println("We're done!");
        return new Building(name, bCode);
    }
}
