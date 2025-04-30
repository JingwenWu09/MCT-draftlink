package TransOperation;

import java.util.List;

import expression_operation.Expression;

public class Rename {
	
	public static List<String> getRenamedTransBlock(List<String> oldName, List<String> block){
		String rename_suffix = "_1";
	
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
	
}
