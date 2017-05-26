package wb;

import java.util.List;
import java.util.ArrayList;

/**
 * M-C tree sim node.
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
public class MctsNode {
	// FIXME(jashankj): move this down a package
	private List<MctsNode> children;
	private MctsNode parent;
	private MctsAction action;
	private int movedDirection;
	private int visited;
	private double totalScore;

	public MctsNode(MctsNode parent){
		children = new ArrayList<MctsNode>();
		this.parent = parent;
		visited = 0;
		totalScore = 0;
	}

	public void setMoveAction(int direction){
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
		for (Direction dir : Direction.values()){
			MctsNode child = new MctsNode(this);
			if (dir == Direction.UP){
				child.setMoveAction(0);
			}else if (dir == Direction.RIGHT) {
				child.setMoveAction(1);
			}else if (dir == Direction.DOWN) {
				child.setMoveAction(2);
			}else{
				child.setMoveAction(3);
			}

			children.add(child);
		}
	}

	public MctsAction getAction(){
		return action;
	}

	public List<MctsNode> getChildren(){
		return children;
	}

	public int getMoveDirection(){
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
