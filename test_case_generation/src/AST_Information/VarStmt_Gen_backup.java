package AST_Information;

import AST_Information.Inform_Gen.AstInform_Gen;
import AST_Information.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class VarStmt_Gen_backup {
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
	
	
	public static void main(String args[]) {
		String filepath = "/home/jing/桌面/test.c";
		
//		AstInform_Gen astgen = new AstInform_Gen();
//		astgen.getAstInform(filepath);
		
		VarStmt_Gen_backup.TestVarStmtGen(filepath);
		
	}
	
	public static void TestVarStmtGen(String filepath) {
		
		File file = new File(filepath);
		List<String> initialList = FileFormat.genInitialList(file);
		
		AstInform_Gen astgen = new AstInform_Gen();
		astgen.getAstInform(filepath);
		VarStmt_Gen_backup varstmt = new VarStmt_Gen_backup(astgen, initialList);
		
//		//test VarDeclBlcoks
//		varstmt.varDeclBlocks = astgen.varDeclBlocks;
//		for(VarDeclareBlock block: varstmt.varDeclBlocks) {
//			AstVariable var = astgen.allVarsMap.get(block.getLeftVar());
//			System.out.println(block.getStartLine() + " " + block.getEndLine());
//			System.out.println(block.getAstStartLine() + " " + block.getAstEndLine());
//			System.out.println("leftVar: " + var.getName() + " " + var.getDeclareLine());
//			System.out.println("rightVar:");
//			List<String> rightVar = block.getRightVar();
//			for(String id: rightVar) {
//				AstVariable right = astgen.allVarsMap.get(id);
//				System.out.println(right.getName() + " " + right.getDeclareLine());
//			}
//			System.out.println();
//		}
		
		Map<Integer, List<String>> globalVarsDeclareMap = varstmt.genGlobalVarsDeclareMap();
		System.out.println("DeclareLine: " + globalVarsDeclareMap.keySet());
		System.out.println("Declare:");
		for( List<String> list: globalVarsDeclareMap.values()) {
			for(String s: list) {
				System.out.println(s);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("StoreStmt:");
		for( String s: varstmt.genStoreGlobalVarStmt()) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println("RestoreStmt:");
		for(String s: varstmt.genRestoreGlobalVarStmt()) {
			System.out.println(s);
		}
		System.out.println();
		
		//useVarRename
		List<AstVariable> var_list = new ArrayList<AstVariable>();
		for(AstVariable var: astgen.allVarsMap.values()) {
			if(var.getDeclareLine() != 20)
				var_list.add(var);
		}
		
		System.out.println();
		System.out.println("UseVarRenameStmt:");
		for(String s: varstmt.genUseVarRenameStmt(42, var_list)) {
			System.out.println(s);
			System.out.println();
		}
		System.out.println();
		System.out.println("ConditionList");
		for(String s: varstmt.genConditionList()) {
			System.out.println(s);
			System.out.println();
		}
		
		
//		LoopInform_Gen loopgen = new LoopInform_Gen(astgen);
//		List<LoopStatement> outmostLoopList = loopgen.outmostLoopList;
//		
//		for(LoopStatement stmt: outmostLoopList) {
//			Test(stmt, astgen, initialList);
//		}
	}
	
	public VarStmt_Gen_backup(AstInform_Gen astgen, List<String> initialList) {
		this.allVarsMap = astgen.allVarsMap;
		this.equalOpBlocks = astgen.equalOpBlocks;
		this.varDeclBlocks = astgen.varDeclBlocks;
		this.varDeclBlockMap = astgen.varDeclBlockMap;
		this.allStructUnionMap = astgen.allStructUnionMap;
		this.allFieldVarsMap = astgen.allFieldVarsMap;
		this.initialList = initialList;
		this.globalVarsSet = astgen.getGlobalVarsSet(allVarsMap, astgen.getAllFunctionBlocks());
	}
	
	public Map<Integer, List<String>> genGlobalVarsDeclareMap(){
		Map<Integer, List<String>> globalVarDeclareMap = new HashMap<Integer, List<String>>();
		
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
			
			if( declareType.contains("const ") || declareType.contains(" const") ) continue;
			
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
		
		return globalVarDeclareMap;
	}
	
	public List<String> genStoreGlobalVarStmt() {
		List<String> storeGlobalVarStmt = new ArrayList<String>();
		storeGlobalVarStmt.add("//storeGlobalVarStmt");
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
			
			if( declareType.contains("const ") || declareType.contains(" const") ) continue;
			
			String varname = var.getName();
			if(var.getKind().equals("common")) {
				storeGlobalVarStmt.add(declareType + " " + varname + rename_store + " = *" + varname + rename_global_p +";");
			}else {
				ArrayVar arr = (ArrayVar)var;
				String store_name = varname + rename_store;
				String store_p = store_name + "_p";
				String global_p = varname + rename_global_p;
				
				String type = arr.getType();
				storeGlobalVarStmt.add(declareType + " " + type.substring(type.indexOf("[")).replaceFirst("\\[", store_name + "[") + ";");
				//截[2][3],变 [0][0]
				storeGlobalVarStmt.add(declareType + " *" + store_p + " = &" + store_name + ArrayVar.getAddressZero(arr.getType()) + ";");
				storeGlobalVarStmt.add("for(int i = 0; i < " + arr.getEleCnt() + "; i++) {");
				storeGlobalVarStmt.add("\t" + store_p +  "[i] = " + global_p +"[i];");		//store_p[i] = global_p[i];
				storeGlobalVarStmt.add("}"); 
			}
		}
		return storeGlobalVarStmt;
		
	}
	
	public List<String> genRestoreGlobalVarStmt() {
		List<String> restoreGlobalVarStmt = new ArrayList<String>();
		restoreGlobalVarStmt.add("//restoreGlobalVarStmt");
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
			
			if( declareType.contains("const ") || declareType.contains(" const") ) continue;
			
			String varname = var.getName();
			if(var.getKind().equals("common")) {
				restoreGlobalVarStmt.add("*" + varname + rename_global_p + " = " + varname + rename_store + ";");
			}else if(var.getKind().equals("array")) {
				ArrayVar arr = (ArrayVar)var;
				restoreGlobalVarStmt.add("for(int i = 0; i < " + arr.getEleCnt() + "; i++) {");
				restoreGlobalVarStmt.add("\t" + varname + rename_global_p +  "[i] = " + varname + rename_store + "_p[i];");		//store_p[i] = global_p[i];
				restoreGlobalVarStmt.add("}"); 
			}
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
//			//System.out.println(var.getKind() + " " + var.getId() + " " + var.getName());
//			isRename.put(var.getId(), false);
//		}
		
		for(AstVariable var: usevarlist) {
			//rename	if(isRename.get(var.getId()) == true) continue;
			if(renamedUseVarId.contains(var.getId()) || var.getIsGlobal() == true) continue ;
			if(var.getKind() == "common") {
				commonRename(var, renameUseVarStmt);
			}else if(var.getKind() == "array") {
				arrayRename((ArrayVar) var, renameUseVarStmt);
			}else {
				pointerRename((PointerVar) var, renameUseVarStmt);
			}
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
		
		return renameUseVarStmt;
	}
	
	public List<String> genConditionList( ) {
		List<String> conditionList = new ArrayList<String>();
		conditionList.add(startSpace + "//conditionStmt");
		//rename if(isRename.size() == 0) {
		if(renamedUseVarId.size() == 0) {
			conditionList.add(startSpace + "assert(addVar == addVar" + rename_suffix +") ;");
		}
		
		//rename	for(String varid: isRename.keySet()) {
		for(String varid: renamedUseVarId) {
			AstVariable var = allVarsMap.get(varid);
			String varname = var.getName();
			String kind = var.getKind();
			if(kind.equals("common")) {
				if(var.getIsStructUnion() == false) {
					conditionList.add(startSpace + "assert(" + varname + " == " + varname + rename_suffix + ") ;");
				}else {
					StructUnionBlock suBlock = this.searchStructUnionBlock(allStructUnionMap, var);
					for(FieldVar fieldVar: suBlock.getChildField()) {
						String fieldname = fieldVar.getName();
						if(fieldVar.getKind().equals("common")) {
							//非struct/union的commom变量
							if( !fieldVar.getIsStructUnion() ) {
								conditionList.add(startSpace + "assert(" + varname + "." + fieldname + " == "
										+ varname + rename_suffix + "." + fieldname + ") ;");
							}
						}else if(fieldVar.getKind().equals("array")) {
							String eleKind = ArrayVar.getEleKindByType(fieldVar.getType());
							if(eleKind.equals("pointer")) continue ;
							
							//元素非struct/union的数组变量
							if( !fieldVar.getIsStructUnion() ) {
								int index;
								String eleType = ArrayVar.getEleTypeByType(fieldVar.getType());	//去掉const，因为不在声明时赋值
								if( (index = eleType.indexOf(" const ")) != -1) {
									eleType = eleType.substring(0, index+1) + eleType.substring(index+7);
								}else if( (index = eleType.indexOf(" const")) != -1) {
									eleType = eleType.substring(0, index+1) + eleType.substring(index+6);
								}else if( (index = eleType.indexOf("const ")) != -1 ){
									eleType = eleType.substring(0, index) + eleType.substring(index+5);
								}
								eleType.replaceFirst("^\\s*", "");
								
								String addressZero = ArrayVar.getAddressZero(fieldVar.getType());
								String filedArrEleCnt = ArrayVar.getEleCntBySizeof(varname + "." + fieldname, fieldVar.getType());
								String fieldVarPointerName = varname + "_" + fieldname + "_p";
								String fieldVarPointerName_1 = varname + rename_suffix +"_" + fieldname + "_p";
								conditionList.add(startSpace + eleType + " *" + fieldVarPointerName + " = &"
										+ varname + "." + fieldname + addressZero + ";");
								conditionList.add(startSpace + eleType + " *" + fieldVarPointerName_1 + " = &"
										+ varname + rename_suffix + "." + fieldname + addressZero + ";");
								conditionList.add(startSpace + "for( int i = 0; i < " + filedArrEleCnt + "; i++ )");
								conditionList.add(startSpace + "\tassert(" + fieldVarPointerName + "[i] == " + fieldVarPointerName_1 + "[i]) ;");
							}
						}
					}
				}
			}else if(kind.equals("array")) {
				if(var.getIsStructUnion() == false) {
				//元素非struct、union的数组
					String eleCnt = ArrayVar.getEleCntBySizeof(varname, var.getType());
					conditionList.add(startSpace + "for( int i = 0; i < " + eleCnt + "; i++ ) ");
					conditionList.add(startSpace + "\tassert(" + varname + "_p[i] == " + varname + rename_suffix + "_p[i]) ;");
				}else {
				//元素存放的是struct/union
					StructUnionBlock suBlock = this.searchStructUnionBlock(allStructUnionMap, var);	
					String varEleCnt = ArrayVar.getEleCntBySizeof(varname, var.getType());
					String outmostForHead = "for( int i = 0; i < " + varEleCnt + "; i++ ) ";
					for(FieldVar fieldVar: suBlock.getChildField()) {
						String fieldname = fieldVar.getName();
						if(fieldVar.getKind().equals("common")) {
							//非struct/union的commom变量
							if( !fieldVar.getIsStructUnion() ) {
								conditionList.add(startSpace +  outmostForHead);
								conditionList.add(startSpace + "\tassert(" + varname + "_p[i]." + fieldname + " == "
										+ varname + rename_suffix + "_p[i]." + fieldname + ") ;");
							}
						}else if(fieldVar.getKind().equals("array")) {
							String eleKind = ArrayVar.getEleKindByType(fieldVar.getType());
							if(eleKind.equals("pointer")) continue ;
							
							//元素非struct/union的数组变量
							if( !fieldVar.getIsStructUnion() ) {
								int index;
								String eleType = ArrayVar.getEleTypeByType(fieldVar.getType());	//去掉const，因为不在声明时赋值
								if( (index = eleType.indexOf(" const ")) != -1) {
									eleType = eleType.substring(0, index+1) + eleType.substring(index+7);
								}else if( (index = eleType.indexOf(" const")) != -1) {
									eleType = eleType.substring(0, index+1) + eleType.substring(index+6);
								}else if( (index = eleType.indexOf("const ")) != -1 ){
									eleType = eleType.substring(0, index) + eleType.substring(index+5);
								}
								eleType.replaceFirst("^\\s*", "");
								
								String addressZero = ArrayVar.getAddressZero(fieldVar.getType());
								String filedArrEleCnt = ArrayVar.getEleCntBySizeof(varname + "_p[i]." + fieldname, fieldVar.getType());
								String varPointerName = varname + "_p";
								String varPointerName_1 = varname + rename_suffix + "_p";
								String fieldVarPointerName = varname + "_" + fieldname + "_p";
								String fieldVarPointerName_1 = varname + rename_suffix +"_" + fieldname + "_p";
								
								conditionList.add(startSpace + outmostForHead + "{");
								conditionList.add(startSpace + "\t" + eleType + " *" + fieldVarPointerName + " = &"
										+ varPointerName + "[i]." + fieldname + " " + addressZero + ";");
								conditionList.add(startSpace + "\t" + eleType + " *" + fieldVarPointerName_1 + " = &"
										+ varPointerName_1 + "[i]." + fieldname + addressZero + ";");
								conditionList.add(startSpace + "\tfor( int j = 0; j < " + filedArrEleCnt + "; j++ )");
								conditionList.add(startSpace + "\t\tassert(" + fieldVarPointerName + "[j] == " + fieldVarPointerName_1 + "[j]) ;");
								conditionList.add(startSpace + "}");
							}
						}
					}
				}
			}
		}
		
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
		String vartype = var.getType();
		String vartype2 = var.getType2();
		for(StructUnionBlock suBlock: suBlockMap.values()) {
			String su_name = suBlock.getName();
			if(vartype.matches(".*[^_0-9a-zA-Z]*\\b" + su_name + "[^_0-9a-zA-Z]*.*")
					|| vartype2.matches(".*[^_0-9a-zA-Z]*\\b" + su_name + "[^_0-9a-zA-Z]*.*")) {
				findBlock = suBlock;
				break ;
			}
		}
		return findBlock;
	}
//	public List<String> genConditionList( int startLine, List<AstVariable> usevarList) {
//		this.startLine = startLine;
//		this.startSpace = LoopBlock.getSpace(initialList.get(startLine));
//		
//		List<String> conditionList = new ArrayList<String>();
//		String common = "";
//		String array = "";
//		List<String> arrayList = new ArrayList<String>();
//		String pointer = "";
//		int com = 0, point = 0;
//		for(AstVariable var: usevarList) {
//			if(var.getType().equals("pointer")) System.out.println("name: " + var.getName() + " " + var.getId());
//			String oldname = var.getName();
//			String rename = oldname + rename_suffix;
//			String kind = var.getKind();
//			String type = var.getType();
//			if(kind.equals("common")) {
//				if(com != 0) common += " || ";
//				common += oldname + "!=" + rename;
//				com++;
//			}else if(kind.equals("array")) {
//				int start = type.indexOf('*');
//				int end = type.indexOf('*');
//				String star;
//				if( start != -1) {
//					star = type.substring(start, end);
//				}else {
//					star = "";
//				}
//				array = startSpace + "for( int i = 0; i < " + ArrayVar.getEleCntBySizeof(oldname,type) + "; i++) {\n";
//				array += startSpace + "\tif("+ star + oldname + "[i] != " + star + rename + "[i]) " + conditionBody + "\n";
//				array += startSpace + "}";
//				arrayList.add(array);
//			}else {
//				if(point != 0) pointer += " || ";
//				String star = PointerVar.getLevelStar(type);
//				pointer = star + oldname + "!=" + star + rename;
//				point++;
//			}
//		}
//		
//		conditionList.add(startSpace + "//conditionList");
//		if(com != 0) {
//			common = startSpace + "if(" + common + ") " + conditionBody;
//			conditionList.add(common);
//		}
//		if(arrayList.size() != 0) {
//			conditionList.addAll(arrayList);
//		}
//		if(point != 0) {
//			pointer = startSpace + "if(" + pointer + ") " +  conditionBody;
//			conditionList.add(pointer);
//		}
//		return conditionList;
//	}
//	
	private void commonRename(AstVariable var, List<String> renameUseVarStmt) {
		//rename	if(isRename.containsKey(var.getId()) && isRename.get(var.getId()) == true) return ;
		if(renamedUseVarId.contains(var.getId()) || var.getIsGlobal() == true) return ;
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
		if( (index = eleType.indexOf(" const ")) != -1) {
			eleType = eleType.substring(0, index+1) + eleType.substring(index+7);
		}else if( (index = eleType.indexOf(" const")) != -1) {
			eleType = eleType.substring(0, index+1) + eleType.substring(index+6);
		}else if( (index = eleType.indexOf("const ")) != -1 ){
			eleType = eleType.substring(0, index) + eleType.substring(index+5);
		}
		eleType = eleType.trim();
		
		String varname = arr.getName();
		String rename = varname + rename_suffix;
		String varname_p = varname + "_p";	//name_p
		String rename_p = rename + "_p";	//name_1_p
		//截[2][3],变 [0][0]
		String addressZero = ArrayVar.getAddressZero(arr.getType());
		//int arr_len = arr.getLength();
		
		//int rename[2][3];w
		//int [2][3] =》 int rename[2][3];
		renameUseVarStmt.add(startSpace + arr.getType().replaceFirst("\\[", rename + "[") + ";");
		//int *name_p = &name[0][0];
		renameUseVarStmt.add(startSpace + eleType + " *" + varname_p + " = &" + varname + addressZero + ";");
		//int *rename_p = &rename[0][0];
		renameUseVarStmt.add(startSpace + eleType + " *" + rename_p + " = &" + rename + addressZero + ";");
		//rename_p[i] = name_p[i];
		renameUseVarStmt.add(startSpace + "for(int i = 0; i < " + arr.getEleCnt() + "; i++) {");
		renameUseVarStmt.add(startSpace + "\t" + rename_p +  "[i] = " + varname_p +"[i];");		
		renameUseVarStmt.add(startSpace + "}");
		
		//rename	isRename.put(arr.getId(), true);
		renamedUseVarId.add(arr.getId());
	}
	
	private void pointerRename(PointerVar pvar, List<String> renameUseVarStmt) {
		//rename	if(isRename.containsKey(pvar.getId()) && isRename.get(pvar.getId()) == true) return;
		if(renamedUseVarId.contains(pvar.getId()) || pvar.getIsGlobal() == true) return ;
		String pointToKind = pvar.getPointToKind();
		
		String pvartype = pvar.getType();
		String vartype;
//		System.out.println("renamePvar: " + pvar.getName() + " " + pvar.getId() + " pointToKind" + pointToKind);
		if(pointToKind.equals("common")) {
			String pvar_name = pvar.getName();
			String pvar_rename = pvar_name + rename_suffix;
			String pointtoid = nearestPointVar(pvar, this.startLine );
//			System.out.println("pointertoid: " + pointtoid);
			if(pointtoid != null) {
				//指向普通变量
				/*temp*/
				if(pointtoid.startsWith("0x")) {
					AstVariable var = allVarsMap.get(pointtoid);
					String var_rename = var.getName()+rename_suffix;
					vartype = var.getType();
					if(var.getKind().equals("common")) {
//						System.out.println("pvar:( " + pvar_name + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//								+ "; pointToKind is common, commonVar:( " + var.getName() + ", " + var.getId() + ", " + var.getDeclareLine() + " ) " );
						commonRename(var, renameUseVarStmt);
						
						//rename	renameUseVarStmt.add(startSpace + pvar.getType()+" "+pvar_rename+" = "+"&"+var_rename+";");
						if(renamedUseVarId.contains(var.getId()) == false) {
//							System.out.println("pointerRename:" + startSpace + pvar.getType()+ " " + pvar_rename+" = &" + var.getName()+";///\n");
							renameUseVarStmt.add(startSpace + pvar.getType()+ " " + pvar_rename+" = &" + var.getName()+";");
						}else {
//							System.out.println("pointerRename:" + startSpace + pvar.getType()+ " "+ pvar_rename+" = "+"&"+var_rename+";///\n");
							renameUseVarStmt.add(startSpace + pvar.getType()+ " "+ pvar_rename+" = "+"&"+var_rename+";");
						}
					}else if(var.getKind().equals("array")){
					//指向数组
//						System.out.println("pvar:( " + pvar_name + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//								+ "; pointToKind is array, arrayVar:( " + var.getName() + ", " + var.getId() + ", " + var.getDeclareLine() + " ) " );
						arrayRename((ArrayVar) var, renameUseVarStmt);
						String oldtype = var.getType();
						//截[2][3],变 [0][0]
						String address = ArrayVar.getAddressZero(oldtype);
						String oldaddress = "&" + var.getName() + address;
						String newaddress = "&" + var_rename + address;
						//rename	renameUseVarStmt.add(startSpace + pvar.getType()+" "+pvar_rename+" = "+newaddress+" + ("+ pvar.getName()+" - "+ oldaddress +");");
						if(renamedUseVarId.contains(var.getId()) == false) {
//							System.out.println("pointerRename:" + startSpace + pvar.getType()+" "+pvar_rename+" = "+oldaddress+" + ("+ pvar_name+" - "+ oldaddress +");///");
							renameUseVarStmt.add(startSpace + pvar.getType()+" "+pvar_rename+" = "+oldaddress+" + ("+ pvar_name +" - "+ oldaddress +");");
						}else {
//							System.out.println("pointerRename:" + startSpace + pvar.getType()+" "+pvar_rename+" = "+newaddress+" + ("+ pvar_name +" - "+ oldaddress +");///");
							renameUseVarStmt.add(startSpace + pvar.getType()+" "+pvar_rename+" = "+newaddress+" + ("+ pvar_name +" - "+ oldaddress +");");
						}
					}else if(var.getKind().equals("pointer")) {
					//int *q = &a; int *p = q;
//						System.out.println("pvar:( " + pvar_name + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//								+ "; pointToKind is pointer, pointerVar:( " + var.getName() + ", " + var.getId() + ", " + var.getDeclareLine() + " ) " );
						pointerRename((PointerVar)var, renameUseVarStmt);	//int a1=a; int *q1=&a1;
						//int *q = &a; int **qq = &q; int *p = *qq;
						//int *q1 = &a1; int **qq1 = &q1; int *p1 = *qq1;
						String star = vartype.substring(vartype.indexOf('*'), vartype.lastIndexOf('*'));
						//rename	renameUseVarStmt.add(startSpace + pvar.getType()+" "+pvar_rename+" = "+star+var_rename+";");//star="*";int *p_1 = *qq1;
						if(renamedUseVarId.contains(var.getId()) == false) {
//							System.out.println("pointerRename:" +startSpace + pvar.getType()+" "+pvar_rename+" = "+star+var.getName()+";"+ "///");
							renameUseVarStmt.add(startSpace + pvar.getType()+" "+pvar_rename+" = "+star+var.getName()+";");//star="*";int *p_1 = *qq1;
						}else {
//							System.out.println("pointerRename: " + startSpace + pvar.getType()+" "+pvar_rename+" = "+star+var_rename+";///");
							renameUseVarStmt.add(startSpace + pvar.getType()+" "+pvar_rename+" = "+star+var_rename+";");//star="*";int *p_1 = *qq1;
						}
					}
				}else {
				//返回的是指向的行号，说明调用了函数，如malloc()；或者直接赋值, 如char c = "12345";
//					System.out.println("pvar:( " + pvar_name + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//							+ "; pointToKind is declareLine: " + pointtoid);
					int existline = Integer.parseInt(pointtoid);
					for(EqualOperatorBlock eblock: equalOpBlocks) {
						if(eblock.getStartLine() == existline) {
							if(eblock.getLeftVar().equals(pvar.getId()) == false
									|| eblock.getCalculateType().equals(pvar.getType()) == false ) continue ;
							String line = initialList.get(existline);
							line = line.replaceFirst("^\\s*", "");
							//e.g. int *p = (int *)malloc();=> (int *)malloc();
							String varRightPart = line.substring(line.indexOf(pvar_name));	//截取从pvar_name开始的line右边部分
							varRightPart = varRightPart.substring(varRightPart.indexOf("=") + 1);	//截取pvar_name等号之后的右边部分
							int end;
							if(eblock.getHasFunction() == true) {
								String functionName = eblock.getFunctionName().get(0);
								//从varpart的malloc的(开始
								end = varRightPart.indexOf("(", varRightPart.indexOf(functionName));
								int leftbraceCnt = 0, rightbraceCnt = 0;
								for(; end < varRightPart.length(); end++) {
									char ch = varRightPart.charAt(end);
									if(ch == '(') leftbraceCnt++;
									else if(ch == ')') rightbraceCnt++;
									if(leftbraceCnt == rightbraceCnt) break;
								}//此时end是function结束位置的索引
								//varRightPart.substring(end): (int *)malloc(); => p_1 = (int *)malloc()
								if(varRightPart.indexOf(",", end) == -1) {
									end = varRightPart.indexOf(";", end);
								}else if(varRightPart.indexOf(";", end) == -1) {
									end = varRightPart.indexOf(",", end);
								}else {
									end = Math.min(varRightPart.indexOf(",", end), varRightPart.indexOf(";", end));
								}
							}else {
								if(varRightPart.indexOf(",") == -1) {
									end = varRightPart.indexOf(";");
								}else if(varRightPart.indexOf(";") == -1) {
									end = varRightPart.indexOf(",");
								}else {
									end = Math.min(varRightPart.indexOf(","), varRightPart.indexOf(";"));
								}
							}
//							System.out.print("pointerRename:" + startSpace + pvar.getType() + " " + pvar_rename + " = " 
//									+ varRightPart.substring(0, end) + ";" + "///");
							renameUseVarStmt.add(startSpace + pvar.getType() + " " + pvar_rename + " = " 
									+ varRightPart.substring(0, end) + ";");
							
							break;
						}
					}
				}
			}else {
			//没有指向任何变量，说明仅声明，未赋值
//				System.out.println("pvar:( " + pvar_name + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//						+ "; only declare not cinit");
				renameUseVarStmt.add(startSpace + pvar.getType() + " " + pvar_rename +";");
			}
		}else if(pointToKind.equals("array")) {
		//指向数组的指针 int a[2]; int (*p)[2] = a;
			String pvar_name = pvar.getName();
			String pvar_rename = pvar_name + rename_suffix;
			String pointtoid = nearestPointVar(pvar, this.startLine);
			if(pointtoid != null) {
				if(pointtoid.startsWith("0x")) {
					AstVariable var = allVarsMap.get(pointtoid);
					String var_rename = var.getName()+rename_suffix;
					vartype = var.getType();
					if(var.getKind().equals("array")){
					//指向数组
					//int a[2][2][3] = {1,2,3,4,5,6,7,8,9,10,11,12};
					//int (*q)[2][3] = &a[1];
					//int (*q_1)[2][3] = &a_1[0] + (q-&a[0]);
//						System.out.println("pvar:( " + pvar_name + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//								+ "; pointToKind is array, arrayVar:( " + var.getName() + ", " + var.getId() + ", " + var.getDeclareLine() + " ) " );
						arrayRename((ArrayVar) var, renameUseVarStmt);
						
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
						//rename	renameUseVarStmt.add(startSpace + left +" = "+revar_address+" + ("+ pvar.getName()+" - "+ var_address +");");
						if(renamedUseVarId.contains(var.getId()) == false) {
//							System.out.println("pointerRename:" + startSpace + left +" = "+ var_address+" + ("+ pvar_name +" - "+ var_address +");" + "///");
							renameUseVarStmt.add(startSpace + left +" = "+ var_address+" + ("+ pvar_name +" - "+ var_address +");");
						}else {
//							System.out.println("pointerRename:" + startSpace + left +" = "+ revar_address+" + ("+ pvar_name +" - "+ var_address +");" + "///");
							renameUseVarStmt.add(startSpace + left +" = "+revar_address+" + ("+ pvar_name+" - "+ var_address +");");
						}
					}else if(var.getKind().equals("pointer")) {
//						System.out.println("pvar:( " + pvar.getName() + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//								+ "; pointToKind is array, pointerVar:( " + var.getName() + ", " + var.getId() + ", " + var.getDeclareLine() + " ) " );
						
						pointerRename((PointerVar)var, renameUseVarStmt);
						//e.g1. int (**q)[2]; int (*p)[2] = *q;
						//e.g2. int (*q)[2]; int (*p)[2] = q;
						String star = vartype.substring(vartype.indexOf('*'), vartype.lastIndexOf('*'));
						int split_index = pvartype.indexOf('*', pvartype.indexOf('('))+1;
						//pvartype = int (*)[2]
						// left = int (* + p_1 + )[2]
						String left = pvartype.substring(0, split_index) + pvar_rename + pvartype.substring(split_index);
						//1.star="*";int (*p_1)[2] = *q_1;
						//rename	renameUseVarStmt.add(startSpace + left+" = "+star+var_rename+";");
						if(renamedUseVarId.contains(var.getId()) == false) {
//							System.out.println("pointerRename:" + startSpace + left+" = "+star+var.getName()+";" + "///");
							renameUseVarStmt.add(startSpace + left+" = "+star+var.getName()+";");
						}else {
//							System.out.println("pointerRename:" + startSpace + left+" = "+star+var_rename+";" + "///");
							renameUseVarStmt.add(startSpace + left+" = "+star+var_rename+";");
						}
					}
				}else {
				//返回的是指向的行号，调用了函数；或者直接赋值（非变量名）
//					System.out.println("pvar:( " + pvar.getName() + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//							+ "; pointToKind is declareLine:" + pointtoid);
					int existline = Integer.parseInt(pointtoid);
					for(EqualOperatorBlock eblock: equalOpBlocks) {
						if(eblock.getStartLine() == existline) {
							if(eblock.getLeftVar().equals(pvar.getId()) == false
									|| eblock.getCalculateType().equals(pvar.getType()) == false ) continue ;
							String line = initialList.get(existline);
							line = line.replaceFirst("^\\s*", "");
							//e.g. int *p = (int *)malloc();=> (int *)malloc();
							String varRightPart = line.substring(line.indexOf(pvar_name));	//截取从pvar_name开始的line右边部分
							varRightPart = varRightPart.substring(varRightPart.indexOf("=") + 1);	//截取pvar_name等号之后的右边部分
							int end;
							if(eblock.getHasFunction() == true) {
								String functionName = eblock.getFunctionName().get(0);
								//从varpart的malloc的(开始
								end = varRightPart.indexOf("(", varRightPart.indexOf(functionName));
								int leftbraceCnt = 0, rightbraceCnt = 0;
								for(; end < varRightPart.length(); end++) {
									char ch = varRightPart.charAt(end);
									if(ch == '(') leftbraceCnt++;
									else if(ch == ')') rightbraceCnt++;
									if(leftbraceCnt == rightbraceCnt) break;
								}//此时end是function结束位置的索引
								//varRightPart.substring(end): (int *)malloc(); => p_1 = (int *)malloc()
								if(varRightPart.indexOf(",", end) == -1) {
									end = varRightPart.indexOf(";", end);
								}else if(varRightPart.indexOf(";", end) == -1) {
									end = varRightPart.indexOf(",", end);
								}else {
									end = Math.min(varRightPart.indexOf(",", end), varRightPart.indexOf(";", end));
								}
							}else {
								if(varRightPart.indexOf(",") == -1) {
									end = varRightPart.indexOf(";");
								}else if(varRightPart.indexOf(";") == -1) {
									end = varRightPart.indexOf(",");
								}else {
									end = Math.min(varRightPart.indexOf(","), varRightPart.indexOf(";"));
								}
							}
							
							int split_index = pvartype.indexOf('*', pvartype.indexOf('('))+1;
							//pvartype = int (*)[2]
							// left = int (* + p_1 + )[2]
							String left = pvartype.substring(0, split_index) + pvar_rename + pvartype.substring(split_index);
//							System.out.print("pointerRename:" + startSpace + left + " = " + varRightPart.substring(0, end) + ";" + "///");
							renameUseVarStmt.add(startSpace + left + " = " + varRightPart.substring(0, end) + ";");
							break;
						}
					}
				}
			}else {
			//没有指向任何变量，说明仅声明，未赋值
//				System.out.println("pvar:( " + pvar.getName() + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//						+ "; only declare not cinit");
				//int (*p)[2][3]; = >pvartype = int (*)[2][3]
				int split_index = pvartype.indexOf('*', pvartype.indexOf('('))+1;
//				System.out.println("pointerRename:" + startSpace + pvartype.substring(0, split_index) + pvar_rename + pvartype.substring(split_index)+";///");
				renameUseVarStmt.add(startSpace + pvartype.substring(0, split_index) + pvar_rename + pvartype.substring(split_index)+";");
				// int (*p_1)[2][3];
			}
		}else if(pointToKind.equals("pointer")) {
		//指向指针
			String pvar_name = pvar.getName();
			String pvar_rename = pvar_name+rename_suffix;
			String pointtoid = nearestPointVar(pvar, this.startLine);
			if(pointtoid != null) {
				if(pointtoid.startsWith("0x")) {
					AstVariable var = allVarsMap.get(pointtoid);
					String var_rename = var.getName()+rename_suffix;
					if(var.getKind().equals("array")){
					//指向数组
					//int* b[2];
					//int **p = b;
					//int **p_1 = &b_1[0] + (p-&b[0]);
//						System.out.println("pvar:( " + pvar.getName() + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//								+ "; pointToKind is pointer, arrayVar:( " + var.getName() + ", " + var.getId() + ", " + var.getDeclareLine() + " ) " );
						arrayRename((ArrayVar) var, renameUseVarStmt);
						String oldtype = var.getType();
						//截[2][3],变 [0][0]
						String address = ArrayVar.getAddressZero(oldtype);
						String oldaddress = "&" + var.getName() + address;
						String newaddress = "&" + var_rename + address;
						//rename	renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+newaddress+" + ("+ pvar.getName()+" - "+ oldaddress +");");
						if(renamedUseVarId.contains(var.getId()) == false) {
//							System.out.println("pointerRename:" + startSpace + pvartype+" "+pvar_rename+" = "+oldaddress+" + ("+ pvar.getName()+" - "+ oldaddress +");///");
							renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+oldaddress+" + ("+ pvar_name +" - "+ oldaddress +");");
						}else {
//							System.out.println("pointerRename:" + startSpace + pvartype+" "+pvar_rename+" = "+newaddress+" + ("+ pvar.getName()+" - "+ oldaddress +");" + "///");
							renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+newaddress+" + ("+ pvar_name +" - "+ oldaddress +");");
						}
					}else if(var.getKind().equals("pointer")) {
//						System.out.println("pvar:( " + pvar.getName() + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//								+ "; pointToKind is pointer, pointerVar:( " + var.getName() + ", " + var.getId() + ", " + var.getDeclareLine() + " ) " );
						
						pointerRename((PointerVar)var, renameUseVarStmt);	//int a1=a; int *q1=&a1;
						
						String var_pointtokind = PointerVar.getPointToKindByType(var.getType());
						if(var_pointtokind.equals("array")) {
							//e.g.
							//int arr[4][3][2] = {1,2,3};
							//int(*q)[2] = &arr[0][0];
							//int(**p)[2] = &q;
							int split_index = pvartype.lastIndexOf('*', pvartype.indexOf('('))+1;
							//pvartype = int (**)[2]
							// left = int (** + p_1 + )[2]
							String left = pvartype.substring(0, split_index) + pvar_rename + pvartype.substring(split_index);
							if(renamedUseVarId.contains(var.getId()) == false) {
//								System.out.println("pointerRename:" + startSpace + left + " = &"+var.getName()+";" + "///");
								renameUseVarStmt.add(startSpace + left + " = &"+var.getName()+";");
							}else {
//								System.out.println("pointerRename:" + startSpace + left + " = &"+var_rename+";" + "///");
								renameUseVarStmt.add(startSpace + left + " = &"+var_rename+";");
							}
						}else {
							int s,e;
							vartype = var.getType();
							String pvarstar, varstar, star, ref;
							s = pvartype.indexOf('*');
							//e = Math.min(pvartype.length(), pvartype.lastIndexOf('*'));
							e = pvartype.lastIndexOf('*');
							pvarstar = pvartype.substring(s, e+1);
							s = vartype.indexOf('*');
							//e = Math.min(vartype.length(), vartype.lastIndexOf('*'));
							e = vartype.lastIndexOf('*');
							varstar = vartype.substring(s, e+1);
							if(pvarstar.length() <= varstar.length()) {
								// int ***q; int **p = *q; (pvarstr="**", varstr = "***")
								//int **p1 = *q1; 
								star = varstar.substring(pvarstar.length());
								//rename	renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+star+var_rename+";");
								if(renamedUseVarId.contains(var.getId()) == false) {
									renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+star+var.getName()+";");
								}else {
									renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+star+var_rename+";");
								}
							}else {
								//int *q; int **p = &q;
								//int **p1 = &q1;
								ref = "&";
								//rename	renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+ref+var_rename+";");
								if(renamedUseVarId.contains(var.getId()) == false) {
//									System.out.println("pointerRename:" +startSpace + pvartype+" "+pvar_rename+" = "+ref+var.getName()+";" + "///");
									renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+ref+var.getName()+";");
								}else {
//									System.out.println("pointerRename:" + startSpace + pvartype+" "+pvar_rename+" = "+ref+var_rename+";" + "///");
									renameUseVarStmt.add(startSpace + pvartype+" "+pvar_rename+" = "+ref+var_rename+";");
								}
							}
						}
					}
				}else {
				//返回的是指向的行号，说明malloc了一个空间；或者声明时直接赋值
//					System.out.println("pvar:( " + pvar.getName() + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//							+ "; pointToKind is declareLine: " + pointtoid);
				
					int existline = Integer.parseInt(pointtoid);
					for(EqualOperatorBlock eblock: equalOpBlocks) {
						if(eblock.getStartLine() == existline) {
							if(eblock.getLeftVar().equals(pvar.getId()) == false
									|| eblock.getCalculateType().equals(pvar.getType()) == false ) continue ;
							String line = initialList.get(existline);
							line = line.replaceFirst("^\\s*", "");
							//e.g. int *p = (int *)malloc();=> (int *)malloc();
							String varRightPart = line.substring(line.indexOf(pvar_name));	//截取从pvar_name开始的line右边部分
							varRightPart = varRightPart.substring(varRightPart.indexOf("=") + 1);	//截取pvar_name等号之后的右边部分
							int end;
							if(eblock.getHasFunction() == true) {
								String functionName = eblock.getFunctionName().get(0);
								//从varpart的malloc的(开始
								end = varRightPart.indexOf("(", varRightPart.indexOf(functionName));
								int leftbraceCnt = 0, rightbraceCnt = 0;
								for(; end < varRightPart.length(); end++) {
									char ch = varRightPart.charAt(end);
									if(ch == '(') leftbraceCnt++;
									else if(ch == ')') rightbraceCnt++;
									if(leftbraceCnt == rightbraceCnt) break;
								}//此时end是function结束位置的索引
								//varRightPart.substring(end): (int *)malloc(); => p_1 = (int *)malloc()
								if(varRightPart.indexOf(",", end) == -1) {
									end = varRightPart.indexOf(";", end);
								}else if(varRightPart.indexOf(";", end) == -1) {
									end = varRightPart.indexOf(",", end);
								}else {
									end = Math.min(varRightPart.indexOf(",", end), varRightPart.indexOf(";", end));
								}
							}else {
								if(varRightPart.indexOf(",") == -1) {
									end = varRightPart.indexOf(";");
								}else if(varRightPart.indexOf(";") == -1) {
									end = varRightPart.indexOf(",");
								}else {
									end = Math.min(varRightPart.indexOf(","), varRightPart.indexOf(";"));
								}
							}
							String left = "";
							if(pvartype.contains("[")) {
								int index;
								index = pvartype.lastIndexOf('*', pvartype.indexOf('('))+1;
								//pvartype = int (**)[2][3]
								//left = int (** + q_1 + )[2][3]
								left = pvartype.substring(0, index) + pvar_rename + pvartype.substring(index);
							}else {
								left = pvartype + " " + pvar_rename;
							}
//							System.out.print("pointerRename:" + startSpace + left + " = " + varRightPart.substring(0, end) + ";" + "///");
							renameUseVarStmt.add(startSpace + left + " = " + varRightPart.substring(0, end) + ";");
							break;
						}
					}
				}
			}else {
			//没有指向任何变量，说明仅声明，未赋值
//				System.out.println("pvar:( " + pvar.getName() + ", " + pvar.getId() + ", " + pvar.getDeclareLine() + " ) " 
//						+ "; only declare not cinit");
//				System.out.println("pointerRename:" + startSpace + pvartype + " " + pvar_rename +";");
				String left = "";
				if(pvartype.contains("[")) {
					int split_index =pvartype.lastIndexOf('*', pvartype.indexOf('('))+1;
					//pvartype = int (**)[2][3]
					//left = int (** + q_1 + )[2][3]
					left = pvartype.substring(0, split_index) + pvar_rename + pvartype.substring(split_index);
				}else left = pvartype + " " + pvar_rename;
//				System.out.println("pointerRename:" + startSpace + left +";" + "///");
				renameUseVarStmt.add(startSpace + left +";");
			}
		}
		//rename	isRename.put(pvar.getId(), true);
		renamedUseVarId.add(pvar.getId());
	}
	

	
