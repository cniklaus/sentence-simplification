package segmentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import transformation.Transformer;

public class SentenceSeparator {
	
	public static void main(String[] args) throws IOException {
		String input = args[0]; //"data/Wikipedia/Eval/baseball/bugs";
		String output = args[1]; //"data/Wikipedia/Eval/baseball/baseballResultBugs";
		
		ArrayList<String> sen = splitIntoSentences(new File(input));
		
		Transformer.simplify(sen, output);
		
	}
	
	
	public static ArrayList<String> splitIntoSentences(File f) throws FileNotFoundException {
		
			String sentences = new String();
			
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
				  
			try {
				String line = "";
				while ((line = br.readLine()) != null ) {
					sentences = sentences.concat(line) + " ";
				}	
				br.close();
						
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

			Annotation document = new Annotation(sentences);

			pipeline.annotate(document);
			
			List<CoreMap> s = document.get(SentencesAnnotation.class);
		
			ArrayList<String> sen = new ArrayList<String>();
			
			for (CoreMap c : s) {
				 int counter = 0;
				 for (@SuppressWarnings("unused") CoreLabel token: c.get(TokensAnnotation.class)) {
					 counter++;      
				 }
				 if (c != null && counter < 100) {
					 String input = c.toString();
					 input = input.replaceAll( "\\[(.*?)\\]", "");
					 input = input.trim();
					 if (!input.isEmpty()) {
						sen.add(input); 
					 } 
				 }
			}
			
			return sen;
	}

}
