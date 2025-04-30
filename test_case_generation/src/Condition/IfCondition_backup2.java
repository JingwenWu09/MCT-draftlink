package Condition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.FileFormat;
import AST_Information.VarStmt_Gen;
import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.Inform_Gen.ConditionInform_Gen;
import AST_Information.model.IfStatement;
import TransOperation.Rename;
import java.util.Stack;

public class IfCondition_backup2 {
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

	public static void main(String[] args) throws IOException, InterruptedException {
		File file = new File("/home/jing/桌面/IfShortCircuit/test.c");
		String mutationDir = "/home/jing/桌面/IfShortCircuit";
		IfCondition_backup2 condition = new IfCondition_backup2();
		condition.Transformation(file, mutationDir);
	}
	
	public void Transformation(File file, String mutationDir) throws IOException, InterruptedException {
	
		try {
			
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
		
//		mutationCnt = 0;
//		for(IfStatement stmt: conditionGen.outmostIfList) {
//			if(stmt.getStartLine() > initialList.size()) continue ;
//			ShortCircuitTrans(stmt);
//		}
//		System.out.println("finish! ShortCircuitCnt = " + mutationCnt);

		mutationCnt = 0;
		for(IfStatement stmt: conditionGen.outmostIfList) {
			if(stmt.getStartLine() > initialList.size()) continue ;
			AndOrSplitTrans(stmt);
		}
		System.out.println("finish! AndOrSplitCnt = " + mutationCnt);

//		mutationCnt = 0;
//		for(IfStatement stmt: conditionGen.outmostIfList) {
//			if(stmt.getStartLine() > initialList.size()) continue ;
//			NegationTrans(stmt);
//		}
//		System.out.println("finish! NegationCnt = " + mutationCnt);
		
//		System.out.println("finish! mutationCnt = " + mutationCnt);
		
		}catch( NullPointerException e){
			
		}
	}
	
