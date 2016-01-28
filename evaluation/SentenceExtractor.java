package evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import sentenceCompression.CoreContextApp;

public class SentenceExtractor {
	static ArrayList<String> toDelete = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {
		toDelete = readNumbersToDelete("data/Wikipedia/Eval/baseball/numbers");
		
		ArrayList<ArrayList<String>> coreFramework = extractCore("data/Wikipedia/Eval/baseball/baseballResult", false);
		ArrayList<ArrayList<String>> coreGold = extractCore("data/Wikipedia/Eval/baseball/gold_annotatedContext", true);
		ArrayList<String> original = extractOriginal("data/Wikipedia/Eval/baseball/baseballResult", false);
		
		for (String s : toDelete) {
			//System.out.println(s);
		}
		
		//generateFile(original, coreFramework);
		generateFile(original, coreGold);
		
		for (String s : original) {
			//System.out.println(s);
		}
		
		
		System.out.println("rate framework: " + Measures.getCompressionRate(coreFramework, original));
		//System.out.println("rate gold: " + Measures.getCompressionRate(coreGold, original));
		//Measures.getF1(coreFramework, coreGold);
	}
	
	
	private static void generateFile(ArrayList<String> original, ArrayList<ArrayList<String>> coreFramework) throws IOException {
		
		
		FileWriter writer = new FileWriter("data/Wikipedia/Eval/Mandela/FrameworkNew");
		int counter = 0;
		for (String s : original) {
			//System.out.println(s);
			writer.write(s);
			writer.write("\n");
			String cores = "";
			
			
			for (String core : coreFramework.get(counter)) {
					
				cores = cores + " " + core;
			}
			
			
			cores = cores.trim();
			writer.write(cores);
			//System.out.println(cores);
			counter++;
			writer.write("\n");
			writer.write("\n");
			System.out.println();
		}
		writer.flush();
		writer.close();	
		
		
	}


	private static ArrayList<String> readNumbersToDelete(String file) throws IOException {
		File f = new File(file);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
	 
		ArrayList<String> l = new ArrayList<String>();
		String line = "";
		
		while ((line = br.readLine()) != null) {
			l.add(line);	
		}
		
		br.close();	 
		return l;	
	}
	
	
	public static ArrayList<ArrayList<String>> extractCore(String string, boolean delete) throws IOException {
		File file = new File (string);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
	 
		ArrayList<ArrayList<String>> l = new ArrayList<ArrayList<String>>();
		
		String line = "";
		
		
		int n = 0;
		
		while ((line = br.readLine()) != null) {
			
			if (line.startsWith("original sentence:")) {
				ArrayList<String> list = new ArrayList<String>();
				line = br.readLine();
				String s = "" + n;
			
				if (!delete) {	
					while (!line.isEmpty()) {
						if (line.startsWith("core sentence: ")) {
							line = line.replace("core sentence: ", "");
							if (!toDelete.contains(s) || !delete) {
								list.add(line);		
							} 
						}
						line = br.readLine();		
					}	
					l.add(n, list);	
					n++;
				} else {
					boolean keep = true;
					while (!line.isEmpty()) {
						
						if (line.startsWith("core sentence: ")) {
							line = line.replace("core sentence: ", "");
							
							/**
							for (String str : toDelete) {
								if (str.equals(s)) {
									
									keep = false;
								}
							}*/
							
							//if (keep) {
								
								list.add(line);
								
							//} else {
							//	System.out.println(line);
								
						//	}
						}
						line = br.readLine();
						
					}
					
						//System.out.println(list);
						l.add(n, list);
						n++;
					
				}
			}
		}
		
		br.close();	 
		
		
		if (delete) {
		int[] del = new int[toDelete.size()];
		
		int c2 = 0;
		for (String str : toDelete) {
			int c = Integer.parseInt(str);
			del[c2] = c;
			c2++;
		}
		for (int i = 0; i < del.length; i++) {
			//System.out.println(del[i]);
			l.set(del[i], null);
		}
		
		ArrayList<ArrayList<String>> newCores = new ArrayList<ArrayList<String>>();
		
		for (ArrayList<String> cores : l) {
			if (cores != null) {
				int counter = 0;
				for (String s : cores) {
					s = s.replaceAll("\\(.*?\\)", "");
					cores.set(counter, s);
					counter++;
				}
				
				
				newCores.add(cores);
			}
			
			//System.out.println(cores);
			
		}
		
		l = newCores;
		
		for (ArrayList<String> cores : newCores) {
			//System.out.println(cores);
		}
		}
		
		return l;
	}
	
	
	
	public static ArrayList<String> extractOriginal(String string, boolean delete) throws IOException {
		File file = new File (string);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
	 
		ArrayList<String> l = new ArrayList<String>();
		
		String line = "";
		int n = 0;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("original sentence:")) {
				String s = "" + n;
				boolean keep = true;
				for (String str : toDelete) {
					if (str.equals(s)) {
						
						keep = false;
					}
				}
				if (keep || !delete) {
					
					line = line.replace("original sentence: ", "");
					l.add(line);
				}
				n++;
			}
			
		}
		br.close();	 
		
		return l;
	}
	
}
