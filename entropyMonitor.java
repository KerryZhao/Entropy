/**
 * entropyMonitor.java -- the monitor class. It limits the threads from printing out of order.
 * 				It also modifies the LisyXYSeries from the Entropy.java class.  
 * 
 * @author Kerry Zhao (kxz8411)
 * 
 */

import edu.rit.numeric.ListXYSeries;

public class entropyMonitor {

	private double counter = 0.00;
	private ListXYSeries xyPoints;
	private ListXYSeries xyPointsPlus;
	private ListXYSeries xyPointsMinus;

	/**
	 * constructor
	 * @param xyPoints -- series that holds the points for the entropy graph
	 * @param xyPointsPlus -- series that holds the points for the entropy + standard deviation graph
	 * @param xyPointsMinus -- series that holds the points for the entropy - standard deviation graph
	 */
	public entropyMonitor(ListXYSeries xyPoints, ListXYSeries xyPointsPlus, ListXYSeries xyPointsMinus) {
		this.xyPoints = xyPoints;
		this.xyPointsPlus = xyPointsPlus;
		this.xyPointsMinus = xyPointsMinus;
	}

	/**
	 * synchronized method that both prints out and modifies the series 
	 * @param number -- the "probability" and the way the threads are ordered
	 * @param mean -- the mean of the entropies after T trials
	 * @param stddev -- the standard deviation
	 * @return -1 if incremented, else 0
	 */
	public synchronized int printOut(String number, String mean, String stddev) {
		String counterFormatted = String.format("%.2f", counter);
		if (number.equals(counterFormatted)) {
			System.out.println(number + " " + mean + " " + stddev);
			Double xPoint = Double.parseDouble(number);
			Double yPoint = Double.parseDouble(mean);
			Double deviation = Double.parseDouble(stddev);
			xyPoints.add(xPoint, yPoint);
			Double plus = yPoint + deviation;
			Double minus = yPoint - deviation;
			xyPointsPlus.add(xPoint, plus);
			xyPointsMinus.add(xPoint, minus);
			counter += 0.01;
			notifyAll();
			return -1;
		} else {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	

}
