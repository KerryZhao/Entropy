/**
 * Vertex.java -- class for the vertex object. Has a field called connections which counts how many 
 * 				connections this particular vertex has. 
 * 
 * @author Kerry Zhao (kxz8411)
 * 
 */

public class Vertex {

	// an int that counts the number of connections
	private int connections = 0;
	
	// constructor
	public Vertex() {
		
	}
	
	/**
	 * addToConnections -- allows for other classes to change the connections variable
	 */
	public void addToConnections() {
		connections++;
	}
	
	/**
	 * getConnections -- allows for other classes to access the connections variable
	 * @return connections
	 */
	public int getConnections() {
		return connections;
	}
	
}
