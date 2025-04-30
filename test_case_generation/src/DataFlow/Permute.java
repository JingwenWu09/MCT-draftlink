package DataFlow;

import java.util.ArrayList;
import java.util.List;

public class Permute {
	private static List<List<Integer>> res;
	private static boolean[] used;

	public static List<List<Integer>> permute(int n, int m) {
		res = new ArrayList<>();
		if(n <= 0 || m < 0 || n < m ) {
			System.out.println("False Parameter");
			return null;
		}
		
		used = new boolean[n];
		int[] nums = new int[n];
		for(int i = 0; i < n; i++) {
			nums[i] = i;
		}
		List<Integer> preList = new ArrayList<>();
		generatePermutation(nums, 0, m, preList);
		return res;

	}

	private static void generatePermutation(int[] nums, int index, int m, List<Integer> preList) {
		//index == m 表示当前已经形成一种m位的排列
		if(index == m) {
			res.add(new ArrayList<>(preList));
			return ;
		}
		
		for(int i = 0; i < nums.length; i++) {
			if (!used[i]) {
				preList.add(nums[i]);
				used[i] = true;
				generatePermutation(nums, index + 1, m, preList);
				//System.out.println("before: " + preList);
				//一定要记得回溯状态
				preList.remove(preList.size() - 1);
				//System.out.println("after: " + preList);
				used[i] = false; 
			}
		}
        return;
	}
	
	
}
