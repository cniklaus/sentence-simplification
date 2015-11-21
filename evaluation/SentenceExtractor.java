package evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SentenceExtractor {
	public static void main(String[] args) throws IOException {
		ArrayList<ArrayList<String>> coreFramework = extractCore("data/Wikipedia/Eval/Mandela/MandelaResult");
		ArrayList<ArrayList<String>> coreGold = extractCore("data/Wikipedia/Eval/Mandela/gold");
		ArrayList<String> original = extractOriginal("data/Wikipedia/Eval/Mandela/gold");
		
		/**
		int i = 0;
		for (ArrayList<String> li : coreFramework) {
			System.out.println(i + " " + li);
			i++;
		}
		
		int j = 0;
		for (ArrayList<String> li : coreGold) {
			System.out.println(j + " " + li);
			j++;
		}
		
		int k = 0;
		for (String s : original) {
			System.out.println(k + " " + s);
			k++;
		}
		*/
		
		Measures.getCompressionRate(coreFramework, original);
		Measures.getF1(coreFramework, coreGold);
	}
	
	
	public static ArrayList<ArrayList<String>> extractCore(String string) throws IOException {
		File file = new File (string);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
	 
		ArrayList<ArrayList<String>> l = new ArrayList<ArrayList<String>>();
		
		String line = "";
		
		int n = 0;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("original sentence:")) {
				ArrayList<String> list = new ArrayList<String>();
				while ((line = br.readLine()).startsWith("core sentence:")) {
					line = line.replace("core sentence: ", "");
					list.add(line);
				}
				//System.out.println(n + " " + list);
				l.add(n, list);
				n++;
			}
		}
		br.close();	 
		
		return l;
	}
	
	
	
	public static ArrayList<String> extractOriginal(String string) throws IOException {
		File file = new File (string);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
	 
		ArrayList<String> l = new ArrayList<String>();
		
		String line = "";
		
		while ((line = br.readLine()) != null) {
			if (line.startsWith("original sentence:")) {
				line = line.replace("original sentence: ", "");
				l.add(line);
			}
		}
		br.close();	 
		
		return l;
	}
	
}
