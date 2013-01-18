Author: Jon Hanks
Email: jonhanks@gmail.com

This is a code sample project I did a while back for Java. It implements an Astar pathfinding
routine.

To run it from the command line type

java Pathfinder

The program will read in a map file of the below format then it will generate a file called 'Results.txt' which has
a path made up of characters (v) that is the optimal path from start point (S) to goal point (G). Points labeled 'X' 
are obstacles that were avoided and points labeled 'o' were not in the results list.


	/**
	 * Initializes the grid map. It will read the gridmap data from a properly formatted text file.
	 * Very little error checking is done here, this is a good thing to add later.
	 * 
	 * @param zGridMapFileName		The name of the file that contains the gridmap definition
	 * 								The format for the file is as follows
	 * <ul>
	 * <li>								Line 1 - Width of the map
	 * <li>								Line 2 - Height of the map
	 * <li>								Line 3 - X coordinate of start point
	 * <li>								Line 4 - Y coordinate of start point
	 * <li>								Line 5 - X coordinate of goal point
	 * <li>								Line 6 - Y coordinate of goal point
	 * <li>								Line 7 to Line Height + 6 - Data of the map
	 * 										Each line should have width numbers seperated by 
	 * 										a single space with each number being the cost to
	 *										move into that particular position and zero being
	 *										a point that can not be moved into (note that cost
	 *										data can only be single digit)
	 *</ul>
	 *@return 						<code>true</code> If the map file was read in with no errors
	 *								<code>false</code> If the map file had any read errors
	 */