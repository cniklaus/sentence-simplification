package sentenceCompression;


import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class ConjoinedClausesExtractor {

	
	public static boolean infixAndOrButSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("S") && !t.ancestor(1, parse).label().value().equals("SBAR")) {
				if (t.getChildrenAsList().size()>=3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("S") && t.getChild(i+1).label().value().equals("CC") && t.getChild(i+2).label().value().equals("S")) {
							if (t.getChild(i+1).getChild(0).label().value().equals("and") || t.getChild(i+1).getChild(0).label().value().equals("or") || t.getChild(i+1).getChild(0).label().value().equals("but")) {
								/**
								String phrase1 = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
								String phraseToDeleteForPhrase2 = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
								String phrase2 = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
								
								String delete = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
								
								if (isOriginal) {
									int[] coreStartEnd1 = SentenceProcessor.matchSentences(Sentence.listToString(coreContextSentence.getOriginal().yield()), SentenceProcessor.collapseWhitespace(phrase1));
									Integer start1 = coreStartEnd1[0];
									Integer end1 = coreStartEnd1[1];
									String pre1 = "";
									String post1 = "";
									Core.getStart().add(start1);
									Core.getEnd().add(end1);
									Core.getPrefix().add(pre1);
									Core.getPostfix().add(post1);
									
									int[] coreStartEnd2 = SentenceProcessor.matchSentences(Sentence.listToString(coreContextSentence.getOriginal().yield()), SentenceProcessor.collapseWhitespace(phrase2));
									Integer start2 = coreStartEnd2[0];
									Integer end2 = coreStartEnd2[1];
									String pre2 = "";
									String post2 = "";
									Core.getStart().add(start2);
									Core.getEnd().add(end2);
									Core.getPrefix().add(pre2);
									Core.getPostfix().add(post2);
								} else {
									SentenceProcessor.updateSentence(phrase1, phraseToDeleteForPhrase2.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									SentenceProcessor.updateSentence(phrase2, delete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								}
								
								isSplit = true;
								*/
								
									//String phrase1 = Sentence.listToString(t.getChild(i).yield()) + " .";
									String phrase2 = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
									String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
									
									//System.out.println(phrase1);
									//System.out.println(phrase2);
									
									String orig = Sentence.listToString(parse.yield());
									//SentenceProcessor.addCore(phrase1, coreContextSentence);
									SentenceProcessor.addCore(phrase2, coreContextSentence);
									
									String phrase1 = orig.replace(phraseToDelete, "");
									String[] phrase1Tokens = phrase1.split(" ");
									Integer pos = phrase1Tokens.length;
									SentenceProcessor.pos(pos-2);
									
									SentenceProcessor.updateSentence("", phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									//SentenceProcessor.updateSentence("", phrase1.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
								
							}
						}
					}
				}
			}
		}
		
		return isSplit;
	}
	
	
	public static boolean infixCommaAndOrButSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size()>=3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("S") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("CC") && t.getChild(i+3).label().value().equals("S")) {
							if (t.getChild(i+2).getChild(0).label().value().equals("and") || t.getChild(i+2).getChild(0).label().value().equals("or") || t.getChild(i+2).getChild(0).label().value().equals("but")) {
								/**
								String phrase1 = Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
								String phraseToDeleteForPhrase2 = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " .";
								String phrase2 = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
								
								String delete = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
								
								if (isOriginal) {
									int[] coreStartEnd1 = SentenceProcessor.matchSentences(Sentence.listToString(coreContextSentence.getOriginal().yield()), SentenceProcessor.collapseWhitespace(phrase1));
									Integer start1 = coreStartEnd1[0];
									Integer end1 = coreStartEnd1[1];
									String pre1 = "";
									String post1 = "";
									Core.getStart().add(start1);
									Core.getEnd().add(end1);
									Core.getPrefix().add(pre1);
									Core.getPostfix().add(post1);
									
									int[] coreStartEnd2 = SentenceProcessor.matchSentences(Sentence.listToString(coreContextSentence.getOriginal().yield()), SentenceProcessor.collapseWhitespace(phrase2));
									Integer start2 = coreStartEnd2[0];
									Integer end2 = coreStartEnd2[1];
									String pre2 = "";
									String post2 = "";
									Core.getStart().add(start2);
									Core.getEnd().add(end2);
									Core.getPrefix().add(pre2);
									Core.getPostfix().add(post2);
								} else {
									SentenceProcessor.updateSentence(phrase1, phraseToDeleteForPhrase2.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									SentenceProcessor.updateSentence(phrase2, delete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								}
								
								isSplit = true;
								*/
								
								//String phrase1 = Sentence.listToString(t.getChild(i).yield()) + " .";
								String phrase2 = Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " .";
								String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
								
								//System.out.println(phrase1);
								//System.out.println(phrase2);
								
								String orig = Sentence.listToString(parse.yield());
								//SentenceProcessor.addCore(phrase1, coreContextSentence);
								SentenceProcessor.addCore(phrase2, coreContextSentence);
								
								String phrase1 = orig.replace(phraseToDelete, "");
								String[] phrase1Tokens = phrase1.split(" ");
								Integer pos = phrase1Tokens.length;
								SentenceProcessor.pos(pos-2);
								
								SentenceProcessor.updateSentence("", phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								//SentenceProcessor.updateSentence("", phrase1.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							
								isSplit = true;
							
							}
						}
					}
				}
			}
		}
		
		return isSplit;
	}
	
	/**Achtung appositives!!!!!
	public static boolean or(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("NP")) {
				if (t.getChildrenAsList().size() >= 4) {
					for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("CC") && t.getChild(i+3).label().value().equals("NP") && i == t.getChildrenAsList().size()-4) {
							if (t.getChild(i+2).getChild(0).label().value().equals("or")) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean singular = SentenceProcessor.isSingular(label.get(label.size()-1));
								boolean present = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
								String aux = SentenceProcessor.setAux(singular, present);
								
								String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+3).yield()) + " .";
								String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield());
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
								
							}
						}
					}
				} 
				
				if (t.getChildrenAsList().size() >= 5) {
					for (int i = 0; i < t.getChildrenAsList().size()-4; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("CC") && t.getChild(i+3).label().value().equals("NP") && t.getChild(i+4).label().value().equals(",")) {
							if (t.getChild(i+2).getChild(0).label().value().equals("or")) {
								List<LabeledWord> label = t.getChild(i).labeledYield();
								boolean singular = SentenceProcessor.isSingular(label.get(label.size()-1));
								boolean present = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
								String aux = SentenceProcessor.setAux(singular, present);
								
								String phrase = Sentence.listToString(t.getChild(i).yield()) + aux + Sentence.listToString(t.getChild(i+3).yield()) + " .";
								String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " ,";
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
		}
	
		return isSplit;
	}*/
	
	public static boolean infixWhenSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		String aux2 = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("VP") || t.label().value().equals("NP")) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR")) {
						//System.out.println("successful1");
						if (t.getChild(i+1).getChild(0).label().value().equals("WHADVP") && t.getChild(i+1).getChild(1).label().value().equals("S")) {
							//System.out.println("successful2");
							for (int j = 0; j < t.getChild(i+1).getChild(0).getChildrenAsList().size(); j++) {
								if (t.getChild(i+1).getChild(0).getChild(j).label().value().equals("WRB") && t.getChild(i+1).getChild(0).getChild(j).getChild(0).label().value().equals("when")) {
									//System.out.println("successful3");
									for (int k = 0; k < t.getChild(i+1).getChild(1).getChildrenAsList().size()-1; k++ ) {
										if (t.getChild(i+1).getChild(1).getChild(k).label().value().equals("NP")) {
											//System.out.println("successful4");
											for (int l = k+1; l < t.getChild(i+1).getChild(1).getChildrenAsList().size(); l++) {
												if (t.getChild(i+1).getChild(1).getChild(l).label().value().equals("VP")) {
													//System.out.println("successful5");
													
													String aux;
													if (t.getChild(i+1).getChild(1).getChild(l).getChild(0).label().value().equals("VBP") || t.getChild(i+1).getChild(1).getChild(l).getChild(0).label().value().equals("VBZ")) {
														aux = " is ";
													} else {
														aux = " was ";
													}
													
													String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
													String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield());
													//System.out.println("to replace: " + toReplace);
											
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
			if (t.label().value().equals("VP")) {
				if (t.getChildrenAsList().size() >= 2) {
					for (int i=0; i < t.getChildrenAsList().size()-1; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("SBAR")) {
							if (t.getChild(i+1).getChildrenAsList().size() >= 2) {
								if (t.getChild(i+1).getChild(0).label().value().equals("WHADVP") && t.getChild(i+1).getChild(1).label().value().equals("S")) {
									if (t.getChild(i+1).getChild(0).getChild(0).label().value().equals("WRB") && t.getChild(i+1).getChild(0).getChild(0).getChild(0).label().value().equals("when")) {
										String phrase = "This " + aux2 + Sentence.listToString(t.getChild(i+1).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
							if (t.getChild(i+1).getChildrenAsList().size() >= 3) {
								if (t.getChild(i+1).getChild(1).label().value().equals("WHADVP") && t.getChild(i+1).getChild(2).label().value().equals("S")) {
									if (t.getChild(i+1).getChild(1).getChild(0).label().value().equals("WRB") && t.getChild(i+1).getChild(1).getChild(0).getChild(0).label().value().equals("when")) {
										String phrase = "This " + aux2 + Sentence.listToString(t.getChild(i+1).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
										
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
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals("SBAR")) {
							if (t.getChild(i+2).getChildrenAsList().size() >= 2) {
								if (t.getChild(i+2).getChild(0).label().value().equals("WHADVP") && t.getChild(i+2).getChild(1).label().value().equals("S")) {
									if (t.getChild(i+2).getChild(0).getChild(0).label().value().equals("WRB") && t.getChild(i+2).getChild(0).getChild(0).getChild(0).label().value().equals("when")) {
										String phrase = "This " + aux2 + Sentence.listToString(t.getChild(i+2).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(i+2).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
							if (t.getChild(i+2).getChildrenAsList().size() >= 3) {
								if (t.getChild(i+2).getChild(1).label().value().equals("WHADVP") && t.getChild(i+2).getChild(2).label().value().equals("S")) {
									if (t.getChild(i+2).getChild(1).getChild(0).label().value().equals("WRB") && t.getChild(i+2).getChild(1).getChild(0).getChild(0).label().value().equals("when")) {
										String phrase = "This " + aux2 + Sentence.listToString(t.getChild(i+2).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(i+2).yield());
										
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
	
	
	public static boolean initialWhenSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {	
				for (int i = 0; i < t.getChildrenAsList().size(); i++) {
					if (t.getChild(i).label().value().equals("SBAR")) {
						if (t.getChild(i).getChild(0).label().value().equals("WHADVP") && t.getChild(i).getChild(1).label().value().equals("S")) {
							if (t.getChild(i).getChild(0).getChild(0).label().value().equals("WRB") && t.getChild(i).getChild(0).getChild(0).getChild(0).label().value().equals("When")) {
								
								String aux = "";
								for (int j = 0; j < t.getChild(i).getChild(1).getChildrenAsList().size()-1; j++) {
									if (t.getChild(i).getChild(1).getChild(j).label().value().equals("NP")) {
										for (int k = j+1; k < t.getChild(i).getChild(1).getChildrenAsList().size(); k++) {
											if (t.getChild(i).getChild(1).getChild(k).label().value().equals("VP")) {
												if (t.getChild(i).getChild(1).getChild(k).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(1).getChild(k).getChild(0).label().value().equals("VBZ")) {
													aux = "is ";
												} else {
													aux = "was ";
												}
											}
										}
									}
								}
								
								String phrase = "This " + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " ,";
								
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
		
		return isSplit;
	}
	
	
	public static boolean initialThoughAlthoughBecauseSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("S")) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals("SBAR") && t.getChild(i+1).label().value().equals(",")) {
						//System.out.println("success1");
						if (t.getChild(i).getChild(0).label().value().equals("IN") && t.getChild(i).getChild(1).label().value().equals("S")) {
							if (t.getChild(i).getChild(0).getChild(0).label().value().equals("Because") || t.getChild(i).getChild(0).getChild(0).label().value().equals("Though") || t.getChild(i).getChild(0).getChild(0).label().value().equals("Although")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This " + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " ,";
								
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
	
	
	public static boolean infixPPSAfterBeforeSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
	
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("VP") || t.label().value().equals("NP")) {
				for (int i = 0; i < t.getChildrenAsList().size(); i++) {
					if (t.getChild(i).label().value().equals("PP")) {
						if (i == 0) {
							for (int n = 0; n < t.getChild(i).getChildrenAsList().size()-1; n++) {
								if (t.getChild(i).getChild(n).label().value().equals("IN") && t.getChild(i).getChild(n+1).label().value().equals("S")) {
									//System.out.println("success2");
									if (t.getChild(i).getChild(n).getChild(0).label().value().equals("after") || t.getChild(i).getChild(n).getChild(0).label().value().equals("before")) {
										//System.out.println("success3");
						
										String aux;
										if (t.getChild(i).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(0).label().value().equals("VBZ")) {
											aux = " is ";
										} else {
											aux = " was ";
										}
											
										String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
										//System.out.println("to replace: " + toReplace);
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
						else if (i > 0) {
							if (!t.getChild(i-1).label().value().equals(",")) {
								//System.out.println("success1");
								for (int n = 0; n < t.getChild(i).getChildrenAsList().size()-1; n++) {
									if (t.getChild(i).getChild(n).label().value().equals("IN") && t.getChild(i).getChild(n+1).label().value().equals("S")) {
										//System.out.println("success2");
										if (t.getChild(i).getChild(n).getChild(0).label().value().equals("after") || t.getChild(i).getChild(n).getChild(0).label().value().equals("before")) {
											//System.out.println("success3");
							
											String aux;
											if (t.getChild(i).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(0).label().value().equals("VBZ")) {
												aux = " is ";
											} else {
												aux = " was ";
											}
												
											String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
											String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
											//System.out.println("to replace: " + toReplace);
											
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
		
		return isSplit;
	}
	
	
	public static boolean infixCommaPPAfterBeforeSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("VP") || t.label().value().equals("NP")) {
				for (int n = 0; n < t.getChildrenAsList().size()-2; n++) {
					if (t.getChild(n).label().value().equals(",") && t.getChild(n+1).label().value().equals("PP") && !t.getChild(n+2).label().value().equals(",")) {
						//System.out.println("success1");
						for (int m = 0; m < t.getChild(n+1).getChildrenAsList().size(); m++) {
							//System.out.println(t.getChild(n+1).getChild(m).label().value());
							if (t.getChild(n+1).getChild(m).label().value().equals("IN")) {
								//System.out.println("success2");
								if (t.getChild(n+1).getChild(m).getChild(0).label().value().equals("after") || t.getChild(n+1).getChild(m).getChild(0).label().value().equals("before")) {
									String aux;
									if (t.getChild(0).label().value().equals("VBP") || t.getChild(0).label().value().equals("VBZ")) {
										aux = " is ";
									} else {
										aux = " was ";
									}
									
									String phrase = "This" + aux + Sentence.listToString(t.getChild(n+1).yield()) + " .";
									String phraseToDelete = Sentence.listToString(t.getChild(n+1).yield());
									//System.out.println("to replace: " + toReplace);
									
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
		
		return isSplit;
	}
	
	
	public static boolean infixPPAfterBeforeSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("VP") || t.label().value().equals("NP")) {
				for (int i = 0; i < t.getChildrenAsList().size(); i++) {
					if (t.getChild(i).label().value().equals("PP")) {
						if (i == 0) {
							for (int n = 0; n < t.getChild(i).getChildrenAsList().size()-1; n++) {
								//System.out.println(t.getChild(i).getChild(n).label().value());
								//System.out.println(t.getChild(i).getChild(n+1).label().value());
								if (t.getChild(i).getChild(n).label().value().equals("IN") && t.getChild(i).getChild(n+1).label().value().equals("NP")) {
									//System.out.println("success2");
									if (t.getChild(i).getChild(n).getChild(0).label().value().equals("after") || t.getChild(i).getChild(n).getChild(0).label().value().equals("before")) {
										for (int m = 0; m < t.getChild(i).getChild(n+1).getChildrenAsList().size(); m++) {
											if (t.getChild(i).getChild(n+1).getChild(m).label().value().equals("VP")) {
	
												String aux;
												if (t.getChild(i).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(0).label().value().equals("VBZ")) {
													aux = " is ";
												} else {
													aux = " was ";
												}
												
												String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
												String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
												
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
						
						else if ( i > 0 ) {
							if (!t.getChild(i-1).label().value().equals(",")) {
								//System.out.println("success1");
								for (int n = 0; n < t.getChild(i).getChildrenAsList().size()-1; n++) {
									//System.out.println(t.getChild(i).getChild(n).label().value());
									//System.out.println(t.getChild(i).getChild(n+1).label().value());
									if (t.getChild(i).getChild(n).label().value().equals("IN") && t.getChild(i).getChild(n+1).label().value().equals("NP")) {
										//System.out.println("success2");
										if (t.getChild(i).getChild(n).getChild(0).label().value().equals("after") || t.getChild(i).getChild(n).getChild(0).label().value().equals("before")) {
											for (int m = 0; m < t.getChild(i).getChild(n+1).getChildrenAsList().size(); m++) {
												if (t.getChild(i).getChild(n+1).getChild(m).label().value().equals("VP")) {
		
													String aux;
													if (t.getChild(i).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(0).label().value().equals("VBZ")) {
														aux = " is ";
													} else {
														aux = " was ";
													}
													
													String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
													String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
													
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
	
	
	public static boolean infixSBARAfterBeforeSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean successful = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("VP") || t.label().value().equals("NP")) {
				for (int n = 0; n < t.getChildrenAsList().size()-1; n++) {
					if (t.getChild(n).label().value().equals("NP")) {
						for (int i = 1; i < t.getChildrenAsList().size(); i++) {
							if (t.getChild(i).label().value().equals("SBAR")) {
								if (!t.getChild(i-1).label().value().equals(",")) {
									if (t.getChild(i).getChild(0).label().value().equals("IN") && t.getChild(i).getChild(1).label().value().equals("S")) {
										//System.out.println("successful2");
										//System.out.println(t.getChild(i).getChild(0).getChild(0));
										if (t.getChild(i).getChild(0).getChild(0).label().value().equals("after") || t.getChild(i).getChild(0).getChild(0).label().value().equals("before")) {
											//System.out.println("successful3");
											for (int k = 0; k < t.getChild(i).getChild(1).getChildrenAsList().size()-1; k++ ) {
												if (t.getChild(i).getChild(1).getChild(k).label().value().equals("NP")) {
												//System.out.println("successful4");
													for (int l = k+1; l < t.getChild(i).getChild(1).getChildrenAsList().size(); l++) {
														if (t.getChild(i).getChild(1).getChild(l).label().value().equals("VP") && successful == false) {
															//System.out.println("successful5");
															
															successful = true;
															
															String aux;
															if (t.getChild(i).getChild(1).getChild(l).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(1).getChild(l).getChild(0).label().value().equals("VBZ")) {
																aux = " is ";
															} else {
																aux = " was ";
															}
															
															String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
															String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
															
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
			}
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 2) {
					for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
						if (t.getChild(i).label().value().equals("SBAR") && t.getChild(i+1).label().value().equals(",")) {
							if ((t.getChild(i).getChild(0).label().value().equals("IN") && t.getChild(i).getChild(1).label().value().equals("S")) ||
									((t.getChild(i).getChild(0).label().value().equals("RB") ||  t.getChild(i).getChild(0).label().value().equals("ADVP"))&& t.getChild(i).getChild(1).label().value().equals("IN") && t.getChild(i).getChild(2).label().value().equals("S"))) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " ,";
								
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
	
	
	public static boolean infixAsSinceSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("NP") || t.label().value().equals("VP") || t.label().value().equals("S")) {
				//System.out.println("success1");
				for (int i = 0; i < t.getChildrenAsList().size(); i++) {
					//System.out.println("success2");
					if (t.getChild(i).label().value().equals("SBAR")) {
						//System.out.println("success3");
						//if (i == t.getChildrenAsList().size()-1) {
							if ((i == 0) && t.getChild(i).getChild(0).label().value().equals("IN") && t.getChild(i).getChild(1).label().value().equals("S")) {
								if (t.getChild(i).getChild(0).getChild(0).label().value().equals("as")  || t.getChild(i).getChild(0).getChild(0).label().value().equals("since") || t.getChild(i).getChild(0).getChild(0).label().value().equals("while")) {
									for (int j = 0; j < t.getChild(i).getChild(1).getChildrenAsList().size()-1; j++) {
										if (t.getChild(i).getChild(1).getChild(j).label().value().equals("NP")) {
											for (int k = j+1; k < t.getChild(i).getChild(1).getChildrenAsList().size(); k++) {
												if (t.getChild(i).getChild(1).getChild(k).label().value().equals("VP")) {
													
													String aux;
													if (t.getChild(i).getChild(1).getChild(k).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(1).getChild(k).getChild(0).label().value().equals("VBZ")) {
														aux = " is ";
													} else {
														aux = " was ";
													}
													
													String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
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
									}
								}
							//}
						}
						
						
						else if (i > 0) {
							if (!t.getChild(i-1).label().value().equals(",") && t.getChild(i).getChild(0).label().value().equals("IN") && t.getChild(i).getChild(1).label().value().equals("S")) {
								if (t.getChild(i).getChild(0).getChild(0).label().value().equals("as")  || t.getChild(i).getChild(0).getChild(0).label().value().equals("since") || t.getChild(i).getChild(0).getChild(0).label().value().equals("while")) {
									for (int j = 0; j < t.getChild(i).getChild(1).getChildrenAsList().size()-1; j++) {
										if (t.getChild(i).getChild(1).getChild(j).label().value().equals("NP")) {
											for (int k = j+1; k < t.getChild(i).getChild(1).getChildrenAsList().size(); k++) {
												if (t.getChild(i).getChild(1).getChild(k).label().value().equals("VP")) {
													
													String aux;
													if (t.getChild(i).getChild(1).getChild(k).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(1).getChild(k).getChild(0).label().value().equals("VBZ")) {
														aux = " is ";
													} else {
														aux = " was ";
													}
													
													String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
													String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
													
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
				
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChildrenAsList().size() >=2 ) {
						if (t.getChild(i).label().value().equals("SBAR") && t.getChild(i+1).label().value().equals(",")) {
							//System.out.println("success3");
							
							if ((i == 0) && t.getChild(i).getChild(0).label().value().equals("IN") && t.getChild(i).getChild(1).label().value().equals("S")) {
								if (t.getChild(i).getChild(0).getChild(0).label().value().equals("as")  || t.getChild(i).getChild(0).getChild(0).label().value().equals("since") || t.getChild(i).getChild(0).getChild(0).label().value().equals("while")) {
									for (int j = 0; j < t.getChild(i).getChild(1).getChildrenAsList().size()-1; j++) {
										if (t.getChild(i).getChild(1).getChild(j).label().value().equals("NP")) {
											for (int k = j+1; k < t.getChild(i).getChild(1).getChildrenAsList().size(); k++) {
												if (t.getChild(i).getChild(1).getChild(k).label().value().equals("VP")) {
													
													String aux;
													if (t.getChild(i).getChild(1).getChild(k).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(1).getChild(k).getChild(0).label().value().equals("VBZ")) {
														aux = " is ";
													} else {
														aux = " was ";
													}
														
													String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
													String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " ,";
													
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
									}
								}
							}
						}
					}
					
				}
				
				if (t.getChildrenAsList().size() >= 3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals(":") && t.getChild(i+1).label().value().equals("SBAR") && t.getChild(i+2).label().value().equals(",")) {
							if (t.getChild(i+1).getChild(0).label().value().equals("IN") && t.getChild(i+1).getChild(1).label().value().equals("S")) {
								if (t.getChild(i+1).getChild(0).getChild(0).label().value().equals("as")  || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("while")) {
									String aux = SentenceProcessor.setAux(true, isPresent);
									String phrase = "This " + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
									String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield()) + " ,";
								
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
	
	
	public static boolean infixBecauseThoughAlthoughSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			
			if (t.label().value().equals("NP") || t.label().value().equals("VP") || t.label().value().equals("S") || t.label().value().equals("ADVP")) {
				//System.out.println("success1");
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					//System.out.println("success2");
					if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR")) {
						//System.out.println("success3");
						
						if ((t.getChild(i+1).getChild(0).label().value().equals("IN") && t.getChild(i+1).getChild(1).label().value().equals("S"))) {
							
							if (t.getChild(i+1).getChild(0).getChild(0).label().value().equals("because") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("though") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("although")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
								String phraseToDelete = "";
								
								if (i == t.getChildrenAsList().size()-2) {
									phraseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield());
								} else {
									phraseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";
								}
								
								
								if (!sentence.equals(phrase)) {
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
								
							}
							
						}
						if (t.getChild(i+1).getChildrenAsList().size() >= 3) {
							if ((t.getChild(i+1).getChild(0).label().value().equals("RB") || t.getChild(i+1).getChild(0).label().value().equals("ADVP")) && t.getChild(i+1).getChild(1).label().value().equals("IN") && t.getChild(i+1).getChild(2).label().value().equals("S")) {
							
								if (t.getChild(i+1).getChild(1).getChild(0).label().value().equals("because") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("though") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("although")) {
									String aux = SentenceProcessor.setAux(true, isPresent);
									String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
									String phraseToDelete = "";
									
									if (i == t.getChildrenAsList().size()-2) {
										
										phraseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield());
										
									} else {
										
										phraseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";
										
									}
									
									
									if (!sentence.equals(phrase)) {
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
									
								}
							}
						}
						
						else if (t.getChild(i).getChild(0).label().value().equals("RB") && t.getChild(i).getChild(1).label().value().equals("IN") && t.getChild(i).getChild(2).label().value().equals("S")) {
							/**
							if (t.getChild(i).getChild(1).getChild(0).label().value().equals("because")) {
								String phrase = "So " + sentence.replace(Sentence.listToString(t.getChild(i).yield()), "");
								String notToDelete = Sentence.listToString(t.getChild(i).getChild(1).yield());
								//System.out.println("phrase: " +  phrase);
								//System.out.println("not to delete: " + notToDelete);
								String phraseToDelete = sentence.replace(notToDelete, "");
								String[] phraseToDeleteTokens = phraseToDelete.split(" ");
								String toDelete = "";
								for (int j = 0; j < phraseToDeleteTokens.length-1; j++) {
									toDelete = toDelete + " " + phraseToDeleteTokens[j];
								}
								//System.out.println("phrase to delete: " + toDelete);
								
								SentenceProcessor.updateSentence(phrase, toDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							}*/
							
							/**
							if (t.getChild(i).getChild(1).getChild(0).label().value().equals("though")) {
								String phrase = "But " + Sentence.listToString(t.getChild(i).getChild(2).yield()) + " .";
								String phraseToDelete = "";
								//System.out.println("phrase to delete: "  + phraseToDelete);
								
								if (i == 0) {
									phraseToDelete = t.getChild(i).getChild(0).getChild(0).label().value() + " " + t.getChild(i).getChild(1).getChild(0).label().value() + " " + Sentence.listToString(t.getChild(i).getChild(2).yield());
								}
								else if (t.getChild(i-1).label().value().equals(",")) {
									phraseToDelete = ", " + t.getChild(i).getChild(0).getChild(0).label().value() + " " + t.getChild(i).getChild(1).getChild(0).label().value() + " " + Sentence.listToString(t.getChild(i).getChild(2).yield());
								}
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
							else if (t.getChild(i).getChild(1).getChild(0).label().value().equals("although")) {
								String phrase = "But " + Sentence.listToString(t.getChild(i).getChild(2).yield()) + " .";
								String phraseToDelete = "";
								//System.out.println("phrase to delete: "  + phraseToDelete);
								
								if (i == 0) {
									phraseToDelete = t.getChild(i).getChild(0).getChild(0).label().value() + " " + t.getChild(i).getChild(1).getChild(0).label().value() + " " + Sentence.listToString(t.getChild(i).getChild(2).yield());
								}
								else if (t.getChild(i-1).label().value().equals(",")) {
									phraseToDelete = ", " + t.getChild(i).getChild(0).getChild(0).label().value() + " " + t.getChild(i).getChild(1).getChild(0).label().value() + " " + Sentence.listToString(t.getChild(i).getChild(2).yield());
								}
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}*/
						}
					}
				}
			}
		}
		
		return isSplit;
	}
	
	
	public static boolean ifSplit(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			/**
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("SBAR") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("NP")) {
							
							if (t.getChild(i).getChildrenAsList().size() >= 2) {
								if (t.getChild(i).getChild(0).label().value().equals("IN") && t.getChild(i).getChild(1).label().value().equals("S")) {
									
									if (t.getChild(i).getChild(0).getChild(0).label().value().equals("If")) {
										
										String phrase1 = Sentence.listToString(t.getChild(i).getChild(1).yield());
										
										String phraseToDeleteForPhrase2 = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield());
										String phrase2 = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "").trim();
										//System.out.println("phrase2: "  + phrase2);
										String delete = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
										
										if (isOriginal) {
											int[] coreStartEnd1 = SentenceProcessor.matchSentences(Sentence.listToString(coreContextSentence.getOriginal().yield()), SentenceProcessor.collapseWhitespace(phrase1));
											Integer start1 = coreStartEnd1[0];
											Integer end1 = coreStartEnd1[1];
											String pre1 = "Suppose ";
											String post1 = "";
											Core.getStart().add(start1);
											Core.getEnd().add(end1);
											Core.getPrefix().add(pre1);
											Core.getPostfix().add(post1);
											
											int[] coreStartEnd2 = SentenceProcessor.matchSentences(Sentence.listToString(coreContextSentence.getOriginal().yield()), SentenceProcessor.collapseWhitespace(phrase2));
											Integer start2 = coreStartEnd2[0];
											Integer end2 = coreStartEnd2[1];
											String pre2 = "Then ";
											String post2 = "";
											Core.getStart().add(start2);
											Core.getEnd().add(end2);
											Core.getPrefix().add(pre2);
											Core.getPostfix().add(post2);
										} else {
											SentenceProcessor.updateSentence(phrase1, phraseToDeleteForPhrase2.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											SentenceProcessor.updateSentence(phrase2, delete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										}
										
										isSplit = true;
									}
								}
							}
						}
					}
				}
			}
			*/
			
			if (t.getChildrenAsList().size() >= 2) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR") && i == t.getChildrenAsList().size()-2) {
						if (t.getChild(i+1).getChildrenAsList().size() >= 2) {
							if (t.getChild(i+1).getChild(0).label().value().equals("RB") && t.getChild(i+1).getChild(1).label().value().equals("IN")) {
								if (t.getChild(i+1).getChild(1).getChild(0).label().value().equals("if")) {
									String aux = SentenceProcessor.setAux(true, isPresent);
									String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
									String phraseToDelete = ", " +  Sentence.listToString(t.getChild(i+1).yield());
									
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
						}
						if (t.getChild(i+1).getChild(0).label().value().equals("IN")) {
							if (t.getChild(i+1).getChild(0).getChild(0).label().value().equals("if")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
								String phraseToDelete = ", " +  Sentence.listToString(t.getChild(i+1).yield());
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
			
			if (t.getChildrenAsList().size() >= 3) {
				for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
					if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR") && t.getChild(i+2).label().value().equals(",")) {
						if (t.getChild(i+1).getChildrenAsList().size() >= 2) {
							if (t.getChild(i+1).getChild(0).label().value().equals("RB") && t.getChild(i+1).getChild(1).label().value().equals("IN")) {
								if (t.getChild(i+1).getChild(1).getChild(0).label().value().equals("if")) {
									String aux = SentenceProcessor.setAux(true, isPresent);
									String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
									String phraseToDelete = ", " +  Sentence.listToString(t.getChild(i+1).yield()) + " ,";
									
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
						}
						if (t.getChild(i+1).getChild(0).label().value().equals("IN")) {
							if (t.getChild(i+1).getChild(0).getChild(0).label().value().equals("if")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String phrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
								String phraseToDelete = ", " +  Sentence.listToString(t.getChild(i+1).yield()) + " ,";
								
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
	
	
	public static boolean extractWhilePlusParticiple(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
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
	
	
	public static boolean extractSo(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			if (t.getChildrenAsList().size() >= 3) {
				for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
					if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("IN") && t.getChild(i+2).label().value().equals("S")) {
						if (t.getChild(i+1).getChild(0).label().value().equals("so")) {
							String phrase = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
							String phraseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
							
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
		}
	
		return isSplit;
	}
}
