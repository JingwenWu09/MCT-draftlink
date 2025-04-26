package remained_other;

public class BasicIV {
	private String name;
	private int num;
	private String op;
	private int lineNum;
	private int lineNumInLoop;
	private String conName;
	private int varStartValue;
	private int varEndValue;
	
	public BasicIV(String name, int num, String op, int lineNum, int lineNumInLoop, String conName, int varEndValue) {
		super();
		this.name = name;
		this.num = num;
		this.op = op;
		this.lineNum = lineNum;
		this.lineNumInLoop = lineNumInLoop;
		this.conName = conName;
		this.varEndValue = varEndValue;
	}
	
	public BasicIV(String name, int num, String op, int lineNum, int lineNumInLoop, String conName, int varStartValue, int varEndValue) {
		super();
		this.name = name;
		this.num = num;
		this.op = op;
		this.lineNum = lineNum;
		this.lineNumInLoop = lineNumInLoop;
		this.conName = conName;
		this.varStartValue = varStartValue;
		this.varEndValue = varEndValue;
	}

	//num和conName 任选其一就可以
	
	public BasicIV(String name, int num, String op, int lineNum, int lineNumInLoop, String conName) {
		this.name = name;
		this.num = num;
		this.op = op;
		this.lineNum = lineNum;
		this.lineNumInLoop = lineNumInLoop;
		this.conName = conName;
	}

	public int getVarStartValue() {
		return varStartValue;
	}

	public void setVarStartValue(int varStartValue) {
		this.varStartValue = varStartValue;
	}

	public int getVarEndValue() {
		return varEndValue;
	}

	public void setVarEndValue(int varEndValue) {
		this.varEndValue = varEndValue;
	}

	public int getLineNumInLoop() {
		return lineNumInLoop;
	}

	public void setLineNumInLoop(int lineNumInLoop) {
		this.lineNumInLoop = lineNumInLoop;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getOp() {
		return op;
	}
	
	public void setOp(String op) {
		this.op = op;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	public String getConName() {
		return conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}
	
	
}
