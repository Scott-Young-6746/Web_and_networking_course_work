import java.util.ArrayList;

/**
 * This is a class for representing a room in a school's building.
 * These rooms contain courses in an Array List, but they may exist
 * without courses in them as well, allowing them to represent a room
 * in which no classes are held.
 * @author Scott Young
 * @version 1.0
 * @see java.lang.Comparable
 * @since 2014-1-30
 *
 */
public class Room implements Comparable<Room>{
    /**
     * A Number to represent the room's room number.
     */
    private int Number;
    /**
     * An Array List to hold the courses that are in this room.
     */
    private ArrayList<Course> courses = new ArrayList<Course>();

    /**
     * The required compare function that allows rooms to be sorted in sorted collections, such as
     * binary trees.
     * @param r A room object to compare this room to.
     * @return and integer number for sorting room objects via comparison following the usual convention.
     */
    public int compareTo(Room r){
        return Number - r.getNumber();
    }

    /**
     * The getter for this room objects room number.
     * @return the value of this room's Number
     */
    public int getNumber() {
        return Number;
    }

    /**
     * A constructor for creating room objects. Takes an argument that represents the rooms number.
     * @param number This room objects room number.
     */
    public Room(int number) {

        Number = number;
    }

    /**
     * A getter for the Array List of courses in this room object
     * @return An Array List of courses that are held in this room.
     */
    public ArrayList<Course> getCourses() {
        return courses;
    }

    /**
     * Adds the Course to the Array List of courses in this room object.
     * @param course A course object to be added to this room object.
     */
    public void addCourse(Course course){
       this.courses.add(course);
    }

    /**
     * A Method used to print out this room's number, followed by all
     * courses which are held in this room, separated via spaces.
     */
    public void printRoom(){
        System.out.print(" ");
        for(Course course : courses){
            System.out.print(" " + course.getName());
        }
    }
}
