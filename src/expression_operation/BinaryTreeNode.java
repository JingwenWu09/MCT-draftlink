package expression_operation;

public class BinaryTreeNode extends Node{
	//这里nodeContent是操作数或操作符
	//这里nodeType是操作符operator、变量var、常数const、左括号leftBrace
	protected BinaryTreeNode parent = null;
	protected BinaryTreeNode leftChild = null;
	protected BinaryTreeNode rightChild = null;
	protected String whichChild = null;
	
	public BinaryTreeNode(String nodeVal, String nodeType){
		this.setNodeVal(nodeVal);
		this.setNodeType(nodeType);
	}
	
	public void setParent(BinaryTreeNode node) {
		this.parent = node;
	}
	public void setLeftChild(BinaryTreeNode node) {
		this.leftChild = node;
	}
	
	public void setRightChild(BinaryTreeNode node) {
		this.rightChild  = node;
	}
	
	public void setWhichChild(String whichChild) {
		this.whichChild = whichChild;
	}
	
	public BinaryTreeNode getParent() {
		return this.parent;
	}
	
	public BinaryTreeNode getLeftChild() {
		return this.leftChild;
	}
	
	public BinaryTreeNode getRightChild() {
		return this.rightChild;
	}
	
	public String getWhichChild() {
		return this.whichChild;
	}
}