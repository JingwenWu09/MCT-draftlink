package AST_Information;

import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.*;
import Filter.CopyFile;
import expression_operation.Expression;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VarStmt_Gen {
	private static String rename_suffix = "_1";
	private static String rename_global_p = "_global_p";
	private static String rename_store = "_store";
	private int startLine;
	private Map<String, AstVariable> allVarsMap;
	private Set<String> globalVarsSet;
	private List<String> initialList;
	public List<EqualOperatorBlock> equalOpBlocks;
	public List<VarDeclareBlock> varDeclBlocks;
	public Map<String, VarDeclareBlock> varDeclBlockMap;
	public Map<String, StructUnionBlock> allStructUnionMap;
	public Map<String, FieldVar> allFieldVarsMap;
	//rename	private Map<String, Boolean> isRename = new HashMap <String, Boolean>();	//<id, boolean>
	private Set<String> renamedUseVarId;
	private String startSpace = "";
	private String globalVarAnnotation = " //globalVarAnnotation";
	//private String conditionBody = "goto error";
	//public List<Variable> allVars;
	public String filepath;
	
//	public static void main(String args[]) {
//		String filepath = "/home/jing/桌面/test.c";
//		AstInform_Gen astgen = new AstInform_Gen();
//		astgen.getAstInform(filepath);
//	}
	
	public VarStmt_Gen(AstInform_Gen astgen, List<String> initialList) {
		this.filepath = astgen.filepath;
		this.allVarsMap = astgen.allVarsMap;
		this.equalOpBlocks = astgen.equalOpBlocks;
		this.varDeclBlocks = astgen.varDeclBlocks;
		this.varDeclBlockMap = astgen.varDeclBlockMap;
		this.allStructUnionMap = astgen.allStructUnionMap;
		this.allFieldVarsMap = astgen.allFieldVarsMap;
		this.initialList = initialList;
		this.globalVarsSet = astgen.getGlobalVarsSet(allVarsMap, astgen.getAllFunctionBlocks());
	}
	
	public Map<Integer, List<String>> genGlobalVarsDeclareMap() throws ClassCastException{
		Map<Integer, List<String>> globalVarDeclareMap = new HashMap<Integer, List<String>>();
		
	try {
		for( String id : this.globalVarsSet) {
			AstVariable var = this.allVarsMap.get(id);
			String declareType;
			if(var.getKind().equals("common")) declareType = var.getType();
			else declareType = ((ArrayVar)var).getElementType();
//			int index;
//			if( (index = declareType.indexOf(" const ")) != -1) {
//				declareType = declareType.substring(0, index+1) + declareType.substring(index+7);
//			}else if( (index = declareType.indexOf(" const")) != -1) {
//				declareType = declareType.substring(0, index+1) + declareType.substring(index+6);
//			}else if( (index = declareType.indexOf("const ")) != -1 ){
//				declareType = declareType.substring(0, index) + declareType.substring(index+5);
//			}
//			declareType.replaceFirst("^\\s*", "");
			
//			if( declareType.contains("const ") || declareType.contains(" const") ) continue;
			if( declareType.matches(".*\\bconst\\b.*")) continue; //避免 *const情况
			if( declareType.equals("jmp_buf")) continue ;	//jmp_buf var
			if( declareType.equals("va_list")) continue ;	//va_list var

			VarDeclareBlock decl_block = varDeclBlockMap.get(var.getId());
			int declareLine = Math.max(decl_block.getStartLine(), decl_block.getEndLine());
			
			String varname = var.getName();
			List<String> declareStmtList = globalVarDeclareMap.getOrDefault(declareLine, new ArrayList<String>());
			if(var.getKind().equals("common")) {
				declareStmtList.add(declareType + " *" + varname + rename_global_p + " = &" + varname + ";" + globalVarAnnotation);
			}else {
				ArrayVar arr = (ArrayVar)var;
				//int *name[2][3] =》 int *name_global_p = &name[0][0];
				//截[2][3],变 [0][0] =》 &name[0][0]
				declareStmtList.add(declareType + " *" + varname + rename_global_p + " = " 
						+ "&" + varname + ArrayVar.getAddressZero(arr.getType()) + ";" + globalVarAnnotation);
			}
			
			globalVarDeclareMap.put(declareLine, declareStmtList);
		}
	}catch(ClassCastException e){
		String errorTestFolder = "/home/jing/桌面/errorTest";
		CopyFile.copyFile(filepath, errorTestFolder);
	}
		
		return globalVarDeclareMap;
	}
	
	public List<String> genStoreGlobalVarStmt() throws ClassCastException{
		List<String> storeGlobalVarStmt = new ArrayList<String>();
		storeGlobalVarStmt.add("//storeGlobalVarStmt");
	try {
		for( String id : this.globalVarsSet) {
			AstVariable var = this.allVarsMap.get(id);
			
			String declareType;
			if(var.getKind().equals("common")) declareType = var.getType();
			else declareType = ((ArrayVar)var).getElementType();
//			int index;
//			if( (index = declareType.indexOf(" const ")) != -1) {
//				declareType = declareType.substring(0, index+1) + declareType.substring(index+7);
//			}else if( (index = declareType.indexOf(" const")) != -1) {
//				declareType = declareType.substring(0, index+1) + declareType.substring(index+6);
//			}else if( (index = declareType.indexOf("const ")) != -1 ){
//				declareType = declareType.substring(0, index) + declareType.substring(index+5);
//			}
//			declareType.replaceFirst("^\\s*", "");
			
//			if( declareType.contains("const ") || declareType.contains(" const") ) continue;
			if( declareType.matches(".*\\bconst\\b.*")) continue; //避免 *const情况
			if( declareType.equals("jmp_buf")) continue ;	//jmp_buf var
			if( declareType.equals("va_list")) continue ;	//va_list var

			String varname = var.getName();
			if(var.getKind().equals("common")) {
				storeGlobalVarStmt.add(declareType + " " + varname + rename_store + " = *" + varname + rename_global_p +";");
			}else {
				ArrayVar arr = (ArrayVar)var;
				String store_name = varname + rename_store;
				String store_p = store_name + "_p";
				String global_p = varname + rename_global_p;
				
				String type = arr.getType();
				String addressZero = ArrayVar.getAddressZero(arr.getType());
				storeGlobalVarStmt.add(declareType + " " + type.substring(type.indexOf("[")).replaceFirst("\\[", store_name + "[") + ";");
				//截[2][3],变 [0][0]
				storeGlobalVarStmt.add(declareType + " *" + store_p + " = &" + store_name + addressZero + ";");
				storeGlobalVarStmt.add("for(int i = 0; i < " + ArrayVar.getEleCntBySizeof(store_name, arr.getType()) + "; i++) {");
//				storeGlobalVarStmt.add("\t" + store_p +  "[i] = " + global_p +"[i];");		//store_p[i] = global_p[i];
				storeGlobalVarStmt.add("\t*(" + store_p +  "+i) = *(" + global_p +"+i);");		//store_p[i] = global_p[i];
				storeGlobalVarStmt.add("}"); 
			}
		}
	}catch(ClassCastException e) {
		String errorTestFolder = "/home/jing/桌面/errorTest";
		CopyFile.copyFile(filepath, errorTestFolder);
	}
		return storeGlobalVarStmt;
		
	}
	
	public List<String> genRestoreGlobalVarStmt() {
		List<String> restoreGlobalVarStmt = new ArrayList<String>();
		restoreGlobalVarStmt.add("//restoreGlobalVarStmt");
	try {
		for( String id : this.globalVarsSet) {
			AstVariable var = this.allVarsMap.get(id);
			
			String declareType;
			if(var.getKind().equals("common")) declareType = var.getType();
			else declareType = ((ArrayVar)var).getElementType();
//			int index;
//			if( (index = declareType.indexOf(" const ")) != -1) {
//				declareType = declareType.substring(0, index+1) + declareType.substring(index+7);
//			}else if( (index = declareType.indexOf(" const")) != -1) {
//				declareType = declareType.substring(0, index+1) + declareType.substring(index+6);
//			}else if( (index = declareType.indexOf("const ")) != -1 ){
//				declareType = declareType.substring(0, index) + declareType.substring(index+5);
//			}
//			declareType.replaceFirst("^\\s*", "");
			
//			if( declareType.contains("const ") || declareType.contains(" const") ) continue;
			if( declareType.matches(".*\\bconst\\b.*")) continue; //避免 *const情况
			if( declareType.equals("jmp_buf")) continue ;	//jmp_buf var
			if( declareType.equals("va_list")) continue ;	//va_list var

			String varname = var.getName();
			String store_name = varname + rename_store;
			if(var.getKind().equals("common")) {
				restoreGlobalVarStmt.add("*" + varname + rename_global_p + " = " + store_name + ";");
			}else if(var.getKind().equals("array")) {
				ArrayVar arr = (ArrayVar)var;
				restoreGlobalVarStmt.add("for(int i = 0; i < " + ArrayVar.getEleCntBySizeof(store_name, arr.getType()) + "; i++) {");
//				restoreGlobalVarStmt.add("\t" + varname + rename_global_p +  "[i] = " + store_name + "_p[i];");		//store_p[i] = global_p[i];
				restoreGlobalVarStmt.add("\t*(" + varname + rename_global_p +  "+i) = *(" + store_name + "_p+i);");		//store_p[i] = global_p[i];
				restoreGlobalVarStmt.add("}"); 
			}
		}
	}catch(ClassCastException e) {
		String errorTestFolder = "/home/jing/桌面/errorTest";
		CopyFile.copyFile(filepath, errorTestFolder);
	}
		return restoreGlobalVarStmt;
	}
	
	public List<String> genUseVarRenameStmt(int startLine, List<AstVariable> usevarlist) {
		this.startLine = startLine;
		this.startSpace = LoopBlock.getSpace(initialList.get(startLine));
		//rename	this.isRename = new HashMap <String, Boolean>();
		this.renamedUseVarId = new HashSet<String>();
		
		List<String> renameUseVarStmt = new ArrayList<String>();
		renameUseVarStmt.add(startSpace + "//renameUseVarStmt");
		
		//rename
//		for(AstVariable var: usevarlist) {
//			System.out.println("var: " + var.getKind());
//			System.out.println(var.getType() + " " + var.getId() + " " + var.getName());
//			System.out.println(var.getIsStructUnion());
//		}
		try {
		for(AstVariable var: usevarlist) {
			//rename	if(isRename.get(var.getId()) == true) continue;
			if(renamedUseVarId.contains(var.getId()) || var.getIsGlobal() == true) continue ;
			if(var.getType().equals("jmp_buf")) continue ;
			if(var.getType().equals("va_list")) continue ;	//va_list var

//			System.out.println();
//			System.out.println("var:" + var.getName() + " " + var.getDeclareLine() + " " + var.getType() + " " + var.getKind());
//			System.out.println();
			if(var.getKind() == "common") {
				commonRename(var, renameUseVarStmt);
			}else if(var.getKind() == "array") {
				arrayRename((ArrayVar) var, renameUseVarStmt);
			}else {
				pointerRename((PointerVar) var, renameUseVarStmt,this.startLine);
			}
		}
		}catch (NullPointerException e) {
			
		}
		if(renamedUseVarId.size() == 0) {	//不存在renamedUseVar，随机生成一个基本类型的addVar
			renameUseVarStmt.add(startSpace + "//addVarStmt");
			String[] varTypes = {
					"signed char", "char", "unsigned char",
					"signed short int", "signed short", "short int", "short", "unsigned short int", "unsigned short",
					"signed int", "int", "unsigned int", 
					"signed long int", "signed long", "long int", "long", "unsigned long int", "unsigned long"};
			Random random = new Random();
			String addVarType = varTypes[random.nextInt(varTypes.length)];
			renameUseVarStmt.add(startSpace + addVarType + " " + "addVar = " + DataFlow.VariableOperation.genRandomValue(addVarType) + ";" );
			renameUseVarStmt.add(startSpace + addVarType + " " + "addVar" + rename_suffix + " = " + "addVar;");
		}
		
//		System.out.println("before: ");
//		for(String s: renameUseVarStmt) {
//			System.out.println(s);
//		}
//		System.out.println("after\n");
//		
		
		return renameUseVarStmt;
	}
	
	public List<String> genConditionList( ) {
		List<String> conditionList = new ArrayList<String>();
		conditionList.add(startSpace + "//conditionStmt");
		//rename if(isRename.size() == 0) {
		if(renamedUseVarId.size() == 0) {
			conditionList.add(startSpace + "assert(addVar == addVar" + rename_suffix +") ;");
		}
		
	try {
		//rename	for(String varid: isRename.keySet()) {
		for(String varid: renamedUseVarId) {
			AstVariable var = allVarsMap.get(varid);
			if(var.getType().equals("va_list")) continue ;
			String varname = var.getName();
			String kind = var.getKind();
			if(kind.equals("common")) {
				if(var.getIsStructUnion() == false) {
					if(var.getType().contains("float") || var.getType().contains("double")) {
						conditionList.add(startSpace + "assert( fabs(" + varname + " - " + varname + rename_suffix + ") < 0.000001 ) ;");
					}else {
						conditionList.add(startSpace + "assert(" + varname + " == " + varname + rename_suffix + ") ;");
					}
				}else {
					StructUnionBlock suBlock = this.searchStructUnionBlock(allStructUnionMap, var);
					for(FieldVar fieldVar: suBlock.getChildField()) {
						String fieldname = fieldVar.getName();
						if(fieldVar.getKind().equals("common")) {
							//非struct/union的commom变量
							if( !fieldVar.getIsStructUnion() ) {
								if(fieldVar.getType().contains("float") || fieldVar.getType().contains("double")) {
									conditionList.add(startSpace + "assert( fabs(" + varname + "." + fieldname + " - "
											+ varname + rename_suffix + "." + fieldname + ") < 0.000001 ) ;");
								}else {
									conditionList.add(startSpace + "assert(" + varname + "." + fieldname + " == "
											+ varname + rename_suffix + "." + fieldname + ") ;");
								}
							}
						}else if(fieldVar.getKind().equals("array")) {
							String eleKind = ArrayVar.getEleKindByType(fieldVar.getType());
							if(eleKind.equals("pointer")) continue ;
							
							//元素非struct/union的数组变量
							if( !fieldVar.getIsStructUnion() ) {
								int index;
								String eleType = ArrayVar.getEleTypeByType(fieldVar.getType());	//去掉const，因为不在声明时赋值
//								if( (index = eleType.indexOf(" const ")) != -1) {
//									eleType = eleType.substring(0, index+1) + eleType.substring(index+7);
//								}else if( (index = eleType.indexOf(" const")) != -1) {
//									eleType = eleType.substring(0, index+1) + eleType.substring(index+6);
//								}else if( (index = eleType.indexOf("const ")) != -1 ){
//									eleType = eleType.substring(0, index) + eleType.substring(index+5);
//								}
								eleType = eleType.replaceAll("\\bconst\\b", "").replaceAll("\\s+", " ").trim();
								
								String addressZero = ArrayVar.getAddressZero(fieldVar.getType());
								String filedArrEleCnt = ArrayVar.getEleCntBySizeof(varname + "." + fieldname, fieldVar.getType());
								String fieldVarPointerName = varname + "_" + fieldname + "_p";
								String fieldVarPointerName_1 = varname + rename_suffix +"_" + fieldname + "_p";
								conditionList.add(startSpace + eleType + " *" + fieldVarPointerName + " = &"
										+ varname + "." + fieldname + addressZero + ";");
								conditionList.add(startSpace + eleType + " *" + fieldVarPointerName_1 + " = &"
										+ varname + rename_suffix + "." + fieldname + addressZero + ";");
								conditionList.add(startSpace + "for( int i = 0; i < " + filedArrEleCnt + "; i++ ) {");
								if( fieldVar.getType().contains("float") || fieldVar.getType().contains("double")) {
									conditionList.add(startSpace + "\tassert( fabs(" + fieldVarPointerName + "[i] - " 
											+ fieldVarPointerName_1 + "[i]) < 0.000001 ) ;");
								}else {
									conditionList.add(startSpace + "\tassert(" + fieldVarPointerName + "[i] == " 
											+ fieldVarPointerName_1 + "[i]) ;");
								}
								conditionList.add(startSpace + "}");
							}
						}
					}
				}
			}else if(kind.equals("array")) {
				int index;
				ArrayVar arr = (ArrayVar)var;
				if(arr.getElementKind().equals("pointer")) continue ;
				String eleType = ArrayVar.getEleTypeByType(var.getType());	//去掉const，因为不在声明时赋值
//				if( (index = eleType.indexOf(" const ")) != -1) {
//					eleType = eleType.substring(0, index+1) + eleType.substring(index+7);
//				}else if( (index = eleType.indexOf(" const")) != -1) {
//					eleType = eleType.substring(0, index+1) + eleType.substring(index+6);
//				}else if( (index = eleType.indexOf("const ")) != -1 ){
//					eleType = eleType.substring(0, index) + eleType.substring(index+5);
//				}
				eleType = eleType.replaceAll("\\bconst\\b", "").replaceAll("\\s+", " ").trim();

				String rename = varname + rename_suffix;
				String varname_cmp = varname + "_cmp";	//name_p
				String rename_cmp = rename + "_cmp";	//name_1_p
				//截[2][3],变 [0][0]
				String addressZero = ArrayVar.getAddressZero(var.getType());
				String eleCnt = ArrayVar.getEleCntBySizeof(varname, var.getType());
				
				//int arr_len = arr.getLength();
				
				if(var.getIsStructUnion() == false) {
				//元素非struct、union的数组
					//int rename[2][3];
					//int [2][3] =》 int rename[2][3];
					//int *name_cmp = &name[0][0]; int *rename_cmp = &rename[0][0];
					conditionList.add(startSpace + eleType + " *" + varname_cmp + " = &" + varname + addressZero + ";");
					//int *rename_p = &rename[0][0];
					conditionList.add(startSpace + eleType + " *" + rename_cmp + " = &" + rename + addressZero + ";");
					//rename_p[i] = name_p[i];
					conditionList.add(startSpace + "for(int i = 0; i < " + eleCnt + "; i++) {");
					if(var.getType().contains("float") || var.getType().contains("double")) {
						conditionList.add(startSpace + "\tassert( fabs(" + varname_cmp + "[i] - " + rename_cmp  + "[i]) < 0.000001 ) ;");
					}else {
						conditionList.add(startSpace + "\tassert(" + varname_cmp + "[i] == " + rename_cmp + "[i]) ;");
					}		
					conditionList.add(startSpace + "}");
					
				}else {
				//元素存放的是struct/union
					//若数组存放的是指向struct、union的指针 ， 则用 -> 否则 用 .
//					ArrayVar arr = (ArrayVar)var;
//					if(arr.getElementKind().equals("pointer")) continue ;
					
					StructUnionBlock suBlock = this.searchStructUnionBlock(allStructUnionMap, var);	
					conditionList.add(startSpace + eleType + " *" + varname_cmp + " = &" + varname + addressZero + ";");
					//int *rename_p = &rename[0][0];
					conditionList.add(startSpace + eleType + " *" + rename_cmp + " = &" + rename + addressZero + ";");
					
					String outmostForHead = "for( int i = 0; i < " + eleCnt + "; i++ )";
					for(FieldVar fieldVar: suBlock.getChildField()) {
						String fieldname = fieldVar.getName();
						if(fieldVar.getKind().equals("common")) {
							//非struct/union的commom变量
							if( !fieldVar.getIsStructUnion() ) {
								conditionList.add(startSpace +  outmostForHead + " {");
								if(fieldVar.getType().contains("float") || fieldVar.getType().contains("double")) {
									conditionList.add(startSpace + "\tassert( fabs(" + varname_cmp + "[i]." + fieldname + " - "
											+ rename_cmp + "[i]." + fieldname + ") < 0.000001 ) ;");
								}else {
									conditionList.add(startSpace + "\tassert(" + varname_cmp + "[i]."+ fieldname + " == "
											+ rename_cmp + "[i]."+ fieldname + ") ;");
								}
								conditionList.add(startSpace + "}");
							}
						}else if(fieldVar.getKind().equals("array")) {
							String eleKind = ArrayVar.getEleKindByType(fieldVar.getType());
							if(eleKind.equals("pointer")) continue ;
							
							//元素非struct/union的数组变量
							if( !fieldVar.getIsStructUnion() ) {
								int index_filed;
								String filedEleType = ArrayVar.getEleTypeByType(fieldVar.getType());	//去掉const，因为不在声明时赋值
//								if( (index_filed = filedEleType.indexOf(" const ")) != -1) {
//									filedEleType = filedEleType.substring(0, index_filed+1) + filedEleType.substring(index_filed+7);
//								}else if( (index_filed = filedEleType.indexOf(" const")) != -1) {
//									filedEleType = filedEleType.substring(0, index_filed+1) + filedEleType.substring(index_filed+6);
//								}else if( (index_filed = filedEleType.indexOf("const ")) != -1 ){
//									filedEleType = filedEleType.substring(0, index_filed) + filedEleType.substring(index_filed+5);
//								}
								filedEleType = filedEleType.replaceAll("\\bconst\\b", "").replaceAll("\\s+", " ").trim();
								
								String addressFieldZero = ArrayVar.getAddressZero(fieldVar.getType());
								String filedArrEleCnt = ArrayVar.getEleCntBySizeof(varname_cmp + "[i]." + fieldname, fieldVar.getType());
								String fieldVarPointerName = varname + "_" + fieldname + "_p";
								String fieldVarPointerName_1 = varname + rename_suffix +"_" + fieldname + "_p";
								
								conditionList.add(startSpace + outmostForHead + " {");
								conditionList.add(startSpace + "\t" + filedEleType + " *" + fieldVarPointerName + " = &"
										+ varname_cmp + "[i]." + fieldname + " " + addressFieldZero + ";");
								conditionList.add(startSpace + "\t" + filedEleType + " *" + fieldVarPointerName_1 + " = &"
										+ rename_cmp + "[i]." + fieldname + addressFieldZero + ";");
								conditionList.add(startSpace + "\tfor( int j = 0; j < " + filedArrEleCnt + "; j++ ) {");
								if(fieldVar.getType().contains("float") || fieldVar.getType().contains("double")) {
									conditionList.add(startSpace + "\t\tassert( fabs(" + fieldVarPointerName + "[j] - " 
											+ fieldVarPointerName_1 + "[j]) < 0.000001 ) ;");
								}else {
									conditionList.add(startSpace + "\t\tassert(" + fieldVarPointerName + "[j] == " + fieldVarPointerName_1 + "[j]) ;");
								}
								conditionList.add(startSpace + "\t}");
								conditionList.add(startSpace + "}");
							}
						}
					}
				}
			}
		}
		
		}catch(NullPointerException e) {
			String errorTestFolder = "/home/jing/桌面/errorTest";
			CopyFile.copyFile(filepath, errorTestFolder);
		}
//		
		return conditionList;
	}
	
	public List<String> getUseVarOldName(){
		List<String> useVarOldName = new ArrayList<String>();
		for(String id: this.renamedUseVarId){
			useVarOldName.add(allVarsMap.get(id).getName());
		}
		return useVarOldName;
	}
	
	public StructUnionBlock searchStructUnionBlock(Map<String, StructUnionBlock> suBlockMap, AstVariable var) {
		StructUnionBlock findBlock = null;
		String vartype = var.getType().replaceAll("\\[.*]", "").trim();
		String vartype2 = var.getType2().replaceAll("\\[.*]", "").trim();
		for(StructUnionBlock suBlock: suBlockMap.values()) {
			String su_name = suBlock.getName();
			if(vartype.matches(".*[^_0-9a-zA-Z]*\\b" + su_name + "[^_0-9a-zA-Z]*.*")
					|| vartype2.matches(".*[^_0-9a-zA-Z]*\\b" + su_name + "[^_0-9a-zA-Z]*.*")) {
				if(var.getDeclareLine() < suBlock.getStartLine()) continue ;
				else{
					if(findBlock != null) {
						int distanceFind = var.getDeclareLine() - findBlock.getStartLine();
						int distanceSuBlock = var.getDeclareLine() - suBlock.getStartLine();
						if(distanceSuBlock < distanceFind) {
							findBlock = suBlock;
						}
					}else findBlock = suBlock;
				}
			}
		}
		return findBlock;
	}

	private void commonRename(AstVariable var, List<String> renameUseVarStmt) {
		//rename	if(isRename.containsKey(var.getId()) && isRename.get(var.getId()) == true) return ;
		if(renamedUseVarId.contains(var.getId()) || var.getIsGlobal() == true) return ;
		if(var.getType().equals("jmp_buf")) return ;
		if(var.getType().equals("va_list")) return ;	//va_list var

		String str = "";
		String rename = var.getName() + rename_suffix;
		String declare = var.getType() + " " + rename;
		str = declare + " = " + var.getName() + ";";
		
		//rename	isRename.put(var.getId(), true);
		renamedUseVarId.add(var.getId());
//		System.out.println("\ncommonRename:" + startSpace + str + "///\n");
		renameUseVarStmt.add(startSpace + str);
		return ;
	}
	
	private void arrayRename(ArrayVar arr, List<String> renameUseVarStmt) {
		//rename	if(isRename.containsKey(arr.getId()) && isRename.get(arr.getId()) == true) return ;
		if(renamedUseVarId.contains(arr.getId()) || arr.getIsGlobal() == true) return ;
		int index;
		String eleType = arr.getElementType();	//去掉const，因为不在声明时赋值
//		if( (index = eleType.indexOf(" const ")) != -1) {
//			eleType = eleType.substring(0, index+1) + eleType.substring(index+7);
//		}else if( (index = eleType.indexOf(" const")) != -1) {
//			eleType = eleType.substring(0, index+1) + eleType.substring(index+6);
//		}else if( (index = eleType.indexOf("const ")) != -1 ){
//			eleType = eleType.substring(0, index) + eleType.substring(index+5);
//		}
		eleType = eleType.replaceAll("\\bconst\\b", "").replaceAll("\\s+", " ").trim();
		
		String varname = arr.getName();
		String rename = varname + rename_suffix;
		String varname_p = varname + "_p";	//name_p
		String rename_p = rename + "_p";	//name_1_p
		//截[2][3],变 [0][0]
		String addressZero = ArrayVar.getAddressZero(arr.getType());
		//int arr_len = arr.getLength();
		
		//int rename[2][3];
		//int [2][3] =》 int rename[2][3];
		renameUseVarStmt.add(startSpace + arr.getType().replaceFirst("\\[", " " + rename + "[") + ";");
		//int *name_p = &name[0][0];
		renameUseVarStmt.add(startSpace + eleType + " *" + varname_p + " = &" + varname + addressZero + ";");
		//int *rename_p = &rename[0][0];
		renameUseVarStmt.add(startSpace + eleType + " *" + rename_p + " = &" + rename + addressZero + ";");
		//rename_p[i] = name_p[i];
		renameUseVarStmt.add(startSpace + "for(int i = 0; i < " + arr.getEleCnt() + "; i++) {");
//		renameUseVarStmt.add(startSpace + "\t" + rename_p +  "[i] = " + varname_p +"[i];");	
		renameUseVarStmt.add(startSpace + "\t*(" + rename_p +  "+i) = *(" + varname_p +"+i);");		

		renameUseVarStmt.add(startSpace + "}");
		
		//rename	isRename.put(arr.getId(), true);
		renamedUseVarId.add(arr.getId());
	}
	
	private void pointerRename(PointerVar pvar, List<String> renameUseVarStmt, int findEndLine) {
		//rename	if(isRename.containsKey(pvar.getId()) && isRename.get(pvar.getId()) == true) return;
		if(renamedUseVarId.contains(pvar.getId()) || pvar.getIsGlobal() == true) return ;
	
	try {
		EqualOperatorBlock eblock = this.nearestPointEblock(pvar, findEndLine);
		String pointtoCase;
		//仅声明，未附初值
		if(eblock == null) { pointtoCase = null; }
		else {
			if(eblock.getRightVar().size() == 0 ) {
				//形如char *p = "abc"; 
				//形如char *p; p = "abc";
				pointtoCase = String.valueOf(eblock.getStartLine());
				//形如int *p; p = (int *) malloc(sizeof(int));
			}else {
				if(eblock.getHasFunction() == false) pointtoCase = eblock.getRightVar().get(0);
				else{	//有函数，有变量，但是变量是func的参数，形如int *p = (int*) malloc(); 形如 int *p = function(parm a, parm b);
					pointtoCase = String.valueOf(eblock.getStartLine());
				}
			}
		}
		String pvar_name = pvar.getName();
		String pvar_rename = pvar_name + rename_suffix;
		String pvartype = pvar.getType();
//		System.out.println("renamePvar: " + pvar.getName() + " " + pvar.getId() + " pointToKind" + pointToKind);
		if(pointtoCase != null) {
			
			String line = initialList.get(eblock.getStartLine()).trim();
//			System.out.println("lineCnt:" + eblock.getStartLine());
//			System.out.println("line:" + line);
//			System.out.println("pvar:" + pvar_name);
			//System.out.println(line);
			String regexPVarName = "(\\W|^)" + pvar_name + "(\\W|$)";
			Pattern pPvarName = Pattern.compile(regexPVarName);
			Matcher mPVarName = pPvarName.matcher(line);
			int pvar_name_index = 0;
			while(mPVarName.find()) {
				int start = mPVarName.start();
				if(start > 0 && line.charAt(start) == '.') continue ;	//结构体Struct S A; A.a中的点；
				else if(line.substring(0,start+1).matches(".*->\\s*")) continue;	//结构体指针Sttuct S *A; A->a中的a
				else {
					pvar_name_index = start == 0 ? start : start+1;
					break ;
				}
			}
//			String varRightPart = line.substring(line.indexOf(pvar_name));	//截取从pvar_name开始的line右边部分
			String varRightPart = line.substring(pvar_name_index);	//截取从pvar_name开始的line右边部分

			varRightPart = varRightPart.substring(varRightPart.indexOf("=") + 1);	//截取从pvar_name等号之后的右边部分
//			System.out.println("eblock:" + eblock.getStartLine());
			if(pointtoCase.startsWith("0x")) {
			//指向变量
				//varRightPart:
				//e.g1. int *p = (int *)&a, b, c; => varRightPart: (int &)&a, b, c;
				//e.g2. int *p, int *q; if( (p = (int *)&a) == q ) ; => varRightPart: (int *)&a) == q );
				AstVariable var = allVarsMap.get(pointtoCase);
				String var_name = var.getName();
				String var_rename = var_name + rename_suffix;
				String varkind = var.getKind();
				if(varkind.equals("common")) commonRename(var, renameUseVarStmt);
				else if(varkind.equals("array")) arrayRename((ArrayVar) var, renameUseVarStmt);
				else if(varkind.equals("pointer")) pointerRename((PointerVar)var, renameUseVarStmt, eblock.getStartLine());
				
				if(varkind.equals("array")) {	//
					int index_const;
//					if( (index_const = pvartype.indexOf(" const ")) != -1) {
//						pvartype = pvartype.substring(0, index_const+1) + pvartype.substring(index_const+7);
//					}else if( (index_const = pvartype.indexOf(" const")) != -1) {
//						pvartype = pvartype.substring(0, index_const+1) + pvartype.substring(index_const+6);
//					}else if( (index_const = pvartype.indexOf("const ")) != -1 ){
//						pvartype = pvartype.substring(0, index_const) + pvartype.substring(index_const+5);
//					}
					pvartype = pvartype.replaceAll("\\bconst\\b", "").replaceAll("\\s+", " ").trim();

					int index_volatile;
					String typeCasting = "";
//					if( (index_volatile = pvartype.indexOf(" volatile ")) != -1) {
//						typeCasting = pvartype.substring(0, index_volatile+1) + pvartype.substring(index_volatile+10);
//					}else if( (index_volatile = pvartype.indexOf(" volatile")) != -1) {
//						typeCasting = pvartype.substring(0, index_volatile+1) + pvartype.substring(index_volatile+9);
//					}else if( (index_volatile = pvartype.indexOf("volatile ")) != -1 ){
//						typeCasting = pvartype.substring(0, index_volatile) + pvartype.substring(index_volatile+8);
//					}else typeCasting = pvartype;
					typeCasting = pvartype.replaceAll("\\bvolatile\\b", "").replaceAll("\\s+", " ").trim();
					
//					System.out.println("pvar: " + pvar.getType() + " " + pvar.getName());
//					System.out.println("arr: " + var.getType() + " " + var.getName());
//					System.out.println("typeCasting: " + typeCasting);
					ArrayVar arr = (ArrayVar)var;
					String arrEleType = arr.getElementType().trim();
					String cmpType = arrEleType.replaceAll("\\bconst\\b", "").replaceAll("\\bvolatile\\b", "").replaceAll("\\s+", " ").trim();
					if( typeCasting.equals(cmpType)) {
						typeCasting = "";
					}else { 
						typeCasting = "(" + typeCasting + ")";
					}
					
//					System.out.println("\npvar: " + pvar.getType() + " " +  pvar.getName());
//					System.out.println("var:" + var.getType() + " " + var.getName());
//					System.out.println("after:Casting: " + typeCasting);
//					System.out.println();
					
					String pointtokind = pvar.getPointToKind();
					if(pointtokind.equals("common")) {
						//case1:array存放的是common；case:array存放的是指针
						String oldtype = var.getType();
						//截[2][3],变 [0][0]
						String prefix = "&";	//case1: int a[2][3]; int *p = &a[0][0];
						if(ArrayVar.getEleKindByType(var.getType()).equals("pointer")) {
							//case2: int *a[2][3]; int *p = a[0][0];
							//case2: int **a[1][2]; int *p = *a[0][0];
							prefix = "";
							for( int i = var.getType().indexOf('*')+1; i < var.getType().lastIndexOf('*'); i++) {
								if(var.getType().charAt(i) == '*') prefix += "*";
							}
						}
						String address = ArrayVar.getAddressZero(oldtype);
						String oldaddress = prefix + var.getName() + address;
						String newaddress = prefix + var_rename + address;
						
						if(renamedUseVarId.contains(pvar.getId())) pvartype = ""; /*add*/
						
						if(renamedUseVarId.contains(var.getId()) == false) {
							renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+oldaddress+" + ("+ pvar_name +" - "+ typeCasting + oldaddress +");");
						}else {
							renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+newaddress+" + ("+ pvar_name +" - "+ typeCasting + oldaddress +");");
						}
					}else if(pointtokind.equals("array")) {
						//指向数组
						//int a[2][2][3] = {1,2,3,4,5,6,7,8,9,10,11,12};
						//int (*q)[2][3] = &a[1];
						//int (*q_1)[2][3] = &a_1[0] + (q-&a[0]);
						String vartype = var.getType();
						String address, var_address, pvar_address, revar_address;
						//截[2][2][3],变[0][0][0]；截[2][3]，变[0][0]
						var_address = ArrayVar.getAddressZero(vartype);
						pvar_address = ArrayVar.getAddressZero(pvartype);
						address = var_address.substring(pvar_address.length());	//[0]
						var_address = "&" + var.getName() + address;	//&a[0];
						revar_address = "&" + var_rename + address;	//&a1[0];.
						int split_index = pvartype.indexOf('*', pvartype.indexOf('('))+1;
						//pvartype = int (*)[2][3]
						//left = int (* + q_1 + )[2][3]
						String left = pvartype.substring(0, split_index) + pvar_rename + pvartype.substring(split_index);
						
						if(renamedUseVarId.contains(pvar.getId())) left = pvar_rename; /*add*/

						//rename	renameUseVarStmt.add(startSpace + left +" = "+revar_address+" + ("+ pvar.getName()+" - "+ var_address +");");
						if(renamedUseVarId.contains(var.getId()) == false) {
//								System.out.println("pointerRename:" + startSpace + left +" = "+ var_address+" + ("+ pvar_name +" - "+ var_address +");" + "///");
							renameUseVarStmt.add(startSpace + left +" = "+ var_address+" + ("+ pvar_name +" - "+ var_address +");");
						}else {
//								System.out.println("pointerRename:" + startSpace + left +" = "+ revar_address+" + ("+ pvar_name +" - "+ var_address +");" + "///");
							renameUseVarStmt.add(startSpace + left +" = "+revar_address+" + ("+ pvar_name+" - "+ var_address +");");
						}
					}else if(pointtokind.equals("pointer")) {
						//指向数组
						//int* b[2];
						//int **p = b;
						//int **p_1 = &b_1[0] + (p-&b[0]);
						String oldtype = var.getType();
						//截[2][3],变 [0][0]
						String address = ArrayVar.getAddressZero(oldtype);
						String oldaddress = "&" + var.getName() + address;
						String newaddress = "&" + var_rename + address;
						//rename	renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+newaddress+" + ("+ pvar.getName()+" - "+ oldaddress +");");
						
						if(renamedUseVarId.contains(pvar.getId())) pvartype = ""; /*add*/
						
						if(renamedUseVarId.contains(var.getId()) == false) {
//							System.out.println("pointerRename:" + startSpace + pvartype+" "+pvar_rename+" = "+oldaddress+" + ("+ pvar.getName()+" - "+ oldaddress +");///");
							renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+oldaddress+" + ("+ pvar_name +" - "+ typeCasting + oldaddress +");");
						}else {
//							System.out.println("pointerRename:" + startSpace + pvartype+" "+pvar_rename+" = "+newaddress+" + ("+ pvar.getName()+" - "+ oldaddress +");" + "///");
							renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+newaddress+" + ("+ pvar_name +" - "+ typeCasting + oldaddress +");");
						}
					}
				}else {
					//指向变量，且等号右边不是数组变量
					//考虑var_name前面有括号的情况 e.g.1 int *p = &(a); e.g.2 int *p = (int *)a;
					//before:varRightPart是=后的一个位置 e.g.1 &(a); e.g.2 (int *)a;
					
					String regexVarName = "(\\W|^)" + var_name + "(\\W|$)";
					Pattern pVarname = Pattern.compile(regexVarName);
					Matcher mVarName = pVarname.matcher(varRightPart);
					int var_name_index = 0;
					while(mVarName.find()) {
						int start_varname = mVarName.start();
						if(start_varname > 0 && varRightPart.charAt(start_varname) == '.') continue ;	//结构体Struct S A; A.a中的点；
						else if(varRightPart.substring(0,start_varname+1).matches(".*->\\s*")) continue;	//结构体指针Sttuct S *A; A->a中的a
						else {
							var_name_index = start_varname == 0 ? start_varname : start_varname+1;
							break ;
						}
					}
					
					int leftBraceCnt = 0;
					int rightBraceCnt = 0;
					int start = 0;
					for( ; start < var_name_index; start++) {
						if(varRightPart.charAt(start) == '(') leftBraceCnt++;
						if(varRightPart.charAt(start) == ')') rightBraceCnt++;
					}
					if(leftBraceCnt != rightBraceCnt) {
						for(; start < varRightPart.length(); start++) {
							if(varRightPart.charAt(start) == '(') leftBraceCnt++;
							else if(varRightPart.charAt(start) == ')') rightBraceCnt++;
							if(leftBraceCnt == rightBraceCnt) {
								start++;
								break ;
							}
						}
					}
					
					int end = start;
					int index_comma = varRightPart.indexOf(",", end);
					int index_ban = varRightPart.indexOf(";", end);
					int index_brace = varRightPart.indexOf(")",end)+1;	/*tip:括号需要*/
					if(index_comma == -1) index_comma = varRightPart.length();
					if(index_ban == -1) index_ban = varRightPart.length();
					if(index_brace == 0) index_brace = varRightPart.length();
					end = Math.min(index_comma, Math.min(index_ban, index_brace));
					varRightPart = varRightPart.substring(0, end);
					if(renamedUseVarId.contains(var.getId())) {
						varRightPart = Expression.ExpReplace(varRightPart, var_name, var_rename);
					}
					
					String left = "";
					if(pvartype.contains("[")) {
						int split_index = pvartype.lastIndexOf('*', pvartype.indexOf(')'))+1;
						//pvartype: int (**)[2][3]
						//left: int (** + q_1 + )[2][3] => int (**q_1)[2][3]
						left = pvartype.substring(0, split_index).trim() + " "+ pvar_rename + pvartype.substring(split_index);
					}else left = pvartype.trim() + " " + pvar_rename;

					if(renamedUseVarId.contains(pvar.getId())) left = pvar_rename; /*add*/
					
					renameUseVarStmt.add(startSpace + left + " = " + varRightPart +";");
				}
			}else {
			//形如char *p = "abc";
			//形如int *p = (int *) malloc(sizefo()), a, b;  // int *q; if( (q = (int *)calloc(,)) == p) ;
				//varRightPart:
				//e.g. (int *)malloc(sizeof()), a, b;
				//e.g. (int *)calloc(,)) == p );
				int end = 0;
				
				if(eblock.getHasFunction()) {
					String functionName = eblock.getFunctionName().get(0);
					//从varpart的functionNamec的(开始
					
					String regexFunctionName = "(\\W|^)" + functionName + "(\\W|$)";
					Pattern pFunctionName = Pattern.compile(regexFunctionName);
					Matcher mFunctionName = pFunctionName.matcher(varRightPart);
					int var_fun_index = 0;
					while(mFunctionName.find()) {
						int start_fun_name = mFunctionName.start();
						if(start_fun_name > 0 && varRightPart.charAt(start_fun_name) == '.') continue ;	//结构体Struct S A; A.a中的点；
						else if(varRightPart.substring(0,start_fun_name+1).matches(".*->\\s*")) continue;	//结构体指针Sttuct S *A; A->a中的a
						else {
							var_fun_index = start_fun_name == 0 ? start_fun_name : start_fun_name+1;
							break ;
						}
					}
					end = varRightPart.indexOf("(", var_fun_index);
					int leftbraceCnt = 0, rightbraceCnt = 0;
					for(; end < varRightPart.length(); end++) {
						char ch = varRightPart.charAt(end);
						if(ch == '(') leftbraceCnt++;
						else if(ch == ')') rightbraceCnt++;
						if(leftbraceCnt == rightbraceCnt) break;
					}//此时end是function结束位置的索引
					end++;	//end是function结束右括号后面一个位置
					//varRightPart.substring(end): (int *)malloc(sizeof()) // (int *)calloc(,)
				}else {
				//形如 int *p = (void *)0; int *p = abc(jioi)lll,io;
					int leftBarceCnt = 0;
					int rightBraceCnt = 0;
					int index = 0;
					int leftbrace = varRightPart.indexOf("(");
					if(leftbrace != -1) {
						int comma = varRightPart.indexOf(",");
						int ban = varRightPart.indexOf(";");
						if(comma == -1) comma = varRightPart.length();
						if(ban == -1) ban = varRightPart.length();
						if(leftbrace < Math.min(comma, ban)) {
							index = leftbrace;
							for( ; index < varRightPart.length(); index++) {
								char ch = varRightPart.charAt(index);
								if(ch == '(') leftBarceCnt++;
								else if(ch == ')') rightBraceCnt++;
								if(leftBarceCnt == rightBraceCnt) break;
							}
							end = index+1; //从0开始;从l开始
						}
					}
				}
				
				//renameRightPart;
				int index_comma = varRightPart.indexOf(",", end);
				int index_ban = varRightPart.indexOf(";", end);
				int index_brace = varRightPart.indexOf(")",end)+1;
				if(index_comma == -1) index_comma = varRightPart.length();
				if(index_ban == -1) index_ban = varRightPart.length();
				if(index_brace == 0) index_brace = varRightPart.length();
				end = Math.min(index_comma, Math.min(index_ban, index_brace));
				varRightPart = varRightPart.substring(0, end);
				
				String left = "";
				if(pvartype.contains("[")) {
					int split_index = pvartype.lastIndexOf('*', pvartype.indexOf(')'))+1;
					//pvartype: int (**)[2][3]
					//left: int (** + q_1 + )[2][3] => int (**q_1)[2][3]
					left = pvartype.substring(0, split_index).trim() + " " + pvar_rename + pvartype.substring(split_index);
				}else left = pvartype + " " + pvar_rename;
				
				if(renamedUseVarId.contains(pvar.getId())) left = pvar_rename; /*add*/
				
				renameUseVarStmt.add(startSpace + left + " = " + varRightPart +";");
			}
		}else {
		//仅声明，未赋值
//			System.out.println(pvar.getName() + " " + pvar.getType());
			if(pvartype.contains("[")) {
				int split_index = pvartype.lastIndexOf('*', pvartype.indexOf(')'))+1;
				//pvartype: int (**)[2][3]
				//left: int (** + q_1 + )[2][3] => int (**q_1)[2][3]
//				System.out.println(startSpace + pvartype.substring(0, split_index).trim() + " " + pvar_rename + pvartype.substring(split_index) + ";");
				renameUseVarStmt.add(startSpace + pvartype.substring(0, split_index).trim() + " " + pvar_rename + pvartype.substring(split_index) + ";");
			}else {
				renameUseVarStmt.add(startSpace + pvartype.trim() + " " + pvar_rename +";");
			}
		}
		renamedUseVarId.add(pvar.getId());
	}catch(StringIndexOutOfBoundsException e) {
		String errorTestFolder = "/home/jing/桌面/errorTest";
		CopyFile.copyFile(filepath, errorTestFolder);
	}
	}
	
	private EqualOperatorBlock nearestPointEblock(PointerVar pvar, int startLine) {
		int existline = 0;
		ArrayList<Integer> varexistline = new ArrayList<Integer>();
		varexistline.add(pvar.getDeclareLine());
		varexistline.addAll(pvar.useLine);
//		System.out.println("find nearestPointVar pvar: " + pvar.getName() + " " + pvar.getId());
		for(int i = varexistline.size()-1; i >= 0; i--) {
			existline = varexistline.get(i);
			if(existline >= startLine) continue;
			else {
				for(int j = equalOpBlocks.size()-1; j >= 0; j--) {
					EqualOperatorBlock eblock = equalOpBlocks.get(j);
					if(eblock.getStartLine() == existline) {
						if(eblock.getLeftVar() != null
								&& eblock.getLeftVar().equals(pvar.getId())
								&& eblock.getCalculateType().equals(pvar.getType()) ) {
							//eblock的leftVar是pvar，且eblock计算结果的类型和pvar的类型一致，说明是pvar指向的语句
							return eblock;
						}
					}
				}
			}
		}
		return null;	//没有指向任何变量，仅声明，没有附初值，形如int*p;
	}
	
}

