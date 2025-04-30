package DataFlow;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.PrintWriter;

import AST_Information.FileFormat;
import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.AstVariable;
import AST_Information.model.FunctionBlock;

public class DataFlowSR_backup {
	static final String[] varTypes = {
			"signed char", "char", "unsigned char",
			"signed short int", "signed short", "short int", "short", "unsigned short int", "unsigned short",
			"signed int", "int", "unsigned int", 
			"signed long int", "signed long", "long int", "long", "unsigned long int", "unsigned long",
			"signed long long int", "signed long long", "long long int", "long long", "unsigned long long int", "unsigned long long"
			};
	
	AstInform_Gen astgen;
	List<String> initialList = new ArrayList<String>();
	int mutationCnt = 0;
	File file;
	String mutationDir;
	static String rename_suffix = "_1";
	public static List<String> modPowFunction = genModPowFunction();
	
	public void Transformation(File file, String mutationDir) {
		mutationCnt = 0;
		this.file = file;
		this.mutationDir = mutationDir;
		initialList = FileFormat.genInitialList(file);
		astgen = new AstInform_Gen();
		astgen.getAstInform(file.getAbsolutePath());
		if(astgen.astList.size() == 0) return ;

		List<AstVariable> allVars = astgen.getAllVars();
		List<FunctionBlock> functionBlockList = astgen.getAllFunctionBlocks();
		int firstFunctionStartline = functionBlockList.get(0).startline;
		
		for(FunctionBlock fun: functionBlockList) {
			if(fun.startline >= fun.endline) continue ;
			List<AstVariable> initializedUseVars = ChooseOperation.getInitializedUseVars(fun.startline, fun.endline, allVars);
			List<AstVariable[]> chosenVarsList;
			
			int addLine;
			Random random = new Random();
			List<List<String>> addedStatementsList = null;	
			List<String> varDeclare;
			List<String> conditionList;
			
			//mutationCaseOne		addadStatementsList分别存放的是vardeclare、expStmt、conditionlist
			chosenVarsList = ChooseOperation.getVarPermute(initializedUseVars, 2);
			if(chosenVarsList.size() == 0) {
				List<String[]> newVar = VariableOperation.genRandomVars(2, varTypes);
				varDeclare = new ArrayList<String>();
				//new_a不能为0
				//while(newVar.get(0)[2].startsWith("0")) newVar.get(0)[2] = VariableOperation.genRandomValue(newVar.get(0)[0]);
				for(String[] var: newVar) {
					varDeclare.add(var[0] + " " + var[1] + " = " + var[2] + ";");
				}
				addedStatementsList = genSRModPow(newVar);
				addedStatementsList.get(0).addAll(varDeclare);
				//在function.startline和returnline(endline)之间随机选择一个位置
				do {
					if(fun.returnLineSet.size() > 0) {
						addLine = random.nextInt(fun.returnLineSet.last() - fun.startline) + fun.startline;
					}else addLine = random.nextInt(fun.endline - fun.startline) + fun.startline;
				}while(initialList.get(addLine).trim().startsWith("switch") == true);
				
				genMutationCaseOne(firstFunctionStartline, addedStatementsList, fun.startline, addLine);
			}else {
				for(AstVariable[] chosenVars: chosenVarsList) {
					addLine = fun.startline;
					List<String[]> vars = new ArrayList<String[]>();
					varDeclare = new ArrayList<String>();
					if(chosenVarsList.get(0).length == 1) {
						String new_a = VariableOperation.genRandomName();
						String new_type = VariableOperation.genRandomType(varTypes);
						String newValue = VariableOperation.genRandomValue(new_type);
						//while(newValue.startsWith("0")) newValue = VariableOperation.genRandomValue(newTypes[0]);
						varDeclare.add(new_type + " " + new_a + " = " + newValue + ";");
						vars.add(new String[] {new_type, new_a});
					}
					for(AstVariable var: chosenVars) {
						vars.add(new String[] {var.getType(), var.getName()});
						//addLine = max( 所有Var在function内首次assign所在行 ) （排除switch的情况，可能没有括号{}）
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
					addedStatementsList = genSRModPow(vars);
					if(varDeclare.size() != 0) addedStatementsList.get(0).addAll(varDeclare);
					genMutationCaseOne(firstFunctionStartline, addedStatementsList, fun.leftBraceLine, addLine);
				}
			}
			
			//mutationCaseTwo	addadStatementsList分别存放的是vardeclare、conditionList、expStmt1、expStmt2
			chosenVarsList = ChooseOperation.getVarPermute(initializedUseVars, 1);
			if(chosenVarsList.size() == 0) {
				varDeclare = new ArrayList<String>();
				String new_name = VariableOperation.genRandomName();
				String new_type = VariableOperation.genRandomType(varTypes);
				String newValue = VariableOperation.genRandomValue(new_type);
				varDeclare.add(new_type + " " + new_name + " = " + newValue + ";"); 
				addedStatementsList = genSROther(new_name);
				varDeclare.addAll(addedStatementsList.get(0));
				conditionList = addedStatementsList.get(1);
				//mutation1
				//在function.startline和returnline(endline)之间随机选择一个位置
				do {
					if(fun.returnLineSet.size() > 0) {
						addLine = random.nextInt(fun.returnLineSet.last() - fun.startline) + fun.startline;
					}else addLine = random.nextInt(fun.endline - fun.startline) + fun.startline;
				}while(initialList.get(addLine).trim().startsWith("switch") == true);
				
				genMutationCaseTwo(varDeclare, conditionList, addedStatementsList.get(2), fun.startline, addLine);
				//mutation2
				genMutationCaseTwo(varDeclare, conditionList, addedStatementsList.get(3), fun.startline, addLine);
			}else {
				for(AstVariable[] chosenVars: chosenVarsList) {
					AstVariable var = chosenVars[0];
					addedStatementsList = genSROther(var.getName());
					varDeclare = addedStatementsList.get(0);
					conditionList = addedStatementsList.get(1);
					//var能够添加的行
					List<Integer> addLineList = new ArrayList<Integer>();
					int line = var.getDeclareLine();
					if( line >= fun.startline && line <= fun.endline) {
						if(var.getIsIntialized() == true) {
							addLineList.add(line);
						}
					}
					for(int i: var.useLine) {
						if(i >= fun.startline && i <= fun.endline) {
							if(initialList.get(i).trim().startsWith("switch") == false)
								addLineList.add(i);
						}
					}
					addLine = addLineList.get(random.nextInt(addLineList.size()));
					
					//mutation1
					genMutationCaseTwo(varDeclare, conditionList, addedStatementsList.get(2), fun.leftBraceLine, addLine);
					//mutation2
					genMutationCaseTwo(varDeclare, conditionList, addedStatementsList.get(3), fun.leftBraceLine, addLine);
				}
			}
		}
	}

	
	public static List<List<String>> genSRModPow(List<String[]> vars){
		List<List<String>> addList = new ArrayList<List<String>>();
		List<String> varDeclare = new ArrayList<String>();
		List<String> expStmt = new ArrayList<String>();
		List<String> conditionList = new ArrayList<String>();
		Random random = new Random(); 
		//程序中自带的a和n的值会更好 check scale
		//variable only use not change value
		int maxN = random.nextInt(18) + 2;
		int maxA = sqrt(Math.pow(2, 52), maxN);
		int a_value = (random.nextBoolean()?1:-1) * (random.nextInt(maxA)+1);
		int n_value = random.nextInt(maxN + 1);
		String leftName1 = "left_" + VariableOperation.genRandomName();
		String leftName2 = leftName1 + rename_suffix;
		String a_name = "a_" + VariableOperation.genRandomName();
		String n_name = "n_" + VariableOperation.genRandomName();
		varDeclare.add("int " + a_name + " = " + a_value + ";");
		varDeclare.add("int " + n_name + " = " + n_value + ";");
		varDeclare.add("long " + leftName1 + ";");
		varDeclare.add("long " + leftName2 + ";");
		addList.add(varDeclare);

		String exist_a = vars.get(0)[1];
		String exist_n = vars.get(1)[1];
		expStmt.add(a_name + " = (" + a_name + ">(int)" + exist_a + " && (int)" + exist_a + "!=0)? " + "(int)" + exist_a + ":" + a_name + ";");
		expStmt.add(n_name + " = " + n_name +" > (int)" + exist_n + "? " + "(int)" + exist_n + ":" + n_name + ";");
		expStmt.add(leftName1 + " = pow(" + a_name + "," + n_name + ");" );
		expStmt.add(leftName2 + " = modPow(" + a_name + "," + n_name + ");");
		addList.add(expStmt);
		
		conditionList.add("assert(" + leftName1 + " == " + leftName2 + ") ;");
		addList.add(conditionList);
		
		return addList;
	}
	
	//c文件中插入这个方法 是将pow转换成加法做的运算
	// e.g. 4^3 = 4 * 4 * 4
	// 4*4 = 4 + 4 + 4 + 4 = 16
	// 16*4 = 16 + 16 + 16 + 16 = 64
	
	public static List<String> genModPowFunction(){
		List<String> functionList = new ArrayList<String>();
		
		functionList.add("long modPow(int a, int n){");
		functionList.add("\tif( n == 0) return 1;");
		functionList.add("\tlong answer = abs(a);");
		functionList.add("\tint exp = n;");
		functionList.add("\twhile (n > 1){");
		functionList.add("\t\tlong added = 0;");
		functionList.add("\t\tfor(int i = 0; i < abs(a); i++){");
		functionList.add("\t\tadded += answer;");
		functionList.add("\t\t}");
		functionList.add("\t\tanswer = added;");
		functionList.add("\t\tn--;");
		functionList.add("\t}");
		functionList.add("\treturn (a < 0 && exp % 2 == 1) ? -answer : answer;");
		functionList.add("}");
		
		return functionList;
	}
	
	public static List<List<String>> genSROther(String var_name) {
		List<List<String>> addList = new ArrayList<List<String>>();
		List<String> varDeclare = new ArrayList<String>();
		List<String> conditionList = new ArrayList<String>();
		List<String> expStmt;
		
		Random random = new Random();
		String leftName1 = "left_" + VariableOperation.genRandomName();
		String leftName2 = leftName1 + rename_suffix;
		String op3 = "";// +/-remainNum*variableName
		int x;//x random
		int step = 0;//>>step
		int[] nums = new int[2];
		
		//varDeclare
		varDeclare.add("long " + leftName1 + ";");
		varDeclare.add("long " + leftName2 + ";");
		addList.add(varDeclare);
		//conditionList
		conditionList.add("assert(" + leftName1 + " == " + leftName2 + ") ;");
		addList.add(conditionList);
		
		//expStmt-case1, "*", "<<"
		int remainNum = 0;
		expStmt = new ArrayList<String>();
		x = random.nextInt((int) Math.pow(2, 32)-2) + 1;
		if(IntegerOperation.isTwoPow(x)) {
			step = IntegerOperation.getTwoExp(x);
		}
		else {
			nums = IntegerOperation.getTwoNums(x);
			step = nums[0];
			remainNum = nums[1];
		}
		if(remainNum > 0) {
			op3 = "+";
		}
		else if(remainNum < 0) {
			op3 = "-";
		}
		expStmt.add(leftName1 + " = " + var_name + " * " + x + ";");
		if(remainNum == 0){
			expStmt.add(leftName2 + " = " + var_name + "<<" + step + ";");
		}
		else {
			expStmt.add(leftName2 + " = (" + var_name + "<<" + step + ") " + op3 + " (" + var_name + " * " + Math.abs(remainNum) + ");");
		}
		addList.add(expStmt);
		
		//expStmt-cas, "/", ">>"
		expStmt = new ArrayList<String>();
		step = random.nextInt(11) + 1;
		x = (int)Math.pow(2, step);
		expStmt.add(leftName1 + " = " + var_name + " / " + x + ";");
		expStmt.add(leftName2 + " = " + var_name + " >> " + step + ";");
		addList.add(expStmt);
		
		return addList;
	}
	
	public static int sqrt(double d, double i) {
		i = 1/i;
		return (int)Math.pow(d, i);
	}
	
	public void genMutationCaseOne(int firstFunctionStartline, List<List<String>> addedStatementsList, int funStartline, int addLine) {
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
				
				if(lineCnt == firstFunctionStartline) {
					modPowFunction.forEach(s ->{
						pw.println(s);
					});
					pw.println();
				}
				
				pw.println(line);

				if(lineCnt == funStartline) { //varDeclare
					addedStatementsList.get(0).forEach(s ->{
						pw.println(s);
					});
				}
				if(lineCnt == addLine) {
					addedStatementsList.get(1).forEach(s ->{	//expStmt
						pw.println(s);
					});
					addedStatementsList.get(2).forEach(s ->{	//conditionList
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
	
	public void genMutationCaseTwo(List<String> varDeclare, List<String> conditionList, List<String> expStmt, int funStartline, int addLine) {
		try {
			File newFile = new File(mutationDir + file.getName().replaceFirst(".c$", "") + "_" + mutationCnt + ".c");
			
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
