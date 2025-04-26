package Condition;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AST_Information.FileFormat;
import AST_Information.VarStmt_Gen;
import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.Inform_Gen.ConditionInform_Gen;
import AST_Information.model.SwitchStatement;
import expression_operation.Expression;

public class SwitchCondition {
	AstInform_Gen astgen;
	List<String> initialList = new ArrayList<String>();
	int mutationCnt = 0;
	File file;
	static String destFolder;
	static String rename_suffix = "_1";
	VarStmt_Gen varStmt;
	Map<Integer, List<String>> globalVarsDeclareMap;
	List<String> restoreGlobalVarStmt;
	List<String> storeGlobalVarStmt;
	static final String[] allTypes = {"char", "short", "int", "long", "float", "double"};
	static final String[] leftTypes = {"double", "float", "long", "int"};
	String regexFor = "\\bfor\\s*\\((.*);(.*);(.*?)\\)\\s*";
	String regexWhile = "\\bwhile\\s*\\((.*)\\)\\s*";
	String regexDo = "\\bdo\\s*";
	Pattern pfor = Pattern.compile(regexFor);
	Pattern pwhile = Pattern.compile(regexWhile);
	Pattern pdo = Pattern.compile(regexDo);
	Matcher mfor, mwhile, mdo;
	
//	public static void main(String[] args) {
//		File file = new File("/home/elowen/桌面/pr44575.c");
//		SwitchCondition Switch = new SwitchCondition();
//		Switch.Transformation(file, "/home/elowen/桌面/test2");
//		System.out.println("mutationCnt = " + Switch.mutationCnt);
//	}

	public void Transformation(File file, String destFolder) {
		mutationCnt = 0;
		this.file = file;
		if(destFolder.trim().endsWith("/") == false) destFolder += "/";
		SwitchCondition.destFolder = destFolder;
		initialList = FileFormat.genInitialList(file);
		astgen = new AstInform_Gen();
		astgen.getAstInform(file.getAbsolutePath());
		if(astgen.astList.size() == 0) return ;
		
		varStmt = new VarStmt_Gen(astgen, initialList);
		globalVarsDeclareMap = varStmt.genGlobalVarsDeclareMap();
		storeGlobalVarStmt = varStmt.genStoreGlobalVarStmt();
		restoreGlobalVarStmt = varStmt.genRestoreGlobalVarStmt();
		
		ConditionInform_Gen conditionGen = new ConditionInform_Gen(astgen);
		for(SwitchStatement stmt: conditionGen.outmostSwitchList) {
			SwitchTransformation(stmt);
		}
		
		/*
		 * switch、case、default单独一行，有无{}都可
		 * */
		
		System.out.println("mutationCnt = " + mutationCnt);
	}
	
