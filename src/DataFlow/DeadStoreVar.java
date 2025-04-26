package DataFlow;


public class DeadStoreVar {
	private String varname;
	private int row;
	private int col;
	
	public DeadStoreVar(String varname, int row, int col) {
		this.varname = varname;
		this.row = row;
		this.col = col;
	}
	
	public DeadStoreVar() {
		
	}
	
	public String getName() {
		return this.varname;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
	
}