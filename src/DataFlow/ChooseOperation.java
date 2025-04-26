package DataFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

import AST_Information.model.AstVariable;

public class ChooseOperation {
	
	
	
	public static List<AstVariable> getInitializedUseVars(int startline, int endline, List<AstVariable> allVars){
		List<AstVariable> useVars = new ArrayList<AstVariable>();
		for(AstVariable var: allVars) {
			
			int decline = var.getDeclareLine();
			if( decline >= startline && decline <= endline) {
				if(var.getIsIntialized() == true) {
					useVars.add(var);
					continue ;
				}
			}
			
			for(int i: var.useLine) {
				if(i >= startline && i <= endline) {
					useVars.add(var);
					break;
				}
			}
		}
		return useVars;
	}

	public static List<AstVariable[]> getVarPermute(List<AstVariable> vars, int m){
		
		List<AstVariable[]> res = new ArrayList<AstVariable[]>();
		AstVariable [] arrange;
		int n = vars.size();
		if( n == 0 ) return res;
		if(n < m) m = n;
		List<List<Integer>> arrangeList = Permute.permute(n, m);
		for(List<Integer> arrStr: arrangeList) {
			arrange = new AstVariable[m];
			for(int i = 0; i < m; i++) {
				arrange[i] = vars.get(arrStr.get(i));
			}
			res.add(arrange);
		}
		return res;
		
	}
	
	public static<T> List<T> getRandomListElements(List<T> allLists, int n){
		int size = allLists.size();
		boolean[] visited = new boolean[size];
		List<T> subList = new ArrayList<T>();
		if(size < n) {
			System.out.println("error: allLists.size() < n ");
			return subList;
		}
		
		Random random = new Random();
		for(int i = 0; i < n; i++) {
			int index = random.nextInt(size);
			while(visited[index] == true) {
				index = random.nextInt(size);
			}
			visited[index] = true;
			subList.add(allLists.get(index));
		}
		
		return subList;
	}
}