	private void SwitchTransformation(SwitchStatement stmt) {
		if(stmt == null) return ;
//		System.out.println(stmt.getStartLine() + " " + stmt.getEndLine());
		
//		List<String> initial_block = getInitialSwitch(stmt.getStartLine(), stmt.getEndLine());
		List<String> trans_block = new ArrayList<String>();
		List<String> varDeclare = new ArrayList<String>();
		List<String> conditionList;
		List<String> oldName = null;
		String space = "";
		
		varDeclare.addAll(this.storeGlobalVarStmt);
		varDeclare.addAll(varStmt.genUseVarRenameStmt(stmt.getStartLine(), stmt.getUseVarList()));
		conditionList = varStmt.genConditionList();
		oldName = varStmt.getUseVarOldName();
		space = getSpace(initialList.get(stmt.getStartLine()));
		trans_block.add(space + "//trans_block");
		
		
		String switch_exp = "";
		String regexSwitch = "\\bswitch\\b\\s*\\((.*)\\)\\s*";
		String regexCase = "\\bcase\\b(.*)\\s*:.*";
		Pattern pSwitch = Pattern.compile(regexSwitch);
		Pattern pCase = Pattern.compile(regexCase);
		Matcher mSwitch, mCase;
		mSwitch = pSwitch.matcher( initialList.get(stmt.getStartLine()) );
		if(mSwitch.find()) {
			switch_exp = mSwitch.group(1);
		}
		
		List<Integer> caseLineList = stmt.getCaseLineList();
		Stack<List<String>> IfStack = new Stack<List<String>>();
		List<String> case_if;
		String line;
		
		boolean stmtHasBrace = initialList.get(stmt.getStartLine()).trim().endsWith("{") ? true : false;
		for(int i = 0; i < caseLineList.size(); i++) {
			case_if = new ArrayList<String>();
			int start = caseLineList.get(i);
			int end = 0;
//			if(i < caseLineList.size()-1) end = caseLineList.get(i+1)-1;
//			else end = stmt.getCaseEndline();
			line = initialList.get(start);
			String caseValue = "";
			boolean hasBrace = line.trim().endsWith("{") ? true :false;
			boolean hasBreak = false;
			
			//从下一个caseSline/defaultSline/stmtEndLine的上一行开始向上找当前case的endline
			if( i < caseLineList.size()-1 ) end = caseLineList.get(i+1)-1;
			else {
				if(stmt.getDefaultStartlLine() != -1) end = stmt.getDefaultStartlLine()-1;
				else {
					if(stmtHasBrace == true) {
						end = stmt.getEndLine()-1;
					}else end = stmt.getEndLine();
				}
			}
			//case若有右括号，endLine是右括号的上一行，右括号不添加
			if(hasBrace == true) {
				while(initialList.get(end--).trim().equals("}") == false) ;
			}
			
			mCase = pCase.matcher(line);
			if(mCase.find()) {
				caseValue = mCase.group(1).trim();
				String regexRange = "^[^'\"]+.*\\s*\\.\\.\\.\\s*.*[^'\"]$";
				Pattern pRange = Pattern.compile(regexRange);
				Matcher mRange = pRange.matcher(caseValue);
				if(mRange.find()) {	//case 1..9   case 5UL ... 7L  case a ... b
					String[] split = mRange.group().trim().split("\\s*\\.\\.\\.\\s*");
					String left = split[0];
					String right = split[1];
					if(i == 0) case_if.add(space + "if( (" + switch_exp + ") >= " + left 
							+ " && ("+ switch_exp + ") <=" + right + " ) {");
					else case_if.add(space + "} else if( (" + switch_exp + ") >= " + left
							+ " && ("+ switch_exp + ") <=" + right + " ) {");
				}else {
					if(i == 0) case_if.add(space + "if( (" + switch_exp + ") == " + caseValue + " ) {");
					else case_if.add(space + "} else if( (" + switch_exp + ") == " + caseValue + " ) {");
				}
			}
			
			for( int row = start+1; row <= end; row++) {
				//判断case中是否有breakline
				if(stmt.getBreakLineSet().contains(row)) {
					hasBreak = true;
					break ;
				}else {
					case_if.add(initialList.get(row));
				}
			}
			if(!IfStack.isEmpty()) {
				for(int j = 1; j < case_if.size(); j++) {
					for(List<String> exist_case_if: IfStack) {
						exist_case_if.add(case_if.get(j));
					}
				}
			}
			
			if(hasBreak == false) {
				IfStack.push(case_if);
			}else {
				for(List<String> exist_case_if:IfStack) {
					trans_block.addAll(exist_case_if);
				}
				trans_block.addAll(case_if);
				IfStack.clear();
			}
		}
		
		for(List<String> exit_case_if: IfStack) {
			trans_block.addAll(exit_case_if);
		}
		IfStack.clear();
//		for(int breakline: stmt.getBreakLineSet()) {
//			System.out.println(breakline);	
//		}
//		System.out.println();
		//若有case，此时if语句的最后一行未添加右括号
		
		int defaultSline = stmt.getDefaultStartlLine();
		if(defaultSline != -1) {		//has default
			if(stmt.getCaseLineList().size() == 0) {
				trans_block.add(space + "if( (" + switch_exp + ") || 1 ) {");
			}else trans_block.add(space + "} else {");
			
			boolean hasBrace = initialList.get(defaultSline).trim().endsWith("{") ? true : false;
			int end;
			if(stmtHasBrace == true) {
				end = stmt.getEndLine()-1;
			}else end = stmt.getEndLine();
			
			if(hasBrace == true) {
				while(initialList.get(end--).trim().equals("}") == false) ;
			}
			//defaultBody
			boolean breakflag = true;	//astInform, defalutBody的第一行语句，会显示和defaultSLine在同一行，当defaultBody的第一句是break时，截不到该break
			for(int start = defaultSline+1; start <= end; start++) {
				String bodyline = initialList.get(start);
				if(breakflag) {
					if(bodyline.trim().matches("break\\s*;")) {
						break ;
					}else if(bodyline.matches("\\s*")) {
						continue ;
					}
				}
				if(stmt.getBreakLineSet().contains(start)) {
					break ;
				}else {
//					System.out.println(bodyline);
					trans_block.add(bodyline);
					breakflag = false;
				}
			}
			
			trans_block.add(space + "}");
			
		}else {		//has not default
			if(stmt.getCaseLineList().size() == 0) {	//has not case
				trans_block.add( space + "if( (" + switch_exp + ") || 1 ) {");
				trans_block.add( space + "\t;");
				trans_block.add( space + "}");
			}else {		//has case
				trans_block.add(space + "}");
			}
		}
		
//		initial_block.addAll(this.restoreGlobalVarStmt);
		trans_block = transBlockRename(oldName, trans_block);
//		genMutation(this.globalVarsDeclareMap, initial_block, trans_block, varDeclare, conditionList, stmt.getStartLine(), stmt.getEndLine());
		genMutation(this.globalVarsDeclareMap, trans_block, varDeclare, conditionList, stmt.getStartLine(), stmt.getEndLine());

		for(SwitchStatement child_switch: stmt.getSwitchList()) {
			SwitchTransformation(child_switch);
		}
	}
	
