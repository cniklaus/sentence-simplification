package sentenceCompression;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class ParticipialPhraseExtractor {
	
	public static boolean extractPresentAndPastParticiples(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {

		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		
		for (Tree t : parse) {
			if (t.label().value().equals("NP")) {
				if (t.getChildrenAsList().size() >= 3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("VP") && i == t.getChildrenAsList().size()-3) {
							if (t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NN") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNS") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNPS")) {
								if (t.getChild(i+2).getChild(0).label().value().equals("VBN") || (t.getChild(i+2).getChild(0).label().value().equals("ADVP") && t.getChild(i+2).getChild(1).label().value().equals("VBN"))) {
									//System.out.println("success1");
									String aux = getAux(t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value());
									String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
									String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
									
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
						}	
					}
				}
					
				if (t.getChildrenAsList().size() >= 4) {
					for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("VP") && t.getChild(i+3).label().value().equals(",")) {
							if (t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NN") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNS") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNPS")) {
								if (t.getChild(i+2).getChild(0).label().value().equals("VBN") || (t.getChild(i+2).getChild(0).label().value().equals("ADVP") && t.getChild(i+2).getChild(1).label().value().equals("VBN"))) {
									//System.out.println("success2");
									String aux = getAux(t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value());
									//System.out.println(t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value());
									String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
									String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
										
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
							
						}
						else if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",") && t.getChild(i+3).label().value().equals("VP") && i == t.getChildrenAsList().size()-5) {
							if (t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NN") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNS") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNPS")) {
								if (t.getChild(i+3).getChild(0).label().value().equals("VBN") || (t.getChild(i+3).getChild(0).label().value().equals("ADVP") && t.getChild(i+3).getChild(1).label().value().equals("VBN"))) {
									//System.out.println("success3");
									String aux = getAux(t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value());
									String phrase = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
									String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+3).yield());
								
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
						}
					}
				}
					
				if (t.getChildrenAsList().size() >= 5) {
					for (int i = 0; i < t.getChildrenAsList().size()-4; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",") && t.getChild(i+3).label().value().equals("VP") && t.getChild(i+4).label().value().equals(",")) {
							if (t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NN") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNS") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNPS")) {
								if (t.getChild(i+3).getChild(0).label().value().equals("VBN") || (t.getChild(i+3).getChild(0).label().value().equals("ADVP") && t.getChild(i+3).getChild(1).label().value().equals("VBN"))) {
									//System.out.println("success4");
									String aux = getAux(t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value());
									String phrase = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + aux + Sentence.listToString(t.getChild(i+3).yield()) + " .";
									String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+3).yield()) + " ,";
									
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
							
						}
					}
				}	
			}
			
			
			for (int i = 0; i < t.getChildrenAsList().size(); i++) {
				if (t.getChild(i).label().value().equals("S") && i == t.getChildrenAsList().size()-1 && !t.getChild(i).ancestor(1, parse).label().value().equals("SBAR") && !t.getChild(i).ancestor(1, parse).label().value().equals("PP")) {
					if (t.getChild(i).getChild(0).label().value().equals("VP") && (t.getChild(i).getChild(0).getChild(0).label().value().equals("VBN") || t.getChild(i).getChild(0).getChild(0).label().value().equals("VBG"))) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						String phrase = "";
						if (t.getChild(i).getChild(0).getChild(0).label().value().equals("VBN")) {
							phrase = "This" + aux + "when being " + Sentence.listToString(t.getChild(i).yield()) + " .";
						} else {
							phrase = "This" + aux + "when " + Sentence.listToString(t.getChild(i).yield()) + " .";
						}
						String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
						
						String[] tokensToDelete = phraseToDelete.split(" ");
						String[] tokens = sentence.split(" ");
						
						int tokensToDeleteCount = tokensToDelete.length;
						int tokensCount = tokens.length;
						
						if (tokensCount - tokensToDeleteCount > 4) {
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
					if (t.getChild(i).getChild(0).label().value().equals("VP") && (t.getChild(i).getChild(0).getChild(0).label().value().equals("ADVP") && (t.getChild(i).getChild(0).getChild(1).label().value().equals("VBN") || t.getChild(i).getChild(0).getChild(1).label().value().equals("VBG")))) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						String phrase = "";
						if (t.getChild(i).getChild(0).getChild(1).label().value().equals("VBN")) {
							phrase = "This" + aux + "when being " + Sentence.listToString(t.getChild(i).yield()) + " .";
						} else {
							phrase = "This" + aux + "when " + Sentence.listToString(t.getChild(i).yield()) + " .";
						}
						String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
						String[] tokensToDelete = phraseToDelete.split(" ");
						String[] tokens = sentence.split(" ");
						
						int tokensToDeleteCount = tokensToDelete.length;
						int tokensCount = tokens.length;
						
						if (tokensCount - tokensToDeleteCount > 4) {
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
			
			
			for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
				if (t.getChild(i).label().value().equals("S") && t.getChild(i+1).label().value().equals(",") && !t.getChild(i).ancestor(1, parse).label().value().equals("SBAR") && !t.getChild(i).ancestor(1, parse).label().value().equals("PP")) {
					if (t.getChild(i).getChild(0).label().value().equals("VP") && (t.getChild(i).getChild(0).getChild(0).label().value().equals("VBN") || t.getChild(i).getChild(0).getChild(0).label().value().equals("VBG"))) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						String phrase = "";
						if (t.getChild(i).getChild(0).getChild(0).label().value().equals("VBN")) {
							phrase = "This" + aux + "when being " + Sentence.listToString(t.getChild(i).yield()) + " .";
						} else {
							phrase = "This" + aux + "when " + Sentence.listToString(t.getChild(i).yield()) + " .";
						}
						String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " , ";
						String[] tokensToDelete = phraseToDelete.split(" ");
						String[] tokens = sentence.split(" ");
						
						int tokensToDeleteCount = tokensToDelete.length;
						int tokensCount = tokens.length;
						
						if (tokensCount - tokensToDeleteCount > 4) {
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
					if (t.getChild(i).getChild(0).label().value().equals("VP") && t.getChild(i).getChild(0).getChild(0).label().value().equals("ADVP") && (t.getChild(i).getChild(0).getChild(1).label().value().equals("VBN") || t.getChild(i).getChild(0).getChild(1).label().value().equals("VBG"))) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						String phrase = "";
						if (t.getChild(i).getChild(0).getChild(1).label().value().equals("VBN")) {
							phrase = "This" + aux + "when being " + Sentence.listToString(t.getChild(i).yield()) + " .";
						} else {
							phrase = "This" + aux + "when " + Sentence.listToString(t.getChild(i).yield()) + " .";
						}
						String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " , ";
						String[] tokensToDelete = phraseToDelete.split(" ");
						String[] tokens = sentence.split(" ");
						
						int tokensToDeleteCount = tokensToDelete.length;
						int tokensCount = tokens.length;
						
						if (tokensCount - tokensToDeleteCount > 4) {
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
			
			if (t.label().value().equals("S") && !t.ancestor(1, parse).label().value().equals("SBAR") && !t.ancestor(1, parse).label().value().equals("PP")) {
				if (t.getChild(0).label().value().equals("VP")) {
					if (t.getChild(0).getChildrenAsList().size() >= 3) {
						if (t.getChild(0).getChild(0).label().value().equals("VP") && t.getChild(0).getChild(1).label().value().equals("CC") && t.getChild(0).getChild(2).label().value().equals("VP")) {
							if ((t.getChild(0).getChild(0).getChild(0).label().value().equals("VBG") || t.getChild(0).getChild(0).getChild(1).label().value().equals("VBG")) && (t.getChild(0).getChild(2).getChild(0).label().value().equals("VBG") || t.getChild(0).getChild(2).getChild(1).label().value().equals("VBG"))) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This" + aux + "when " + Sentence.listToString(t.yield()) + " .";
								
								String phraseToDelete = Sentence.listToString(t.yield());
								String[] tokensToDelete = phraseToDelete.split(" ");
								String[] tokens = sentence.split(" ");
								
								int tokensToDeleteCount = tokensToDelete.length;
								int tokensCount = tokens.length;
								
								if (tokensCount - tokensToDeleteCount > 4) {
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
							if ((t.getChild(0).getChild(0).getChild(0).label().value().equals("VBN") || t.getChild(0).getChild(0).getChild(1).label().value().equals("VBN")) && (t.getChild(0).getChild(2).getChild(0).label().value().equals("VBN") || t.getChild(0).getChild(2).getChild(1).label().value().equals("VBN"))) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This" + aux + "when being " + Sentence.listToString(t.yield()) + " .";
								
								String phraseToDelete = Sentence.listToString(t.yield());
								String[] tokensToDelete = phraseToDelete.split(" ");
								String[] tokens = sentence.split(" ");
								
								int tokensToDeleteCount = tokensToDelete.length;
								int tokensCount = tokens.length;
								
								if (tokensCount - tokensToDeleteCount > 5) {
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
						}
					}
					if (t.getChild(0).getChildrenAsList().size() >= 4) {
						if (t.getChild(0).getChild(0).label().value().equals("VP") && t.getChild(0).getChild(1).label().value().equals(",") && t.getChild(0).getChild(2).label().value().equals("CC") && t.getChild(0).getChild(3).label().value().equals("VP")) {
							if ((t.getChild(0).getChild(0).getChild(0).label().value().equals("VBG") || t.getChild(0).getChild(0).getChild(1).label().value().equals("VBG")) && (t.getChild(0).getChild(3).getChild(0).label().value().equals("VBG") || t.getChild(0).getChild(3).getChild(1).label().value().equals("VBG"))) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This" + aux + "when " + Sentence.listToString(t.yield()) + " .";
								
								String phraseToDelete = Sentence.listToString(t.yield());
								String[] tokensToDelete = phraseToDelete.split(" ");
								String[] tokens = sentence.split(" ");
								
								int tokensToDeleteCount = tokensToDelete.length;
								int tokensCount = tokens.length;
								
								if (tokensCount - tokensToDeleteCount > 4) {
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
							if ((t.getChild(0).getChild(0).getChild(0).label().value().equals("VBN") || t.getChild(0).getChild(0).getChild(1).label().value().equals("VBN")) && (t.getChild(0).getChild(3).getChild(0).label().value().equals("VBN") || t.getChild(0).getChild(3).getChild(1).label().value().equals("VBN"))) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This" + aux + "when being " + Sentence.listToString(t.yield()) + " .";
								
								String phraseToDelete = Sentence.listToString(t.yield());
								String[] tokensToDelete = phraseToDelete.split(" ");
								String[] tokens = sentence.split(" ");
								
								int tokensToDeleteCount = tokensToDelete.length;
								int tokensCount = tokens.length;
								
								if (tokensCount - tokensToDeleteCount > 5) {
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
						}
					}
 				}
			}
		}

		return isSplit;
	}
	
	
	public static boolean extractPresentParticiplesAfterNNP(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
				if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && (t.getChild(i+2).label().value().equals("S")) && t.getChild(i+3).label().value().equals(",")) {
					if (t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNPS")) {
						if (t.getChild(i+2).getChild(0).label().value().equals("VP")) {
							if (t.getChild(i+2).getChild(0).getChild(0).label().value().equals("VBG") || (t.getChild(i+2).getChild(0).getChild(0).label().value().equals("ADVP") && t.getChild(i+2).getChild(0).getChild(1).label().value().equals("VBG"))) {
								String gerundPhrase = Sentence.listToString(t.getChild(i).yield()) + " is " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
								String gerundPhraseToDelete = " , " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
								
								SentenceProcessor.updateSentence(gerundPhrase, gerundPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
				
				else if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && (t.getChild(i+2).label().value().equals("VP")) && t.getChild(i+3).label().value().equals(",")) {
					if (t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNPS")) {
						
						if (t.getChild(i+2).getChild(0).label().value().equals("VBG") || (t.getChild(i+2).getChild(0).label().value().equals("ADVP") && t.getChild(i+2).getChild(1).label().value().equals("VBG"))) {
							String gerundPhrase = Sentence.listToString(t.getChild(i).yield()) + " is " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
							String gerundPhraseToDelete = " , " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
							
							SentenceProcessor.updateSentence(gerundPhrase, gerundPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
			
			for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
				if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("S") && i == t.getChildrenAsList().size()-3) {
					if (t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNPS")) {
						if (t.getChild(i+2).getChild(0).label().value().equals("VP")) {
							if (t.getChild(i+2).getChild(0).getChild(0).label().value().equals("VBG") || (t.getChild(i+2).getChild(0).getChild(0).label().value().equals("ADVP") && t.getChild(i+2).getChild(0).getChild(1).label().value().equals("VBG"))) {
								String gerundPhrase = Sentence.listToString(t.getChild(i).yield()) + " is " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
								String gerundPhraseToDelete = " , " + Sentence.listToString(t.getChild(i+2).yield());
								
								SentenceProcessor.updateSentence(gerundPhrase, gerundPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
				
				else if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("VP") && i == t.getChildrenAsList().size()-3) {
					if (t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i).getChild(t.getChild(i).getChildrenAsList().size()-1).label().value().equals("NNPS")) {
						
						if (t.getChild(i+2).getChild(0).label().value().equals("VBG") || (t.getChild(i+2).getChild(0).label().value().equals("ADVP") && t.getChild(i+2).getChild(1).label().value().equals("VBG"))) {
							String gerundPhrase = Sentence.listToString(t.getChild(i).yield()) + " is " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
							String gerundPhraseToDelete = " , " + Sentence.listToString(t.getChild(i+2).yield());
							
							SentenceProcessor.updateSentence(gerundPhrase, gerundPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
		}
		
		return isSplit;
	}
	

	private static String getAux(String aux) {

		if (aux.equals("NNP") || aux.equals("NN")) {
			aux = " is ";
		} else {
			aux = " are ";
		}
		
		return aux;
	}
}
