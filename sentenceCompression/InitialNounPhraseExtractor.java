package sentenceCompression;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class InitialNounPhraseExtractor {
	
	public static boolean extractInitialParentheticalNounPhrases(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 2) {
					if (t.getChild(0).label().value().equals("NP") && t.getChild(1).label().value().equals(",")) {
						if (t.getChildrenAsList().size() >= 5) {
							if (! (t.getChild(2).label().value().equals("S") && t.getChild(3).label().value().equals(",") && t.getChild(4).label().value().equals("VP"))) {
								String phrase = "This " + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield());
							
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
						else {
							String phrase = "This " + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
							String phraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield());
						
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
				if (t.getChildrenAsList().size() >= 3) {
					if (t.getChild(0).label().value().equals("ADVP") && t.getChild(1).label().value().equals("NP") && t.getChild(2).label().value().equals(",")) {
						String phrase = "This " + aux + Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " .";
						String phraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " " + Sentence.listToString(t.getChild(2).yield());
						
						SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
 			}
		}
		
		return isSplit;
	}
	

}
