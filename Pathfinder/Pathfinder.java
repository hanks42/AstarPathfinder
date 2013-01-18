/**
 * This is the pathfinder program, it is a program that reads in a square grid map from
 * a map file and then uses the A* pathfinding algorithm to find a path between the requested
 * start and end points in the map
 * 
 * @author Jonathan Hanks (jonhanks@gmail.com)
 * @version 1.0
 *
 */
public class Pathfinder {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		GridMap myGM = new GridMap();
		
		// initialize the gridmap with the data from the map file
		myGM.Init("Map.txt");
		
		// perform the A* search over the map
		myGM.performAStarSearch("Results.txt");
	}

}
