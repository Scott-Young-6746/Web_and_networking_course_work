
import java.util.TreeSet;

/**
 * This is a class used to represent a building object on a school campus
 * @author Scott Young
 * @version 1.1
 * @since 2014-2-5
 * @see java.lang.Comparable
 */
public class Building implements Comparable<Building>{
    /**
     * The buildings name
     */
    private String name;
    /**
     * The buildings building code
     */
    private String bCode;
    /**
     * A Tree Set of all the rooms in the building
     */
    private TreeSet<Room> rooms = new TreeSet<Room>();
    /**
     * A compare to function to allow comparing of buildings
     * @param b the building to which we are comparing this building to
     * @return an integer value to sort the buildings by comparison, follows the usual convetions
     */
    public int compareTo(Building b){
        return bCode.compareTo(b.getbCode());
    }
    /**
     * The constructor for a building object, which gives the building it's name and building code.
     * @param name The buildings name
     * @param bCode The buildings building code
     */
    public Building(String name, String bCode) {
        this.name = name;
        this.bCode = bCode;
    }
    /**
     * a getter for the buildings building code
     * @return the buildings building code
     */
    public String getbCode() {
        return bCode;
    }
    /**
     * a setter for the buildings building code
     * @param bCode the new building code to be used
     */
    public void setbCode(String bCode) {
        this.bCode = bCode;
    }
    /**
     * A getter for the buildings name
     * @return the buildings name
     */
    public String getName() {

        return name;
    }
    /**
     * A setter for the buildings name
     * @param name The buildings new name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * A getter for the tree set of rooms in this building.
     * @return the tree set containing the rooms in this building.
     */
    public TreeSet<Room> getRooms() {
        return rooms;
    }
    /**
     * A setter for this buildings tree set of rooms
     * @param rooms The new tree set of rooms to be used.
     * @deprecated
     */
    public void setRooms(TreeSet<Room> rooms) {
        this.rooms = rooms;
    }
    /**
     * A method for adding rooms to this building
     * @param room The room to be added to this building
     */
    private void addRoom(Room room){
        this.rooms.add(room);
    }
    /**
     * A method for adding courses to this building
     * @param course The course to add to this building
     * @param i An integer index used to get the room number for the course from the courses
     *          Array List of room numbers.
     */
    public void addCourse(Course course, int i){
        int rnum = Integer.parseInt(course.getRoomNumbers().get(i));
        boolean flag = false;
        for(Room room:rooms){
            if(room.getNumber() == rnum){
                flag = true;
                room.addCourse(course);
            }
        }
        if(!flag){
            Room room = new Room(rnum);
            room.addCourse(course);
            rooms.add(room);
        }
    }
    /**
     * A Method used to print out this buildings name, followed by all of the rooms
     * in the building, and all of the courses in those rooms, uses an html format.
     */
    public void printBuilding(){
        System.out.print("<ul><h2>" + name + "</h2>\n\n");
        for(Room room : rooms){
            System.out.print("<li>"+bCode+room.getNumber());
            room.printRoom();
            System.out.print("</li>\n");
        }
        System.out.print("</ul>\n\n");
    }
}
