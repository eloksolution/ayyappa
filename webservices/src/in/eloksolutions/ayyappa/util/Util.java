package in.eloksolutions.ayyappa.util;

import java.util.List;

public class Util {
	public static boolean isListEmpty(List lst){
		return lst==null || lst.size()==0;
	}
	
	public static boolean isEmpty(String str){
		return str==null || str.trim().length()==0;
	}
}
