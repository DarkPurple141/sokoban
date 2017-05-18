package wb;

import java.util.List;
import java.util.ArrayList;
import java.awt	.Point;
public class Node{
	private List<Node> children;
	private Node parent;
	private MctsAction action;
	//private Direction movedDirection;
	private int movedDirection;
	private Point actedOn;

	public Node(Node parent){
		children = new ArrayList<Node>();
		this.parent = parent;
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
			Node child = new Node(this);
			child.setEvaluateAction();
			children.add(child);
		}
		for (Direction dir : Direction.values()){
			Node child = new Node(this);
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

	public List<Node> getChildren(){
		return children;
	}

	public int getMoveDirection(){
		return movedDirection;
	}


}