//	private String nearestPointVar(PointerVar pvar, int startLine) {
//		int existline = 0;
//		ArrayList<Integer> varexistline = new ArrayList<Integer>();
//		varexistline.add(pvar.getDeclareLine());
////		System.out.println("varexistline:" + varexistline);
//		varexistline.addAll(pvar.useLine);
////		System.out.println("varexistline:" + varexistline);
//		System.out.println("find nearestPointVar pvar: " + pvar.getName() + " " + pvar.getId());
//		System.out.println(varexistline);
//		for(int i = varexistline.size()-1; i >= 0; i--) {
//			existline = varexistline.get(i);
//			if(existline >= startLine) continue;
//			else {
//				for(int j = equalOpBlocks.size()-1; j >= 0; j--) {
//					EqualOperatorBlock eblock = equalOpBlocks.get(j);
//					if(eblock.getStartLine() == 18) {
//						System.out.println("existline: " + existline);
//						System.out.println("eblock: " + eblock.getStartLine() + " " + eblock.getEndLine());
//						if(eblock.getRightVar().size() != 0) {
//							AstVariable rightVar = allVarsMap.get(eblock.getRightVar().get(0));
//							System.out.println("eblockRightVar:" + rightVar.getName() + " " + rightVar.getDeclareLine());
//							
//						}
//					}
//					if(eblock.getStartLine() >= existline && eblock.getEndLine() <= existline) {
//						//System.out.println("existline" + existline);
//						if(eblock.getLeftVar().equals(pvar.getId())) {
//							//System.out.println("cal: " + eblock.getCalculateType());
//							if(eblock.getCalculateType().equals(pvar.getType())) {
//							//eblock的类型和pvar的类型一致，说明是指向某个变量
////								System.out.println("pvar: " + pvar.getName() + ";pointerToKind " + 
////											allVarsMap.get(eblock.getLeftVar()).getName());
////								System.out.println("rightvar:" + eblock.getRightVar());
//								
//								if(eblock.getRightVar().size() == 0 ) {
//								//是指向，但是右边不是某个变量，说明是直接赋值;或者是声明行的赋值
//								//形如char *p = "abc"; 
//								//形如int *p; p = (int *) malloc(sizeof(int));
//								//返回赋值的行号
//									return String.valueOf(existline);
//								}else return eblock.getRightVar().get(0);
//							}else continue;
//						}else {
//							continue ;
//						}
//					}
//				}
//			}
//		}
//		return null;	//没有指向任何变量，仅声明，无cinit；
//	}
	
	private String nearestPointVar(PointerVar pvar, int startLine) {
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
						if(eblock.getLeftVar().equals(pvar.getId()) && eblock.getCalculateType().equals(pvar.getType()) ) {
							//eblock的leftVar是pvar，且eblock计算结果的类型和pvar的类型一致，说明是pvar指向的语句
							if(eblock.getRightVar().size() == 0 ) {
								//是指向，但是右边不是某个变量，或是声明时的赋值；或者是直接赋值；或者是调用了函数，函数返回值是pvar类型
								//形如char *p = "abc"; 
								//形如char *p; p = "abc";
								//形如int *p; p = (int *) malloc(sizeof(int));
								//返回赋值的行号
								return String.valueOf(existline);
							}else {
								if(eblock.getHasFunction() == false) return eblock.getRightVar().get(0);
								else{	//有函数，有变量，但是变量是func的参数，形如int *p = (int*) malloc(); 形如 int *p = function(parm a, parm b);
									//int firstRightFunAstLine = eblock.getRightFirstVarAstLine();
									//int firstRightVarAstLine = eblock.getRightFirstVarAstLine();
									return String.valueOf(existline);
								}
							}
						}
					}
				}
			}
		}
		return null;	//没有指向任何变量，仅声明，没有附初值，形如int*p;
	}
	
}

