package sentenceCompression;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class IntraSententialAttribution {

	
	public static void extractIntraSententialAttributions(CoreContextSentence coreContextSentence, Tree tree) {
		
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		
		for (Tree t : tree) {
			if (t.getChildrenAsList().size() >= 2) {
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
												System.out.println("......................................................");
												String aux = SentenceProcessor.setAux(true, isPresent);
												String phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " .";
												String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
												//System.out.println(phraseToDelete);
												//System.out.println("success1: " + phraseToDelete);
												coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
												
												ArrayList<Tree> core = coreContextSentence.getCore();
												ArrayList<Tree> coreNew = coreContextSentence.getCoreNew();
												
												ArrayList<String> coreString = new ArrayList<String>();
												ArrayList<String> coreNewString = new ArrayList<String>();
												
												for (Tree tr : core) {
													String s = Sentence.listToString(tr.yield());
													coreString.add(s);
												}
												
												for (Tree tr : coreNew) {
													String s = Sentence.listToString(tr.yield());
													coreNewString.add(s);
												}
												
												int n = 0;
												for (String str : coreString) {
													if (str.contains(phraseToDelete)) {
														str = str.replace(phraseToDelete, "");
														coreContextSentence.getCore().set(n, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
													}
													n++;
												}
												
												int m = 0;
												for (String str : coreNewString) {
													if (str.contains(phraseToDelete)) {
														str = str.replace(phraseToDelete, "");
														coreContextSentence.getCoreNew().set(m, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
													}
													m++;
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
															System.out.println("......................................................");
															String aux = SentenceProcessor.setAux(true, isPresent);
															String phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " .";
															
															String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " that";
															String phraseToDelete2 = " , " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(2).getChild(0).yield()) + " that " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(3).getChild(1).yield()) + " .";
															
															String core2 = Sentence.listToString(t.getChild(i+1).getChild(1).getChild(2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(3).getChild(1).yield()) + " .";
															coreContextSentence.getCoreNew().add(SentenceProcessor.parse(SentenceProcessor.tokenize(core2)));
															
															System.out.println(phraseToDelete2);
															//System.out.println("success1: " + phraseToDelete);
															coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
															
															ArrayList<Tree> core = coreContextSentence.getCore();
															ArrayList<Tree> coreNew = coreContextSentence.getCoreNew();
															
															ArrayList<String> coreString = new ArrayList<String>();
															ArrayList<String> coreNewString = new ArrayList<String>();
															
															for (Tree tr : core) {
																String s = Sentence.listToString(tr.yield());
																coreString.add(s);
															}
															
															for (Tree tr : coreNew) {
																String s = Sentence.listToString(tr.yield());
																coreNewString.add(s);
															}
															
															int n = 0;
															for (String str : coreString) {
																if (str.contains(phraseToDelete)) {
																	str = str.replace(phraseToDelete, "");
																	str = str.replace(phraseToDelete2, "");
																	str = str + " .";
																	coreContextSentence.getCore().set(n, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
																}
																n++;
															}
															
															int m = 0;
															for (String str : coreNewString) {
																if (str.contains(phraseToDelete)) {
																	str = str.replace(phraseToDelete, "");
																	str = str.replace(phraseToDelete2, "");
																	coreContextSentence.getCoreNew().set(m, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
																}
																m++;
															}
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
													System.out.println("......................................................");
													String aux = SentenceProcessor.setAux(true, isPresent);
													String phrase = "";
													if (noun) {
														phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " .";
													} else {
														phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " .";
													}
													 
													String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).yield()) + " that";
													System.out.println(phraseToDelete);
													//System.out.println("success1: " + phraseToDelete);
													coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
													
													ArrayList<Tree> core = coreContextSentence.getCore();
													ArrayList<Tree> coreNew = coreContextSentence.getCoreNew();
													
													ArrayList<String> coreString = new ArrayList<String>();
													ArrayList<String> coreNewString = new ArrayList<String>();
													
													for (Tree tr : core) {
														String s = Sentence.listToString(tr.yield());
														coreString.add(s);
													}
													
													for (Tree tr : coreNew) {
														String s = Sentence.listToString(tr.yield());
														coreNewString.add(s);
													}
													
													int n = 0;
													for (String str : coreString) {
														if (str.contains(phraseToDelete)) {
															str = str.replace(phraseToDelete, "");
															coreContextSentence.getCore().set(n, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
														}
														n++;
													}
													
													int m = 0;
													for (String str : coreNewString) {
														if (str.contains(phraseToDelete)) {
															str = str.replace(phraseToDelete, "");
															coreContextSentence.getCoreNew().set(m, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
														}
														m++;
													}
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
												System.out.println("......................................................");
												String aux = SentenceProcessor.setAux(true, isPresent);
												String phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield())  + " .";
												String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " that";
												//System.out.println(phraseToDelete);
												//System.out.println("success1: " + phraseToDelete);
												coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
												
												ArrayList<Tree> core = coreContextSentence.getCore();
												ArrayList<Tree> coreNew = coreContextSentence.getCoreNew();
												
												ArrayList<String> coreString = new ArrayList<String>();
												ArrayList<String> coreNewString = new ArrayList<String>();
												
												for (Tree tr : core) {
													String s = Sentence.listToString(tr.yield());
													coreString.add(s);
												}
												
												for (Tree tr : coreNew) {
													String s = Sentence.listToString(tr.yield());
													coreNewString.add(s);
												}
												
												int n = 0;
												for (String str : coreString) {
													if (str.contains(phraseToDelete)) {
														str = str.replace(phraseToDelete, "");
														coreContextSentence.getCore().set(n, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
													}
													n++;
												}
												
												int m = 0;
												for (String str : coreNewString) {
													if (str.contains(phraseToDelete)) {
														str = str.replace(phraseToDelete, "");
														coreContextSentence.getCoreNew().set(m, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
													}
													m++;
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
												System.out.println("......................................................");
												String aux = SentenceProcessor.setAux(true, isPresent);
												String phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield())  + " .";
												String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " that";
												//System.out.println(phraseToDelete);
												//System.out.println("success1: " + phraseToDelete);
												coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
																
												ArrayList<Tree> core = coreContextSentence.getCore();
												ArrayList<Tree> coreNew = coreContextSentence.getCoreNew();
												
												ArrayList<String> coreString = new ArrayList<String>();
												ArrayList<String> coreNewString = new ArrayList<String>();
																
												for (Tree tr : core) {
													String s = Sentence.listToString(tr.yield());
													coreString.add(s);
												}
												
												for (Tree tr : coreNew) {
													String s = Sentence.listToString(tr.yield());
													coreNewString.add(s);
												}
												
												int n = 0;
												for (String str : coreString) {
													if (str.contains(phraseToDelete)) {
														str = str.replace(phraseToDelete, "");
														coreContextSentence.getCore().set(n, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
													}
													n++;
												}
																
												int m = 0;
												for (String str : coreNewString) {
													if (str.contains(phraseToDelete)) {
														str = str.replace(phraseToDelete, "");
														coreContextSentence.getCoreNew().set(m, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
													}
													m++;
												}
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
															System.out.println("......................................................");
															String aux = SentenceProcessor.setAux(true, isPresent);
															String phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " .";
															
															String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " that";
															String phraseToDelete2 = " , " + Sentence.listToString(t.getChild(i+2).getChild(1).getChild(2).getChild(0).yield()) + " that " + Sentence.listToString(t.getChild(i+2).getChild(1).getChild(3).getChild(1).yield()) + " .";
															
															String core2 = Sentence.listToString(t.getChild(i+2).getChild(1).getChild(2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).getChild(3).getChild(1).yield()) + " .";
															coreContextSentence.getCoreNew().add(SentenceProcessor.parse(SentenceProcessor.tokenize(core2)));
															
															//System.out.println(phraseToDelete2);
															//System.out.println("success1: " + phraseToDelete);
															coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
															
															ArrayList<Tree> core = coreContextSentence.getCore();
															ArrayList<Tree> coreNew = coreContextSentence.getCoreNew();
															
															ArrayList<String> coreString = new ArrayList<String>();
															ArrayList<String> coreNewString = new ArrayList<String>();
															
															for (Tree tr : core) {
																String s = Sentence.listToString(tr.yield());
																coreString.add(s);
															}
															
															for (Tree tr : coreNew) {
																String s = Sentence.listToString(tr.yield());
																coreNewString.add(s);
															}
															
															int n = 0;
															for (String str : coreString) {
																if (str.contains(phraseToDelete)) {
																	str = str.replace(phraseToDelete, "");
																	str = str.replace(phraseToDelete2, "");
																	str = str + " .";
																	coreContextSentence.getCore().set(n, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
																}
																n++;
															}
															
															int m = 0;
															for (String str : coreNewString) {
																if (str.contains(phraseToDelete)) {
																	str = str.replace(phraseToDelete, "");
																	str = str.replace(phraseToDelete2, "");
																	coreContextSentence.getCoreNew().set(m, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
																}
																m++;
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
						if (t.getChild(i+2).getChildrenAsList().size() >= 3) {
							
							if (t.getChild(i+2).getChild(0).label().value().equals("VBD") ||
									t.getChild(i+2).getChild(0).label().value().equals("VBP") ||
									t.getChild(i+2).getChild(0).label().value().equals("VBZ")) {
								if (t.getChild(i+2).getChild(1).label().value().equals("PP") ||
										t.getChild(i+2).getChild(1).label().value().equals("ADVP")) {
									if (t.getChild(i+2).getChild(2).label().value().equals("SBAR")) {
										if (t.getChild(i+2).getChild(2).getChild(0).label().value().equals("IN")) {
											if (t.getChild(i+2).getChild(2).getChild(0).getChild(0).label().value().equals("that")) {
												System.out.println("......................................................");
												String aux = SentenceProcessor.setAux(true, isPresent);
												String phrase = "This" + aux + " what " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield())  + " .";
												String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " that";
												//System.out.println(phraseToDelete);
												//System.out.println("success1: " + phraseToDelete);
												coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
												
												ArrayList<Tree> core = coreContextSentence.getCore();
												ArrayList<Tree> coreNew = coreContextSentence.getCoreNew();
												
												ArrayList<String> coreString = new ArrayList<String>();
												ArrayList<String> coreNewString = new ArrayList<String>();
												
												for (Tree tr : core) {
													String s = Sentence.listToString(tr.yield());
													coreString.add(s);
												}
												
												for (Tree tr : coreNew) {
													String s = Sentence.listToString(tr.yield());
													coreNewString.add(s);
												}
												
												int n = 0;
												for (String str : coreString) {
													if (str.contains(phraseToDelete)) {
														str = str.replace(phraseToDelete, "");
														coreContextSentence.getCore().set(n, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
													}
													n++;
												}
												
												int m = 0;
												for (String str : coreNewString) {
													if (str.contains(phraseToDelete)) {
														str = str.replace(phraseToDelete, "");
														coreContextSentence.getCoreNew().set(m, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
													}
													m++;
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
