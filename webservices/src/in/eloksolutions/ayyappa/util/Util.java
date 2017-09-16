package in.eloksolutions.ayyappa.util;

import java.text.SimpleDateFormat;
import java.util.List;

public class Util {
	public static boolean isListEmpty(List lst){
		return lst==null || lst.size()==0;
	}
	
	public static boolean isEmpty(String str){
		return str==null || str.trim().length()==0;
	}
	
	public static boolean isValidDate(String str){
		try{
			new SimpleDateFormat("dd/MM/yyyy").parse(str);
		}catch(Exception ex){
			return false;
		}
		return true;
	}
}
