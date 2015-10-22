package sentenceCompression;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class ProperNounsExtractor {

	public static boolean extractAdjectivesNounsNNPs(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
			
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
				
		for (Tree t : parse) {	
			if (t.label().value().equals("NP")) {
				if (t.getChildrenAsList().size()>=3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {		
						if (t.getChild(i).label().value().equals("NN") && t.getChild(i+1).label().value().equals("NNP") && t.getChild(i+2).label().value().equals("NNP")) {
								
							String adjectiveNoun = Sentence.listToString(t.getChild(i).yield());
							String nnp = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
							
							int j = i+3;	
							while (j < t.getChildrenAsList().size() && t.getChild(j).label().value().equals("NNP")) {
								nnp = nnp + " " + Sentence.listToString(t.getChild(j).yield());
								j++;
							}
								
							int k = i-1;
							while (k >= 0 && t.getChild(k).label().value().equals("NN")) {
								adjectiveNoun = Sentence.listToString(t.getChild(k).yield()) + " " + adjectiveNoun;
								k--;
							}
								
							int n = k;
							while (n >= 0 && t.getChild(n).label().value().equals("JJ")) {
								adjectiveNoun = Sentence.listToString(t.getChild(n).yield()) + " " + adjectiveNoun;
								n--;
							}
								
							String rest = adjectiveNoun;
							int m = n;
							boolean isDetOrPronoun;
							
							if (m < 0) {
								isDetOrPronoun = false;
							} else {
								isDetOrPronoun = true;
							}
							 
							while (m >= 0) {
								rest = Sentence.listToString(t.getChild(m).yield()) + " " + rest;
								//System.out.println(t.getChild(m).label().value());
								if (!t.getChild(m).label().value().equals("DT") && !t.getChild(m).label().value().equals("PRP$")) {
									isDetOrPronoun = false;
								}
								m--;
							}
								
							//System.out.println("nnp: " + nnp);
							//System.out.println("adjectiveNoun: " + adjectiveNoun);
							//System.out.println("rest: " + rest);
							String det = "";
							if (!isDetOrPronoun) {
								det = " a ";
							}
							
							String phrase = nnp + " is " + det + " " + rest + " .";
							String phraseToDelete = rest;
								
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
				
			if (t.getChildrenAsList().size()>=2) {
				if (t.getChild(0).label().value().equals("NP") && t.getChild(1).label().value().equals("NP")) {
					boolean npCombination = true;
					for (int i = 0; i < t.getChild(1).getChildrenAsList().size(); i++) {
						if (!t.getChild(1).getChild(i).label().value().equals("NNP")) {
							npCombination = false;
						}
					}
					if (npCombination == true) {
						String phrase = Sentence.listToString(t.getChild(1).yield()) + " is " + Sentence.listToString(t.getChild(0).yield()) + " .";
						String phraseToDelete = Sentence.listToString(t.getChild(0).yield());
							
						SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
			}
		}
		
		return isSplit;
	}
		
}
