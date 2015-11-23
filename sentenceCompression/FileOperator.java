package sentenceCompression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class FileOperator {
	
	
	public ArrayList<String> readFile(File file) throws FileNotFoundException {
		
		ArrayList<String> sentences = new ArrayList<String>();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
			  
		try {
			 String line = "";
			 
			 while ((line = br.readLine()) != null ) {
				 sentences.add(line);
			 }
					
			 br.close();
					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sentences;
	}
	
	
	public void writeFile(ArrayList<CoreContextSentence> sentences, File file) throws IOException {
		
		FileWriter writer = new FileWriter(file);
		
				
		for (CoreContextSentence s : sentences) {
			
			try {
			
				writer.write("original sentence: " + Sentence.listToString(s.getOriginal().yield()) + "\n");
				
				for (Tree t : s.getCore()) {
					if (!Sentence.listToString(t.yield()).equals("If")) {
						writer.write("core sentence: " + Sentence.listToString(t.yield()) + "\n");
					}
					
				}
				Collections.reverse(s.getCoreNew());
				for (Tree t : s.getCoreNew()) {
					writer.write("core sentence: " + Sentence.listToString(t.yield()) + "\n");
				}
				
				for (Tree t : s.getContext()) {
					if (t != null ) {
						
						writer.write("context sentence: " + Sentence.listToString(t.yield()) + "\n");
						
						
					}
					
				}
				
				writer.write("\n");
				writer.write("\n");
								
			} catch (IOException e) {
				e.printStackTrace();
			}	 
		}
		writer.flush();
		writer.close();	
	}
		

}
