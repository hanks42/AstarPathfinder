/**
 * GridMap is the class that represents a 2 dimensional map that can be placed on a
 * grid squares. Each square may be traversable or not and it may also have a cost
 * associated with moving into the square.
 * <p>
 * GridMap provides functionality for converting itself into a graph for use with
 * pathfinding routines, functionality to turn the resultant path back into a gridmap,
 * functionality to read the the map in from a file, write it out to a file and write
 * a path returned from a pathfinding routine to a file for inspection.
 * 
 * @author Jonathan Hanks (jonhanks@gmail.com)
 * @version 1.0
 */

import java.io.*;
import java.util.*;

public class GridMap 
{
	/*
	 * Members
	 */
	
	/**
	 * The width of the gridmap
	 */
	private int m_nWidth;
	
	/**
	 * The height of the gridmap 
	 */
	private int m_nHeight;
	
	/**
	 * The x value of the start point to search for a path in the map
	 */
	private int m_nStartPointX;
	
	/**
	 * The y value of the start point to search for a path in the map
	 */
	private int m_nStartPointY;

	/**
	 * The x value of the goal point to search for a path in the map
	 */
	private int m_nGoalPointX;
	
	/**
	 * The y value of the goal point to search for a path in the map
	 */
	private int m_nGoalPointY;
	
	/**
	 * A 2d array that contains the costs for moving between the points on the map
	 * any 0 cost is assumed to be a position you can't move into.
	 */
	private int [][] m_nMapData;
	
	/**
	 * A 2d array of the map that corresponds with the nodes in the graph
	 * this is used to determine what the resultant path from the pathfinder means
	 */
	private int [][] m_nNodeIDs;
	
	/**
	 * Whether or not the grid map has been initialized
	 */
	private boolean m_bInitialized;
	
	/**
	 * The graph that represents the gridmap
	 */
	private Graph m_gridMapGraph;
	
	/*
	 * Functions
	 */
	
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
	public boolean Init(String zGridMapFileName)
	{
		m_bInitialized = false;
		
		// create the graph
		m_gridMapGraph = new Graph();
		
		try	// beware input file exceptions
		{
			// create a new file reader
			BufferedReader mapFR = new BufferedReader(new FileReader(zGridMapFileName));	

			String zCurrLine = "";
			
			// read each line individually and parse it
			
			try // beware the number format exceptions
			{
				// read the width
				zCurrLine = mapFR.readLine();
				m_nWidth = Integer.parseInt(zCurrLine);
				
				// read the height
				zCurrLine = mapFR.readLine();
				m_nHeight = Integer.parseInt(zCurrLine);
				
				// create the array to hold the data and node ids
				m_nMapData = new int[m_nWidth][m_nHeight];
				m_nNodeIDs = new int[m_nWidth][m_nHeight];
				
				// read the start point
				zCurrLine = mapFR.readLine();
				m_nStartPointX = Integer.parseInt(zCurrLine);
				zCurrLine = mapFR.readLine();
				m_nStartPointY = Integer.parseInt(zCurrLine);
				
				// read the goal point
				zCurrLine = mapFR.readLine();
				m_nGoalPointX = Integer.parseInt(zCurrLine);
				zCurrLine = mapFR.readLine();
				m_nGoalPointY = Integer.parseInt(zCurrLine);
				
				// read in height number of lines
				for (int y = 0; y < m_nHeight; y++)
				{
					// read the map line
					zCurrLine = mapFR.readLine();
					
					// parse width numbers from it
					for (int x = 0; x < m_nWidth; x++)
					{
						// convert each character plus its trailing space to a new string
						String zCurrNum = zCurrLine.substring(2*x, (2*x) + 1);
						// convert that string to the cost number for that tile
						m_nMapData[x][y] = Integer.parseInt(zCurrNum);
					}
				}
				
				// close the stream
				mapFR.close();
			}
			catch (NumberFormatException e)
			{
				System.err.println("Invalid file format...exiting!");
				return false;
			}
			
			m_bInitialized = true;
		}
		catch (FileNotFoundException e)
		{
			// couldn't find the file so write to the error stream
			System.err.println("Map file not found...exiting!");
			return false;
		}
		catch (IOException e)
		{
			// io exception so tell the error stream
			System.err.println("Map file io exception...exiting!");
			return false;
		}
		
		return m_bInitialized;
	}
	
