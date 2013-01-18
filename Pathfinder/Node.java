/**
 * The Node class is for the internal representation of nodes inside of a graph.
 * <p>
 * Each node can have connections to any number of other nodes, each of these connections
 * can then have a cost to travel associated with them. The cost is only for travel
 * from this node to that as the cost could be different in the other direction and also
 * the connection could be singly directional.
 * 
 * @author Jonathan Hanks (jonhanks@gmail.com)
 * @version 1.0
 *
 */
import java.util.*;

public class Node 
{
	/*
	 * Members
	 */
	/**
	 * A counter to keep track of how many nodes have been created, this will be used
	 * to set the id number as well
	 */
	private static int m_nNodeCounter;
	
	/**
	 * The id number of the node, they must be identified somehow and this is how.
	 */
	private int m_nNodeID;
	
	/**
	 * The x position in the world of the point
	 */
	private int m_nPosX;
	
	/**
	 * The y position in the world of the point
	 */
	private int m_nPosY;
	
	/**
	 * The vector of all nodes connected to this node. This is a vector of NodeConnection
	 * objects.
	 */
	private Vector m_vConnectedNodes;
	
	/*
	 * Functions
	 */
	/**
	 * This constructor creates the node and sets its ID number
	 */
	Node(int xPos, int yPos)
	{
		// increment the number of nodes created
		m_nNodeCounter++;
		
		// set the id number
		m_nNodeID = m_nNodeCounter;
				
		// set the position
		m_nPosX = xPos;
		m_nPosY = yPos;
		
		// create the connected nodes vector
		m_vConnectedNodes = new Vector();
	}
	
	/**
	 * This provides the ID number of the node
	 * @return the id number of the node
	 */
	public int getID()
	{
		return m_nNodeID;
	}	
	
	/**
	 * Creates a connection to another node
	 * 
	 * @param linkedNode the node to connect
	 * @param linkCost the cost to travel to that node
	 */
	public void addConnection(Node linkedNode, int linkCost)
	{
		// create the connectoin and link this node up
		m_vConnectedNodes.add(new NodeConnection(linkedNode, linkCost));
	}
	
	/**
	 * This checks if you can travel to a node from this node and provides the cost of 
	 * the travel
	 * 
	 * @param nodeToCheck 	the node to check whether you can travel to or not
	 * @return 				the cost to travel to the other node, zero if no connection or a 
	 * 						singly directional link that does not go in the requested direction
	 */
	public int costToTravel(Node nodeToCheck)
	{
		// check the vector of links for a connection
		for (int i = 0; i < m_vConnectedNodes.size(); i++)
		{
			// if this is the connection to the node we are checking
			if (((NodeConnection)m_vConnectedNodes.elementAt(i)).getLinkedNode().getID() 
					== nodeToCheck.getID())
			{
				return ((NodeConnection)m_vConnectedNodes.elementAt(i)).getCost();
			}
		}
		
		// not found so no connection is a zero cost
		return 0;
	}
	
	/**
	 * Get the x position value of this node
	 * @return	x position of this node
	 */
	public int getX()
	{
		return m_nPosX;
	}
	
	/**
	 * Get the y positioni value of this node
	 * @return the y position of this node
	 */
	public int getY()
	{
		return m_nPosY;
	}
	
	/**
	 * This provides a vector filled with all the nodes that can be travelled to from this node
	 * 
	 * @return 			the list of nodes that can be travelled to
	 */
	public Vector getAdjacentNodes()
	{
		Vector vAdjNodes = new Vector();
		
		for (int i = 0; i < m_vConnectedNodes.size(); i++)
		{
			if ( ((NodeConnection)m_vConnectedNodes.get(i)).getCost() > 0 )
			{
				vAdjNodes.add(((NodeConnection)m_vConnectedNodes.get(i)).getLinkedNode());
			}
		}
		
		return vAdjNodes;
	}
}
