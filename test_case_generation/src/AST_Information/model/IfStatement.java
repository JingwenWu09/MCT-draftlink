package AST_Information.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IfStatement extends ConditionStatement{
	
	private boolean has_else = false;
	private IfStatement parentIfStmt = null;
	private List<IfStatement> ifList = new ArrayList<IfStatement>();
	private int elseline = -1;	//elseline = -1 表示还未设置elseline
	
	public IfStatement() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public IfStatement(int startLine, int endLine, int astStartLine) {
		super(startLine, endLine, astStartLine);
		this.elseline = endLine;
	}
	
	public void setHasElse(boolean has_else) {
		this.has_else = has_else;
	}
	public boolean getHasElse() {
		return this.has_else;
	}
	public void setParentIfStmt(IfStatement parentIfStmt) {
		this.parentIfStmt = parentIfStmt;
	}
	public IfStatement getParentIfStmt() {
		return parentIfStmt;
	}
	public void setIfList(List<IfStatement> ifList) {
		this.ifList = ifList;
	}
	public List<IfStatement> getIfList() {
		return ifList;
	}
	
	public void setElseline(int elseline) {
		this.elseline = elseline;
	}
	
	public int getElseline() {
		return this.elseline;
	}
	
	public static int getElseLine(IfStatement stmt, List<String> initialList) {
		if(stmt.getHasElse() == false) return stmt.endLine;
		
		int templine = stmt.startLine + 1;
		for(IfStatement child: stmt.getIfList()) {
			for( int i = templine; i <= child.startLine; i++) {
				if(initialList.get(i).matches("^.*\\belse\\b.*$")) return i;
			}
			templine = child.endLine + 1;
		}
		while(templine < stmt.endLine) {
			if(initialList.get(templine).matches("^.*\\belse\\b.*$")) return templine;
			templine++;
		}
		
		return stmt.endLine;
	}
	
	public static String getCondition(String ifStartline) {
		String regexIf = "\\bif\\b\\s*\\((.*)\\)\\s*";
		Pattern pIf = Pattern.compile(regexIf);
		Matcher mIf = pIf.matcher(ifStartline);
		String if_condition = "";
		if(mIf.find()) {
			if_condition = mIf.group(1);
		}
		return if_condition;
	}
	
}
