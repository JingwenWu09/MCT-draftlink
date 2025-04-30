package expression_operation;

public abstract class Node {
	protected int nodeId;
	protected String nodeType;
	protected String nodeVal;
	
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	
	public void setNodeType(String nodeType ) {
		this.nodeType = nodeType;
	}
	
	public void setNodeVal(String nodeVal) {
		this.nodeVal = nodeVal;
	}
	
	public int getNodeId() {
		return this.nodeId;
	}
	
	public String getNodeType() {
		return this.nodeType;
	}
	
	public String getNodeVal() {
		return this.nodeVal;
	}
}
