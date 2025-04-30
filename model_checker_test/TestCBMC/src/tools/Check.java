package tools;

import java.util.ArrayList;
import java.util.List;

public class Check {
	public boolean isConsistent(List<List<String>> lists) {
		List<String> notEmpty = new ArrayList<String>();
		for(List<String> l: lists) {
			if(l.size() != 0) {
				notEmpty.add("not empty");
			}
		}
		//inconsistent
		if(notEmpty.size() >= 2) {
			return false;
		}
		//consistent
		return true;
	}
}
