package sentenceCompression;

import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class GerundClauseExtractor {

	public static boolean extractGerundAfterNNP(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
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
	
	
	public static boolean extractGerundAfterWhile(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("PP")) {
				if (t.getChildrenAsList().size() >= 2) {
					for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
						if (t.getChild(i).label().value().equals("IN") && t.getChild(i).getChild(0).label().value().equals("while") && t.getChild(i+1).label().value().equals("S")) {
							
							if (t.getChild(i+1).getChild(0).label().value().equals("VP")) {
								if (t.getChild(i+1).getChild(0).getChildrenAsList().size() >= 2) {
									if (t.getChild(i+1).getChild(0).getChild(0).label().value().equals("VBG") && (t.getChild(i+1).getChild(0).getChild(1).label().value().equals("S") || t.getChild(i+1).getChild(0).getChild(1).label().value().equals("NP"))) {
										
										String phrase = "This" + aux + Sentence.listToString(t.yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.yield());
										
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
					}
				}
			}
		}
		
		return isSplit;
	}
	
	/**
	public static boolean extractGerundAfterComma(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 2) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("ADVP") && t.getChild(i+2).label().value().equals("VP")) {
							Tree tree = t.getChild(i+2);
							
							boolean end = false;
							
							while (!end) {
								int numberOfChildren = tree.getChildrenAsList().size();
								
								if (numberOfChildren < 2) {
									if (tree.getChild(0).label().value().equals("VP")) {
										tree = tree.getChild(0);
									}
									else {
										end = true;
									}
								} else {
									if (tree.getChild(0).label().value().equals("VP")) {
										tree = tree.getChild(0);
									}
									else if (tree.getChild(1).label().value().equals("VP")) {
										tree = tree.getChild(1);
									}
									else {
										end = true;
									}
								}
							}
							//System.out.println(tree.yield());
							
							for (int j = 0; j < tree.getChildrenAsList().size()-2; j++) {
								if ((tree.getChild(j).label().value().equals("NP") || tree.getChild(j).label().value().equals("PP") || tree.getChild(j).label().value().equals("S")) && tree.getChild(j+1).label().value().equals(",") && tree.getChild(j+2).label().value().equals("S") && j == tree.getChildrenAsList().size()-3) {
									if (tree.getChild(j+2).getChildrenAsList().size() < 2) {
										if (tree.getChild(j+2).getChild(0).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success1");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
												//System.out.println("gerund phrase: " + phrase);
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(0).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(0).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(0).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(0).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(0).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
													//System.out.println("gerund phrase: " + phrase);
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
									} else {
										if (tree.getChild(j+2).getChild(0).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success2");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(0).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(0).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(0).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(0).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(0).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
													//System.out.println("gerund phrase: " + phrase);
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
										} else if (tree.getChild(j+2).getChild(0).label().value().equals("ADVP") && tree.getChild(j+2).getChild(1).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(1).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success3");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(1).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(1).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(1).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(1).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(1).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(1).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
													//System.out.println("gerund phrase: " + phrase);
													
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
								}
							}
							
							for (int j = 0; j < tree.getChildrenAsList().size()-3; j++) {
								if ((tree.getChild(j).label().value().equals("NP") || tree.getChild(j).label().value().equals("PP") || tree.getChild(j).label().value().equals("S")) && tree.getChild(j+1).label().value().equals(",") && tree.getChild(j+2).label().value().equals("S") && tree.getChild(j+3).label().value().equals(",")) {
									if (tree.getChild(j+2).getChildrenAsList().size() < 2) {
										if (tree.getChild(j+2).getChild(0).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success1");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
												//System.out.println("gerund phrase: " + phrase);
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(0).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(0).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(0).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(0).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(0).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
													//System.out.println("gerund phrase: " + phrase);
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
									} else {
										if (tree.getChild(j+2).getChild(0).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success2");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(0).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(0).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(0).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(0).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(0).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
													//System.out.println("gerund phrase: " + phrase);
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
										} else if (tree.getChild(j+2).getChild(0).label().value().equals("ADVP") && tree.getChild(j+2).getChild(1).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(1).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success3");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(1).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(1).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(1).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(1).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(1).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(1).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
													//System.out.println("gerund phrase: " + phrase);
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
								}
							}
						}
					}
					
					for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("VP")) {
							Tree tree = t.getChild(i+1);
							
							boolean end = false;
							
							while (!end) {
								int numberOfChildren = tree.getChildrenAsList().size();
								
								if (numberOfChildren < 2) {
									if (tree.getChild(0).label().value().equals("VP")) {
										tree = tree.getChild(0);
									}
									else {
										end = true;
									}
								} else {
									if (tree.getChild(0).label().value().equals("VP")) {
										tree = tree.getChild(0);
									}
									else if (tree.getChild(1).label().value().equals("VP")) {
										tree = tree.getChild(1);
									}
									else {
										end = true;
									}
								}
							}
							//System.out.println(tree.yield());
							
							for (int j = 0; j < tree.getChildrenAsList().size()-2; j++) {
								if ((tree.getChild(j).label().value().equals("NP") || tree.getChild(j).label().value().equals("PP") || tree.getChild(j).label().value().equals("S")) && tree.getChild(j+1).label().value().equals(",") && tree.getChild(j+2).label().value().equals("S") && j == tree.getChildrenAsList().size()-3) {
									if (tree.getChild(j+2).getChildrenAsList().size() < 2) {
										if (tree.getChild(j+2).getChild(0).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success1");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
												//System.out.println("gerund phrase: " + phrase);
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(0).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(0).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(0).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(0).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(0).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
													//System.out.println("gerund phrase: " + phrase);
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
									} else {
										if (tree.getChild(j+2).getChild(0).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success2");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(0).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(0).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(0).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(0).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(0).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
													//System.out.println("gerund phrase: " + phrase);
													
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
										} else if (tree.getChild(j+2).getChild(0).label().value().equals("ADVP") && tree.getChild(j+2).getChild(1).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(1).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success3");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(1).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(1).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(1).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(1).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(1).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(1).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield());
													//System.out.println("gerund phrase: " + phrase);
													
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
								}
							}
							
							for (int j = 0; j < tree.getChildrenAsList().size()-3; j++) {
								if ((tree.getChild(j).label().value().equals("NP") || tree.getChild(j).label().value().equals("PP") || tree.getChild(j).label().value().equals("S")) && tree.getChild(j+1).label().value().equals(",") && tree.getChild(j+2).label().value().equals("S") && tree.getChild(j+3).label().value().equals(",")) {
									if (tree.getChild(j+2).getChildrenAsList().size() < 2) {
										if (tree.getChild(j+2).getChild(0).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success1");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
												//System.out.println("gerund phrase: " + phrase);
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(0).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(0).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(0).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(0).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(0).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
													//System.out.println("gerund phrase: " + phrase);
													
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
									} else {
										if (tree.getChild(j+2).getChild(0).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success2");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(0).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(0).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(0).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(0).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(0).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(0).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
													//System.out.println("gerund phrase: " + phrase);
													
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
										} else if (tree.getChild(j+2).getChild(0).label().value().equals("ADVP") && tree.getChild(j+2).getChild(1).label().value().equals("VP")) {
											if (tree.getChild(j+2).getChild(1).getChild(0).label().value().equals("VBG")) {
												//System.out.println("success3");
												List<LabeledWord> label = t.getChild(i).labeledYield();
												boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
												String aux = SentenceProcessor.setAux(isSingular, isPresent);
												
												String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
												String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
												
												String[] tokensToDelete = phraseToDelete.split(" ");
												String[] tokens = sentence.split(" ");
												
												int tokensToDeleteCount = tokensToDelete.length;
												int tokensCount = tokens.length;
												
												if (tokensCount - tokensToDeleteCount > 3) {
													SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													isSplit = true;
												}
											}
											else if (tree.getChild(j+2).getChild(1).getChild(0).label().value().equals("VP") && tree.getChild(j+2).getChild(1).getChild(1).label().value().equals("CC") && tree.getChild(j+2).getChild(1).getChild(2).label().value().equals("VP")) {
												if (tree.getChild(j+2).getChild(1).getChild(0).getChild(0).label().value().equals("VBG") && tree.getChild(j+2).getChild(1).getChild(1).getChild(0).label().value().equals("and") && tree.getChild(j+2).getChild(1).getChild(2).getChild(0).label().value().equals("VBG")) {
													List<LabeledWord> label = t.getChild(i).labeledYield();
													boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
													String aux = SentenceProcessor.setAux(isSingular, isPresent);
													
													String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(tree.getChild(j+2).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(tree.getChild(j+2).yield()) + " ,";
													//System.out.println("gerund phrase: " + phrase);
													
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
								}
							}
						}
					}
				}
				
			}
		}
		
		return isSplit;
	}
	*/
	
	/**
	public static boolean extractGerundSentenceStart(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 2) {
					if (t.getChild(0).label().value().equals("S") && t.getChild(1).label().value().equals(",")) {
						if (t.getChild(0).getChild(0).label().value().equals("VP") && t.getChild(0).getChild(0).getChild(0).label().value().equals("VBG")) {
							String phrase = "This" + aux + "when " + Sentence.listToString(t.getChild(0).yield()) + " .";
							String phraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " ,";
							//System.out.println("gerund phrase: " + phrase);
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						else if (t.getChild(0).getChild(0).label().value().equals("ADVP") && t.getChild(0).getChild(1).label().value().equals("VP") && t.getChild(0).getChild(1).getChild(0).label().value().equals("VBG")) {
							String phrase = "This" + aux + "when " + Sentence.listToString(t.getChild(0).yield()) + " .";
							String phraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " ,";
							//System.out.println("gerund phrase: " + phrase);
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
		}
		
		return isSplit;
	}
	*/
	
	public static boolean extractByGerund(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
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
