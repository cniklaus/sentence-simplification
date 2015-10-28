package sentenceCompression;

import java.util.ArrayList;

public class Core {
	
	public static ArrayList<Integer> start = new ArrayList<Integer>();
	public static ArrayList<Integer> end = new ArrayList<Integer>();
	public static ArrayList<String> prefix = new ArrayList<String>();
	public static ArrayList<String> postfix = new ArrayList<String>();
	
	public static ArrayList<Integer> getStart() {
		return start;
	}
	
	public static ArrayList<Integer> getEnd() {
		return end;
	}
	
	public static ArrayList<String> getPrefix() {
		return prefix;
	}
	
	public static ArrayList<String> getPostfix() {
		return postfix;
	}
	
	public static void setStart(Integer st) {
		start.add(st);
	}
	
	public static void setEnd(Integer e) {
		start.add(e);
	}
	
	public static void setPrefix(String pre) {
		prefix.add(pre);
	}
	
	public static void setPostfix(String post) {
		postfix.add(post);
	}
	
	
}
