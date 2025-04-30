package Condition;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.FileFormat;
import AST_Information.VarStmt_Gen;
import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.Inform_Gen.ConditionInform_Gen;
import AST_Information.model.IfStatement;
import TransOperation.Rename;

public class IfCondition_backup {
	AstInform_Gen astgen;
	List<String> initialList = new ArrayList<String>();
	int mutationCnt = 0;
	File file;
	String mutationDir;
	static String rename_suffix = "_1";
	VarStmt_Gen varStmt;
	Map<Integer, List<String>> globalVarsDeclareMap;
	List<String> storeGlobalVarStmt;
	List<String> restoreGlobalVarStmt;
	static final String[] allTypes = {"char", "short", "int", "long", "float", "double"};
	static final String[] leftTypes = {"double", "float", "long", "int"};
	String[] operators = {"!", "==", "!=", ">=", "<=", ">", "<", "&&", "\\|\\|"};
	String[] oppositeOperators = {"", "!=", "==", "<", ">", "<=", ">="};
	String regexFor = "\\bfor\\s*\\((.*);(.*);(.*?)\\)\\s*";
	String regexWhile = "\\bwhile\\s*\\((.*)\\)\\s*";
	String regexDo = "\\bdo\\s*";
	Pattern pfor = Pattern.compile(regexFor);
	Pattern pwhile = Pattern.compile(regexWhile);
	Pattern pdo = Pattern.compile(regexDo);
	Matcher mfor, mwhile, mdo;

	public static void main(String[] args) {
		File file = new File("/home/elowen/桌面/builtin-object-size-6.c");
		String mutationDir = "/home/elowen/桌面/test1";
		IfCondition_backup condition = new IfCondition_backup();
		condition.Transformation(file, mutationDir);
	}
	
	public void Transformation(File file, String mutationDir) {
		mutationCnt = 0;
		this.file = file;
		this.mutationDir = mutationDir;
		initialList = FileFormat.genInitialList(file);
		astgen = new AstInform_Gen();
		astgen.getAstInform(file.getAbsolutePath());
		if(astgen.astList.size() == 0) return ;
		
		varStmt = new VarStmt_Gen(astgen, initialList);
		globalVarsDeclareMap = varStmt.genGlobalVarsDeclareMap();
		storeGlobalVarStmt = varStmt.genStoreGlobalVarStmt();
		restoreGlobalVarStmt = varStmt.genRestoreGlobalVarStmt();
		
		ConditionInform_Gen conditionGen = new ConditionInform_Gen(astgen);
		for(IfStatement stmt: conditionGen.outmostIfList) {
			IfTransformation(stmt);
		}
		
		System.out.println("finish! mutationCnt = " + mutationCnt);
	}
	
