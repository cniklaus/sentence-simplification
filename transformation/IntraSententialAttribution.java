package transformation;

import java.util.ArrayList;
import java.util.List;

import analysis.RepresentationGenerator;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

/**
 * Class for extracting intra-sentential attributions
 * 
 * @author christina
 *
 */
public class IntraSententialAttribution {

	/**
	 * extracts intra-sentential attributions of the type "A says that ..." from the input sentence and transforms them into stand-alone context sentences,
	 * returns the attribution which is to be deleted from the input
	 * 
	 * @param coreContextSentence
	 * @param parse
	 * @param isOriginal
	 * @param contextNumber
	 * @return
	 */
	public static ArrayList<String> extractIntraSententialAttributions(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		String sentence = Sentence.listToString(parse.yield());
		String aux = SentenceProcessor.setAux(true, isPresent);
		String phrase = "";
		String phraseToDelete = "";
		ArrayList<String> del = new ArrayList<String>();
		
		for (Tree t : parse) {
			if (t.getChildrenAsList().size() >= 2 && t.label().value().equals("S") && !t.ancestor(1, parse).label().value().equals("SBAR")) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals("NP") &&
							t.getChild(i+1).label().value().equals("VP")) {
						boolean noun = false;
						List<LabeledWord> label = t.getChild(i).labeledYield();
						for (LabeledWord l : label) {
							if (l.tag().value().equals("NN") || l.tag().value().equals("NNS") || l.tag().value().equals("NNP") || l.tag().value().equals("NNPS")) {
								noun = true;
							}
						}
						
						if (t.getChild(i+1).getChildrenAsList().size() >= 2) {
							if (t.getChild(i).getChild(0).label().value().equals("PRP") || noun) {
								if (t.getChild(i+1).getChild(0).label().value().equals("VBD") || 
										t.getChild(i+1).getChild(0).label().value().equals("VBZ") ||
										t.getChild(i+1).getChild(0).label().value().equals("VBP")) {
									if (t.getChild(i+1).getChild(1).label().value().equals("SBAR")) {
										if (t.getChild(i+1).getChild(1).getChild(0).label().value().equals("IN")) {
											if (t.getChild(i+1).getChild(1).getChild(0).getChild(0).label().value().equals("that")) {
												boolean pp = false;
												boolean vp = false;
												
												if (i > 1) {
													if (t.getChild(i-2).label().value().equals("PP") &&
															t.getChild(i-1).label().value().equals(",")) {
														pp = true;
													}
													if (t.getChild(i-2).label().value().equals("S") &&
															t.getChild(i-1).label().value().equals(",")) {
														if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
															if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																	t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																vp = true;
															}
														}
													}
												}
												
												if (pp) {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
												} else if (vp) {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
												} else {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
												}
												
												coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
												SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											    del.add(phraseToDelete);
											}
										}
									}
									
									if (t.getChild(i+1).getChildrenAsList().size() >= 4) {
										if (t.getChild(i+1).getChild(0).label().value().equals("VBD") ||
												t.getChild(i+1).getChild(0).label().value().equals("VBP") ||
												t.getChild(i+1).getChild(0).label().value().equals("VBZ")) {
											if (t.getChild(i+1).getChild(1).label().value().equals(",")) {
												if (t.getChild(i+1).getChild(2).label().value().equals("``")) {
													if (t.getChild(i+1).getChild(3).label().value().equals("S")) {
														boolean pp = false;
														boolean vp = false;
														
														if (i > 1) {
															if (t.getChild(i-2).label().value().equals("PP") &&
																	t.getChild(i-1).label().value().equals(",")) {
																pp = true;
															}
															if (t.getChild(i-2).label().value().equals("S") &&
																	t.getChild(i-1).label().value().equals(",")) {
																if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
																	if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																			t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																		vp = true;
																	}
																}
															}
														}
														
														if (pp) {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
															phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield());
														} else if (vp) {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
															phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield());
														} else {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " .";
															phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
														}
														
														coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
														SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													    del.add(phraseToDelete);
													}
												}
											}
										}
									}
									
									if (t.getChild(i+1).getChild(1).getChildrenAsList().size() >= 4) {
										if (t.getChild(i+1).getChild(1).getChild(0).label().value().equals("SBAR") &&
												t.getChild(i+1).getChild(1).getChild(1).label().value().equals(",") &&
												t.getChild(i+1).getChild(1).getChild(2).label().value().equals("CC") &&
												t.getChild(i+1).getChild(1).getChild(3).label().value().equals("SBAR")) {
											if (t.getChild(i+1).getChild(1).getChild(0).getChild(0).label().value().equals("IN")) {
												if (t.getChild(i+1).getChild(1).getChild(0).getChild(0).getChild(0).label().value().equals("that")) {
													if (t.getChild(i+1).getChild(1).getChild(3).getChild(0).label().value().equals("IN")) {
														if (t.getChild(i+1).getChild(1).getChild(3).getChild(0).getChild(0).label().value().equals("that")) {
															String phraseToDelete2 = " , " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(2).getChild(0).yield()) + " that " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(3).getChild(1).yield()) + " .";
															boolean pp = false;
															boolean vp = false;
															
															if (i > 1) {
																if (t.getChild(i-2).label().value().equals("PP") &&
																		t.getChild(i-1).label().value().equals(",")) {
																	pp = true;
																}
																if (t.getChild(i-2).label().value().equals("S") &&
																		t.getChild(i-1).label().value().equals(",")) {
																	if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
																		if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																				t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																			vp = true;
																		}
																	}
																}
															}
															
															String phrase2 = Sentence.listToString(t.getChild(i+1).getChild(1).getChild(2).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(3).getChild(1).yield()) + " .";
															
															if (pp) {
																phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
																phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
															} else if (vp) {
																phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
																phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
															} else {
																 phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " .";	
																 phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
															}
															
															SentenceProcessor.addCore(phrase2, coreContextSentence);
															coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
															SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
															SentenceProcessor.updateSentence(null, phraseToDelete2.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
															del.add(phraseToDelete);
														}
													}
												}
											}
											
										}
									}
								}
								if (t.getChild(i+1).getChild(1).label().value().equals("VP")) {
									if (t.getChild(i+1).getChild(1).getChildrenAsList().size() >= 2) {
										if (t.getChild(i+1).getChild(1).getChild(0).label().value().equals("VBN") &&
												t.getChild(i+1).getChild(1).getChild(1).label().value().equals("SBAR")) {
											if (t.getChild(i+1).getChild(1).getChild(1).getChild(0).label().value().equals("IN")) {
												if (t.getChild(i+1).getChild(1).getChild(1).getChild(0).getChild(0).label().value().equals("that")) {
													boolean pp = false;
													boolean vp = false;
														
													if (i > 1) {
														if (t.getChild(i-2).label().value().equals("PP") &&
																t.getChild(i-1).label().value().equals(",")) {
															pp = true;
														}
														if (t.getChild(i-2).label().value().equals("S") &&
																t.getChild(i-1).label().value().equals(",")) {
															if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
																if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																		t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																	vp = true;
																}
															}
														}
													}
														
													if (pp) {
														if (noun) {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
														} else {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
														}
														phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " that";
													} else if (vp) {
														if (noun) {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
														} else {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
														}
														phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " that";
													} else {
														if (noun) {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " .";
														} else {
															phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " .";
														}
														phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " that";	
													}
													coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
													SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													del.add(phraseToDelete);
												}		
											}
										}
									}
								}
							}
						}
						
						if (t.getChild(i+1).getChildrenAsList().size() >= 3) {
							if (t.getChild(i+1).getChild(0).label().value().equals("VBD") ||
									t.getChild(i+1).getChild(0).label().value().equals("VBP") ||
									t.getChild(i+1).getChild(0).label().value().equals("VBZ")) {
								if (t.getChild(i+1).getChild(1).label().value().equals("PP") ||
										t.getChild(i+1).getChild(1).label().value().equals("ADVP")) {
									if (t.getChild(i+1).getChild(2).label().value().equals("SBAR")) {
										if (t.getChild(i+1).getChild(2).getChild(0).label().value().equals("IN")) {
											if (t.getChild(i+1).getChild(2).getChild(0).getChild(0).label().value().equals("that")) {
												boolean pp = false;
												boolean vp = false;
												
												if (i > 1) {
													if (t.getChild(i-2).label().value().equals("PP") &&
															t.getChild(i-1).label().value().equals(",")) {
														pp = true;
													}
													if (t.getChild(i-2).label().value().equals("S") &&
															t.getChild(i-1).label().value().equals(",")) {
														if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
															if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																	t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																vp = true;
															}
														}
													}
												}
															
												if (pp) {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " that";
												} else if (vp) {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " that";
												} else {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield())  + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " that";
												}	
													
												coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
												SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
												del.add(phraseToDelete);	
											}
										}
									}
									if (t.getChild(i+1).getChild(2).label().value().equals("VP")) {
										if (t.getChild(i+1).getChild(2).getChild(0).label().value().equals("VBN") &&
												t.getChild(i+1).getChild(2).getChild(1).label().value().equals("SBAR")) {
											if (t.getChild(i+1).getChild(2).getChild(1).getChild(0).label().value().equals("IN")) {
												if (t.getChild(i+1).getChild(2).getChild(1).getChild(0).getChild(0).label().value().equals("that")) {
													boolean pp = false;
													boolean vp = false;
													
													if (i > 1) {
														if (t.getChild(i-2).label().value().equals("PP") &&
																t.getChild(i-1).label().value().equals(",")) {
															pp = true;
														}
														if (t.getChild(i-2).label().value().equals("S") &&
																t.getChild(i-1).label().value().equals(",")) {
															if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
																if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																		t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																	vp = true;
																}
															}
														}
													}	
														
													if (pp) {
														phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(2).getChild(0).yield())  + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
														phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield())  + " " + Sentence.listToString(t.getChild(i+1).getChild(2).getChild(0).yield())  + " that";
													} else if (vp) {
														phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(2).getChild(0).yield())  + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
														phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(2).getChild(0).yield())  + " that";
													} else {
														phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(2).getChild(0).yield())  + " .";
														phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(2).getChild(0).yield())  + " that";
													}	
														
													coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
													SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
													del.add(phraseToDelete);
												}
											}
										}
									}
								}
							}
						}
						
						if (t.getChild(i+1).getChild(0).label().value().equals("ADVP")) {
							if (t.getChild(i+1).getChild(1).label().value().equals("VBD") ||
									t.getChild(i+1).getChild(1).label().value().equals("VBZ") ||
									t.getChild(i+1).getChild(1).label().value().equals("VBP")) {
								if (t.getChild(i+1).getChild(2).label().value().equals("SBAR")) {
									if (t.getChild(i+1).getChild(2).getChild(0).label().value().equals("IN")) {
										if (t.getChild(i+1).getChild(2).getChild(0).getChild(0).label().value().equals("that")) {
											boolean pp = false;
											boolean vp = false;
											
											if (i > 1) {
												if (t.getChild(i-2).label().value().equals("PP") &&
														t.getChild(i-1).label().value().equals(",")) {
													pp = true;
												}
												if (t.getChild(i-2).label().value().equals("S") &&
														t.getChild(i-1).label().value().equals(",")) {
													if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
														if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
															vp = true;
														}
													}
												}
											}	
												
											if (pp) {
												phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
												phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " that";
											} else if (vp) {
												phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
												phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " that";
											} else {
												phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield())  + " .";
												phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " that";
											}	
												
											coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
											SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											del.add(phraseToDelete);	
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
					if (t.getChild(i).label().value().equals("NP") &&
						t.getChild(i+1).label().value().equals("ADVP") &&
						t.getChild(i+2).label().value().equals("VP")) {
						boolean noun = false;
						List<LabeledWord> label = t.getChild(i).labeledYield();
						for (LabeledWord l : label) {
							if (l.tag().value().equals("NN") || l.tag().value().equals("NNS") || l.tag().value().equals("NNP") || l.tag().value().equals("NNPS")) {
								noun = true;
							}
						}
							
						if (t.getChild(i+2).getChildrenAsList().size() >= 2) {
							if (t.getChild(i).getChild(0).label().value().equals("PRP") || noun) {
								if (t.getChild(i+2).getChild(0).label().value().equals("VBD") || 
										t.getChild(i+2).getChild(0).label().value().equals("VBZ") ||
										t.getChild(i+2).getChild(0).label().value().equals("VBP")) {
									if (t.getChild(i+2).getChild(1).label().value().equals("SBAR")) {
										if (t.getChild(i+2).getChild(1).getChild(0).label().value().equals("IN")) {
											if (t.getChild(i+2).getChild(1).getChild(0).getChild(0).label().value().equals("that")) {
												boolean pp = false;
												boolean vp = false;
													
												if (i > 1) {
													if (t.getChild(i-2).label().value().equals("PP") &&
															t.getChild(i-1).label().value().equals(",")) {
														pp = true;
													}
													if (t.getChild(i-2).label().value().equals("S") &&
															t.getChild(i-1).label().value().equals(",")) {
														if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
															if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																	t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																vp = true;
															}
														}
													}
												}		
														
												if (pp) {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " that";
												} else if (vp) {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " that";
												} else {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield())  + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " that";
												}	
														
												coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
												SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);	
												del.add(phraseToDelete);	
											}
										}
									}
									
									if (t.getChild(i+2).getChild(1).getChildrenAsList().size() >= 4) {
										if (t.getChild(i+2).getChild(1).getChild(0).label().value().equals("SBAR") &&
												t.getChild(i+2).getChild(1).getChild(1).label().value().equals(",") &&
												t.getChild(i+2).getChild(1).getChild(2).label().value().equals("CC") &&
												t.getChild(i+2).getChild(1).getChild(3).label().value().equals("SBAR")) {
											if (t.getChild(i+2).getChild(1).getChild(0).getChild(0).label().value().equals("IN")) {
												if (t.getChild(i+2).getChild(1).getChild(0).getChild(0).getChild(0).label().value().equals("that")) {
													if (t.getChild(i+2).getChild(1).getChild(3).getChild(0).label().value().equals("IN")) {
														if (t.getChild(i+2).getChild(1).getChild(3).getChild(0).getChild(0).label().value().equals("that")) {
															String phraseToDelete2 = " , " + Sentence.listToString(t.getChild(i+2).getChild(1).getChild(2).getChild(0).yield()) + " that " + Sentence.listToString(t.getChild(i+2).getChild(1).getChild(3).getChild(1).yield()) + " .";
															boolean pp = false;
															boolean vp = false;
															
															if (i > 1) {
																if (t.getChild(i-2).label().value().equals("PP") &&
																		t.getChild(i-1).label().value().equals(",")) {
																	pp = true;
																}
																if (t.getChild(i-2).label().value().equals("S") &&
																		t.getChild(i-1).label().value().equals(",")) {
																	if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
																		if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																				t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																			vp = true;
																		}
																	}
																}
															}
																
															String phrase2 = Sentence.listToString(t.getChild(i+2).getChild(1).getChild(2).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).getChild(3).getChild(1).yield()) + " .";	
															
															if (pp) {
																phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
																phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " that";
															} else if (vp) {
																phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
																phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " that";
															} else {
																 phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " .";
																 phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " that";
																
															}	
														
															coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
															SentenceProcessor.addCore(phrase2, coreContextSentence);
															SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
															SentenceProcessor.updateSentence(null, phraseToDelete2.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
															del.add(phraseToDelete);
														}
													}
												}
											}
										}
									}
								}
							}
						}
						
						if (t.getChild(i+2).getChildrenAsList().size() >= 3) {
							if (t.getChild(i+2).getChild(0).label().value().equals("VBD") ||
									t.getChild(i+2).getChild(0).label().value().equals("VBP") ||
									t.getChild(i+2).getChild(0).label().value().equals("VBZ")) {
								if (t.getChild(i+2).getChild(1).label().value().equals("PP") ||
										t.getChild(i+2).getChild(1).label().value().equals("ADVP")) {
									if (t.getChild(i+2).getChild(2).label().value().equals("SBAR")) {
										if (t.getChild(i+2).getChild(2).getChild(0).label().value().equals("IN")) {
											if (t.getChild(i+2).getChild(2).getChild(0).getChild(0).label().value().equals("that")) {
												boolean pp = false;
												boolean vp = false;
													
												if (i > 1) {
													if (t.getChild(i-2).label().value().equals("PP") &&
															t.getChild(i-1).label().value().equals(",")) {
														pp = true;
													}
													if (t.getChild(i-2).label().value().equals("S") &&
															t.getChild(i-1).label().value().equals(",")) {
														if (t.getChild(i-2).getChild(0).label().value().equals("VP")) {
															if (t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBN") ||
																	t.getChild(i-2).getChild(0).getChild(0).label().value().equals("VBG")) {
																vp = true;
															}
														}
													}
												}		
														
												if (pp) {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " that";
												} else if (vp) {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " when " + Sentence.listToString(t.getChild(i-2).yield()) + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i-2).yield()) + " , " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " that";
												} else {
													phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield())  + " .";
													phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " that";
												}	
														
												coreContextSentence.getAttribution().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(phrase)));
												SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);	
												del.add(phraseToDelete);	 
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
		return del;
	}
}