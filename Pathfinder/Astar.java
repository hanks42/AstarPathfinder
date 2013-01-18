/**
 * This class implements the A* pathfinding routine on a graph object.
 * 
 * @author Jonathan Hanks (jonhanks@gmail.com)
 * @version 1.0
 * 
 */

import java.util.*;
import java.lang.*;

public class Astar 
{
	/**
	 * This function performs the A* path searching algorithm on the graph that it is given.
	 * It will find the best path between the desired start and end nodes in the graph as long
	 * as a path exists in the graph.
	 * <p>
	 * The results of this algorithm are returned to the caller in a vector of node IDs, 
	 * any nodes in this vector are the nodes in the path that the A* algorithm found. This
	 * results vector will be empty if no path could be found because no path exists.
	 * 
	 * @param searchGraph		The graph to perform the A* search over
	 * @param nStartNodeID		The ID of the start node
	 * @param nGoalNodeID		The ID of the goal node
	 * @return					The path that algorithm found as a vector of node ids
	 */
	public static Vector performSearch(Graph searchGraph, int nStartNodeID, int nGoalNodeID)
	{
		// create the open and closed lists
		Vector vOpenList = new Vector();
		Vector vClosedList = new Vector();
		
		// create the start node as an AstarNode
		Astar.AstarNode currNode = new Astar.AstarNode();
		currNode.m_Node = searchGraph.getNode(nStartNodeID);
		currNode.m_nCost = 0;
		currNode.m_parentNode = null;
		currNode.m_nHeuristic = getHeuristic(searchGraph, nStartNodeID, nGoalNodeID);
		currNode.m_nScore = currNode.m_nCost + currNode.m_nHeuristic;
		
		// add the start node to the open list
		vOpenList.add(currNode);
		
		// while the open list is not empty loop through list searching for the
		// final path
		while (vOpenList.size() != 0)
		{
			// Pick the current node to be the node in the open list
			// with the least cost
			currNode = getLowestCostNode(vOpenList);
			
			// if we found the goal node then we're done - HOORAY!
			if (currNode.m_Node.getID() == nGoalNodeID)
			{
				// we found the right node so break out of the search
				break;
			}
			
			// otherwise this isn't the right one so move it to the closed list
			removeNodeFromList(vOpenList, currNode);
			vClosedList.add(currNode);
			
			// examine each adjacent node to it
			Vector vAdjNodes = currNode.m_Node.getAdjacentNodes();
			for (int i = 0; i < vAdjNodes.size(); i++)
			{
				// if not in the open list
				if (!isInList(vOpenList, (Node)vAdjNodes.get(i)))
				{
					// and not in the closed list
					if (!isInList(vClosedList, (Node)vAdjNodes.get(i)))
					{
						// we found a node to put in the open list so calculate its data
						Astar.AstarNode newOpenNode = new Astar.AstarNode();
						newOpenNode.m_Node = (Node)vAdjNodes.get(i);
						newOpenNode.m_nCost = 
							currNode.m_nCost + currNode.m_Node.costToTravel(newOpenNode.m_Node);
						newOpenNode.m_parentNode = currNode;
						newOpenNode.m_nHeuristic = 
							getHeuristic(searchGraph, newOpenNode.m_Node.getID(), nGoalNodeID);
						newOpenNode.m_nScore = currNode.m_nCost + currNode.m_nHeuristic;
						
						// now add it to the open list
						vOpenList.add(newOpenNode);
					}
				}
			}// end adjacent node check
		}
		
		Vector vResults = new Vector();
		
		// did we find the path?
		if (vOpenList.size() == 0)
		{
			// no we ran out of nodes to check there is no path
			return vResults;
		}
		
		if (currNode.m_Node.getID() != nGoalNodeID)
		{
			// something went wrong!
			return vResults;
		}
		
		// now we've found the path and it is the currNode so we just need to
		// trace it back and fill up the results vector
		while (currNode.m_Node.getID() != nStartNodeID)
		{
			vResults.add(new Integer(currNode.m_Node.getID()));
			currNode = currNode.m_parentNode;
		}
				
		// add the start node
		vResults.add(new Integer(currNode.m_Node.getID()));
		
		// return the path results
		return vResults;
	}
	
