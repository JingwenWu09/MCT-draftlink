package AST_Information.Inform_Gen;

import AST_Information.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConditionInform_Gen{
	
	public List<IfStatement> outmostIfList = new ArrayList<IfStatement>();
	public List<SwitchStatement> switchList= new ArrayList<SwitchStatement>();
	public List<SwitchStatement> outmostSwitchList = new ArrayList<SwitchStatement>();
	
	public ConditionInform_Gen(AstInform_Gen astGen) {
		getConditionInform(astGen);
	}
	
	public void getConditionInform(AstInform_Gen astGen) {
		//astList
		String filepath = astGen.filepath;
		Map<String, AstVariable> allVarsMap = astGen.allVarsMap;
		List<String> astList = astGen.astList;
		Stack<IfStatement> ifStack = new Stack<IfStatement>();
		Stack<SwitchStatement> switchStack = new Stack<SwitchStatement>();
//		String regexStartLine1 = "<.*>\\sline:([0-9]+)(:[0-9]+)";
//		String regexStartLine2 = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*:([0-9]+):[0-9]+(,\\s.*)?>.*"; 
//		String regexStartLine3 = "<line:([0-9]+)(:[0-9]+)";
		String regexStartLine1 = "<line:([0-9]+)(:[0-9]+)";
		String regexStartLine2 = ".*[a-zA-Z]+\\s0x[a-z0-9]+\\s<" + filepath + ".*?:([0-9]+):[0-9]+(,\\s.*)?>.*"; 
		String regexStartLine3 = "<.*>\\sline:([0-9]+)(:[0-9]+)";
		String regexEndLine = "line:([0-9]+)(:[0-9]+>)";
		String regexStartCol1 = "<line:[0-9]+:([0-9]+)";
		String regexStartCol2 = "<col:([0-9]+)";
		String regexEndCol1 = "line:[0-9]+:([0-9]+)>";
		String regexEndCol2 = "col:([0-9]+)>";
		String regexVarUse = "\\s(Parm)?Var\\s(0x[0-9a-z]+)\\s'([_a-zA-Z]+[_a-zA-Z0-9]*)'\\s'([_a-zA-Z]+[_a-zA-Z0-9\\s\\(\\)\\*\\[\\]]*)'";	
		//String regexVarDecl = "(Parm)?VarDecl\\s(0x[0-9a-z]+)\\s<.*>.*\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'([_a-zA-Z]+[_a-zA-Z0-9\\s\\(\\)\\*\\[\\]]*)'(\\s)?(cinit)?"; 
		String regexVarDecl = "(Parm)?VarDecl\\s(0x[0-9a-z]+)\\s<.*>.*?(\\bused\\b)?\\s([_a-zA-Z]+[_a-zA-Z0-9]*)\\s'([_a-zA-Z]+[_a-zA-Z0-9,\\.\\s\\(\\)\\*\\[\\]]*)'"; 
		String regexNewStmt = "(.*)([A-Z]{1}[a-z]+Stmt)(\\s)";
		//String regexFileStart = ".*FunctionDecl\\s0x[0-9a-z]+\\s<" + filepath + ".*>.*";
		Pattern pStartLine1= Pattern.compile(regexStartLine1);
		Pattern pStartLine2 = Pattern.compile(regexStartLine2);
		Pattern pStartLine3 = Pattern.compile(regexStartLine3);
		Pattern pEndLine = Pattern.compile(regexEndLine);
		Pattern pStartCol1 = Pattern.compile(regexStartCol1);
		Pattern pStartCol2 = Pattern.compile(regexStartCol2);
		Pattern pEndCol1 = Pattern.compile(regexEndCol1);
		Pattern pEndCol2 = Pattern.compile(regexEndCol2);
		Pattern pVarUse = Pattern.compile(regexVarUse);
		Pattern pVarDecl = Pattern.compile(regexVarDecl);
		Pattern pNewStmt = Pattern.compile(regexNewStmt);
		//Pattern pFileStart = Pattern.compile(regexFileStart);
		Matcher mStartLine1, mStartLine2, mStartLine3, mEndLine;
		Matcher mStartCol1, mStartCol2, mEndCol1, mEndCol2;
		Matcher mVarUse, mVarDecl, mNewStmt;
		int startLine = 0, endLine = 0, tempLine = 0;
		int astTempLine = 0;
		int topEndLine = 0;
		IfStatement topIfStmt = null;
		IfStatement popIfStmt = null;
		SwitchStatement topSwitchStmt = null;
		SwitchStatement popSwitchStmt = null;
		AstVariable outsideVar = null;
		AstVariable insideVar = null;
		AstVariable useVar = null;

		//一、遍历ast所有行
		for(String line: astList) {
			astTempLine++;
			//1.update tempLine；不用改变startline
			mStartLine1 = pStartLine1.matcher(line);
			mStartLine2 = pStartLine2.matcher(line);
			mStartLine3 = pStartLine3.matcher(line);
			if(mStartLine1.find()) {
				tempLine = Integer.parseInt(mStartLine1.group(1));
			}else if(mStartLine2.find()) {
				tempLine = Integer.parseInt(mStartLine2.group(1));
			}else if(mStartLine3.find()) {
				tempLine = Integer.parseInt(mStartLine3.group(1));
			}
			
			//2.根据tempLine判断栈顶元素是否弹出
			//弹出所有不是当前操作行外层的ifstmt
			while(!ifStack.isEmpty()) {	//考虑两层嵌套在同一行结束的情况，for(){...for(){}}
				topIfStmt = ifStack.peek();
				topEndLine = topIfStmt.getEndLine();
				if(tempLine <= topEndLine) {	//当前操作行是栈顶元素的内部，退出循环
					break;
				}else {
					//System.out.println("??? hasPopStmt  newline: " + tempLine);
				//当前操作行不属于栈顶元素内部，弹出栈顶元素
					popIfStmt = ifStack.pop();
					popIfStmt.setAstEndLine(astTempLine-1);
					popIfStmt.getIfList().sort(new SortIfList());
					if(ifStack.isEmpty()) {
						//弹出popStmt后，栈为空，说明popStmt是最外层For循环
						outmostIfList.add(popIfStmt);
					}
				}
			}
			//弹出switchStmt
			while(!switchStack.isEmpty()) {
				topSwitchStmt = switchStack.peek();
				topEndLine = topSwitchStmt.getEndLine();
				if(tempLine <= topEndLine) {
					break;
				}else {
					popSwitchStmt = switchStack.pop();
					popSwitchStmt.setAstEndLine(astTempLine-1);
					popSwitchStmt.getSwitchList().sort(new SortSwitchList());
					if(switchStack.isEmpty()) {
//						switchList.add(popSwitchStmt);
						outmostSwitchList.add(popSwitchStmt);
					}
				}
			}
			
			//stack里存放的都是没有pop出来的，所以useVar和declVar都离topStackStmt最近
			//3.无论栈顶元素是否弹出，判断是否有declVar，将其放入最近的Loop中insideVarList中
			mVarDecl = pVarDecl.matcher(line);
			if(mVarDecl.find()) {
				String varid = mVarDecl.group(2);
				insideVar = allVarsMap.get(varid);
				if(!ifStack.isEmpty()) {
					topIfStmt = ifStack.peek();
					topIfStmt.getInsideList().add(insideVar);
				}
				if(!switchStack.isEmpty()) {
					topSwitchStmt = switchStack.peek();
					topSwitchStmt.getInsideList().add(insideVar);
				}
			}
			
			//4.无论栈顶元素是否弹出，判断是否有varUse
			mVarUse = pVarUse.matcher(line);
			if(mVarUse.find()) {
				String varid = mVarUse.group(2);
				useVar = allVarsMap.get(varid);
				
				//(1).栈非空，从栈顶if开始，层层遍历if的parentIf
				IfStatement childIf, parentIf;
				if(!ifStack.isEmpty()) {
					//System.out.println("VarUse: " + vartype + " " + varname);
					//System.out.println("startline: " + startLine + "; endeline: " + endLine + "； templine: " + tempLine);
					//System.out.println("line: " + line + "\n");
					childIf = ifStack.peek();
					parentIf = childIf.getParentIfStmt();
					boolean isFindInsideList = false;
					//childIf的inVar包括useVar
					for(AstVariable inVar: childIf.getInsideList()) {
						if(inVar.getId().equals(varid)) {
							isFindInsideList = true;
							break;
						}
					}
					//childLoop的inVar不包括useVar
					while( parentIf != null ) {
						//varid是否出现在childIf的inside中
						//不是childIf的inside，那么一定是childIf的useVar，再判断是否是childIf的outVar
						if(isFindInsideList == false) {
							boolean isExsitUseVar = false;
							//usevarList
							for(AstVariable usevar: childIf.getUseVarList()) {
								if(usevar.getId().equals(varid)) {
									isExsitUseVar = true;
									break;
								}
							}
							if(isExsitUseVar == false) {
								childIf.getUseVarList().add(useVar);
							}
							
							//outsideVarList
							for(AstVariable inVar: parentIf.getInsideList()) {
								if(inVar.getId().equals(varid)) {
									boolean isExsitOutVar = false;
									for(AstVariable outVar: childIf.getOutsideList()) {
										if(outVar.getId().equals(varid)) {
											isExsitOutVar = true;
											break;
										}
									}
									if(isExsitOutVar == false) {
										outsideVar = useVar;
										//outsideVar.getUseLine().add(tempLine);
										childIf.getOutsideList().add(outsideVar);
									}
									isFindInsideList = true;
									break;
								}
							}
						}
						
						if(isFindInsideList == false ) {
							childIf = parentIf;
							parentIf = childIf.getParentIfStmt();
						}else {
							break;
						}
					}
					
					//parent为空还没找到var的insideList，说明当前childIf是最外层的ifloop，var是最外层if外声明的变量
					if( isFindInsideList == false ) {
						boolean isExsitUseVar = false;
						//usevarList
						for(AstVariable usevar: childIf.getUseVarList()) {
							if(usevar.getId().equals(varid)) {
								isExsitUseVar = true;
								break;
							}
						}
						if(isExsitUseVar == false) {
							//useVar.getUseLine().add(tempLine);
							childIf.getUseVarList().add(useVar);
						}
						
						//outsideVarList
						boolean isExsitOutVar = false;
						for(AstVariable outVar: childIf.getOutsideList()) {
							if(outVar.getId().equals(varid)) {
								isExsitOutVar = true;
								break;
							}
						}
						if(isExsitOutVar == false) {
							outsideVar = useVar;
							//outsideVar.getUseLine().add(tempLine);
							childIf.getOutsideList().add(outsideVar);
						}
					}
				}//在stack中找到首个if，判断usevar是否为outvar并放入相应outlist中
				
				//(2)
				SwitchStatement childSwitch, parentSwitch;
				if(!switchStack.isEmpty()) {
					childSwitch = switchStack.peek();
					parentSwitch = childSwitch.getParentSwitchStmt();
					boolean isFindInsideList = false;
					//childIf的inVar包括useVar
					for(AstVariable inVar: childSwitch.getInsideList()) {
						if(inVar.getId().equals(varid)) {
							isFindInsideList = true;
							break;
						}
					}
					
					while( parentSwitch != null ) {
						//varid是否出现在childIf的inside中
						//不是childIf的inside，那么一定是childIf的useVar，再判断是否是childIf的outVar
						if(isFindInsideList == false) {
							boolean isExsitUseVar = false;
							//usevarList
							for(AstVariable usevar: childSwitch.getUseVarList()) {
								if(usevar.getId().equals(varid)) {
									isExsitUseVar = true;
									break;
								}
							}
							if(isExsitUseVar == false) {
								childSwitch.getUseVarList().add(useVar);
							}
							
							//outsideVarList
							for(AstVariable inVar: parentSwitch.getInsideList()) {
								if(inVar.getId().equals(varid)) {
									boolean isExsitOutVar = false;
									for(AstVariable outVar: childSwitch.getOutsideList()) {
										if(outVar.getId().equals(varid)) {
											isExsitOutVar = true;
											break;
										}
									}
									if(isExsitOutVar == false) {
										outsideVar = useVar;
										//outsideVar.getUseLine().add(tempLine);
										childSwitch.getOutsideList().add(outsideVar);
									}
									isFindInsideList = true;
									break;
								}
							}
						}
						
						if(isFindInsideList == false ) {
							childSwitch = parentSwitch;
							parentSwitch = childSwitch.getParentSwitchStmt();
						}else {
							break;
						}
					}
					
					//parent为空还没找到var的insideList，说明当前childIf是最外层的ifloop，var是最外层if外声明的变量
					if( isFindInsideList == false ) {
						boolean isExsitUseVar = false;
						//usevarList
						for(AstVariable usevar: childSwitch.getUseVarList()) {
							if(usevar.getId().equals(varid)) {
								isExsitUseVar = true;
								break;
							}
						}
						if(isExsitUseVar == false) {
							//useVar.getUseLine().add(tempLine);
							childSwitch.getUseVarList().add(useVar);
						}
						
						//outsideVarList
						boolean isExsitOutVar = false;
						for(AstVariable outVar: childSwitch.getOutsideList()) {
							if(outVar.getId().equals(varid)) {
								isExsitOutVar = true;
								break;
							}
						}
						if(isExsitOutVar == false) {
							outsideVar = useVar;
							//outsideVar.getUseLine().add(tempLine);
							childSwitch.getOutsideList().add(outsideVar);
						}
					}
				}
				
//				//(2).switchStmt的insideVar
//				//考虑嵌套switch的情况
//				if(!switchStack.isEmpty()) {
//					boolean isFindInsideList = false;
//					topSwitchStmt = switchStack.peek();
//					for(AstVariable inVar: topSwitchStmt.getInsideList()) {
//						if(inVar.getId().equals(varid)) {
//							isFindInsideList = true;
//							break;
//						}
//					}
//					//不是switch里定义的变量，那么就是在switch外定义的useVar
//					if(isFindInsideList == false) {
//						boolean isExsitUseVar = false;
//						//usevarList
//						for(AstVariable usevar: topSwitchStmt.getUseVarList()) {
//							if(usevar.getId().equals(varid)) {
//								isExsitUseVar = true;
//								break;
//							}
//						}
//						if(isExsitUseVar == false) {
//							topSwitchStmt.getUseVarList().add(useVar);
//						}
//					}
//				}
			}
			
			//5.是否有新的Stmt,有则压入栈，tempLine就是新的stmt的startLine，第1步已经更新了
			boolean flagNewIf = false;
			boolean has_else = false;
			boolean flagNewSwitch = false;
			boolean flagNewCaseLine = false;
			boolean flagNewDefaultLine = false;
			boolean flagNewBreakLine = false;
			int startCol = -1, endCol = -1;
			String stmtType = null;
			mNewStmt = pNewStmt.matcher(line);
			if(mNewStmt.find()) {
				startLine = tempLine;
				mEndLine = pEndLine.matcher(line);
				if(mEndLine.find()) {
					endLine = Integer.parseInt(mEndLine.group(1));
				}else {
					endLine = tempLine;
				}
				mStartCol1 = pStartCol1.matcher(line);
				mStartCol2 = pStartCol2.matcher(line);
				mEndCol1 = pEndCol1.matcher(line);
				mEndCol2 = pEndCol2.matcher(line);
				if(mStartCol1.find()) {
					startCol = Integer.parseInt(mStartCol1.group(1));
				}else if(mStartCol2.find()) {
					startCol = Integer.parseInt(mStartCol2.group(1));
				}
				if(mEndCol1.find()) {
					endCol = Integer.parseInt(mEndCol1.group(1));
				}else if(mEndCol2.find()) {
					endCol = Integer.parseInt(mEndCol2.group(1));
				}

				stmtType = mNewStmt.group(2);
				//declstmt没有compoundStmt
				if(stmtType.equals("IfStmt")) {
					flagNewIf = true;
					if(line.matches("^.*IfStmt\\s.*<.*>.*\\shas_else\\s*$")) {
						has_else = true;
					}
				}else if(stmtType.equals("SwitchStmt")) {
					flagNewSwitch = true;
				}else if(stmtType.equals("CaseStmt")) {
					flagNewCaseLine = true;
				}else if(stmtType.equals("DefaultStmt")) {
					flagNewDefaultLine = true;
				}else if(stmtType.equals("BreakStmt")) {
					flagNewBreakLine = true;
				}
			}
		
			if(flagNewIf == true) {
				IfStatement newIfStmt = new IfStatement();
				newIfStmt.setStartLine(startLine);
				newIfStmt.setEndLine(endLine);
				newIfStmt.setStartCol(startCol);
				newIfStmt.setEndCol(endCol);
				newIfStmt.setHasElse(has_else);
				newIfStmt.setAstStartLine(astTempLine);
				if(!ifStack.isEmpty()) {
					topIfStmt = ifStack.peek();
					topIfStmt.getIfList().add(newIfStmt);
					newIfStmt.setParentIfStmt(topIfStmt);
				}
				ifStack.push(newIfStmt);
				
			}else if(flagNewSwitch == true) {
				SwitchStatement newSwitchStmt = new SwitchStatement();
				newSwitchStmt.setStartLine(startLine);
				newSwitchStmt.setEndLine(endLine);
				newSwitchStmt.setStartCol(startCol);
				newSwitchStmt.setEndCol(endCol);
				newSwitchStmt.setAstStartLine(astTempLine);
				if(!switchStack.isEmpty()) {
					topSwitchStmt = switchStack.peek();
					topSwitchStmt.getSwitchList().add(newSwitchStmt);
					newSwitchStmt.setParentSwitchStmt(topSwitchStmt);
				}
				switchStack.push(newSwitchStmt);
				
			}else if(flagNewCaseLine == true) {
				if(!switchStack.isEmpty()) {
					topSwitchStmt = switchStack.peek();
					topSwitchStmt.getCaseLineList().add(startLine);
					topSwitchStmt.setCaseEndline(endLine);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
				}
				
			}else if(flagNewDefaultLine == true) {
				if(!switchStack.isEmpty()) {
					switchStack.peek().setDefaultStartLine(startLine);
					//switchStack.peek().setDefaultEndLine(endLine);
				}
				
			}else if(flagNewBreakLine == true) {
				if(!switchStack.isEmpty()) {
					switchStack.peek().getBreakLineSet().add(startLine);
				}
			}
		}

		//二、遍历完ast所有行，stmtStack出栈
		while(!ifStack.isEmpty()) {
			popIfStmt = ifStack.pop();
			/*更新popStmt的所有顺序*/
			popIfStmt.getIfList().sort(new SortIfList());
			/**/
			if(ifStack.isEmpty()) {
				outmostIfList.add(popIfStmt);
			}
		}
		while(!switchStack.isEmpty()) {
			popSwitchStmt = switchStack.pop();
			popSwitchStmt.getSwitchList().sort(new SortSwitchList());
			if(switchStack.isEmpty()) {
//				switchList.add(switchStack.pop());
				outmostSwitchList.add(popSwitchStmt);
			}
		}
	}
	
	
	class SortIfList implements Comparator<IfStatement>{
		@Override
        public int compare(IfStatement stmt1, IfStatement stmt2) {
			int cnt1 = stmt1.getStartLine();
			int cnt2 = stmt2.getStartLine();
            return cnt1 - cnt2;    // 升序排列
        } 
	}
	class SortSwitchList implements Comparator<SwitchStatement>{
		@Override
        public int compare(SwitchStatement stmt1, SwitchStatement stmt2) {
			int cnt1 = stmt1.getStartLine();
			int cnt2 = stmt2.getStartLine();
            return cnt1 - cnt2;    // 升序排列
        } 
	}
}

