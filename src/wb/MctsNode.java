package wb;

import java.util.List;
import java.util.ArrayList;

/**
 * M-C tree sim node.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class MctsNode {
	// FIXME(jashankj): move this down a package
	private List<MctsNode> children;
	private MctsNode parent;
	private MctsAction action;
	private Direction movedDirection;
	private int visited;
	private double totalScore;

	public MctsNode(MctsNode parent){
		children = new ArrayList<MctsNode>();
		this.parent = parent;
		visited = 0;
		totalScore = 0;
	}

	public void setMoveAction(Direction direction){
		action = MctsAction.MOVE;
		movedDirection = direction;
	}

	public void setEvaluateAction(){
		action = MctsAction.EVALUATE;
	}

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

	public MctsAction getAction(){
		return action;
	}

	public List<MctsNode> getChildren(){
		return children;
	}

	public Direction getMoveDirection(){
		return movedDirection;
	}

	public void visited(){
		visited++;
	}

	public int timesVisited(){
		return visited;
	}

	public void updateValue(double add){
		totalScore += add;
	}

	public double getScore(){
		return totalScore;
	}




}