	private void ShortCircuitTrans(IfStatement stmt) throws IOException, InterruptedException {
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
		String suffix = "sc";
		
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
		if(if_condition.equals("")) {
			for(IfStatement child_if: stmt.getIfList()) {
				ShortCircuitTrans(child_if);
			}
			return ;
		}
		
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
		List<String> opOR_list = new ArrayList<String>();
		List<String> opAnd_list = new ArrayList<String>();
		List<String> leftConditionList = new ArrayList<String>();
		List<String> rightConditionList = new ArrayList<String>();
		int cntAnd = 0;
		int cntOr = 0;
		String regex = "(\\|\\||&&)+";
		Pattern p = Pattern.compile(regex);
		Matcher m;
		m = p.matcher(if_condition);
		while(m.find()) {
			op1 = m.group().equals("||")? "||" : "";
			op2 = m.group().equals("&&")? "&&" : "";
			int start = m.start();
			int end = m.end();
			leftCondition = if_condition.substring(0,start).trim();
			rightCondition = if_condition.substring(end).trim();
			
			if( !isBraceValid(leftCondition) ) continue ;
			
			if( !isBraceValid(rightCondition) ) continue ;
			
			if( op1.equals("||") ) {
				cntAnd++;
				opOR_list.add(op1);
				opAnd_list.add("");
				leftConditionList.add(leftCondition);
				rightConditionList.add(rightCondition);
			}else if( op2.equals("&&") ) {
				cntOr++;
				opAnd_list.add(op2);
				opOR_list.add("");
				leftConditionList.add(leftCondition);
				rightConditionList.add(rightCondition);
			}
		}
		
		if(cntAnd != 0 && cntOr != 0) {
			opOR_list.clear();
			opAnd_list.clear();
			leftConditionList.clear();
			rightConditionList.clear();
		}
		
		//case1:(1) if( a || b || c) 若a永真，消除b||c；若 a非永真，b永真，消除 c
		if(opOR_list.size() > 0) {
			String op = "";
			String assertStmt;
			if(!opOR_list.get(0).equals("")) {
				op = "||";
				assertStmt = "assert( tempValue == 1 ) ;";
			}else  {
				op = "&&";
				assertStmt = "assert( tempValue == 0 ) ;";
			}
			
			for(int i = 0; i < leftConditionList.size(); i++) {
				Map<Integer, List<String>> modifyLineMap = new HashMap<Integer, List<String>>();
				
				if(beforeIfSline != null) {
				/* if(){
				 * }else if( a op b ){
				 * }else{
				 * }
				 * =>
				 * if(){
				 * }else{
				 * 	_Bool tempValue = a ;
				 * 	if( tempValue op b ){
				 * 	} else{
				 *  }
				 * }
				 * */
					List<String> ifSlineList = new ArrayList<String>();
					List<String> ifElineList = new ArrayList<String>();
					
					ifSlineList.add(space + "} else {");
//					ifSlineList.add(space + "\tint tempValue = (" + leftConditionList.get(i) + ")? 1 : 0 ;");
					ifSlineList.add(space + "\t_Bool tempValue = " + leftConditionList.get(i) + " ;");
					ifSlineList.add(space + "\t" + assertStmt);
					ifSlineList.add(space + "\tif ( tempValue " + op + " " + rightConditionList.get(i) + " ) {");
					ifElineList.add(space + "\t}");
					ifElineList.add(space + "}");
					
					modifyLineMap.put(stmt.getStartLine(), ifSlineList);
					modifyLineMap.put(stmt.getEndLine(), ifElineList);
				}else {
					List<String> ifSlineList = new ArrayList<String>();
//					ifSlineList.add(space + "int tempValue = (" + leftConditionList.get(i) + ")? 1 : 0 ;");
					ifSlineList.add(space + "_Bool tempValue = " + leftConditionList.get(i) + " ;");
					ifSlineList.add(space + assertStmt);
					ifSlineList.add(space + "if ( tempValue " + op + " " + rightConditionList.get(i) + " ) {");
					modifyLineMap.put(stmt.getStartLine(), ifSlineList);
				}
				
				boolean assertResult = IfExecResult.assertIf(mutationDir, mutationCnt, file, initialList, modifyLineMap);
				if(assertResult) {
					initial_block = new ArrayList<String>();
					trans_block = new ArrayList<String>();
					initial_block.add(space + "//initial_block");
					trans_block.add(space + "//trans_block");
					
					for(int row = child.getStartLine(); row <= child.getEndLine(); row++) {
						String s = initialList.get(row);
						initial_block.add(s);
						if(row == stmt.getStartLine()) {
							trans_block.add( ifSline + "if( " + leftConditionList.get(i) +" ) {"); 
						}else {
							trans_block.add(s);
						}
					}
					
					trans_block = Rename.getRenamedTransBlock(oldName, trans_block);
					genMutation(suffix, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
				
					break ;
				}
			}
		}
		
		//case1:(2) if condition的整体是否是 永假/不到达
		String assertStmt = "assert( tempValue == 0 ) ;";
		Map<Integer, List<String>> modifyLineMap = new HashMap<Integer, List<String>>();
		if(beforeIfSline != null) {
		/* if(){
		 * }else if( a op b ){
		 * }else{
		 * }
		 * =>
		 * if(){
		 * }else{
		 * 	_Bool tempValue = a op b;
		 *  assert( tempValue == 0 );
		 * 	if( tempValue ){
		 * 	} else{
		 *  }
		 * }
		 * */
			List<String> ifSlineList = new ArrayList<String>();
			List<String> ifElineList = new ArrayList<String>();
			
			ifSlineList.add(space + "} else {");
//			ifSlineList.add(space + "\tint tempValue = (" + if_condition + ")? 1 : 0 ;");
			ifSlineList.add(space + "\t_Bool tempValue = " + if_condition + " ;");
			ifSlineList.add(space + "\t" + assertStmt);
			ifSlineList.add(space + "\tif ( tempValue ) {");
			ifElineList.add(space + "\t}");
			ifElineList.add(space + "}");
			
			modifyLineMap.put(stmt.getStartLine(), ifSlineList);
			modifyLineMap.put(stmt.getEndLine(), ifElineList);
		}else {
			List<String> ifSlineList = new ArrayList<String>();
//			ifSlineList.add("int tempValue = (" + if_condition + ")? 1 : 0 ;");
			ifSlineList.add(space + "_Bool tempValue = " + if_condition + " ;");
			ifSlineList.add(space + assertStmt);
			ifSlineList.add(space + "if ( tempValue ) {");
			modifyLineMap.put(stmt.getStartLine(), ifSlineList);
		}
		
		boolean assertResult = IfExecResult.assertIf(mutationDir, mutationCnt, file, initialList, modifyLineMap);
		if(assertResult) {
			initial_block = new ArrayList<String>();
			trans_block = new ArrayList<String>();
			initial_block.add(space + "//initial_block");
			trans_block.add(space + "//trans_block_false");
			
			for(int row = child.getStartLine(); row <= child.getEndLine(); row++) {
				initial_block.add(initialList.get(row));
			}
			if(beforeIfSline != null) {
				/*if() {
				 *} else if(a && b) {	//永假
				 * ...
				 *} else if( c ) {
				 * 	,,,
				 *} 
				 * =>
				 *if() {
				 *} else { 
				 * a && b; 
				 * if( c ){
				 *  ,,,
				 * }
				 *}
				 */
				trans_block.addAll(beforeIfSline);
				trans_block.add(space + "} else {");
				trans_block.add(space + "\t" + if_condition + ";");
				if(stmt.getHasElse() == true) {
					String regexElseif = "^.*\\belse\\b\\s+(\\bif\\b.*$)";
					Pattern pElseif = Pattern.compile(regexElseif);
					Matcher mElseif = pElseif.matcher(initialList.get(stmt.getElseline()));
					if(mElseif.find()) {
						trans_block.add(space + "\t" + mElseif.group(1));	//if( c ) {
						for( int i = stmt.getElseline() + 1; i < child.getEndLine(); i++) {
							trans_block.add("\t" + initialList.get(i));
						}
						trans_block.add(space + "\t}");
					}else {
						for( int i = stmt.getElseline() + 1; i < child.getEndLine(); i++) {
							trans_block.add(initialList.get(i));
						}
					}
				}
				trans_block.add(space +"}");
			}else {
				trans_block.add(space + if_condition + ";");
				if(stmt.getHasElse() == true) {
					String regexElseif = "^.*\\belse\\b\\s+(\\bif\\b.*$)";
					Pattern pElseif = Pattern.compile(regexElseif);
					Matcher mElseif = pElseif.matcher(initialList.get(stmt.getElseline()));
					if(mElseif.find()) {
						trans_block.add(space + mElseif.group(1));	//if( c ) {
						for( int i = stmt.getElseline() + 1; i < child.getEndLine(); i++) {
							trans_block.add(initialList.get(i));
						}
						trans_block.add(space + "}");
					}else {
						for( int i = stmt.getElseline() + 1; i < child.getEndLine(); i++) {
							trans_block.add(initialList.get(i));
						}
					}
				}
			}
			
			trans_block = Rename.getRenamedTransBlock(oldName, trans_block);
			genMutation(suffix, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
		}
		
		//case1:(3) if condition的整体是否是 永真/不到达
		assertStmt = "assert( tempValue == 1 ) ;";
		modifyLineMap = new HashMap<Integer, List<String>>();
		if(beforeIfSline != null) {
		/* if(){
		 * }else if( a op b ){
		 * }else{
		 * }
		 * =>
		 * if(){
		 * }else{
		 * 	_Bool tempValue = a op b;
		 *  assert( tempValue == 1 );
		 * 	if( tempValue ){
		 * 	} else{
		 *  }
		 * }
		 * */
			List<String> ifSlineList = new ArrayList<String>();
			List<String> ifElineList = new ArrayList<String>();
			
			ifSlineList.add(space + "} else {");
			ifSlineList.add(space + "\t_Bool tempValue = " + if_condition + " ;");
			ifSlineList.add(space + "\t" + assertStmt);
			ifSlineList.add(space + "\tif ( tempValue ) {");
			ifElineList.add(space + "\t}");
			ifElineList.add(space + "}");
			
			modifyLineMap.put(stmt.getStartLine(), ifSlineList);
			modifyLineMap.put(stmt.getEndLine(), ifElineList);
		}else {
			List<String> ifSlineList = new ArrayList<String>();
			ifSlineList.add(space + "_Bool tempValue = " + if_condition + " ;");
			ifSlineList.add(space + assertStmt);
			ifSlineList.add(space + "if ( tempValue ) {");
			modifyLineMap.put(stmt.getStartLine(), ifSlineList);
		}
		
		assertResult = IfExecResult.assertIf(mutationDir, mutationCnt, file, initialList, modifyLineMap);
		if(assertResult) {
			initial_block = new ArrayList<String>();
			trans_block = new ArrayList<String>();
			initial_block.add(space + "//initial_block");
			trans_block.add(space + "//trans_block_true");
			
			for(int row = child.getStartLine(); row <= child.getEndLine(); row++) {
				initial_block.add(initialList.get(row));
			}
			if(beforeIfSline != null) {
				/*if() {
				 *} else if(a && b) {	//永真
				 * ...
				 *} else if( c ) {
				 * 	,,,
				 *} 
				 * =>
				 *if() {
				 *} else { 
				 * a && b; 
				 * ...
				 *}
				 */
				trans_block.addAll(beforeIfSline);
				trans_block.add(space + "} else {");
				trans_block.add(space + "\t" + if_condition + ";");
				for( int i = stmt.getStartLine() + 1; i < stmt.getElseline(); i++) {
					trans_block.add(initialList.get(i));
				}
				trans_block.add(space +"}");
			}else {
				trans_block.add(space + if_condition + ";");
				for( int i = stmt.getStartLine() + 1; i < stmt.getElseline(); i++) {
					trans_block.add(initialList.get(i));
				}
			}
			
			trans_block = Rename.getRenamedTransBlock(oldName, trans_block);
			genMutation(suffix, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
		
		}
		
		
		for(IfStatement child_if: stmt.getIfList()) {
			ShortCircuitTrans(child_if);
		}
	}
	
	private void AndOrSplitTrans(IfStatement stmt) throws IOException, InterruptedException {
		if(stmt == null) return;
		
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
		String suffix = "aos";
		
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
		if(if_condition.equals("")) {
			for(IfStatement child_if: stmt.getIfList()) {
				AndOrSplitTrans(child_if);
			}
			return ;
		}
		
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
		
		List<String> opOR_list = new ArrayList<String>();
		List<String> opAnd_list = new ArrayList<String>();
		List<String> leftConditionList = new ArrayList<String>();
		List<String> rightConditionList = new ArrayList<String>();
		int cntAnd = 0;
		int cntOr = 0;
//		int split_start = 0;
		String regex = "(\\|\\||&&)+";
		Pattern p = Pattern.compile(regex);
		Matcher m;
		m = p.matcher(if_condition);
		while(m.find()) {
			op1 = m.group().equals("||")? "||" : "";
			op2 = m.group().equals("&&")? "&&" : "";
			int start = m.start();
			int end = m.end();
			leftCondition = if_condition.substring(0,start).trim();
			rightCondition = if_condition.substring(end).trim();
			
			if( !isBraceValid(leftCondition) ) continue ;
			
			if( !isBraceValid(rightCondition) ) continue ;
			
			if( op1.equals("||") ) {
				cntAnd++;
				opOR_list.add(op1);
				opAnd_list.add("");
				leftConditionList.add(leftCondition);
				rightConditionList.add(rightCondition);
			}else if( op2.equals("&&") ) {
				cntOr++;
				opAnd_list.add(op2);
				opOR_list.add("");
				leftConditionList.add(leftCondition);
				rightConditionList.add(rightCondition);
			}
		}
		
		if(cntAnd != 0 && cntOr != 0) {
			opOR_list.clear();
			opAnd_list.clear();
			leftConditionList.clear();
			rightConditionList.clear();
		}
		
		//case2: 
		//(1)if(1 && ); if(0 || ) 
		//(2)if(a && b){} => if(a){if(b)}; if(a || b){} => if(a){}else if(b){};
		if(opOR_list.size() == 0) {
			opOR_list.add("||");
			opAnd_list.add("&&");
//			leftConditionList.add(if_condition);
//			rightConditionList.add("");
			leftConditionList.add("");
			rightConditionList.add("(" + if_condition + ")");
		}
		
		for(int index = 0; index < opOR_list.size(); index++) {
			
			op1 = opOR_list.get(index);
			op2 = opAnd_list.get(index); 
			
			if(op1.equals("||")) {
				leftCondition = leftConditionList.get(index);
				rightCondition = rightConditionList.get(index);
				
				initial_block = new ArrayList<String>();
				trans_block = new ArrayList<String>();
				initial_block.add(space + "//initial_block");
				trans_block.add(space + "//trans_block");
				if(beforeIfSline != null) {
					initial_block.addAll(beforeIfSline);
					trans_block.addAll(beforeIfSline);
				}
//				rightCondition = rightCondition.equals("")? "0": rightCondition;
				leftCondition = leftCondition.equals("")? "0": leftCondition;
				initial_block.add(ifSline + "if( " + leftCondition + " || " + rightCondition + " ) {");
				trans_block.add(ifSline + "if( " + leftCondition + " ) {");
				for(int i = stmt.getStartLine()+1; i < stmt.getElseline(); i++) {
					initial_block.add(initialList.get(i));
					trans_block.add(initialList.get(i));
				}
				
				trans_block.add(space + "} else if( " + rightCondition + " ) {");
				for( int i = stmt.getStartLine()+1; i < stmt.getElseline(); i++) {
					trans_block.add(initialList.get(i));
				}
				
				for( int i = stmt.getElseline(); i <= child.getEndLine(); i++) {
					initial_block.add(initialList.get(i));
					trans_block.add(initialList.get(i));
				}
				
				trans_block = Rename.getRenamedTransBlock(oldName, trans_block);
				genMutation(suffix, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
			}
			
			if(op2.equals("&&")) {
				leftCondition = leftConditionList.get(index);
				rightCondition = rightConditionList.get(index);
				
				initial_block = new ArrayList<String>();
				trans_block = new ArrayList<String>();
				initial_block.add(space + "//initial_block");
				trans_block.add(space + "//trans_block");
				if(beforeIfSline != null) {
					initial_block.addAll(beforeIfSline);
					trans_block.addAll(beforeIfSline);
				}
//				rightCondition = rightCondition.equals("")? "1": rightCondition;
				leftCondition = leftCondition.equals("")? "1": leftCondition;
				initial_block.add(ifSline + "if( " + leftCondition + " && " + rightCondition + " ) {");
				trans_block.add(ifSline + "if( " + leftCondition + " ) {");
				trans_block.add(space + "\tif( " + rightCondition + " ) {");
//				for( int i = stmt.getStartLine()+1; i < stmt.getElseline(); i++) {
//					initial_block.add(initialList.get(i));
//					trans_block.add("\t" + initialList.get(i));
//				}
//				trans_block.add(space + "\t" + "}");
//				for( int i = stmt.getElseline(); i <= child.getEndLine(); i++) {
//					initial_block.add(initialList.get(i));
//					trans_block.add(initialList.get(i));
//				}
				for( int i = stmt.getStartLine()+1; i <= child.getEndLine(); i++) {
					initial_block.add(initialList.get(i));
					trans_block.add("\t" + initialList.get(i));
				}
				for( int i = stmt.getElseline(); i <= child.getEndLine(); i++) {
					trans_block.add(initialList.get(i));
				}

				trans_block = Rename.getRenamedTransBlock(oldName, trans_block);
				genMutation(suffix, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
			}
		}
		
		for(IfStatement child_if: stmt.getIfList()) {
			AndOrSplitTrans(child_if);
		}
	}
	
	private void NegationTrans(IfStatement stmt) throws IOException, InterruptedException {
		if(stmt == null) return;
		
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
		String suffix = "ne";
		
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
		if(if_condition.equals("")) {
			for(IfStatement child_if: stmt.getIfList()) {
				NegationTrans(child_if);
			}
			return ;
		}
		
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
		
		//case3: 条件取反
		//mutationCaseTwo if(==)else -> if(!=)else
		//限制是operator[0,6]中的一个，不存在&&或||
		//不考虑if中有括号的情况 function?
		List<List<Integer>> op_start_end_index = getExistOperator( if_condition );
		List<Integer> op_index = op_start_end_index.get(0);
		if(op_index.size() == 1 && op_index.get(0) != 7 && op_index.get(0) != 8 
				&& if_condition.contains("&") == false && if_condition.contains("|") == false ) {
			
//			System.out.println(op_index);
			op1 = operators[op_index.get(0)];
			op2 = oppositeOperators[op_index.get(0)];
			int start = op_start_end_index.get(1).get(0);
			leftCondition = if_condition.substring(0,start).trim();
			rightCondition = if_condition.substring(start + op1.length()).trim();
			
			if( !isBraceValid(leftCondition) || !isBraceValid(rightCondition)) {
				for(IfStatement child_if: stmt.getIfList()) {
					NegationTrans(child_if);
				}
				return ;
			}
			
			initial_block = new ArrayList<String>();
			trans_block = new ArrayList<String>();
			initial_block.add(space + "//initial_block");
			trans_block.add(space + "//trans_block");
			for(int i = child.getStartLine(); i <= child.getEndLine(); i++) {
				initial_block.add(initialList.get(i));
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
			genMutation(suffix, initial_block, trans_block, varDeclare, conditionList, child.getStartLine(), child.getEndLine());
		}
		
		for(IfStatement child_if: stmt.getIfList()) {
			NegationTrans(child_if);
		}
	}
	public String getCondition(String line) {
		String regexIf = "\\bif\\b\\s*\\((.*)\\)\\s*\\{";
		Pattern pIf = Pattern.compile(regexIf);
		Matcher mIf = pIf.matcher(line);
		String if_condition = "";
		if(mIf.find()) {
			if_condition = mIf.group(1).trim();
		}
		else {
			return "";
		}
		int end = if_condition.length()-1;
		//去除多余的括号 形如 if((a + b)  == c))
		while(if_condition.charAt(0) == '(' && if_condition.charAt(end) == ')') {
			if( isBraceValid(if_condition.substring(1, end)) == true ) {
				if_condition = if_condition.substring(1,end).trim();
				end = if_condition.length()-1;
			}else break ;
		}
		
		return if_condition;
	}

	public boolean isBraceValid(String s) {
	    
	    Stack<Character> stack = new Stack<Character>();
	    for (int i = 0; i < s.length(); i++) {
	        char ch = s.charAt(i);
	        if(ch == ')') {
	            if (stack.isEmpty() || stack.peek() != '(') {
	                return false;
	            }
	            stack.pop();
	        } else if(ch == '('){
	            stack.push(ch);
	        }
	    }
	    return stack.isEmpty();
	}

	private List<List<Integer>> getExistOperator(String if_condition) {
	//	String[] operators = {"!", "==", "!=", ">=", "<=", ">", "<", "&&", "\\|\\|"};
		List<List<Integer>> op_start_end_index = new ArrayList<List<Integer>>();
		List<Integer> op_index = new ArrayList<Integer>();
		List<Integer> start_index = new ArrayList<Integer>();
		String regex = operators[0] + "[_0-9a-zA-Z\\s\\*\\(]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(if_condition);
		while(m.find()) {
			start_index.add(m.start());
			op_index.add(0);
		}
		for(int i = 1; i < operators.length; i++) {
			regex = "[_0-9a-zA-Z\\s\\*)]" + operators[i] + "[_0-9a-zA-Z\\s\\*\\(]";
			p = Pattern.compile(regex);
			m = p.matcher(if_condition);
			while(m.find()) {
				start_index.add(m.start()+1);
				op_index.add(i);
			}
		}
		op_start_end_index.add(op_index);
		op_start_end_index.add(start_index);
		
		return op_start_end_index;
	}
	
	public static String getSpace(String line) {
		String regexSpace = "^(\\s*)\\S";
		Pattern pspace = Pattern.compile(regexSpace);
		Matcher mspace = pspace.matcher(line);
		String space = "";
		if(mspace.find()) space = mspace.group(1);
		return space;
	}
	
	private void genMutation(String suffix, List<String> initial_block, List<String> trans_block, List<String> varDeclare, List<String> conditionList, int startline, int endline) {
		try {
			//System.out.println(dir);
			String filename = file.getName();
			File newFile = new File(mutationDir + "/" + filename.substring(0, filename.lastIndexOf(".c")) + "_" + suffix + "_"+ mutationCnt + ".c");
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