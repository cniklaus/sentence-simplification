package evaluation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

public class Measures {
	public static double getCompressionRate(ArrayList<ArrayList<String>> compressed, ArrayList<String> original) {
		
		double[] rates = new double[original.size()];
		double compressionRate = 0.0;
		
		int counter = 0;
		for (String s : original) {
			System.out.println("original: " + s);
			
			int numberTokensOriginal = 0;
			String[] tokensOriginal = s.split(" ");
			numberTokensOriginal = numberTokensOriginal + tokensOriginal.length;
			System.out.println(numberTokensOriginal);
			
			String cores = "";
			for (String core : compressed.get(counter)) {
				cores = cores + " " + core;
			}
			cores = cores.trim();
			int numberTokensCompressed = 0;
			String[] comTokens = cores.split(" ");
			numberTokensCompressed = numberTokensCompressed + comTokens.length;
			
			double rate = (double) numberTokensCompressed / numberTokensOriginal;
			
			rates[counter] = rate; 
			
			System.out.println("cores: " + cores);
			System.out.println(numberTokensCompressed);
			System.out.println("rate: " + rate);
			counter++;
			
			System.out.println();
		}
		
		double sum = 0.0;
		for (int i = 0; i < rates.length; i++) {
			sum = sum + rates[i];
		}
		System.out.println("sum " + sum);
		System.out.println("size: " + original.size());
		
		compressionRate = sum / original.size();
		
		return compressionRate;
		
	}
	
	
	public static void getF1(ArrayList<ArrayList<String>> coreFramework, ArrayList<ArrayList<String>> coreGold) throws IOException {
		double score = 0.0;
		
		FileWriter writer = new FileWriter("data/Wikipedia/Eval/Mandela/Comp", false);
		
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		List<CoreLabel> rawWordsFramework = null;
		List<CoreLabel> rawWordsGold = null;
		List<CoreLabel> rawWordsGoldForComparison = null;
				
		double recallAcc = 0.0;
		double precisionAcc = 0.0;
			
		int i = 0;
		for (ArrayList<String> sentences : coreFramework) {
				
			double recall = 0.0;
			double precision = 0.0;
			int truePositives = 0;
			int falsePositives = 0;
			int falseNegatives = 0;
				
			String framework = "";
			for (String s : sentences) {
				framework = framework.concat(" " + s);
			}
			System.out.println("framework: " + framework);
				
			Tokenizer<CoreLabel> tokFramework = tokenizerFactory.getTokenizer(new StringReader(framework));
			rawWordsFramework = tokFramework.tokenize();
				
			ArrayList<String> g = coreGold.get(i);
			String gold = "";
			for (String s : g) {
				gold = gold.concat(" " + s);
			}
				
			System.out.println("gold: " + gold );
			
			writer.write("framework: " + framework + "\n");
			writer.write("gold: " + gold + "\n");
			
			Tokenizer<CoreLabel> tokGold = tokenizerFactory.getTokenizer(new StringReader(gold));
			rawWordsGold = tokGold.tokenize();
			rawWordsGoldForComparison = rawWordsGold;
				
			for (CoreLabel l : rawWordsFramework) {
				boolean var = false;
				//System.out.println("l " + l);
				for (CoreLabel k : rawWordsGoldForComparison) {
					//System.out.println("all: " + rawWordsGold);
					//System.out.println("k " + k);
					if (l.value().equalsIgnoreCase(k.value())) {
						truePositives++;
						rawWordsGoldForComparison.remove(k);
						//System.out.println("removed: " + rawWordsGold);
						var = true;
						break;
					}
				}
				if (var == false) {
					falsePositives++;
				}
			}
					 
			for (CoreLabel l : rawWordsGold) {
				boolean var = false;
				for (CoreLabel k : rawWordsFramework) {
					if (l.value().equalsIgnoreCase(k.value())) {
						var = true;
						break;
					}
				}
				if (var == false) {
					falseNegatives++;
				}
			}
			
			
			System.out.println("sentence " + i + ": ");
			System.out.println("falsePositives: " + falsePositives);
			System.out.println("falseNegatives: " + falseNegatives);
			System.out.println("truePositives: " + truePositives);
			
			
			//System.out.println("\n");
			
			precision = (double) truePositives / (truePositives + falsePositives);
			recall = (double) truePositives / (truePositives + falseNegatives);
			
			recallAcc =  recallAcc + recall;
			precisionAcc =  precisionAcc + precision;
			
			System.out.println("recallAcc: " + recallAcc);
			System.out.println("precisionAcc: " + precisionAcc );
			
			writer.write("recallAcc: " + recallAcc + "\n");
			writer.write("precisionAcc: " + precisionAcc + "\n");
			
			i++;
		}
		
		writer.flush();
		writer.close();
		
		i++;
		
		score = (double) 2 * (recallAcc/i) * (precisionAcc/i) / (precisionAcc/i + recallAcc/i);
		
		System.out.println("F1 score: " + score);
		System.out.println("precision:" + precisionAcc/i);
		System.out.println("recall:" + recallAcc/i);
			
	}
	  
	
}
