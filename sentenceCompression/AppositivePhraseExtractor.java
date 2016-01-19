package sentenceCompression;

import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class AppositivePhraseExtractor {
	
	public static boolean extractNonRestrictiveAppositives(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("NP")) {
				
				if (t.getChildrenAsList().size() >= 5) {
					for (int i = 0; i < t.getChildrenAsList().size()-4; i++) {
						
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("CC") && t.getChild(i+3).label().value().equals("NP") && t.getChild(i+4).label().value().equals(",")) {
							if (t.getChild(i).getChild(0).label().value().equals("NNP") || t.getChild(i).getChild(0).label().value().equals("NNPS")) {
								if (t.getChild(i+2).getChild(0).label().value().equals("or")) {
									List<LabeledWord> label = t.getChild(i).labeledYield();
									boolean singular = SentenceProcessor.isSingular(label.get(label.size()-1));
									
									String aux = SentenceProcessor.setAux(singular, isPresent);
									
									String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+3).yield()) + " .";
									String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " ,";
									
									
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
							
						}
						
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("RB") && t.getChild(i+3).label().value().equals("NP") && t.getChild(i+4).label().value().equals(",")) {
							boolean isConjoinedNP = false;
							for (int j = i+4; j < t.getChildrenAsList().size(); j++) {
								if (t.getChild(j).label().value().equals("CC") && (t.getChild(j).getChild(0).label().value().equals("and") || t.getChild(j).getChild(0).label().value().equals("or"))) {
									isConjoinedNP = true;
								}
							}
							
							List<LabeledWord> label = t.getChild(i).labeledYield();
							
							if (!isConjoinedNP) {
								if (label.get(label.size()-1).tag().value().equals("NNP") || label.get(label.size()-1).tag().value().equals("NNPS")) {
									String part1 = "";
									boolean number = SentenceProcessor.isSingular(label.get(label.size()-1));
									String aux = SentenceProcessor.setAux(number, isPresent);
								
									
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
									
									if (t.getChild(i+3).getChildrenAsList().size() >= 2) {
											String phrase = part1 + aux + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " .";
											String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " ,";
											//System.out.println("success1: " + phraseToDelete);
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										
									}
								}
								else {
									if (!label.get(label.size()-1).tag().value().equals("CD")) {
										List<LabeledWord> label2;
											label2 = t.getChild(i+3).labeledYield();
											boolean number = SentenceProcessor.isSingular(label2.get(label2.size()-1));
											String aux = SentenceProcessor.setAux(number, isPresent);
											String part1 = Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
											String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + part1 + " .";
											String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " ,";
											//System.out.println("success2");
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										//}
									}
								}
							}
						}
					}
				}
				
				if (t.getChildrenAsList().size() >= 4) {
					for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
						
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("RB") && t.getChild(i+3).label().value().equals("NP") && i == t.getChildrenAsList().size()-4) {
							
							List<LabeledWord> label = t.getChild(i).labeledYield();
							
							
								if (label.get(label.size()-1).tag().value().equals("NNP") || label.get(label.size()-1).tag().value().equals("NNPS")) {
									String part1 = "";
									boolean number = SentenceProcessor.isSingular(label.get(label.size()-1));
									String aux = SentenceProcessor.setAux(number, isPresent);
									
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
									
									if (t.getChild(i+3).getChildrenAsList().size() >= 2) {
											String phrase = part1 + aux + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " .";
											String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
											//System.out.println("success1: " + phraseToDelete);
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										
									}
								}
								if (!label.get(label.size()-1).tag().value().equals("NNP")) {
									if (!label.get(label.size()-1).tag().value().equals("NNPS")) {
										if (!label.get(label.size()-1).tag().value().equals("CD")) {
										List<LabeledWord> label2;
											label2 = t.getChild(i+3).labeledYield();
											boolean number = SentenceProcessor.isSingular(label2.get(label2.size()-1));
											String aux = SentenceProcessor.setAux(number, isPresent);
											String part1 = Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
											String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + part1 + " .";
											String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
											//System.out.println("success2");
											
											//System.out.println(sentence);
											//System.out.println(phrase);
											//System.out.println(phraseToDelete);
											
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
											
										}
									}
									
								}
							
						}
						
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("NP") && t.getChild(i+3).label().value().equals(",")) {
							boolean isConjoinedNP = false;
							for (int j = i+3; j < t.getChildrenAsList().size(); j++) {
								if (t.getChild(j).label().value().equals("CC") && (t.getChild(j).getChild(0).label().value().equals("and") || t.getChild(j).getChild(0).label().value().equals("or"))) {
									isConjoinedNP = true;
									//System.out.println("true");
								}
							}
							
							List<LabeledWord> label = t.getChild(i).labeledYield();
							
							if (!isConjoinedNP) {
								if (label.get(label.size()-1).tag().value().equals("NNP") || label.get(label.size()-1).tag().value().equals("NNPS") || label.get(label.size()-1).tag().value().equals("''")) {
									String part1 = "";
									boolean number = SentenceProcessor.isSingular(label.get(label.size()-1));
									String aux = SentenceProcessor.setAux(number, isPresent);
									
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
										//List<LabeledWord> label2;
										//if (t.getChild(i+2).getChild(0).label().value().equals("NP")) {
											//label2 = t.getChild(i+2).getChild(0).labeledYield();
										//} else {
											//label2 = t.getChild(i+2).labeledYield();
										//}
										//if (!label2.get(label2.size()-1).tag().value().equals("NNP") && !label2.get(label2.size()-1).tag().value().equals("NNPS")) {
											String phrase = part1 + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
											String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
											//System.out.println("success1: " + phraseToDelete);
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										//}
									}
								}
								else {
									if (!label.get(label.size()-1).tag().value().equals("CD")) {
										List<LabeledWord> label2;
										//if (t.getChild(i+2).getChild(0).label().value().equals("NP")) {
											//label2 = t.getChild(i+2).getChild(0).labeledYield();
										//} else {
											label2 = t.getChild(i+2).labeledYield();
										//}
										//if (label2.get(label2.size()-1).tag().value().equals("NNP") || label2.get(label2.size()-1).tag().value().equals("NNPS")) {				
											boolean number = SentenceProcessor.isSingular(label2.get(label2.size()-1));
											String aux = SentenceProcessor.setAux(number, isPresent);
											
											String part1 = Sentence.listToString(t.getChild(i+2).yield());
											String phrase = part1 + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
											String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
											//System.out.println("success2");
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										//}
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
							
							if (label.get(label.size()-1).tag().value().equals("NNP") || label.get(label.size()-1).tag().value().equals("NNPS") || label.get(label.size()-1).tag().value().equals("''")) {
								String part1 = "";
								boolean number = SentenceProcessor.isSingular(label.get(label.size()-1));
								String aux = SentenceProcessor.setAux(number, isPresent);
								
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
									//List<LabeledWord> label2;
									//if (t.getChild(i+2).getChild(0).label().value().equals("NP")) {
										//label2 = t.getChild(i+2).getChild(0).labeledYield();
		
									//} else {
										//label2 = t.getChild(i+2).labeledYield();
									//}
									//if (!label2.get(label2.size()-1).tag().value().equals("NNP") && !label2.get(label2.size()-1).tag().value().equals("NNPS")) {
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									//}
								}
							}
							else {
								List<LabeledWord> label2;
								if (!label.get(label.size()-1).tag().value().equals("CD")) {
									//if (t.getChild(i+2).getChild(0).label().value().equals("NP")) {
										//label2 = t.getChild(i+2).getChild(0).labeledYield();
									//} else {
										label2 = t.getChild(i+2).labeledYield();
									//}
									if (label2.get(label2.size()-1).tag().value().equals("NNP") || label2.get(label2.size()-1).tag().value().equals("NNPS")) {
										boolean number = SentenceProcessor.isSingular(label2.get(label2.size()-1));
										String aux = SentenceProcessor.setAux(number, isPresent);	
										String part1 = Sentence.listToString(t.getChild(i+2).yield());
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
											
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
									else {
										String part1 = Sentence.listToString(t.getChild(i).yield());
										boolean number = SentenceProcessor.isSingular(label.get(label.size()-1));
										String aux = SentenceProcessor.setAux(number, isPresent);
										String phrase = part1 + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
										String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
										
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
		
		return isSplit;
	}
	
	
	public static boolean extractRestrictiveAppositives(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
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
								if (t.getChild(m).label().value().equals("DT") || t.getChild(m).label().value().equals("PRP$") || t.getChild(m).label().value().equals("POS")) {
									
									isDetOrPronoun = true;
									
								}
								
								m--;
							}
								
							//System.out.println("nnp: " + nnp);
							//System.out.println("adjectiveNoun: " + adjectiveNoun);
							//System.out.println("rest: " + rest);
							String det = "";
							if (!isDetOrPronoun) {
								
								if (rest.startsWith("a") || rest.startsWith("e") || rest.startsWith("i") || rest.startsWith("o") || rest.startsWith("u") ||
										rest.startsWith("A") || rest.startsWith("E") || rest.startsWith("I") || rest.startsWith("O") || rest.startsWith("U")) {
									det = " an ";
								} else {
									det = " a ";
								}
							}
							
							boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
							String aux = SentenceProcessor.setAux(true, isPresent);
							String phrase = nnp + aux + det + " " + rest + " .";
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
					boolean isNNP = false;
					for (int i = 0; i < t.getChild(0).getChildrenAsList().size(); i++) {
						if (t.getChild(0).getChild(i).label().value().equals("NNP")) {
							isNNP = true;
						}
					}
					for (int i = 0; i < t.getChild(1).getChildrenAsList().size(); i++) {
						if (!t.getChild(1).getChild(i).label().value().equals("NNP")) {
							npCombination = false;
						}
					}
					if (npCombination == true && isNNP == false) {
						boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
						String aux = SentenceProcessor.setAux(true, isPresent);
						String phrase = Sentence.listToString(t.getChild(1).yield()) + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
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
