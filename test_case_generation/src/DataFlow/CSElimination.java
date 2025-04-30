package DataFlow;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import AST_Information.FileFormat;
import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.AstVariable;
import AST_Information.model.FunctionBlock;


public class CSElimination {
	static final String[] varTypes = {
			"signed char", "char", "unsigned char",
			"signed short int", "signed short", "short int", "short", "unsigned short int", "unsigned short",
			"signed int", "int", "unsigned int", 
			"signed long int", "signed long", "long int", "long", "unsigned long int", "unsigned long",
			"signed long long int", "signed long long", "long long int", "long long", "unsigned long long int", "unsigned long long",
			};
	
	static final List<String> operators = Arrays.asList("+","-","*","/",">>","<<","pow", "%");
	
	AstInform_Gen astgen;
	List<String> initialList = new ArrayList<String>();
	int mutationCnt = 0;
	File file;
	String mutationDir;
	static String rename_suffix = "_1";

	public void Transformation(File file, String mutationDir) {
		mutationCnt = 0;
		this.file = file;
		this.mutationDir = mutationDir;
		initialList = FileFormat.genInitialList(file);
		astgen = new AstInform_Gen();
		astgen.getAstInform(file.getAbsolutePath());
		if(astgen.astList.size() == 0) return ;
		
		//满足vartype条件的useVar
		List<AstVariable> allVars = astgen.getAllVars();
		List<AstVariable> satisfyTypeVar = new ArrayList<AstVariable>();
		for(AstVariable var: allVars) {
			for(String type: varTypes) {
				if(var.getType().equals(type)) {
					satisfyTypeVar.add(var);
				}
			}
		}
		if(satisfyTypeVar.size() == 0) return ;	//没有满足条件的var return
		
		List<FunctionBlock> functionBlockList = astgen.getAllFunctionBlocks();
		for(FunctionBlock fun: functionBlockList) {
			if(fun.startline >= fun.endline) continue ;
			//在startline和endline之间使用的、已有初始值的所有var
			List<AstVariable> initializedVars = new ArrayList<AstVariable>();
			List<Integer> firstAssignedLine = new ArrayList<Integer>();
			for(AstVariable var: satisfyTypeVar) {
				//在function内已有初值的useVar(避免因未初始化导致的错误)这里initialaListed指的是声明时赋值，或者在useline出现
				int decline = var.getDeclareLine();
				if( decline >= fun.startline && decline < fun.endline) {
					if(var.getIsIntialized() == true) {
						initializedVars.add(var);
						firstAssignedLine.add(Math.max(fun.leftBraceLine, decline));	//考虑到function声明在多行的情况
						continue ;
					}
				}
				for(int i: var.useLine) {
					if(i > fun.startline && i < fun.endline) {
						if(initialList.get(i).trim().startsWith("switch") == true) {
							continue ;
						}
						initializedVars.add(var);
						firstAssignedLine.add(i);
						break ;
					}
				}
			}
			if(initializedVars.size() == 0) continue ;		//没有满足条件的useVar

			//生成所有可能的表达式
			int operandVarCnt = 4;
			int addLine;
			List<List<String>> addedStatementsList;
			List<String> varDeclare;
			List<String> conditionList;
			
			for(int op_index = 0; op_index < operators.size(); op_index++) {		//满足条件的var大于等于一个小于四个
				
				if(initializedVars.size() < operandVarCnt) {
					
					addLine = fun.leftBraceLine;
					varDeclare = new ArrayList<String>();
					List<String[]> existVars = new ArrayList<String[]>();
					
					for(int index = 0; index < initializedVars.size(); index++) {
						AstVariable var = initializedVars.get(index);
						existVars.add(new String[] {var.getType(), var.getName()});
						//addLine = max( existvar在function内首次assingen的行 )
						addLine = Math.max(addLine, firstAssignedLine.get(index));
					}
					
					int newVarCnt = operandVarCnt - initializedVars.size();
					List<String[]> newVar = VariableOperation.genRandomVars(newVarCnt, varTypes);
					for(String[] var: newVar) {
						varDeclare.add(var[0] + " " + var[1] + " = " + var[2] + ";");
						existVars.add(new String[] {var[0], var[1]});
					}
					
					addedStatementsList = genTransBlockStatement(existVars, op_index);
					varDeclare.addAll(addedStatementsList.get(0));
					conditionList = addedStatementsList.get(1);
					genMutation(varDeclare, conditionList, addedStatementsList.get(2), addLine, fun.leftBraceLine);
					
				}else {
					
					for( int start = 0; start <= initializedVars.size() - 4; start++) {		//大于等于四个，窗口为4，保证每个变量都能使用到
						int end = start + 4;
						
						addLine = fun.leftBraceLine;
						varDeclare = new ArrayList<String>();
						List<String[]> existVars = new ArrayList<String[]>();
						
						for(int index = start; index < end; index++) {
							AstVariable var = initializedVars.get(index);
							existVars.add(new String[] {var.getType(), var.getName()});
							//addLine = max( existvar在function内首次assingen的行 )
							addLine = Math.max(addLine, firstAssignedLine.get(index));
						}
						
						addedStatementsList = genTransBlockStatement(existVars, op_index);
						varDeclare.addAll(addedStatementsList.get(0));
						conditionList = addedStatementsList.get(1);
						genMutation(varDeclare, conditionList, addedStatementsList.get(2), addLine, fun.leftBraceLine);
					}
					
				}
			}
		}
		System.out.println(file.getName() + ", mutationCnt = " + mutationCnt);
	}
	
