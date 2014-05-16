/**
 * Entropy.java -- the main program. It creates threads which calculate the entropy. 
 * 					It also creates a graph displaying the entropy, along with the maximum and minimum.
 * @author Kerry Zhao (kxz8411)
 * 
 */

import edu.rit.util.Random;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.plot.Plot;
import edu.rit.numeric.plot.Strokes;
import java.awt.Color;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Entropy {

	// an arraylist that holds the threads
	private static ArrayList<Thread> threadList = new ArrayList<Thread>();
	// an arraylist that holds the runnable objects
	private static ArrayList<entropyRunnable> runnableList = new ArrayList<entropyRunnable>();
	// the points for the entropy graph
	private static ListXYSeries xyPoints = new ListXYSeries();
	// the points for the entropy + standard deviation graph
	private static ListXYSeries xyPointsPlus = new ListXYSeries();
	// the points for the entropy - standard deviation graph
	private static ListXYSeries xyPointsMinus = new ListXYSeries();
	// the monitor that limits the threads
	private static entropyMonitor monitor = new entropyMonitor(xyPoints,
			xyPointsPlus, xyPointsMinus);
	// the plot that will be displayed
	private static Plot plot = new Plot();

	
	// main
	public static void main(String[] args) {
		// check that inputs are correct
		if (args.length != 3 || !(args[0].matches("[0-9]*"))
				|| !(args[1].matches("[0-9]*")) || !(args[2].matches("[0-9]*"))) {
			System.err.println("Error: incorrect number of arguments");
			usage();
		}
		int vertices = Integer.parseInt(args[1]);
		int trials = Integer.parseInt(args[2]);
		// create runnables
		for (double i = 0.00; i < 1.01; i += 0.01) {
			entropyRunnable runnable = new entropyRunnable(
					Long.parseLong(args[0]), vertices, trials, i, monitor);
			runnableList.add(runnable);
		}
		System.out.println("p    mean H  stddev H");
		// create and start threads
		for (entropyRunnable runnable : runnableList) {
			Thread newThread = new Thread(runnable);
			threadList.add(newThread);
			newThread.start();
		}
		// the plot methods
		String titleString = "Entropy vs. Edge Probability, V = " + vertices;
		DecimalFormat df = new DecimalFormat("0.0");
		// format the x axis
		plot.xAxisLength(360);
		plot.xAxisMajorDivisions(10);
		plot.xAxisTickFormat(df);
		plot.xAxisStart(0.0);
		plot.xAxisEnd(1.0);
		// format the y axis
		plot.yAxisLength(222);
		plot.yAxisMajorDivisions(5);
		plot.yAxisTickFormat(df);
		plot.yAxisStart(0.0);
		plot.yAxisEnd(5.0);
		// titles
		plot.xAxisTitle("Edge Probability");
		plot.yAxisTitle("Entropy");
		plot.plotTitle(titleString);
		// no dots, only strokes
		plot.seriesDots(null);
		plot.seriesStroke(Strokes.solid(2));
		// plot first series in black, size 2 stroke
		plot.xySeries(xyPoints);
		plot.seriesStroke(Strokes.solid(1));
		plot.seriesColor(Color.RED);
		// plot series +- standard deviation in red, size 1 stroke
		plot.xySeries(xyPointsPlus);
		plot.xySeries(xyPointsMinus);
		// set visible
		plot.getFrame().setVisible(true);

	}

	// usage message
	private static void usage() {
		System.err.println("Usage: java Entropy <seed> <Vertices> <Trials>");
		System.exit(1);
	}

}
