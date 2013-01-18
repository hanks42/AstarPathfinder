/**
 * A node connection is just a connection between two nodes. It has a node that is being
 * connected as well as a cost to travel to that node.
 * 
 * @author Jonathan Hanks (jonhanks@gmail.com)
 * @version 1.0
 *
 */
public class NodeConnection 
{
	/*
	 * Members
	 */
	
	/**
	 * The cost to travel to the connected node
	 */
	private int m_nTravelCost;
	
	/**
	 * The node that is connected by this link
	 */
	private Node m_linkedNode;
	
	/*
	 * Functions
	 */
	
	/**
	 * This constructor creates the connection data. Only this constructor is provided
	 * and no mutators are provided for the data so you must create a new connection if you
	 * wish to change it, this is because the connections are very simple things.
	 * 
	 * @param linkNode The node that is connected by this link
	 * @param linkCost The cost to travel to the connected node from the one that is
	 * 					being connected
	 */
	NodeConnection(Node linkNode, int linkCost)
	{
		m_nTravelCost = linkCost;
		m_linkedNode = linkNode;
	}
	
	/**
	 * This provides the cost to travel on this link
	 * 
	 * @return The cost to travel to the connected node
	 */
	public int getCost()
	{
		return m_nTravelCost;
	}
	
	/**
	 * This tells which node is connected by this link
	 * 
	 * @return The connected node
	 */
	public Node getLinkedNode()
	{
		return m_linkedNode;
	}
	
	

}