	/**
	 * This will generate the graph that represents the gridmap
	 * 
	 * @return A boolean indicating whether the graph generation succeeded
	 */
	private boolean generateGraph()
	{
		// make sure we've initialized the gridmap
		if ( !m_bInitialized )
		{
			return false;
		}
		
		// create a node for each accessable point in the map
		for (int y = 0; y < m_nHeight; y++)
		{
			for (int x = 0; x < m_nWidth; x++)
			{
				// if this is a valid node create it
				if (m_nMapData[x][y] > 0)
				{
					m_nNodeIDs[x][y] = m_gridMapGraph.addNode(x, y);
					
				}
			}
		}
		
		// a second loop connects all the connectable points
		// the graph needs all the nodes before connections can be made
		for (int y = 0; y < m_nHeight; y++)
		{
			for (int x = 0; x < m_nWidth; x++)
			{
				// if this is a valid node check its connections
				if (m_nMapData[x][y] > 0)
				{
					// check all 8 posible directions while making sure they are in the grid
					for (int i = x - 1; i <= x + 1; i++)
					{
						for (int j = y - 1; j <= y + 1; j++)
						{
							// make sure this point is in the grid
							if (	(i >= 0) 
									&& (i < m_nWidth) 
									&& (j >= 0) 
									&& (j < m_nHeight)
									&& ((i != x) || (j != y))	// not the same point
								)
							{
								// this point is in the grid 
								// is it a valid node to travel to?
								if (m_nMapData[i][j] > 0)
								{
									m_gridMapGraph.addConnection(
											m_nNodeIDs[x][y], 
											m_nNodeIDs[i][j], 
											m_nMapData[i][j]);
								}
							}
						}
					}
				}
			} // end connection loop
			
		}
		
		// we succeeded so return true
		return true;		
	}
	
	/**
	 * This function will perform the A star search on the gridmap and output the data
	 * to the specified file
	 * 
	 * @param outputFile		The name of the output file to output the results to
	 * @return					A boolean indicating success or failure
	 */
	public boolean performAStarSearch(String outputFile)
	{
		// make sure the gridmap has been initialized
		if (!m_bInitialized)
		{
			return false;
		}
		
		// generate the graph that corresponds to the gridmap
		generateGraph();
		
		// perform the search
		Vector vSearchResults = Astar.performSearch(m_gridMapGraph,
													m_nNodeIDs[m_nStartPointX][m_nStartPointY],
													m_nNodeIDs[m_nGoalPointX][m_nGoalPointY]);
		
		// output the results
		boolean bSuccess = outputResults(outputFile, vSearchResults);	
		
		System.out.println("Completed!");
		
		return bSuccess;
		
	}
	
	/**
	 * This function does the output logic for the results of the search
	 * 
	 * @param zOutputFile		The name of the output file
	 * @param vResults			The results of the search
	 * @return					Whether the output succeeded or not
	 */
	private boolean outputResults(String zOutputFile, Vector vResults)
	{
		try
		{
			// create the output stream
			BufferedWriter outWriter = new BufferedWriter(new FileWriter(zOutputFile));
			
			// go through each point in the grid and diagram the path
			// Use X for obstacle squares o for clear squares that aren't in the path
			// and v for squares in the path
			for (int y = 0; y < m_nHeight; y++)
			{
				for (int x = 0; x < m_nWidth; x++)
				{
					
					// start and goal squares
					if ((x == m_nStartPointX) && (y == m_nStartPointY))
					{
						outWriter.write("S");
					}
					else if ((x == m_nGoalPointX) && (y == m_nGoalPointY))
					{
						outWriter.write("G");
					}
					// obstacle squares
					else if (m_nMapData[x][y] == 0)
					{
						outWriter.write("X");
					}
					else
					{
						if (isInList(vResults, m_nNodeIDs[x][y]))
						{
							// this is in the list so output a v
							outWriter.write("v");
						}
						else
						{
							// not in the results list
							outWriter.write("o");
						}
					}
					// output a space
					outWriter.write(" ");
				}
				// place a return in the file
				outWriter.newLine();
			}
			
			// flush the stream
			outWriter.flush();
			// close the stream
			outWriter.close();
		}
		catch (IOException e)
		{
			System.err.println("Cannot output to file...");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Whether the requested int is in the list
	 * 
	 * @param vList			The list to check
	 * @param nNum			The number to check
	 * @return				True if it is in the list and false otherwise
	 */
	private boolean isInList(Vector vList, int nNum)
	{
		Integer nToCheck = new Integer(nNum);
		for (int i = 0; i < vList.size(); i++)
		{
			if (vList.contains(nToCheck))
			{
				// found in the list
				return true;
			}
		}
		
		// not found in the list
		return false;
	}
}
