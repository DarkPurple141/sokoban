package wb;

import java.util.List;
import java.util.ArrayList;

/**
 * M-C tree sim node. Acts as a node in the Mcts tree
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class MctsNode {
	private List<MctsNode> children;
	private MctsNode parent;
	private MctsAction action;
	private Direction movedDirection;
	private int visited;
	private double totalScore;


	/**
	 * 
	 * Constructor
	 * 
	 * @param parent : The new node's parent node
	 */
	public MctsNode(MctsNode parent){
		children = new ArrayList<MctsNode>();
		this.parent = parent;
		visited = 0;
		totalScore = 0;
	}


	/**
	 * 
	 * Sets the action of the current node to a MOVE and sets the
	 * direction of that move.
	 * 
	 * @param direction : The direction of the node's MOVE action
	 */
	public void setMoveAction(Direction direction){
		action = MctsAction.MOVE;
		movedDirection = direction;
	}


	/**
	 * 
	 * Set this nodes action as an evaluation
	 */
	public void setEvaluateAction(){
		action = MctsAction.EVALUATE;
	}


	/**
	 * 
	 * Expand the current node by adding all possible actions as children
	 */
	public void addOptions(){
		if (parent != null){
			MctsNode child = new MctsNode(this);
			child.setEvaluateAction();
			children.add(child);
		}
		for (Direction d : Direction.values()){
			MctsNode child = new MctsNode(this);
			child.setMoveAction(d);

			children.add(child);
		}
	}


	/**
	 * 
	 * Returns the action of this node
	 * 
	 * @return the action that this node represents
	 */
	public MctsAction getAction(){
		return action;
	}


	/**
	 * 
	 * Gets all of the children of this node
	 * 
	 * @return a list of all of the children nodes of this node
	 */
	public List<MctsNode> getChildren(){
		return children;
	}


	/**
	 * 
	 * Gets the move direction of a node
	 * 
	 * @param parent : The new node's parent node
	 * @precondition The node must have a move action
	 */
	public Direction getMoveDirection(){
		return movedDirection;
	}


	/**
	 * 
	 * Increments the visited count of the node
	 */
	public void visited(){
		visited++;
	}


	/**
	 * 
	 * Gets the number of times this node was visited
	 * 
	 * @return The number of times this node has been visited during the search
	 */
	public int timesVisited(){
		return visited;
	}


	/**
	 * 
	 * Updates the score of this node by adding the param to the current score
	 * 
	 * @param add : The new score to be added the this node's total
	 */
	public void updateValue(double add){
		totalScore += add;
	}


	/**
	 * 
	 * @return This node's score (as a double)
	 */
	public double getScore(){
		return totalScore;
	}




}
