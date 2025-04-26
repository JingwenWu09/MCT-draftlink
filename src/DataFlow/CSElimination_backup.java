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


public class CSElimination_backup {
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
		
		int operandVarCnt = 4; //A op B ± C  A op B ± D
		
		List<FunctionBlock> functionBlockList = astgen.getAllFunctionBlocks();
		for(FunctionBlock fun: functionBlockList) {
			if(fun.startline >= fun.endline) continue ;
			//在startline和endline之间使用的、已有初始值的所有var
			List<AstVariable> initializedVars = new ArrayList<AstVariable>();
			for(AstVariable var: satisfyTypeVar) {
				//在function内已有初值的useVar(避免因未初始化导致的错误)这里initialaListed指的是声明时赋值，或者在useline出现
				int decline = var.getDeclareLine();
				if( decline >= fun.startline && decline < fun.endline) {
					if(var.getIsIntialized() == true) {
						satisfyTypeVar.add(var);
						continue ;
					}
				}
				for(int i: var.useLine) {
					if(i > fun.startline && i < fun.endline) {
						satisfyTypeVar.add(var);
						break ;
					}
				}
			}
			if(initializedVars.size() == 0) continue ;
				
			List<AstVariable[]> chosenVarsList = ChooseOperation.getVarPermute(initializedVars, operandVarCnt);

			//生成所有可能的表达式
			int addLine;
			Random random = new Random();
			List<List<String>> addedStatementsList = null;
			List<String> varDeclare;
			List<String> newVarDeclare;
			List<String> conditionList;
			
			if(chosenVarsList.size() == 0) {
			//没有变量，生成4	addedStatementsList存放的分别是varDeclare、conditionList、expStmt1、expStmt2、expStmt3、expStmt4
				for(int i = 0; i < operators.size(); i++) {
					List<String[]> newVar = VariableOperation.genRandomVars(operandVarCnt, varTypes);
					newVarDeclare = new ArrayList<String>();
					for(String[] var: newVar) {
						newVarDeclare.add(var[0] + " " + var[1] + " = " + var[2] + ";");
					}
					addedStatementsList = genTransBlockStatement(newVar, i);
					varDeclare = new ArrayList<String>();
					varDeclare.addAll(newVarDeclare);
					varDeclare.addAll(addedStatementsList.get(0));
					conditionList = addedStatementsList.get(1);
					for(int j = 2; j < addedStatementsList.size(); j++) {
						do {
							if(fun.returnLineSet.size() > 0) {
								addLine = random.nextInt(fun.returnLineSet.last() - fun.startline) + fun.startline;
							}else addLine = random.nextInt(fun.endline - fun.startline) + fun.startline;
						}while(initialList.get(addLine).trim().startsWith("switch") == true);
						genMutation(varDeclare, conditionList, addedStatementsList.get(j), fun.startline, addLine);
					}
				}
			}else {
				int addedCnt = operandVarCnt - chosenVarsList.get(0).length;
				for(AstVariable[] chosenVars: chosenVarsList) {
					addLine = fun.startline;
					List<String[]> existVars = new ArrayList<String[]>();
					newVarDeclare = null;
					for(AstVariable var: chosenVars) {
						existVars.add(new String[] {var.getType(), var.getName()});
						//addLine = max( existvar在function内首次assingen的行 )
						int line = var.getDeclareLine();
						if( line >= fun.startline && line <= fun.endline && var.getIsIntialized() == true ) {
							addLine = Math.max(addLine, line);
							continue ;
						}
						for(int i: var.useLine) {
							if(i >= fun.startline && i <= fun.endline) {
								if(initialList.get(i).trim().startsWith("switch") == false) {
									addLine = Math.max(addLine, i);
									break ;
								}
							}
						}
					}
					for(int i = 0; i < operators.size(); i++) {
						List<String[]> vars = new ArrayList<String[]>();
						vars.addAll(existVars);
						if(addedCnt != 0) {
							newVarDeclare = new ArrayList<String>();
							List<String[]> newVar = VariableOperation.genRandomVars(addedCnt, varTypes);
							for(String[] var: newVar) {
								newVarDeclare.add(var[0] + " " + var[1] + " = " + var[2] + ";");
								vars.add(new String[] {var[0], var[1]});
							}
						}
						addedStatementsList = genTransBlockStatement(vars, i);
						varDeclare = new ArrayList<String>();
						if(newVarDeclare != null) varDeclare.addAll(newVarDeclare);
						varDeclare.addAll(addedStatementsList.get(0));
						conditionList = addedStatementsList.get(1);
						for(int j = 2; j < addedStatementsList.size(); j++) {
							genMutation(varDeclare, conditionList, addedStatementsList.get(j), fun.startline, addLine);
						}
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
		
		expStmt = new ArrayList<String>();
		expStmt.add(exp1 + " + " + c[1] + ";");
		expStmt.add(exp2 + " + " + d[1] + ";");
		expStmt.add(exp_motion + ";");
		expStmt.add(exp1_rename + " +" + c[1] + ";");
		expStmt.add(exp2_rename + " + " + d[1] + ";");
		statementsList.add(expStmt);
		expStmt = new ArrayList<String>();
		expStmt.add(exp1 + " + " + c[1] + ";");
		expStmt.add(exp2 + " - " + d[1] + ";");
		expStmt.add(exp_motion + ";");
		expStmt.add(exp1_rename + " +" + c[1] + ";");
		expStmt.add(exp2_rename + " - " + d[1] + ";");
		statementsList.add(expStmt);
		expStmt = new ArrayList<String>();
		expStmt.add(exp1 + " - " + c[1] + ";");
		expStmt.add(exp2 + " + " + d[1] + ";");
		expStmt.add(exp_motion+";");
		expStmt.add(exp1_rename + " -" + c[1] + ";");
		expStmt.add(exp2_rename + " + " + d[1] + ";");
		statementsList.add(expStmt);
		expStmt = new ArrayList<String>();
		expStmt.add(exp1 + " - " + c[1] + ";");
		expStmt.add(exp2 + " - " + d[1] + ";");
		expStmt.add(exp_motion+";");
		expStmt.add(exp1_rename + " - " + c[1] + ";");
		expStmt.add(exp2_rename + " - " + d[1] + ";");
		statementsList.add(expStmt);
		
		return statementsList;
	}
	
	public void genMutation(List<String> varDeclare, List<String> conditionList, List<String> expStmt, int funStartline, int addLine) {
		try {
		
			File newFile = new File(mutationDir + "/" + file.getName().replaceFirst(".c$", "") + "_" + mutationCnt + ".c");
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

				if(lineCnt == funStartline) { //varDeclare
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
