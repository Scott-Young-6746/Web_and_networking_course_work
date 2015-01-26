import static java.lang.System.out;
import java.util.Scanner;
import java.util.Random;

/**
 * Pi uses a Monte Carlo experiment to calculate PI.
 *
 * See http://en.wikipedia.org/wiki/Monte_Carlo_method
 * 
 * This is an example of calculator PI without using threads.
 */
public class PiCalculator {
    public static void main( String[] args ) {
        final int MAX_SIMULATIONS = Integer.parseInt( args[0] );
        Random r = new Random();
        int inside = 0;
        int simulations = MAX_SIMULATIONS;
	// output value at 10% complete steps
	int printInterval = MAX_SIMULATIONS / 10;
	int printCount = 0;

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
        out.printf( "aprox pi = %.12f%n", (4.0*inside)/MAX_SIMULATIONS );
    }
}