	private List<String> getInitialSwitch(int startline, int endline){
		List<String> initial_switch = new ArrayList<String>();
		for(int i = startline; i <= endline; i++) {
			initial_switch.add(initialList.get(i));
		}
		return initial_switch;
	}
	
	private List<String> transBlockRename(List<String> oldName, List<String> block){
		for(int i = 0; i < block.size(); i++) {
			String str = block.get(i);
			for(String oldname: oldName) {
				String newname = oldname + rename_suffix;
				str = Expression.ExpReplace(str, oldname, newname);
			}
			block.set(i, str);
		}
		return block;
	}

	public String getSpace(String line) {
		String regexSpace = "^(\\s*)\\S";
		Pattern pspace = Pattern.compile(regexSpace);
		Matcher mspace = pspace.matcher(line);
		String space = "";
		if(mspace.find()) space = mspace.group(1);
		return space;
	}
	
	private void genMutation(Map<Integer, List<String>> globalVarsDeclareMap, List<String> trans_block, List<String> varDeclare, List<String> conditionList, int startline, int endline) {
		try {
			//System.out.println(dir);
			String filename = file.getName();
			File newFile = new File(destFolder + filename.substring(0, filename.lastIndexOf(".c")) + "_" + mutationCnt + ".c");
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
				
				if(lineCnt == startline) {
					varDeclare.forEach( s -> {
						pw.println(s);
					});
				}
				
				pw.println(line);
				
				if(lineCnt == endline) {
					this.restoreGlobalVarStmt.forEach(s -> {
						pw.println(s);
					});
					trans_block.forEach( s -> {
						pw.println(s);
					});
					conditionList.forEach( s -> {
						pw.println(s);
					});
				}
				
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
