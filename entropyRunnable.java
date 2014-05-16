/**
 * entropyRunnable.java -- the runnable class. It overrides the threads' run() method. 
 *  
 * @author Kerry Zhao (kxz8411)
 * 
 */

import java.util.ArrayList;
import edu.rit.util.Random;
import edu.rit.numeric.ListSeries;

public class entropyRunnable implements Runnable {
	
	private Long PRNGnumber;
	private int vertices;
	private int trials;
	private double numericalOrder;
	private entropyMonitor monitor;
	// series that contains the entropies; will be used to find mean and stddev
	private ListSeries entropies = new ListSeries();
	
	/**
	 * constructor
	 * @param PRNGnumber -- the args[0] taken from inputs
	 * @param vertices -- the args[1] taken from inputs; the vertices of the graphs
	 * @param trials -- the args[2] taken from inputs; the number of trials that will be completed
	 * @param numericalOrder -- the "probability" 
	 * @param monitor -- the monitor
	 */
	public entropyRunnable(Long PRNGnumber, int vertices, int trials, double numericalOrder, entropyMonitor monitor) {
		this.PRNGnumber = PRNGnumber;
		this.vertices = vertices;
		this.trials = trials;
		this.numericalOrder = numericalOrder;
		this.monitor = monitor;
	}
	
	// the overridden run() method
	public void run() {
		// creates pseudorandom number generator
		Random PRNG = new Random(PRNGnumber); 
		// for each trial
		for (int i = 0; i < trials; i++) {
			// list of vertices
			ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
			// list of degrees
			ArrayList<Integer> degrees = new ArrayList<Integer>();
			// list of probabilites
			ArrayList<Double> probabilities = new ArrayList<Double>();
			// list of calculations done to the probabilities
			ArrayList<Double> sums = new ArrayList<Double>();
			// for each vertex
			for (int h = 0; h < vertices; h++) {
				// create a vertex object and put into a list of vertices
				Vertex v = new Vertex();
				vertexList.add(v);
			}
			// for each vertex
			for (int j = 0; j < vertices; j++) {
				// simulate creation of graph (maximum (vertex - 1) connections)
				for (int k = j; k < vertices - 1; k++) {
					// check if there is a connection
					if (PRNG.nextDouble() < numericalOrder) {
						// vertex at j +1 connection
						vertexList.get(j).addToConnections();
						// vertex at k+1 +1 connection
						vertexList.get(k+1).addToConnections();
					}
				}
			}
			// go through vertex list and place connections in a list
			for (Vertex v: vertexList) {
				int connectionCounter = v.getConnections();
				degrees.add(connectionCounter);
			}
			// for every element in degrees, count same numbers and put into probabilities
			for (int l = 0; l < degrees.size(); l++) {
				double connectionAmount = 0.00;
				for (int num: degrees) {
					if (num == l) {
						connectionAmount++;
					}
				}
				probabilities.add(connectionAmount);
			}
			// for every element in probabilities, do calculations
			for (double num: probabilities) {
				double first = (num / vertices);
				if (first != 0.00) {
					double second = ((Math.log10(num / vertices)) / (Math.log10(2)));
					double summation = first * second;
					sums.add(summation);
					}
			}
			// sum up the calculations
			Double total = 0.00;
			for (double num: sums) {
				total += num;
			}
			// multiply by -1 as per the formula
			total = total * -1.00;
			// add result to series
			entropies.add(total);
		}
		// find the mean
		double mean = 0;
		for (double num: entropies) {
			mean += num;
		}
		mean = mean / (entropies.length());
		
		// find the variance and then the standard deviation
		double variance = 0.00;
		for (double num: entropies) {
			double variancePart = num - mean;
			variancePart = variancePart * variancePart;
			variance += variancePart;
		}
		variance = variance / vertices;
		double stddev = Math.sqrt(variance);
		
		// format the p, mean(H), and stddev(H)
		String numericalOut = String.format("%.2f", numericalOrder);
		String meanOut = String.format("%.5f", mean); 
		String stddevOut = String.format("%.5f", stddev);
		
		// monitor method to limit threads
		int loopout = 0;
		while (loopout != -1) {
			loopout = monitor.printOut(numericalOut, meanOut, stddevOut);
		}
		
	}

}