	private void IfTransformation(IfStatement stmt) {
		if(stmt == null) return;
		
		/*有以下四种情况
		 *1.无parent
		 *if(){}else{}
		 * rename brefore if.startline
		 * inital_block = trans_if_startline + [if.startline+1, if.elseline-1] + [if.elseline, if.endline];
		 * trans_block = trans_if_startline + [if.startline+1, if.elseline-1] + [if.elseline, if.endline];
		 *2.有parent，parent无else 
		 *if(){
		 *	if(){}else{}
		 *}
		 * 变化，同1.
		 *3.有parent，parent有else，parent else与stmt.startline不在同一行 
		 * if(){
		 * 	if(){}else{}
		 * }else{
		 * 	if(){}else{}
		 * }
		 * 变化，同1
		 *4.有parent，parent有else，parent else与stmt.startline在同一行，找最近的需要重命名的外层if_parent
		 * if_parent(){
		 * }else if(){
		 * }
		 * rename brefore if_parent.startline
		 * initial_block = [if_parent.startline,if_parent.elseline-1] + trans_if_startline + [if_parent.elseline, if_parent.endline];
		 * trans_block = [if_parent.startline, if_parent.elseline-1] + trans[if.starline, if.endline-1] + [if.endline, if_parent,endline];
		 * */
		
		if(stmt.getHasElse() == false) stmt.setElseline(stmt.getEndLine());
		else if( stmt.getElseline() == -1) {
		//haselse且elseline未设置，设置else所在行
			stmt.setElseline(IfStatement.getElseLine(stmt, initialList));
		}
		
		List<String> initial_block = null;
		List<String> trans_block = null;
		List<String> varDeclare = new ArrayList<String>();
		List<String> conditionList = null;
		List<String> oldName = null;
		
		IfStatement child = stmt;
		IfStatement parent = child.getParentIfStmt();
		//child是最外层rename的ifStatement
		while(parent != null && parent.getElseline() == child.getStartLine()) {
			child = parent; 
			parent = child.getParentIfStmt();
		}
		
		varDeclare.addAll(this.storeGlobalVarStmt);
		varDeclare.addAll( varStmt.genUseVarRenameStmt(child.getStartLine(), child.getUseVarList()) );
		conditionList = varStmt.genConditionList();
		oldName = varStmt.getUseVarOldName();
		
		List<String> beforeIfSline = null;
		String space = getSpace(initialList.get(stmt.getStartLine()));
		String ifSline = space;
		String if_condition = getCondition(initialList.get(stmt.getStartLine())).trim();
		String leftCondition = "", rightCondition = "";
		String op1 = "", op2 = "";
		//情况4.before ifSline
		if(child != stmt) {
			beforeIfSline = new ArrayList<String>();
			for(int i = child.getStartLine(); i < stmt.getStartLine(); i++) {
				beforeIfSline.add(initialList.get(i));
			}
			ifSline += "} else ";
		}
		
		//mutationCaseOne
		String regex = "(\\|\\||&&)+";
		Pattern p = Pattern.compile(regex);
		Matcher m;
		m = p.matcher(if_condition);
		if(m.find() && if_condition.contains("(") == false) {
			op1 = m.group().equals("||")? "||" : "";
			op2 = m.group().equals("&&")? "&&" : "";
			int start = m.start();
			int end = m.end();
			leftCondition = if_condition.substring(0,start).trim();
			rightCondition = if_condition.substring(end).trim();
		}else {
			op1 = "||";
			op2 = "&&";
			leftCondition = "(" + if_condition + ")";
			rightCondition = "";
		}
		
		if(op1.equals("||")) {
			String leftCon = leftCondition;
			String rightCon = rightCondition;
			
			initial_block = new ArrayList<String>();
			trans_block = new ArrayList<String>();
			initial_block.add(space + "//initial_block");
			trans_block.add(space + "//trans_block");
			if(beforeIfSline != null) {
				initial_block.addAll(beforeIfSline);
				trans_block.addAll(beforeIfSline);
			}
			rightCon = rightCon.equals("")? "0": rightCon;
			initial_block.add(ifSline + "if( " + leftCon + " || " + rightCon + " ) {");
			trans_block.add(ifSline + "if( " + leftCon + " ) {");
			for(int i = stmt.getStartLine()+1; i < stmt.getElseline(); i++) {
				initial_block.add(initialList.get(i));
				trans_block.add(initialList.get(i));
			}
			
			trans_block.add(space + "} else if( " + rightCon + " ) {");
			for( int i = stmt.getStartLine()+1; i < stmt.getElseline(); i++) {
				trans_block.add(initialList.get(i));
			}
			
			for( int i = stmt.getElseline(); i <= child.getEndLine(); i++) {
				initial_block.add(initialList.get(i));
				trans_block.add(initialList.get(i));
			}
			
			trans_block = Rename.getRenamedTransBlock(oldName, trans_block);
			genMutation(this.globalVarsDeclareMap, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
		}
		
		if(op2.equals("&&")) {
			String leftCon = leftCondition;
			String rightCon = rightCondition;
			
			initial_block = new ArrayList<String>();
			trans_block  = new ArrayList<String>();
			initial_block.add(space + "//initial_block");
			trans_block.add(space + "//trans_block");
			if(beforeIfSline != null) {
				initial_block.addAll(beforeIfSline);
				trans_block.addAll(beforeIfSline);
			}
			rightCon = rightCon.equals("")? "1": rightCon;
			initial_block.add(ifSline + "if( " + leftCon + " && " + rightCon + " ) {");
			trans_block.add(ifSline + "if( " + leftCon + " ) {");
			trans_block.add(space + "\tif( " + rightCon + " ) {");
			for( int i = stmt.getStartLine()+1; i < stmt.getElseline(); i++) {
				initial_block.add(initialList.get(i));
				trans_block.add("\t" + initialList.get(i));
			}
			trans_block.add(space + "\t" + "}");
			for( int i = stmt.getElseline(); i <= child.getEndLine(); i++) {
				initial_block.add(initialList.get(i));
				trans_block.add(initialList.get(i));
			}

			trans_block = Rename.getRenamedTransBlock(oldName, trans_block);
			genMutation(this.globalVarsDeclareMap, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
		}
		
		//mutationCaseTwo if(==)else -> if(!=)else
		//限制是operator[0,6]中的一个，不存在&&或||
		//不考虑if中有括号的情况 function?
		List<Integer> op_index = getExistOperator( if_condition );
		if(op_index.size() == 1 && op_index.get(0) != 7 && op_index.get(0) != 8 && if_condition.contains("(") == false) {
//			System.out.println(op_index);
			initial_block = new ArrayList<String>();
			trans_block = new ArrayList<String>();
			initial_block.add(space + "//initial_block");
			trans_block.add(space + "//trans_block");
			for(int i = child.getStartLine(); i <= child.getEndLine(); i++) {
				initial_block.add(initialList.get(i));
			}
			
			op1 = operators[op_index.get(0)];
			op2 = oppositeOperators[op_index.get(0)];
			Pattern pOp = Pattern.compile("(" + op1 + ")[_0-9a-zA-Z\\s\\(\\*]+");
			Matcher mOp = pOp.matcher(if_condition);
			if(mOp.find()) {
				leftCondition = if_condition.substring(0,mOp.start()).trim();
				rightCondition = if_condition.substring(mOp.start() + op1.length()).trim();
			}
			
			//生成trans_block。没有else，一句封号； 有else，判断else if
			if(beforeIfSline != null) {
				trans_block.addAll(beforeIfSline);
			}
			trans_block.add(ifSline + "if( " + leftCondition + " " + op2 + " " + rightCondition +" ) {");
			if(stmt.getHasElse() == false) {
				trans_block.add(space + "\t;");
			}else {
				String regexElseif = "^.*\\belse\\b\\s+(\\bif\\b.*$)";
				Pattern pElseif = Pattern.compile(regexElseif);
				Matcher mElseif = pElseif.matcher(initialList.get(stmt.getElseline()));
				String tab = "";
				boolean stmtHasElseIf = false;
				if(mElseif.find()) {
					tab = "\t";
					trans_block.add(space + tab + mElseif.group(1));
					stmtHasElseIf = true;
				}
				for( int i = stmt.getElseline() + 1; i < child.getEndLine(); i++) {
					trans_block.add(tab + initialList.get(i));
				}
				if(stmtHasElseIf == true) {
					trans_block.add(space + tab + "}");
				}
			}
			trans_block.add(space + "} else {");
			for(int i = stmt.getStartLine()+1; i < stmt.getElseline(); i++) {
				trans_block.add(initialList.get(i));
			}
			trans_block.add(space + "}");
			
			trans_block = Rename.getRenamedTransBlock(oldName, trans_block);
			genMutation(this.globalVarsDeclareMap, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
		}
			
		for(IfStatement child_if: stmt.getIfList()) {
			IfTransformation(child_if);
		}
	}
	
	private String getCondition(String ifStartline) {
		String regexIf = "\\bif\\b\\s*\\((.*)\\)\\s*";
		Pattern pIf = Pattern.compile(regexIf);
		Matcher mIf = pIf.matcher(ifStartline);
		String if_condition = "";
		if(mIf.find()) {
			if_condition = mIf.group(1);
		}
		return if_condition;
	}
	
	private List<Integer> getExistOperator(String line) {
		List<Integer> op_index = new ArrayList<Integer>();
		String regex = operators[0] + "[_0-9a-zA-Z\\s\\*\\(]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);
		if(m.find()) {
			op_index.add(0);
		}
		for(int i = 1; i < operators.length; i++) {
			regex = "[_0-9a-zA-Z\\s\\*\\()]+" + operators[i] + "[_0-9a-zA-Z\\s\\*\\(]+";
			p = Pattern.compile(regex);
			m = p.matcher(line);
			if(m.find()) {
				op_index.add(i);
			}
		}
		
		return op_index;
	}
	
	public static String getSpace(String line) {
		String regexSpace = "^(\\s*)\\S";
		Pattern pspace = Pattern.compile(regexSpace);
		Matcher mspace = pspace.matcher(line);
		String space = "";
		if(mspace.find()) space = mspace.group(1);
		return space;
	}
	
	private void genMutation(Map<Integer, List<String>> globalVarsDeclareMap, List<String> initial_block, List<String> trans_block, List<String> varDeclare, List<String> conditionList, int startline, int endline) {
		try {
			//System.out.println(dir);
			String filename = file.getName();
			File newFile = new File(mutationDir + "/" + filename.substring(0, filename.lastIndexOf(".c")) + "_" + mutationCnt + ".c");
			mutationCnt++;
			
			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
//			System.out.println("outmostSline: " +trans.outmostSline);
//			System.out.println(initialList.get(trans.outmostSline));
			
			int lineCnt = -1;
			for(String line: initialList) {
				lineCnt++;
				if(lineCnt == 0) {
					pw.println("#include <assert.h>");
					continue;
				}
				
				if(lineCnt == startline ) {
					varDeclare.forEach( s -> {
						pw.println(s);
					});
					initial_block.forEach( s -> {
						pw.println(s);
					});
					this.restoreGlobalVarStmt.forEach( s -> {
						pw.println(s);
					});
					trans_block.forEach( s -> {
						pw.println(s);
					});
					conditionList.forEach( s -> {
						pw.println(s);
					});
				}
				if(lineCnt >= startline && lineCnt <= endline) continue ;
				
				pw.println(line);
				
				if(globalVarsDeclareMap.containsKey(lineCnt)) {
					globalVarsDeclareMap.get(lineCnt).forEach( s -> {
						pw.println(s);
					});
				}
				
			}
			
			pw.flush();
			fw.flush();
			pw.close();
			fw.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
