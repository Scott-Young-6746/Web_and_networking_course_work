
import java.util.ArrayList;

/**
 * Created by scott on 30/01/14.
 */

/**
 * This is a class for managing courses in a slot book
 * @author scott
 * @version 2.0
 * @see java.lang.Comparable
 * @since 2014-01-30
 */
public class Course implements Comparable<Course>{
    /**
     * The ArrayList of building codes where the ith building code matches the ith room number.
     */
    private ArrayList<String> buildingCodes = new ArrayList<String>();
    /**
     * The ArrayList of room numbers where the ith room number matches the ith building code.
     */
    private ArrayList<String> roomNumbers = new ArrayList<String>();
    /**
     * The name of the course in the format [a-zA-Z]{4}-\\d{4}
     */
    private String name;


    /**
     * A compareTo method, used for sorting.
     * @param c Another course object to compare this one too using the names of the courses
     * @return an integer that is negative if c comes after this course, 0 if they are the same course
     * and a positive integer if c comes before this course.
     */
    public int compareTo(Course c){
        return this.name.compareTo(c.getName());
    }

    /**
     * Uses a Regex to determine if the name given is valid, and if so, sets the course name to be that name.
     * @param name sets the name of the course, must be a valid name.
     */
    public void setName(String name) throws IllegalArgumentException {
        if(name.matches("[a-zA-z]{4}-\\d{4}")){
            this.name = name;
        }

        else{
            throw new IllegalArgumentException("Must be of the format '[a-zA-Z]{4}-\\d{4}");
        }
    }

    /**
     * A getter method for the name String
     * @return returns the name of the course in the dept-num format (eg COMP-3715)
     */
    public String getName(){
        return this.name;

    }

    /**
     * A getter method for the buildingCodes ArrayList
     * @return An ArrayList of building codes that this course uses
     */
    public ArrayList<String> getBuildingCodes() {
        return buildingCodes;
    }

    /**
     * A getter method for the roomNumbers ArrayList
     * @return An ArrayList of room numbers that this course uses
     */
    public ArrayList<String> getRoomNumbers() {
        return roomNumbers;
    }

    /**
     * The constructor you use to create a course object for the purpose of checking if a course is in a room
     *
     * @param buildingCodes an ArrayList of buildingCodes that this class meets in
     * @param roomNumbers an ArrayList of room numbers, the ith number should match the ith building code
     * @param name The name of the course in the format of a 4 character department followed by a 4 digit course number
     */
    public Course(ArrayList<String> buildingCodes, ArrayList<String> roomNumbers, String name) {
        this.buildingCodes = buildingCodes;
        this.roomNumbers = roomNumbers;
        this.name = name;
    }
}
