package sentenceCompression;

import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class PrepositionalPhraseExtractor {

	public static boolean extractInitialPPs(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
	 	
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		
		for (Tree t : parse) {
			
			if (t.label().value().equals("S")) {
				
				if (t.getChild(0).label().value().equals("PP")) {
					if (t.getChild(0).getChild(1).label().value().equals("TO") || t.getChild(0).getChild(1).label().value().equals("IN") || t.getChild(0).getChild(0).label().value().equals("TO") || t.getChild(0).getChild(0).label().value().equals("IN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
						String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield());
						
						SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
				
				if (t.getChildrenAsList().size() >= 2) {
					
					for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
						if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP")) {
							if (t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
								String PPphraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
								
								SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
					
					if (t.getChild(0).label().value().equals("PP") && t.getChild(1).label().value().equals(",")) {
						if (t.getChild(0).getChild(1).label().value().equals("TO") || t.getChild(0).getChild(1).label().value().equals("IN") || t.getChild(0).getChild(0).label().value().equals("TO") || t.getChild(0).getChild(0).label().value().equals("IN")) {
							String aux = SentenceProcessor.setAux(true, isPresent);
							String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
							String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
							
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(0).getChild(0).label().value().equals("VBN") || t.getChild(0).getChild(0).label().value().equals("VBG")) {
							String aux = SentenceProcessor.setAux(true, isPresent);
							String PPphrase = "This" + aux + "when " + Sentence.listToString(t.getChild(0).yield()) + " .";
							String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
							
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(0).getChild(0).label().value().equals("ADVP") && (t.getChild(0).getChild(1).label().value().equals("VBN") || t.getChild(0).getChild(1).label().value().equals("VBG"))) {
							String aux = SentenceProcessor.setAux(true, isPresent);
							String PPphrase = "This" + aux + "when " + Sentence.listToString(t.getChild(0).yield()) + " .";
							String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
							
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
				
				if (t.getChildrenAsList().size() >= 3) {
					if (t.getChild(0).label().value().equals("PP") && t.getChild(1).label().value().equals("NP") && t.getChild(2).label().value().equals(",")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " .";
						String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " , ";
						
						SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
					
					if (t.getChild(0).label().value().equals("S") && t.getChild(1).label().value().equals(",") && t.getChild(2).label().value().equals("NP")) {
						if (t.getChild(0).getChild(0).label().value().equals("NP")) {
							List<LabeledWord> label = t.getChild(2).labeledYield();
							boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
							String aux = SentenceProcessor.setAux(isSingular, isPresent);
							
							String PPphrase = Sentence.listToString(t.getChild(2).yield()) + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
							String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
							
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
				
				if (t.getChildrenAsList().size() >= 2) {
					if (t.getChild(0).label().value().equals("SBAR") && t.getChild(1).label().value().equals(",")) {
						if (t.getChild(0).getChild(0).label().value().equals("IN") && t.getChild(0).getChild(1).label().value().equals("S")) {
							if (t.getChild(0).getChild(0).getChild(0).label().value().equals("As") || t.getChild(0).getChild(0).getChild(0).label().value().equals("Before") || t.getChild(0).getChild(0).getChild(0).label().value().equals("After") || t.getChild(0).getChild(0).getChild(0).label().value().equals("Since") || t.getChild(0).getChild(0).getChild(0).label().value().equals("While")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								
								String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
								String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
								
								SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
						
						else if (t.getChild(0).getChild(1).label().value().equals("IN") && t.getChild(0).getChild(2).label().value().equals("S")) {
							if (t.getChild(0).getChild(1).getChild(0).label().value().equals("As") || t.getChild(0).getChild(1).getChild(0).label().value().equals("Before") || t.getChild(0).getChild(1).getChild(0).label().value().equals("After") || t.getChild(0).getChild(1).getChild(0).label().value().equals("Since") || t.getChild(0).getChild(1).getChild(0).label().value().equals("While")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								
								String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
								String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
								
								SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
				
				if (t.getChildrenAsList().size() >= 3) {
					if (t.getChild(0).label().value().equals("ADVP") && t.getChild(1).label().value().equals("SBAR") && t.getChild(2).label().value().equals(",")) {
						if (t.getChild(1).getChild(0).label().value().equals("IN") && t.getChild(1).getChild(1).label().value().equals("S")) {
							if (t.getChild(1).getChild(0).getChild(0).label().value().equals("as") || t.getChild(1).getChild(0).getChild(0).label().value().equals("before") || t.getChild(1).getChild(0).getChild(0).label().value().equals("after") || t.getChild(1).getChild(0).getChild(0).label().value().equals("since") || t.getChild(1).getChild(0).getChild(0).label().value().equals("while")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								
								String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " .";
								String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " , ";
								
								SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}	
		}
		
		return isSplit;
	}
	
	
	public static boolean extractInfixPPs(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		 	
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
			
		for (Tree t : parse) {
			for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals("NP") && t.getChild(i+3).label().value().equals(",")) {
					if (t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("VBN") ||
							t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
						
						SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
			}
			for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",")) {
					if (t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("VBN") ||
							t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";
						
						SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR") && t.getChild(i+2).label().value().equals(",")) {
					if ((t.getChild(i+1).getChild(0).label().value().equals("IN") && t.getChild(i+1).getChild(1).label().value().equals("S") &&
							(t.getChild(i+1).getChild(0).getChild(0).label().value().equals("before") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("after") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("as") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("while"))) ||
							(t.getChild(i+1).getChild(1).label().value().equals("IN") && t.getChild(i+1).getChild(2).label().value().equals("S") &&
							(t.getChild(i+1).getChild(1).getChild(0).label().value().equals("before") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("after") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("as") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("while")))) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";

						SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
					
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals("NP") && i == t.getChildrenAsList().size()-3) {
					if (t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("VBN") ||
							t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
							
						SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
			}
				
			for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && i == t.getChildrenAsList().size()-2) {
					if (t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("VBN") ||
							t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield());
							
						SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
					
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR") && i == t.getChildrenAsList().size()-2) {
					if ((t.getChild(i+1).getChild(0).label().value().equals("IN") && t.getChild(i+1).getChild(1).label().value().equals("S") &&
							(t.getChild(i+1).getChild(0).getChild(0).label().value().equals("before") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("after") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("as") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("while"))) ||
							(t.getChild(i+1).getChild(1).label().value().equals("IN") && t.getChild(i+1).getChild(2).label().value().equals("S") &&
							(t.getChild(i+1).getChild(1).getChild(0).label().value().equals("before") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("after") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("as") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("while")))) {
						//System.out.println("here");
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield());
						//System.out.println(PPphrase);
						//System.out.println(PPphraseToDelete);
						
						SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
			}
		}
		
		return isSplit;
	}
	
	
	public static boolean extractFromTo(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
	 	
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux = SentenceProcessor.setAux(true, isPresent);
			
		for (Tree t : parse) {
			if (t.label().value().equals("PP")) {
				if (t.getChildrenAsList().size() == 2) {
					if (t.getChild(0).label().value().equals("IN") && t.getChild(1).label().value().equals("NP")) {
						if (t.getChild(0).getChild(0).label().value().equals("from")) {
							if (t.getChild(1).getChildrenAsList().size() == 3) {
								if (t.getChild(1).getChild(0).label().value().equals("CD") && t.getChild(1).getChild(1).label().value().equals("TO") && t.getChild(1).getChild(2).label().value().equals("CD")) {
									String PPphrase = "This " + aux + Sentence.listToString(t.yield()) + " .";
									String PPphraseToDelete = Sentence.listToString(t.yield());
									//System.out.println(PPphrase);
									//System.out.println(PPphraseToDelete);
									String[] tokensToDelete = PPphraseToDelete.split(" ");
									String[] tokens = sentence.split(" ");
									
									int tokensToDeleteCount = tokensToDelete.length;
									int tokensCount = tokens.length;
									
									if (tokensCount - tokensToDeleteCount > 3) {
									
										SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
						}
					}
				}
			}
		}
		return isSplit;
	}
	
	
	public static boolean extractIncluding(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		final String presentSingular = " includes ";
		final String presentPlural = " include ";
		final String past = " included ";
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		boolean isPresent = SentenceProcessor.isPresent(parse);
		
		for (Tree t : parse) {
			if (t.label().value().equals("VP")) {
				if (t.getChildrenAsList().size() >= 4) {
					for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",") && t.getChild(i+3).label().value().equals("PP")) {
							if (t.getChild(i+3).getChild(0).label().value().equals("VBG") && t.getChild(i+3).getChild(0).getChild(0).label().value().equals("including")) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
								
								String aux = "";
								if (isSingular && isPresent) {
									aux = presentSingular;
								} else if (!isSingular && isPresent) {
									aux = presentPlural;
								} else {
									aux = past;
								}
								
								String includingPhrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+3).getChild(1).yield()) + " .";
								String includingPhraseToDelete = " , " + Sentence.listToString(t.getChild(i+3).yield());
								
								SentenceProcessor.updateSentence(includingPhrase, includingPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
				
				if (t.getChildrenAsList().size() >= 3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("PP")) {
							if (t.getChild(i+2).getChild(0).label().value().equals("VBG") && t.getChild(i+2).getChild(0).getChild(0).label().value().equals("including")) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
								String aux = "";
								if (isSingular && isPresent) {
									aux = presentSingular;
								} else if (!isSingular && isPresent) {
									aux = presentPlural;
								} else {
									aux = past;
								}
								
								String includingPhrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " .";
								String includingPhraseToDelete = " , " + Sentence.listToString(t.getChild(i+2).yield());
								
								SentenceProcessor.updateSentence(includingPhrase, includingPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
			
			if (t.label().value().equals("NP")) {
				if (t.getChildrenAsList().size() >= 2) {
					for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP")) {
							if (t.getChild(i+1).getChild(0).label().value().equals("VBG") && t.getChild(i+1).getChild(0).getChild(0).label().value().equals("including")) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
								String aux = "";
								if (isSingular && isPresent) {
									aux = presentSingular;
								} else if (!isSingular && isPresent) {
									aux = presentPlural;
								} else {
									aux = past;
								}
								
								String includingPhrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " .";
								String includingPhraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
								
								SentenceProcessor.updateSentence(includingPhrase, includingPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
 			}
		}
		
		return isSplit;
	}
	
	
	public static boolean extractAccording(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 2) {
					if (t.getChild(0).label().value().equals("PP") && t.getChild(1).label().value().equals(",")) {
						if (t.getChild(0).getChild(0).label().value().equals("VBG") && t.getChild(0).getChild(0).getChild(0).label().value().equals("According")) {
							String phrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
							String phraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
							
							String[] tokensToDelete = phraseToDelete.split(" ");
							String[] tokens = sentence.split(" ");
							
							int tokensToDeleteCount = tokensToDelete.length;
							int tokensCount = tokens.length;
							
							if (tokensCount - tokensToDeleteCount > 3) {
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
			
			for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",")) {
					if (t.getChild(i+1).getChild(0).label().value().equals("VBG") && t.getChild(i+1).getChild(0).getChild(0).label().value().equals("according")) {
						String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String phraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";
						
						String[] tokensToDelete = phraseToDelete.split(" ");
						String[] tokens = sentence.split(" ");
						
						int tokensToDeleteCount = tokensToDelete.length;
						int tokensCount = tokens.length;
						
						if (tokensCount - tokensToDeleteCount > 3) {
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
			
			for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && i == t.getChildrenAsList().size()-2) {
					if (t.getChild(i+1).getChild(0).label().value().equals("VBG") && t.getChild(i+1).getChild(0).getChild(0).label().value().equals("according")) {
						String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String phraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield());
						
						String[] tokensToDelete = phraseToDelete.split(" ");
						String[] tokens = sentence.split(" ");
						
						int tokensToDeleteCount = tokensToDelete.length;
						int tokensCount = tokens.length;
						
						if (tokensCount - tokensToDeleteCount > 3) {
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
		}
		
		return isSplit;
	}

	
	public static boolean extractByPlusParticiple(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux2 = SentenceProcessor.setAux(true, isPresent);
		
		final String presentSingular = " does this ";
		final String presentPlural = " do this ";
		final String past = " did this ";
		
		for (Tree t : parse) {
			if (t.getChildrenAsList().size() >= 2) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("VP")) {
						if (!t.getChild(i+1).getChild(0).getChild(0).label().value().equals("does") && !t.getChild(i+1).getChild(0).getChild(0).label().value().equals("do") && !t.getChild(i+1).getChild(0).getChild(0).label().value().equals("did")) {
							for (int j = 0; j < t.getChild(i+1).getChildrenAsList().size(); j++) {
								if (t.getChild(i+1).getChild(j).label().value().equals("PP")) {
									if (t.getChild(i+1).getChild(j).getChild(0).label().value().equals("IN") && t.getChild(i+1).getChild(j).getChild(0).getChild(0).label().value().equals("by")) {
										//System.out.println("success2");
										if (t.getChild(i+1).getChild(j).getChild(1).label().value().equals("S") && t.getChild(i+1).getChild(j).getChild(1).getChild(0).label().value().equals("VP")) {
											if (t.getChild(i+1).getChild(j).getChild(1).getChild(0).getChild(0).label().value().equals("VBG")) {
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												
												String aux = "";
												if (isSingular && isPresent) {
													aux = presentSingular;
												} else if (!isSingular && isPresent) {
													aux = presentPlural;
												} else {
													aux = past;
												}
												
												String byPhrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+1).getChild(j).yield()) + " .";
												String byPhraseToDelete = Sentence.listToString(t.getChild(i+1).getChild(j).yield());
												
												String[] tokensToDelete = byPhraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(byPhrase, byPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (t.getChild(i+1).getChild(j).getChild(1).getChild(0).getChild(0).label().value().equals("VP") && t.getChild(i+1).getChild(j).getChild(1).getChild(0).getChild(0).getChild(0).label().value().equals("VBG")) {
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												
												String aux = "";
												if (isSingular && isPresent) {
													aux = presentSingular;
												} else if (!isSingular && isPresent) {
													aux = presentPlural;
												} else {
													aux = past;
												}
												
												String byPhrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+1).getChild(j).yield()) + " .";
												String byPhraseToDelete = Sentence.listToString(t.getChild(i+1).getChild(j).yield());
												
												String[] tokensToDelete = byPhraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(byPhrase, byPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (t.getChildrenAsList().size() >= 2) {
				
				if (!t.getChild(0).label().value().equals("NP") && t.getChild(1).label().value().equals("VP")) {
					
					for (int i = 0; i < t.getChild(1).getChildrenAsList().size(); i++) {
						if (t.getChild(1).getChild(i).label().value().equals("PP")) {
							if (t.getChild(1).getChild(i).getChild(0).label().value().equals("IN") && t.getChild(1).getChild(i).getChild(0).getChild(0).label().value().equals("by")) {
								//System.out.println("success2");
								if (t.getChild(1).getChild(i).getChild(1).label().value().equals("S") && t.getChild(1).getChild(i).getChild(1).getChild(0).label().value().equals("VP")) {
									if (t.getChild(1).getChild(i).getChild(1).getChild(0).getChild(0).label().value().equals("VBG")) {
										
										String byPhrase = "This" + aux2 + Sentence.listToString(t.getChild(1).getChild(i).yield()) + " .";
										//System.out.println(byPhrase);
										String byPhraseToDelete = Sentence.listToString(t.getChild(1).getChild(i).yield());
										
										String[] tokensToDelete = byPhraseToDelete.split(" ");
										String[] tokens = sentence.split(" ");
										
										int tokensToDeleteCount = tokensToDelete.length;
										int tokensCount = tokens.length;
										
										if (tokensCount - tokensToDeleteCount > 3) {
											SentenceProcessor.updateSentence(byPhrase, byPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										}
									}
									else if (t.getChild(1).getChild(i).getChild(1).getChild(0).getChild(0).label().value().equals("VP") && t.getChild(1).getChild(i).getChild(1).getChild(0).getChild(0).getChild(0).label().value().equals("VBG")) {
										
										String byPhrase = "This" + aux2 + Sentence.listToString(t.getChild(1).getChild(i).yield()) + " .";
										//System.out.println(byPhrase);
										String byPhraseToDelete = Sentence.listToString(t.getChild(1).getChild(i).yield());
										
										String[] tokensToDelete = byPhraseToDelete.split(" ");
										String[] tokens = sentence.split(" ");
										
										int tokensToDeleteCount = tokensToDelete.length;
										int tokensCount = tokens.length;
										
										if (tokensCount - tokensToDeleteCount > 3) {
											SentenceProcessor.updateSentence(byPhrase, byPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return isSplit;
	}
}
