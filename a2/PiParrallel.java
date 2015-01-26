import java.util.ArrayList;
import java.util.Random;
import static java.lang.System.out;
/**
 * This class uses threads running in parallel to make use of multi-core
 * processors in calculating an estimate for pi.
 * @author Scott Young
 * @version 1.0
 * @see java.lang.Thread
 * @since 2014-1-30
 */
public class PiParrallel {
    /**
     * The main method of this program, It takes in an array of string arguments
     * from the commandline at run time and uses them to assign the number of
     * iterations to be done, and the number fo threads to use.
     * @param args An array of strings that gets read of teh command line,
     *             there should be two arguments, both integers, first being
     *             the number of iterations to do, and the second being the
     *             number of threads to make to do the simulations.
     * @throws Exception which is currently not handled in any way
     */
    public static void main( String[] args ) throws Exception{
        final int MAX_SIMULATIONS = Integer.parseInt( args[0] );
        Random r = new Random();
        int inside = 0;
        int numberOFThreads = Integer.parseInt(args[1]);
        // output value at 10% complete steps
        int printInterval = MAX_SIMULATIONS / 10;
        ArrayList<CheckInside> threads = new ArrayList<CheckInside>();
        for(int i=0; i<numberOFThreads; i++){
            threads.add(new CheckInside(r, MAX_SIMULATIONS/numberOFThreads, printInterval));
        }
        for(CheckInside checkInside : threads){
            checkInside.start();
        }
        for(CheckInside checkInside : threads){
            checkInside.join();
        }
        for(CheckInside checkInside : threads){
            inside+=checkInside.getInside();
        }
        out.printf( "aprox pi = %.12f%n", (4.0*inside)/MAX_SIMULATIONS );
    }
}

/**
 * This is a thread class which is used to estimate pi via the Monte Carlo method
 */
class CheckInside extends Thread {
    /**
     * Random number generator
     */
    private Random r;
    /**
     * The number of randomly generated points that land inside the circle
     */
    private int inside = 0;
    /**
     * The maximum number of simulations that this thread will run
     */
    private int MAX_SIMULATIONS;
    /**
     * the number of simulations left to run
     */
    private int simulations;
    /**
     * A Counter used to determine when to print the current estimate for pi.
     */
    private int printCount = 0;
    /**
     * The number of iterations to be performed between prints.
     */
    private int printInterval;

    /**
     * The constructor for our CheckInside object, which takes in a random number generator,
     * the maximum number of simulations to do, and the print interval for printing current estimates
     * @param r A random number generator
     * @param MAX_SIMULATIONS The maximum number of simulations to be done in this thread
     * @param printInterval The number of iterations between prints of the current estimate
     */
    public CheckInside(Random r, int MAX_SIMULATIONS, int printInterval) {
        this.r = r;
        this.MAX_SIMULATIONS = MAX_SIMULATIONS;
        this.simulations = MAX_SIMULATIONS;
        this.printInterval = printInterval;
    }

    /**
     * A getter for the inside variable, used to determine the number of points which
     * land inside the circle in this thread.
     * @return This objects Inside variables value
     */
    public int getInside(){
        return inside;
    }

    /**
     * The run method of this object, which when run, uses the above variables and
     * their values to obtain an estimate for pi using the Monte Carlo Method
     */
    public void run(){
        do {
            double x = r.nextDouble();
            double y = r.nextDouble();
            if ( Math.hypot( x, y ) <= 1.0 ) {
                inside += 1;
            }
            if ( printCount >= printInterval ) {
                printCount = 0;
                int n = MAX_SIMULATIONS - simulations;
                if ( n != 0 ) {
                    out.printf( "aprox pi = %.12f%n", (4.0*inside)/n );
                }
            }
            else {
                printCount++;
            }
            simulations--;
        } while ( simulations > 0 );
    }
}
