/**
 * The Graph class is a class that represents a generic graph that contains any
 * number of nodes that connect to any number of other nodes. Each connection also
 * may have a cost associated with traversing it.
 * <p>
 * Any cost of zero is assumed to be an untravelable direction along this node connection
 * 
 * @author Jonathan Hanks (jonhanks@gmail.com)
 * @version 1.0
 * 
 */

import java.util.*;

public class Graph 
{
	/*
	 * Members
	 */
	/**
	 * The number of nodes in the graph
	 */
	private int m_nNumNodes;
	
	/**
	 * The vector holding all the nodes in the graph
	 */
	private Vector m_vNodes;
	
	/*
	 * Functions
	 */
	/**
	 * Default constructor to create the variables for this class
	 */
	Graph()
	{
		m_vNodes = new Vector();
	}
	
	/**
	 * This adds a new node to the graph and returns the id number of the node
	 * 
	 * @return The id number of the node added
	 * @param nXPos 	The x position in the world of the node
	 * @param nYPos		The y position in the world of the node
	 */
	public int addNode(int nXPos, int nYPos)
	{
		// create the node
		Node newNode = new Node(nXPos, nYPos);
		int nNodeID = newNode.getID();
		
		// add the node to the vector
		m_vNodes.add(newNode);
		// increment the number of nodes in the graph
		m_nNumNodes++;
		
		// return the nodes id number
		return nNodeID;
	}
	
	/**
	 * This adds a connection into the graph
	 * 
	 * @param nStartNodeID 		The start node of the connection
	 * @param nDestNodeID 		The end node of the connection
	 * @param nTravelCost 		The cost to travel through this connection
	 * @return 					whether the connection was added or not, a failure occurs if the two nodes
	 * 							cannot be found in the graph already
	 */
	public boolean addConnection(int nStartNodeID, int nDestNodeID, int nTravelCost)
	{
		// Seach for the two nodes in the node vector
		//  (this isn't all that efficient, some sort of hash based on the ID would be
		//		much faster for node access, this is a place to look for a speed boost if needed)
		Node nodeStart = null;
		Node nodeDest = null;
		
		for (int i = 0; i < m_vNodes.size(); i++)
		{
			if (((Node)m_vNodes.get(i)).getID() == nStartNodeID)
			{
				nodeStart = ((Node)m_vNodes.get(i));
			}
			if (((Node)m_vNodes.get(i)).getID() == nDestNodeID)
			{
				nodeDest = ((Node)m_vNodes.get(i));
			}
		}
		
		if ((nodeStart == null) || (nodeDest == null))
		{
			// both nodes weren't found in the graph so return with a failure
			return false;
		}
		
		// add the connection
		nodeStart.addConnection(nodeDest, nTravelCost);
		
		return true;
	}
	
	/**
	 * Finds the node corresponding with the given ID
	 * @param nNodeID	the node id of the node to find
	 * @return			the node requested or <code>null</code> if not found
	 */
	public Node getNode(int nNodeID)
	{
		// find the node that corresponds to this id and return it
		for (int i = 0; i < m_vNodes.size(); i++)
		{
			if (((Node)m_vNodes.get(i)).getID() == nNodeID)
			{
				return ((Node)m_vNodes.get(i));
			}
		}
		
		// couldn't find the node
		return null;
	}
	
}