	/**
	 * This returns the heuristic value for the node it is given
	 * <p>
	 * The current heuristic is just the travel distance from the node
	 * to the goal node if there were no obstacles in the way, the graph were fully connected
	 * and if the costs were all 1 in all the connections remaining to the goal. This is
	 * a rather simple heuristic but should work rather well for most problems that can
	 * be represented on a two dimensional graph
	 * 
	 * @param searchGraph		The graph that is being search
	 * @param nNodeID			The id of the node you wish to get the heuristic value for
	 * @param nGoalID			The id of the goal node
	 * @return
	 */
	private static int getHeuristic(Graph searchGraph, int nNodeID, int nGoalID)
	{
		// find the nodes in the graph
		Node startNode = searchGraph.getNode(nNodeID);
		Node goalNode = searchGraph.getNode(nGoalID);
		
		// now find the x and y positions of the start and goal
		int nStartX = startNode.getX();
		int nStartY = startNode.getY();
		int nGoalX = goalNode.getX();
		int nGoalY = goalNode.getY();
		
		// calculate the travel distance between these two points
		int nTravelDist = 0;
		
		// the travel distance would be the greatest of the two differences
		if (Math.abs(nStartX - nGoalX) > Math.abs(nStartY - nGoalY))
		{
			nTravelDist = Math.abs(nStartX - nGoalX);
		}
		else
		{
			nTravelDist = Math.abs(nStartY - nGoalY);
		}
		
		// return the travel distance as the heuristic
		return nTravelDist;
	}
	
	/**
	 * This will find the lowest cost node in the vector of AstarNodes and return it
	 * 
	 * @param vNodes		The AstarNodes to search for the lowest cost one in
	 * @return				The node with the lowest cost in the vector
	 */
	private static Astar.AstarNode getLowestCostNode(Vector vNodes)
	{
		int nLowestCost = Integer.MAX_VALUE;
		Astar.AstarNode lowestNode = null;
		
		for (int i = 0; i < vNodes.size(); i++)
		{
			// if this node is lower then set it as the lowest node
			if (((Astar.AstarNode)vNodes.get(i)).m_nCost < nLowestCost)
			{
				lowestNode = (Astar.AstarNode)vNodes.get(i);
				nLowestCost = lowestNode.m_nCost;
			}
		}
		
		// we found the lowest cost node so return it
		return lowestNode;
	}
	
	/**
	 * Removes the given node from the vector given
	 * 
	 * @param vNodes		The list of nodes
	 * @param toRemove 		The node to remove
	 */
	private static void removeNodeFromList(Vector vNodes, Astar.AstarNode toRemove)
	{
		// find the node in the list and remove it
		for (int i = 0; i < vNodes.size(); i++)
		{
			if ( ((Astar.AstarNode)vNodes.get(i)).m_Node.getID() == toRemove.m_Node.getID() )
			{
				vNodes.remove(i);
			}
		}
	}
	
	/**
	 * Checks whether the node is in the list given
	 * 
	 * @param vNodes		The list of nodes
	 * @param toCheck		The node to check
	 * @return				<code>true</code> if in the list <code>false</code> otherwise
	 */
	private static boolean isInList(Vector vNodes, Node toCheck)
	{
		// search for the node
		for (int i = 0; i < vNodes.size(); i++)
		{
			if (((Astar.AstarNode)vNodes.get(i)).m_Node.getID() == toCheck.getID())
			{
				return true;
			}
		}
		
		// never found
		return false;
	}
	
	/**
	 * A private class of nodes only used by the A* routine to help keep track of
	 * costs of nodes (the cost to and parent nodes
	 * 
	 * @author Jonathan Hanks (jonhanks@gmail.com)
	 * @version 1.0
	 */
	private static class AstarNode
	{
		/**
		 * The node this keeps track of
		 */
		public Node m_Node;
		/**
		 * The cost to reach this node from the start node (the sum of the connection costs
		 * from the start to this node)
		 */
		public int m_nCost;
		/**
		 * The heuristic cost for this node (the estimated cost to reach the goal node from
		 * this node)
		 */
		public int m_nHeuristic;
		/**
		 * The score of this node (the sum of the cost and heuristic) or the total estimated
		 * cost
		 */
		public int m_nScore;
		/**
		 * The parent node to this node (the last node travelled through when ariving at this
		 * node)
		 */
		public AstarNode m_parentNode;
	}
}
