package sentenceCompression;

import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class AppositivePhraseExtractor {
	
	//private static final String singularPresent = " is ";
	//private static final String singularPast = " was ";
	//private static final String pluralPresent = " are ";
	//private static final String pluralPast = " were ";

	public static boolean extractVerbPhraseAppositives(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {

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
			
/**
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 3) {
					
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("S") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("NP")) {
							
							if (t.getChild(i).getChild(0).label().value().equals("VP") && (t.getChild(i+2).getChild(t.getChild(i+2).getChildrenAsList().size()-1).label().value().equals("NNP") || t.getChild(i+2).getChild(t.getChild(i+2).getChildrenAsList().size()-1).label().value().equals("NNPS"))) {
								//System.out.println("here");
								if (t.getChild(i).getChild(0).getChildrenAsList().size() >= 2) {
									if ((t.getChild(i).getChild(0).getChild(0).label().value().equals("VBN") && (t.getChild(i).getChild(0).getChild(1).label().value().equals("PP") || t.getChild(i).getChild(0).getChild(1).label().value().equals("NP"))) ||
										(t.getChild(i).getChild(0).getChild(0).label().value().equals("ADVP") && t.getChild(i).getChild(0).getChild(1).label().value().equals("VBN") && (t.getChild(i).getChild(0).getChild(2).label().value().equals("PP") || t.getChild(i).getChild(0).getChild(2).label().value().equals("NP")))) {
										//System.out.println("test");
										String aux = getAux(t.getChild(i+2).getChild(t.getChild(i+2).getChildrenAsList().size()-1).label().value());
										String phrase = Sentence.listToString(t.getChild(i+2).yield()) + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " , ";
										//System.out.println("VPapp: " + phrase);
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
						}
					}
				}
			}*/
			
			
			
			
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
	
	
	public static boolean extractNounPhraseAppositives(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 2) {
					if (t.getChild(0).label().value().equals("NP") && t.getChild(1).label().value().equals(",")) {
						String phrase = "This " + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
						String phraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield());
						
						SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
			}
		}
		
		return isSplit;
	}
	
	
	public static boolean extractAdjectivePhraseAppositives(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
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
								
								String phrase = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
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
	
	
	public static boolean extractAdverbialPhraseAppositives(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
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
	
	
	public static boolean extractProperNounPhraseAppositives(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("NP")) {
				if (t.getChildrenAsList().size() >= 4) {
					for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("NP") && t.getChild(i+3).label().value().equals(",")) {
							List<LabeledWord> label = t.getChild(i).labeledYield();
							
							if (label.get(label.size()-1).tag().value().equals("NNP") || label.get(label.size()-1).tag().value().equals("NNPS")) {
								String part1 = "";
								String aux = getAux(label.get(label.size()-1).tag().value());
								
								boolean isNPPP = false;
								if (t.getChild(i).getChildrenAsList().size() >=2) {
									if (t.getChild(i).getChild(0).label().value().equals("NP") && t.getChild(i).getChild(1).label().value().equals("PP")) {
										part1 = Sentence.listToString(t.getChild(i).getChild(1).getChild(1).yield());
										isNPPP = true;
									}
								}
								if (!isNPPP){
									part1 = Sentence.listToString(t.getChild(i).yield());
								}
								
								if (t.getChild(i+2).getChildrenAsList().size() >= 2) {
									List<LabeledWord> label2;
									if (t.getChild(i+2).getChild(0).label().value().equals("NP")) {
										label2 = t.getChild(i+2).getChild(0).labeledYield();
									} else {
										label2 = t.getChild(i+2).labeledYield();
									}
									if (!label2.get(label2.size()-1).tag().value().equals("NNP") && !label2.get(label2.size()-1).tag().value().equals("NNPS") && !label2.get(label2.size()-1).tag().value().equals("CD")) {
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
										//System.out.println("success1: " + phraseToDelete);
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
							else {
								if (!label.get(label.size()-1).tag().value().equals("CD")) {
									List<LabeledWord> label2;
									if (t.getChild(i+2).getChild(0).label().value().equals("NP")) {
										label2 = t.getChild(i+2).getChild(0).labeledYield();
									} else {
										label2 = t.getChild(i+2).labeledYield();
									}
									if (label2.get(label2.size()-1).tag().value().equals("NNP") || label2.get(label2.size()-1).tag().value().equals("NNPS")) {				
										String aux = getAux(label2.get(label2.size()-1).tag().value());
										String part1 = Sentence.listToString(t.getChild(i+2).yield());
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
										//System.out.println("success2");
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
						}
					}
				}
				
				if (t.getChildrenAsList().size() >= 3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("NP") && i==t.getChildrenAsList().size()-3) {
							List<LabeledWord> label = t.getChild(i).labeledYield();
							
							if (label.get(label.size()-1).tag().value().equals("NNP") || label.get(label.size()-1).tag().value().equals("NNPS")) {
								String part1 = "";
								String aux = getAux(label.get(label.size()-1).tag().value());
								
								boolean isNPPP = false;
								if (t.getChild(i).getChildrenAsList().size() >=2) {
									if (t.getChild(i).getChild(0).label().value().equals("NP") && t.getChild(i).getChild(1).label().value().equals("PP")) {
										part1 = Sentence.listToString(t.getChild(i).getChild(1).getChild(1).yield());
										isNPPP = true;
									}
								}
								if (!isNPPP){
									part1 = Sentence.listToString(t.getChild(i).yield());
								}
								
								if (t.getChild(i+2).getChildrenAsList().size() >= 2) {
									List<LabeledWord> label2;
									if (t.getChild(i+2).getChild(0).label().value().equals("NP")) {
										label2 = t.getChild(i+2).getChild(0).labeledYield();
		
									} else {
										label2 = t.getChild(i+2).labeledYield();
									}
									if (!label2.get(label2.size()-1).tag().value().equals("NNP") && !label2.get(label2.size()-1).tag().value().equals("NNPS") && !label2.get(label2.size()-1).tag().value().equals("CD")) {
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
							else {
								List<LabeledWord> label2;
								if (!label.get(label.size()-1).tag().value().equals("CD")) {
									if (t.getChild(i+2).getChild(0).label().value().equals("NP")) {
										label2 = t.getChild(i+2).getChild(0).labeledYield();
									} else {
										label2 = t.getChild(i+2).labeledYield();
									}
									if (label2.get(label2.size()-1).tag().value().equals("NNP") || label2.get(label2.size()-1).tag().value().equals("NNPS")) {
										String aux = getAux(label2.get(label2.size()-1).tag().value());	
										String part1 = Sentence.listToString(t.getChild(i+2).yield());
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
											
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
						}
					}
				}
				if (t.getChildrenAsList().size() >= 6) {
					for (int i = 0; i < t.getChildrenAsList().size()-5; i++) {

						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("NP") && t.getChild(i+3).label().value().equals("CC") && t.getChild(i+4).label().value().equals("NP") && t.getChild(i+5).label().value().equals(",")) {
							if (t.getChild(i+3).getChild(0).label().value().equals("and") || t.getChild(i+3).getChild(0).label().value().equals("or")) {
								
								List<LabeledWord> label = t.getChild(i).labeledYield();
								if (!label.get(label.size()-1).tag().value().equals("NNP") && !label.get(label.size()-1).tag().value().equals("NNPS")) {
									List<LabeledWord> label2 = t.getChild(i+2).labeledYield();
									List<LabeledWord> label3 = t.getChild(i+4).labeledYield();
									if ((label2.get(label2.size()-1).tag().value().equals("NNP") || label2.get(label2.size()-1).tag().value().equals("NNPS")) &&
											(label3.get(label3.size()-1).tag().value().equals("NNP") || label3.get(label3.size()-1).tag().value().equals("NNPS"))) {
										
										String aux = " are ";
										String part1 = Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield());
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield()) + " ,";
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								} else {
									List<LabeledWord> label2 = t.getChild(i+2).labeledYield();
									List<LabeledWord> label3 = t.getChild(i+4).labeledYield();
									if ((!label2.get(label2.size()-1).tag().value().equals("NNP") && !label2.get(label2.size()-1).tag().value().equals("NNPS")) &&
											(!label3.get(label3.size()-1).tag().value().equals("NNP") && !label3.get(label3.size()-1).tag().value().equals("NNPS"))) {
										
										String aux = getAux(label.get(label.size()-1).tag().value());
										String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield()) + " ,";
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
						}
						else if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("``") && t.getChild(i+3).label().value().equals("NP") && t.getChild(i+4).label().value().equals("''") && t.getChild(i+5).label().value().equals(",")) {
							List<LabeledWord> label1 = t.getChild(i).labeledYield();
							List<LabeledWord> label2 = t.getChild(i+3).labeledYield();
							if ((label1.get(label1.size()-1).tag().value().equals("NNP") || label1.get(label1.size()-1).tag().value().equals("NNPS")) &&  
									(label2.get(label2.size()-1).tag().value().equals("NNP") || label2.get(label2.size()-1).tag().value().equals("NNPS"))) {
								
								String aux = getAux(label2.get(label2.size()-1).tag().value());
								String phrase = Sentence.listToString(t.getChild(i+2).yield()) + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield()) + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
								String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield()) + " ,";
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
				if (t.getChildrenAsList().size() >= 5) {
					for (int i = 0; i < t.getChildrenAsList().size()-4; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("NP") && t.getChild(i+3).label().value().equals("CC") && t.getChild(i+4).label().value().equals("NP") && i == t.getChildrenAsList().size()-5) {
							if (t.getChild(i+3).getChild(0).label().value().equals("and") || t.getChild(i+3).getChild(0).label().value().equals("or")) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								if (!label.get(label.size()-1).tag().value().equals("NNP") && !label.get(label.size()-1).tag().value().equals("NNPS")) {
									List<LabeledWord> label2 = t.getChild(i+2).labeledYield();
									List<LabeledWord> label3 = t.getChild(i+4).labeledYield();
									if ((label2.get(label2.size()-1).tag().value().equals("NNP") || label2.get(label2.size()-1).tag().value().equals("NNPS")) &&
											(label3.get(label3.size()-1).tag().value().equals("NNP") || label3.get(label3.size()-1).tag().value().equals("NNPS"))) {
										
										String aux = " are ";
										String part1 = Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield());
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								} else {
									List<LabeledWord> label2 = t.getChild(i+2).labeledYield();
									List<LabeledWord> label3 = t.getChild(i+4).labeledYield();
									if ((!label2.get(label2.size()-1).tag().value().equals("NNP") && !label2.get(label2.size()-1).tag().value().equals("NNPS")) &&
											(!label3.get(label3.size()-1).tag().value().equals("NNP") && !label3.get(label3.size()-1).tag().value().equals("NNPS"))) {
										
										String aux = getAux(label.get(label.size()-1).tag().value());
										String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
						}
						else if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("``") && t.getChild(i+3).label().value().equals("NP") && t.getChild(i+4).label().value().equals("''") && i==t.getChildrenAsList().size()-5) {
							List<LabeledWord> label1 = t.getChild(i).labeledYield();
							List<LabeledWord> label2 = t.getChild(i+3).labeledYield();
							if ((label1.get(label1.size()-1).tag().value().equals("NNP") || label1.get(label1.size()-1).tag().value().equals("NNPS")) &&  
									(label2.get(label2.size()-1).tag().value().equals("NNP") || label2.get(label2.size()-1).tag().value().equals("NNPS"))) {
								String aux = getAux(label2.get(label2.size()-1).tag().value());
								String phrase = Sentence.listToString(t.getChild(i+2).yield()) + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield()) + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
								String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield());
								
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
	
	
	private static String getAux(String aux) {

		if (aux.equals("NNP") || aux.equals("NN")) {
			aux = " is ";
		} else {
			aux = " are ";
		}
		
		return aux;
	}

	
	private static boolean checkForEnum(Tree t, int n) {
		
		for (int i = n+1; i < t.getChildrenAsList().size(); i++) {
			if (t.getChild(i).label().value().equals("CC") && (t.getChild(i).getChild(0).label().value().equals("and") || t.getChild(i).getChild(0).label().value().equals("or"))) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	private static boolean isNNP(List<LabeledWord> label) {
		
		for (LabeledWord l : label) {
			if (!l.tag().value().equals("NNP") && !l.tag().value().equals("NNPS")) {
				return false;
			}
		}
		
		return true;
	}
	
	
	private static boolean isSingular(List<LabeledWord> label) {
		
		for (LabeledWord l : label) {
			if (!l.tag().value().equals("NN") && !l.tag().value().equals("NNP") && l.tag().value().equals("DT") && l.tag().value().equals("JJ")) {
				return false;
			}
		}
		
		return true;
	}
	
	
	private static boolean isPresent(Tree tree) {
		
		for (Tree t : tree) {
			if (t.label().value().equals("S")) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("VP")) {
						if (t.getChild(i+1).getChild(0).label().value().equals("VBP") || t.getChild(i+1).getChild(0).label().value().equals("VBZ")) {
							return true;
						}
					}
				}
				for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+2).label().value().equals("VP")) {
						if (t.getChild(i+2).getChild(0).label().value().equals("VBP") || t.getChild(i+2).getChild(0).label().value().equals("VBZ")) {
							return true;
						}
					}
				}
			}
			else if (t.label().value().equals("SINV")) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals("VP") && t.getChild(i+1).label().value().equals("NP")) {
						if (t.getChild(i).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(0).label().value().equals("VBZ")) {
							return true;
						}
					}
				}
				for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
					if (t.getChild(i).label().value().equals("VP") && t.getChild(i+2).label().value().equals("NP")) {
						if (t.getChild(i).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(0).label().value().equals("VBZ")) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	
	private static String setAux(boolean singular, boolean present) {
		String aux = "";
		
		if (singular && present) {
			aux = singularPresent;
		} else if (singular && !present) {
			aux = singularPast;
		} else if (!singular && present) {
			aux = pluralPresent;
		} else if (!singular && !present) {
			aux = pluralPast;
		}
		
		return aux;
	}
	*/
}
