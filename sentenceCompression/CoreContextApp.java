package sentenceCompression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;

public class CoreContextApp {
	
	public static void main(String[] args) throws IOException {
		
		String input = "data/Wikipedia/Eval/Google/GoogleOutput";
		String output = "data/Wikipedia/Eval/Google/GoogleResult";
		
		ArrayList<CoreContextSentence> sen = new ArrayList<CoreContextSentence>();
		
		FileOperator fo = new FileOperator();
		ArrayList<String> sentences = new ArrayList<String>();
		
		try {
			sentences = fo.readFile(new File(input));
			
			File f = new File("data/Wikipedia/Eval/Google/GoogleParsed");
			PrintWriter pw = new PrintWriter(f);
			TreePrint print = new TreePrint("penn");
			int i = 0;
			
			for (String s : sentences) {
				Core.getStart().clear();
				Core.getEnd().clear();
				Core.getPrefix().clear();
				Core.getPostfix().clear();
				
				List<CoreLabel> tokens = SentenceProcessor.tokenize(s);
				Tree parse = SentenceProcessor.parse(tokens);
				
				CoreContextSentence sentence = new CoreContextSentence();
				sentence.setOriginal(parse);
				boolean[] delete = new boolean[tokens.size()];
				sentence.setDelete(delete);
				
				print.printTree(sentence.getOriginal(),pw); 
				
				
				System.out.println(i + " " + Sentence.listToString(sentence.getOriginal().yield()));
				i++;
				
				RelativeClauseExtractor.extractNonRestrictiveRelativeClauses(sentence, sentence.getOriginal(), true, -1);
				
				PrepositionalPhraseExtractor.extractInitialPPs(sentence, sentence.getOriginal(), true, -1);
				PrepositionalPhraseExtractor.extractInfixPPs(sentence, sentence.getOriginal(), true, -1);
				PrepositionalPhraseExtractor.extractFromTo(sentence, sentence.getOriginal(), true, -1);
				PrepositionalPhraseExtractor.extractAccording(sentence, sentence.getOriginal(), true, -1);
				PrepositionalPhraseExtractor.extractIncluding(sentence, sentence.getOriginal(), true, -1);
				PrepositionalPhraseExtractor.extractToDo(sentence, sentence.getOriginal(), true, -1);
				
				ParticipialPhraseExtractor.extractPresentParticiplesAfterNNP(sentence, sentence.getOriginal(), true, -1);
				ParticipialPhraseExtractor.extractPresentAndPastParticiples(sentence, sentence.getOriginal(), true, -1);
				
				InitialNounPhraseExtractor.extractInitialParentheticalNounPhrases(sentence, sentence.getOriginal(), true, -1);
				
				AdjectiveAdverbPhraseExtractor.extractAdjectivePhrases(sentence, sentence.getOriginal(), true, -1);
				AdjectiveAdverbPhraseExtractor.extractAdverbPhrases(sentence, sentence.getOriginal(), true, -1);
				
				AppositivePhraseExtractor.extractNonRestrictiveAppositives(sentence, sentence.getOriginal(), true, -1);
				AppositivePhraseExtractor.extractRestrictiveAppositives(sentence, sentence.getOriginal(), true, -1);
				
				ConjoinedPhrasesExtractor.infixWhenSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.infixAsSinceSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.infixCommaPPAfterBeforeSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.infixPPAfterBeforeSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.infixPPSAfterBeforeSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.infixSBARAfterBeforeSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.initialWhenSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.initialThoughAlthoughBecauseSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.infixBecauseThoughAlthoughSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.infixAndOrButSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.infixCommaAndOrButSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.ifSplit(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.extractWhilePlusParticiple(sentence, sentence.getOriginal(), true, -1);
				ConjoinedPhrasesExtractor.extractSo(sentence, sentence.getOriginal(), true, -1);
				
				Punctuation.splitAtColon(sentence, sentence.getOriginal(), true, -1);
				Punctuation.extractParentheses(sentence, sentence.getOriginal(), true, -1);
				Punctuation.removeBrackets(sentence, sentence.getOriginal(), true, -1);
				
				boolean isContextPruned = SentenceProcessor.pruneContextSentences(sentence);
				
				while (isContextPruned) {
					isContextPruned = SentenceProcessor.pruneContextSentences(sentence);
				}
				
				/**
				boolean isCorePruned = SentenceProcessor.pruneCoreSentences(sentence);
				//System.out.println(sentence.getCoreNew());
				
				int num = 0;
				while (isCorePruned && num < 20) {
					if (sentence.getCoreNew().size() > 0) {
						isCorePruned = SentenceProcessor.pruneCoreSentences(sentence);
						num++;
					}
				}
				
				ArrayList<String> coreSen = new ArrayList<String>();
				for (Tree tree : sentence.getCoreNew()) {
					String str = Sentence.listToString(tree.yield());
					coreSen.add(str);
				}
				
				int n = 0;
				
				for (String str : coreSen) {
					//System.out.println("to compare: " + str);
					for (int j = n+1; j < coreSen.size(); j++) {
						//System.out.println("compared: " + coreSen.get(j));
						if (str.equals(coreSen.get(j))) {
							sentence.getCoreNew().remove(n);
						}
					}
					n++;
				}
				*/
				
				ArrayList<String> contextSen = new ArrayList<String>();
				int v = 0;
				for (Tree tree : sentence.getContext()) {
					String str = Sentence.listToString(tree.yield());
					
					if (str.endsWith(", .")) {
						str = str.replace(", .", ".");
						sentence.getContext().set(v, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
					}
					if (str.endsWith(", . ''")) {
						str = str.replace(", . ''", ".");
						sentence.getContext().set(v, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
					}
					contextSen.add(str);
					//System.out.println(str);
					v++;
				}
				
				
				int m = 0;
				for (String str : contextSen) {
					
					for (int j = m+1; j < contextSen.size(); j++) {
						
						if (str.equals(contextSen.get(j))) {
							sentence.getContext().set(m, null);
						}
					}
					m++;
				}
				
				Tree original = sentence.getOriginal();
				String inputSentence = Sentence.listToString(original.yield());
				String[] tokensOriginal = inputSentence.split(" ");
				String core = "";
				boolean[] deleteCore = sentence.getDelete();
				
				
				if (!Core.getStart().isEmpty()) {
					
					int y = 0;
					for (Integer start : Core.getStart()) {
						//core = "";
						core = Core.getPrefix().get(y);
						for (int x = start; x <= Core.getEnd().get(y); x++) {
							
							if (deleteCore[x] == false) {
								core = core + " " + tokensOriginal[x];
								
							}
							
						}
						if (!core.isEmpty()) {		
							if (!core.matches(" .")) {
								//System.out.println(core);
									if (core.endsWith(" .")) {
										sentence.getCoreNew().add(SentenceProcessor.parse(SentenceProcessor.tokenize(core.trim())));
									} else if (core.endsWith(",")) {
										core = core.replace(" ,", "");
										sentence.getCoreNew().add(SentenceProcessor.parse(SentenceProcessor.tokenize(core.trim() + " .")));
									} else {
										sentence.getCoreNew().add(SentenceProcessor.parse(SentenceProcessor.tokenize(core.trim() + " .")));
									}
								
							}			
						}
						
								
						int[] setToFalse = new int[2];
						setToFalse[0] = start;
						setToFalse[1] = Core.getEnd().get(y) + 1;
						SentenceProcessor.updateDelete(setToFalse, sentence);
						
						y++;
					}
				}
				SentenceProcessor.produceCore(sentence.getDelete(), sentence);
				
				sen.add(sentence);
			}
			
			fo.writeFile(sen, new File(output));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
