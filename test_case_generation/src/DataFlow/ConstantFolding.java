package DataFlow;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import AST_Information.FileFormat;
import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.AstVariable;
import AST_Information.model.FunctionBlock;

public class ConstantFolding {
	static final String[] varTypes = {
//		add("signed short int"); add("signed short"); add("short int"); add("short"); add("unsigned short int") ; add("unsigned short");
//		add("signed int"); add("int"); add("unsigned int");
//		add("signed long int"); add("signed long"); add("long int"); add("long"); add("unsigned long int"); add("unsigned long");
			"signed short int", "signed short", "short int", "short", "unsigned short int", "unsigned short",
			"signed int", "int", "unsigned int", 
			"signed long int", "signed long", "long int", "long", "unsigned long int", "unsigned long"
			};
	
	static final List<String> operators = Arrays.asList("+","-","*","/","%",">>","<<","&","^","|");
	
	AstInform_Gen astgen;
	List<String> initialList = new ArrayList<String>();
	int mutationCnt = 0;
	File file;
	String mutationDir;
	static String rename_suffix = "_1";
	static String rename_suffix2 = "_2";
	
	
//	public static void main(String args[]) {
//		String dir = "/home/jing/MCTesting/mutation/constantFolding/";
//		String filepath = "/home/jing/MCTesting/loopAll/920411-1.c";
//		ConstantFolding cf = new ConstantFolding();
//		cf.Transformation(new File(filepath), dir);
//		System.out.println("finish! mutationCnt = " + cf.mutationCnt);
////		
////		String a_value = "1992835746";
////		String b_value = "13";
////		System.out.println(String.valueOf(Long.parseLong(a_value) << Integer.parseInt(b_value)));
//	}
//	
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
		
				
		//chose one var X, generate Var A,B, then exp: A op B ± X
		List<FunctionBlock> functionBlockList = astgen.getAllFunctionBlocks();
		for(FunctionBlock fun: functionBlockList) {
			if(fun.startline >= fun.endline) continue ;	//clang-format中function语句<=1行时，整个function只有一行
//			System.out.println("fun:" + fun.startline + fun.endline);
			for(AstVariable var: satisfyTypeVar) {
				//在function内已有初值的useVar(避免因未初始化导致的错误)这里initialaListed指的是声明时赋值，或者在useline出现
				//得到能添加的所有行 addLineList , declareline cinit 或者 useline(非swtichLine，因为可能switchStmt没有括号)
				List<Integer> addLineList = new ArrayList<Integer>();
				boolean flag_init = false;
				int decline = var.getDeclareLine();
				if( decline >= fun.startline && decline < fun.endline) {
					if(var.getIsIntialized() == true) {
						addLineList.add(Math.max(fun.leftBraceLine, decline));
						flag_init = true;
					}
				}
				
				for(int i: var.useLine) {
					if(i > fun.startline && i < fun.endline) {
						if(initialList.get(i).trim().startsWith("switch") == true) {
							continue ;
						}
						addLineList.add(Math.max(fun.leftBraceLine, i));
						flag_init = true;
						break;
					}
				}
				if(flag_init == false) continue ;	//useVar不满足initialized条件 continue；或者满足条件但是只在switch中出现
				
				Random random = new Random();
				int randomAddLine = addLineList.get( random.nextInt(0, addLineList.size()) );
				genTransformation(var.getType(), var.getName(), randomAddLine, fun.leftBraceLine);
			}
		}
		System.out.println(file.getName() + ", mutationCnt = " + mutationCnt);
	}
	
	private void genTransformation(String var_type, String var_name, int randomAddLine, int funLeftBraceLine) {
		List<String> varDeclare;
		List<String> conditionList;
		List<String> expStmt;
		
		Random random = new Random();
		for(int i = 0; i < operators.size(); i++) {
			String op = operators.get(i);
			String[] types = VariableOperation.genRandomType(2,varTypes);
			String a_name = VariableOperation.genRandomName();
			String b_name = VariableOperation.genRandomName();
			String a_value = genRandomValue(types[0]);
			String b_value = genRandomValue(types[0]);
			/*tip: >> << 的右侧需为正整型，且右侧(unsigned/signed)的short、char、int都会转换为int计算，不能超过31*/
			/*而(unsigned/signed)long不能超过63，鉴于32位运行时，long为4字节，限制位移范围[1,31]*/
			if(op.equals(">>") || op.equals("<<")) b_value = String.valueOf( random.nextInt(31)+1 );
			String leftName1 = VariableOperation.genRandomName();
			String leftName2 = leftName1 + rename_suffix;
			String leftName3 = leftName1 + rename_suffix2;
			
			//varDeclare
			varDeclare = new ArrayList<String>();
			varDeclare.add(types[0] + " " + a_name + " = " + a_value + ";");
			if(op.equals(">>") || op.equals("<<")) {
				varDeclare.add("int " + b_name + " = " + b_value + ";");
			}else {
				varDeclare.add(types[0] + " " + b_name + " = " + b_value + ";");
			}
			varDeclare.add(types[1] + " " + leftName1 + ";");
			varDeclare.add(types[1] + " " + leftName2 + ";");
			varDeclare.add(types[1] + " " + leftName3 + ";");
			
			//expStmt
			conditionList = new ArrayList<String>();
			conditionList.add("assert(" + leftName1 + " == " + leftName2 + ") ;");
			conditionList.add("assert(" + leftName1 + " == " + leftName3 + ") ;");
			conditionList.add("assert(" + leftName2 + " == " + leftName3 + ") ;");
	
			//expStmt
			//>>和<<的优先级比+-d低
			String calculateResult = calculateTwoVar(types[0], a_value, b_value, op);
			String op2 = random.nextBoolean()? "+":"-";
			expStmt = new ArrayList<String>();
			if(i > 4) {
				expStmt.add(leftName1 + " = ( " + a_name + " " + op + " " + b_name + " ) " + op2 + " " + var_name + ";");
				expStmt.add(leftName2 + " = ( (" + types[0] + ")" + a_value + " " + op + " " + b_value + " ) " + op2 + " " + var_name + ";");
			}else {
				expStmt.add(leftName1 + " = " + a_name + " " + op + " " + b_name + " " + op2 + " " + var_name + ";");
				expStmt.add(leftName2 + " = (" + types[0] + ")" + a_value + " " + op + " ("+ types[0] + ")" + b_value + " " + op2 + " " + var_name + ";");
			}
			expStmt.add(leftName3 + " = (" + types[0] + ")" + calculateResult + " " + op2 + " " + var_name + ";");
			genMutation(varDeclare, conditionList, expStmt, randomAddLine, funLeftBraceLine);
		}
		return ;
	}
	
	private String calculateTwoVar(String type, String a_value, String b_value, String op) {
		String res = "";
		if(type.contains("short")) {
			if(op.equals("+")) 
				res = String.valueOf(Short.parseShort(a_value) + Short.parseShort(b_value));
			else if(op.equals("-"))
				res = String.valueOf(Short.parseShort(a_value) - Short.parseShort(b_value));
			else if(op.equals("*"))
				res = String.valueOf(Short.parseShort(a_value) * Short.parseShort(b_value));
			else if(op.equals("/"))
				res = String.valueOf(Short.parseShort(a_value) / Short.parseShort(b_value));
			else if(op.equals("%"))
				res = String.valueOf(Short.parseShort(a_value) % Short.parseShort(b_value));
			else if(op.equals(">>"))
				res = String.valueOf(Short.parseShort(a_value) >> Integer.parseInt(b_value));
			else if(op.equals("<<"))
				res = String.valueOf(Short.parseShort(a_value) << Integer.parseInt(b_value));
			else if(op.equals("&"))
				res = String.valueOf(Short.parseShort(a_value) & Short.parseShort(b_value));
			else if(op.equals("^"))
				res = String.valueOf(Short.parseShort(a_value) ^ Short.parseShort(b_value));
			else if(op.equals("|"))
				res = String.valueOf(Short.parseShort(a_value) | Short.parseShort(b_value));
		}
		else if(type.contains("unsigned long")) {
			if(op.equals("+")) 
				res = String.valueOf(Long.parseUnsignedLong(a_value) + Long.parseUnsignedLong(b_value));
			else if(op.equals("-"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) - Long.parseUnsignedLong(b_value));
			else if(op.equals("*"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) * Long.parseUnsignedLong(b_value));
			else if(op.equals("/"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) / Long.parseUnsignedLong(b_value));
			else if(op.equals("%"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) % Long.parseUnsignedLong(b_value));
			else if(op.equals(">>"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) >> Integer.parseInt(b_value));
			else if(op.equals("<<"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) << Integer.parseInt(b_value));
			else if(op.equals("&"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) & Long.parseUnsignedLong(b_value));
			else if(op.equals("^"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) ^ Long.parseUnsignedLong(b_value));
			else if(op.equals("|"))
				res = String.valueOf(Long.parseUnsignedLong(a_value) | Long.parseUnsignedLong(b_value));
		}
		else if(type.contains("long")) {
			if(op.equals("+")) 
				res = String.valueOf(Long.parseLong(a_value) + Long.parseLong(b_value));
			else if(op.equals("-"))
				res = String.valueOf(Long.parseLong(a_value) - Long.parseLong(b_value));
			else if(op.equals("*"))
				res = String.valueOf(Long.parseLong(a_value) * Long.parseLong(b_value));
			else if(op.equals("/"))
				res = String.valueOf(Long.parseLong(a_value) / Long.parseLong(b_value));
			else if(op.equals("%"))
				res = String.valueOf(Long.parseLong(a_value) % Long.parseLong(b_value));
			else if(op.equals(">>"))
				res = String.valueOf(Long.parseLong(a_value) >> Integer.parseInt(b_value));
			else if(op.equals("<<"))
				res = String.valueOf(Long.parseLong(a_value) << Integer.parseInt(b_value));
			else if(op.equals("&"))
				res = String.valueOf(Long.parseLong(a_value) & Long.parseLong(b_value));
			else if(op.equals("^"))
				res = String.valueOf(Long.parseLong(a_value) ^ Integer.parseInt(b_value));
			else if(op.equals("|"))
				res = String.valueOf(Long.parseLong(a_value) | Integer.parseInt(b_value));
			}
		else if(type.contains("unsigned int")) {
			if(op.equals("+")) 
				res = String.valueOf(Integer.parseUnsignedInt(a_value) + Integer.parseUnsignedInt(b_value));
			else if(op.equals("-"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) - Integer.parseUnsignedInt(b_value));
			else if(op.equals("*"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) * Integer.parseUnsignedInt(b_value));
			else if(op.equals("/"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) / Integer.parseUnsignedInt(b_value));
			else if(op.equals("%"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) % Integer.parseUnsignedInt(b_value));
			else if(op.equals(">>"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) >> Integer.parseInt(b_value));
			else if(op.equals("<<"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) << Integer.parseInt(b_value));
			else if(op.equals("&"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) & Integer.parseUnsignedInt(b_value));
			else if(op.equals("^"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) ^ Integer.parseInt(b_value));
			else if(op.equals("|"))
				res = String.valueOf(Integer.parseUnsignedInt(a_value) | Integer.parseUnsignedInt(b_value));
		}
		else if(type.contains("int")) {
			if(op.equals("+")) 
				res = String.valueOf(Integer.parseInt(a_value) + Integer.parseInt(b_value));
			else if(op.equals("-"))
				res = String.valueOf(Integer.parseInt(a_value) - Integer.parseInt(b_value));
			else if(op.equals("*"))
				res = String.valueOf(Integer.parseInt(a_value) * Integer.parseInt(b_value));
			else if(op.equals("/"))
				res = String.valueOf(Integer.parseInt(a_value) / Integer.parseInt(b_value));
			else if(op.equals("%"))
				res = String.valueOf(Integer.parseInt(a_value) % Integer.parseInt(b_value));
			else if(op.equals(">>"))
				res = String.valueOf(Integer.parseInt(a_value) >> Integer.parseInt(b_value));
			else if(op.equals("<<"))
				res = String.valueOf(Integer.parseInt(a_value) << Integer.parseInt(b_value));
			else if(op.equals("&"))
				res = String.valueOf(Integer.parseInt(a_value) & Integer.parseInt(b_value));
			else if(op.equals("^"))
				res = String.valueOf(Integer.parseInt(a_value) ^ Integer.parseInt(b_value));
			else if(op.equals("|"))
				res = String.valueOf(Integer.parseInt(a_value) | Integer.parseInt(b_value));
		}
		return res;
	}
	
	private static String genRandomValue(String type) {
		String value = null;
		Random random = new Random();
		String op = random.nextBoolean()? "":"-";
		if(type.equals("unsigned char")) {
			return String.valueOf(random.nextInt(1,(int)Math.pow(2, 3)));
		}
		else if(type.equals("char")) {
			//return String.valueOf(random.nextInt(-(int)Math.pow(2, 7),(int)Math.pow(2, 7)));
			return op + String.valueOf(random.nextInt(1,(int)Math.pow(2, 3)));
		}
		else if(type.contains("unsigned short")) {
			return String.valueOf(random.nextInt(1,(int)Math.pow(2, 7)));
		}
		else if(type.contains("short")) {
			//return String.valueOf(random.nextInt(-(int)Math.pow(2, 15),(int)Math.pow(2, 15)));
			return op + String.valueOf(random.nextInt(1,(int)Math.pow(2, 7)));
		}
		else if(type.contains("unsigned long")) {
			return String.valueOf(random.nextInt(1,(int)Math.pow(2,31)));
		}
		else if(type.contains("long")) {
			//return String.valueOf(random.nextLong((int)Math.pow(2, 64)));
			return op + String.valueOf(random.nextInt(1,(int)Math.pow(2,31)));
		}
		else if(type.contains("unsigned int")) {
			return String.valueOf(random.nextInt(1,(int)Math.pow(2, 15)));
		}
		else if(type.contains("int")) {
			//return String.valueOf(random.nextInt(-(int)Math.pow(2, 31),(int)Math.pow(2, 31)));
			return op + String.valueOf(random.nextInt(1,(int)Math.pow(2, 15)));
		}
		
		return value;
	}
	
	public void genMutation(List<String> varDeclare, List<String> conditionList, List<String> addedExpStmt, int randomAddLine, int funLeftBraceLine) {
		try {
			//System.out.println(dir);
			String filename = file.getName().trim();
			File newFile = new File(mutationDir + "/" + filename.substring(0, filename.indexOf(".c"))+ "_" + mutationCnt + ".c");
			
			mutationCnt++;
		
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
				if(lineCnt == funLeftBraceLine) {
					varDeclare.forEach( s ->{
						pw.println(s);
					});
				}
				if(lineCnt == randomAddLine) {
					addedExpStmt.forEach(s ->{
						pw.println(s);
					});
					conditionList.forEach(s ->{
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
