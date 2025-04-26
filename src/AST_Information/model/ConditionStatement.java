package AST_Information.model;

import java.util.ArrayList;
import java.util.List;

public class ConditionStatement {
	protected String id;
	protected int startLine;
	protected int endLine;
	protected int startCol;
	protected int endCol;
	protected int astStartLine;
	protected int astEndLine;
	protected String conditionType;
	protected boolean hasBrace = false;
	protected int braceStartLine = -1;
	protected int braceStartCol = -1;
	protected int braceEndLine = -1;
	protected int braceEndCol = -1;
	
	protected List<AstVariable> insideList = new ArrayList<AstVariable>();	//本condition里定义的变量
	protected List<AstVariable> useVarList = new ArrayList<AstVariable>();	//本condition里使用，但是在本condition之外定义的变量
	protected List<AstVariable> outsideList = new ArrayList<AstVariable>();	//在本condition中使用，在parentondition中定义的variable
	
	public ConditionStatement() {
		// TODO Auto-generated constructor stub
	}
	
	public ConditionStatement(int startLine, int endLine, int astStartLine) {
		super();
		this.startLine = startLine;
		this.endLine = endLine;
		this.astStartLine = astStartLine;
	}
	
	public void setIfId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	public int getStartLine() {
		return startLine;
	}
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	public int getEndLine() {
		return endLine;
	}
	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}
	public int getStartCol() {
		return startCol;
	}
	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}
	public int getEndCol() {
		return endCol;
	}
	public void setAstStartLine(int astStartLine) {
		this.astStartLine = astStartLine;
	}
	public int getAstStartLine() {
		return astStartLine;
	}
	public void setAstEndLine(int astEndLine) {
		this.astEndLine = astEndLine;
	}
	public int getAstEndLine() {
		return astEndLine;
	}
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	public String getConditionType() {
		return conditionType;
	}
	
	public void setHasBrace(boolean hasBrace) {
		this.hasBrace = hasBrace;
	}
	public boolean getHasBrace() {
		return hasBrace;
	}
	public void setBraceStartLine(int braceStartLine) {
		this.braceStartLine = braceStartLine;
	}
	public int getBraceStartLine() {
		return braceStartLine;
	}
	public void setBraceStartCol(int braceStartCol) {
		this.braceStartCol = braceStartCol;
	}
	public int getBraceStartCol() {
		return braceStartCol;
	}
	public void setBraceEndLine(int braceEndLine) {
		this.braceEndLine = braceEndLine;
	}
	public int getBraceEndLine() {
		return braceEndLine;
	}
	public void setBraceEndCol(int braceEndCol) {
		this.braceEndCol = braceEndCol;
	}
	public int getBraceEndCol() {
		return braceEndCol;
	}
	public void setInsideList(List<AstVariable> insideList) {
		this.insideList = insideList;
	}
	public List<AstVariable> getInsideList() {
		return insideList;
	}
	public void setUseVarList(List<AstVariable> useVarList) {
		this.useVarList = useVarList;
	}
	public List<AstVariable> getUseVarList(){
		return useVarList;
	}
	public void setOutsideList(List<AstVariable> outsideList) {
		this.outsideList = outsideList;
	}
	public List<AstVariable> getOutsideList() {
		return outsideList;
	}
	
}
