package AST_Information.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SwitchStatement extends ConditionStatement{
	
	private SwitchStatement parentSwitchStmt = null;
	private List<Integer> caseStartLineList = new ArrayList<Integer>();
	private List<SwitchStatement> switchList = new ArrayList<SwitchStatement>();
	private Set<Integer> breakLineSet = new HashSet<Integer>();
	private int caseEndline = -1;
	private int defaultStartline = -1;
	private int defalutEndline = -1;
	
	public SwitchStatement() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public SwitchStatement(int startLine, int endLine, int astStartLine) {
		super(startLine, endLine, astStartLine);
	}
	
	public void setParentSwitchStmt(SwitchStatement parentSwitchStmt) {
		this.parentSwitchStmt = parentSwitchStmt;
	}
	public SwitchStatement getParentSwitchStmt() {
		return this.parentSwitchStmt;
	}
	public void setCaseLineList(List<Integer> caseStartLineList) {
		this.caseStartLineList = caseStartLineList;
	}
	public List<Integer> getCaseLineList(){
		return this.caseStartLineList;
	}
	public void setSwitchList(List<SwitchStatement> switchList) {
		this.switchList = switchList;
	}
	public List<SwitchStatement> getSwitchList(){
		return this.switchList;
	}
	public void setBreakLineSet(Set<Integer> breakLineSet) {
		this.breakLineSet = breakLineSet;
	}
	public Set<Integer> getBreakLineSet(){
		return this.breakLineSet;
	}
	public void setCaseEndline(int caseEndline) {
		this.caseEndline = caseEndline;
	}
	public int getCaseEndline() {
		return this.caseEndline;
	}
	public void setDefaultStartLine(int defaultStartline) {
		this.defaultStartline = defaultStartline ;
	}
	public int getDefaultStartlLine() {
		return this.defaultStartline;
	}
	public void setDefaultEndLine(int defaultEndline) {
		this.defalutEndline = defaultEndline;
	}
	public int getDefaultEndLine() {
		return this.defalutEndline;
	}

}