	private List<List<String>> genTransBlockStatement(List<String[]> vars, int operator_index){
		List<List<String>> statementsList = new ArrayList<List<String>>();
		List<String> varDeclare = new ArrayList<String>();
		List<String> conditionList = new ArrayList<String>();
		List<String> expStmt;
		String subexp = "";
		String[] a = vars.get(0);
		String[] b = vars.get(1);
		String[] c = vars.get(2);
		String[] d = vars.get(3);
		
		if(operator_index < 3) {
			subexp = a[1] + operators.get(operator_index) + b[1]; //a*b
		}else if(operator_index == 3) {
			subexp = a[1] + operators.get(operator_index) + "(" + b[1] + "? " + b[1] + ":1)";
		}else if(operator_index == 4 || operator_index == 5){
			subexp = "(int)" + a[1] + operators.get(operator_index) + "((int)" + b[1] + "? (int)" + b[1] + ":1)";
		}else if(operator_index == 6 ) {
			subexp = "pow(" + a[1] + "," + b[1] + ")";
		}else if( operator_index == 7) {
			subexp = "(int)" + a[1] + operators.get(operator_index) + "((int)" + b[1] + "? (int)" + b[1] + ":1)";
		}
		
		//<+,+> <+,-> <-,+> <-,->;
		String exp1 = "", exp2 = "";
		String exp_motion = "", exp1_rename = "", exp2_rename = "";
		String[] names = VariableOperation.genRandomName(3);
		String[] types = VariableOperation.genRandomType(3, varTypes);
		//varDeclare
		varDeclare.add( types[0] + " " + names[0] + ";" );
		varDeclare.add( types[1] + " " + names[1] + ";" );
		varDeclare.add( types[0] + " " + names[0] + rename_suffix + ";" );
		varDeclare.add( types[1] + " " + names[1] + rename_suffix + ";" );
		varDeclare.add( types[2] + " " + names[2] + ";");
		statementsList.add(varDeclare);
		
		//conditionsList
		conditionList.add("assert(" + names[0] + " == " + names[0] + rename_suffix + ") ;");
		conditionList.add("assert(" + names[1] + " == " + names[1] + rename_suffix + ") ;");
		statementsList.add(conditionList);
		
		//expStmt
		exp1 = names[0] + " = (" + types[2] + ")(" + subexp + ")";
		exp2 = names[1] + " = (" + types[2] + ")(" + subexp + ")";
		exp_motion = names[2] + " = " + subexp;
		exp1_rename = names[0] + rename_suffix + " = " + names[2];
		exp2_rename = names[1] + rename_suffix + " = " + names[2];
		
		Random random = new Random();
		String op1 = random.nextBoolean() ? "-" : "+";
		String op2 = random.nextBoolean() ? "-" : "+";
		expStmt = new ArrayList<String>();
		expStmt.add(exp1 + " " + op1 + " " + c[1] + ";");
		expStmt.add(exp2 + " " + op2 + " " + d[1] + ";");
		expStmt.add(exp_motion + ";");
		expStmt.add(exp1_rename + " " + op1 + " " + c[1] + ";");
		expStmt.add(exp2_rename + " " + op2 + " " + d[1] + ";");
		statementsList.add(expStmt);
		
		return statementsList;
	}
	
	public void genMutation(List<String> varDeclare, List<String> conditionList, List<String> expStmt, int addLine, int funLeftBraceLine) {
		try {
			String filename = file.getName();
			File newFile = new File(mutationDir + "/" + filename.substring(0, filename.lastIndexOf(".c")) + "_" + mutationCnt + ".c");
			mutationCnt ++;
			
			if(newFile.exists()) {
				newFile.delete();
			}
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			PrintWriter pw = new PrintWriter(fw);
			
			int lineCnt = -1;
			for(String line: initialList) {
				lineCnt++;
				if(lineCnt == 0) {
					pw.println("#include <assert.h>");
					continue;
				}
				
				pw.println(line);

				if(lineCnt == funLeftBraceLine) { //varDeclare
					varDeclare.forEach(s ->{
						pw.println(s);
					});
				}
				if(lineCnt == addLine) {
					expStmt.forEach(s ->{	//expStmt
						pw.println(s);
					});
					conditionList.forEach(s ->{	//conditionList
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
