package sentenceCompression;

import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class AdjectiveAdverbPhraseExtractor {
	
	public static boolean extractAdjectivePhrases(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux2 = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.getChildrenAsList().size() >= 5) {
				for (int i = 0; i < t.getChildrenAsList().size()-4; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",") && t.getChild(i+3).label().value().equals("ADJP") && t.getChild(i+4).label().value().equals(",")) {
						if (t.getChild(i+3).getChildrenAsList().size() >= 2) {
							int n = i+3;
							boolean isEnum = checkForEnum(t, n);
							
							if (!isEnum) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
								String aux = SentenceProcessor.setAux(isSingular, isPresent);
								
								String phrase = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + aux + Sentence.listToString(t.getChild(i+3).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield());
								
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
			if (t.getChildrenAsList().size() >= 4) {
				for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("ADJP") && t.getChild(i+3).label().value().equals(",")) {
						if (t.getChild(i+2).getChildrenAsList().size() >= 2) {
							int n = i+2;
							boolean isEnum = checkForEnum(t, n);
							if (!isEnum) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
								String aux = SentenceProcessor.setAux(isSingular, isPresent);
								
								String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
								
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
					else if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",") && t.getChild(i+3).label().value().equals("ADJP") && i == t.getChildrenAsList().size()-4) {
						if (t.getChild(i+3).getChildrenAsList().size() >= 2) {
							int n = i+3;
							boolean isEnum = checkForEnum(t, n);
							if (!isEnum) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
								String aux = SentenceProcessor.setAux(isSingular, isPresent);
								
								String phrase = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + aux + Sentence.listToString(t.getChild(i+3).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
								
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
			if (t.getChildrenAsList().size() >= 3) {
				for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("ADJP") && i == t.getChildrenAsList().size()-3) {
						if (t.getChild(i+2).getChildrenAsList().size() >= 2) {
							int n = i+2;
							boolean isEnum = checkForEnum(t, n);
							if (!isEnum) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
								String aux = SentenceProcessor.setAux(isSingular, isPresent);
								
								String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
								
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
			
			if (t.getChildrenAsList().size() >= 2) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals("S") && t.getChild(i+1).label().value().equals(",")) {
						if (t.getChild(i).getChild(0).label().value().equals("ADJP")) {
							String phrase = "This " + aux2 + " when being " + Sentence.listToString(t.getChild(i).yield()) + " .";
							String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield());
							
							
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(i).getChildrenAsList().size() >= 2) {
							if (t.getChild(i).getChild(0).label().value().equals("ADVP") && t.getChild(i).getChild(1).label().value().equals("ADJP")) {
								String phrase = "This " + aux2 + " when being " + Sentence.listToString(t.getChild(i).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield());
								
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
		}
		
		return isSplit;
	}
	
	
	public static boolean extractAdverbPhrases(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 2) {
					if (t.getChild(0).label().value().equals("ADVP") && t.getChild(1).label().value().equals(",")) {
						String phrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
						String phraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " ,";
						
						if (!(t.label().value().equals("S") && t.getChild(0).label().value().equals("NP") && t.getChild(1).label().value().equals("VP") && t.getChild(2).label().value().equals(".")
								&& t.getChild(1).getChild(1).label().value().equals("ADVP") && t.getChild(1).getChildrenAsList().size()==2)) {
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
			
			for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("ADVP") && t.getChild(i+2).label().value().equals(",")) {
					String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
					String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";
					
					SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
					isSplit = true;
				}
			}
			for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("ADVP") && i == t.getChildrenAsList().size()-2) {
					String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
					String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield());
					
					SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
					isSplit = true;
				}
			}
		}
		
		return isSplit;
	}
	
	
	private static boolean checkForEnum(Tree t, int n) {
		
		for (int i = n+1; i < t.getChildrenAsList().size(); i++) {
			if (t.getChild(i).label().value().equals("CC") && (t.getChild(i).getChild(0).label().value().equals("and") || t.getChild(i).getChild(0).label().value().equals("or"))) {
				return true;
			}
		}
		
		return false;
	}
}
