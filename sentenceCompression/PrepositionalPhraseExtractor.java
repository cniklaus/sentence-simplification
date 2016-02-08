package sentenceCompression;


import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;

public class PrepositionalPhraseExtractor {

	static MaxentTagger tagger = new MaxentTagger("tagger/english-left3words-distsim.tagger");
	
	
	public static boolean extractInitialPPs(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
	 	
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		//System.out.println(isPresent);
		
		for (Tree t : parse) {
			
			if (t.label().value().equals("S")) {
				if (t.getChild(0).label().value().equals("PP")) {
					if (t.getChildrenAsList().size()==1) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
						String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield());
						
						String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " .";
						//System.out.println(phraseToCompare);
						
						boolean extract = true;
						ArrayList<Tree> a = coreContextSentence.getAttribution();
						for (Tree tr : a) {
							String attr = Sentence.listToString(tr.yield());
							//System.out.println("sfaf " + attr);
							
							String ph = Sentence.listToString(t.getChild(0).yield());
							//System.out.println("afsdf" + ph);
							if (attr.contains(ph)) {
								extract = false;
								//System.out.println("yay");
							}
						}
						
						if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
					else {
						boolean comma = false;
						for (int i = 1; i < t.getChildrenAsList().size(); i++) {
							if (t.getChild(i).label().value().equals(",")) {
								comma = true;
							}
						}
						if (comma == false && !(t.getChild(1).getChild(0).getChild(0).label().value().equals("is") || t.getChild(1).getChild(0).getChild(0).label().value().equals("are") || t.getChild(1).getChild(0).getChild(0).label().value().equals("was") || t.getChild(1).getChild(0).getChild(0).label().value().equals("were"))) {
							String aux = SentenceProcessor.setAux(true, isPresent);
							String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
							String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield());
							
							String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " .";
							//System.out.println(phraseToCompare);
							
							boolean extract = true;
							ArrayList<Tree> a = coreContextSentence.getAttribution();
							for (Tree tr : a) {
								String attr = Sentence.listToString(tr.yield());
								//System.out.println("sfaf " + attr);
								
								String ph = Sentence.listToString(t.getChild(0).yield());
								//System.out.println("afsdf" + ph);
								if (attr.contains(ph)) {
									extract = false;
									//System.out.println("yay");
								}
							}
							
							
							if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
								SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
				if (t.getChildrenAsList().size() >= 2)	{
					if (t.getChild(0).label().value().equals("PP") && t.getChild(1).label().value().equals(",")) {
						//if (t.getChild(0).getChild(1).label().value().equals("TO") || t.getChild(0).getChild(1).label().value().equals("IN") || t.getChild(0).getChild(0).label().value().equals("TO") || t.getChild(0).getChild(0).label().value().equals("IN")) {
							String aux = SentenceProcessor.setAux(true, isPresent);
							String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
							String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " ,";
							
							String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " .";
							//System.out.println(phraseToCompare);
							
							boolean extract = true;
							ArrayList<Tree> a = coreContextSentence.getAttribution();
							for (Tree tr : a) {
								String attr = Sentence.listToString(tr.yield());
								//System.out.println("sfaf " + attr);
								
								String ph = Sentence.listToString(t.getChild(0).yield());
								//System.out.println("afsdf" + ph);
								if (attr.contains(ph)) {
									extract = false;
									//System.out.println("yay");
								}
							}
							//System.out.println(extract);
							
							if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
								SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						//}
					}
					
					if (t.getChildrenAsList().size() >= 2) {
						
						for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
							if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP")) {
								if (t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN")) {
									String aux = SentenceProcessor.setAux(true, isPresent);
									String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
									String PPphraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
									
									String phraseToCompare = Sentence.listToString(t.getChild(i+1).yield()) + " .";
									//System.out.println(phraseToCompare);
									//System.out.println(PPphrase);
									boolean extract = true;
									ArrayList<Tree> a = coreContextSentence.getAttribution();
									for (Tree tr : a) {
										String attr = Sentence.listToString(tr.yield());
										//System.out.println("sfaf " + attr);
										
										String ph = Sentence.listToString(t.getChild(0).yield());
										//System.out.println("afsdf" + ph);
										if (attr.contains(ph)) {
											extract = false;
											//System.out.println("yay");
										}
									}
									
									if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
										SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
						}
						
						if (t.getChild(0).label().value().equals("PP") && t.getChild(1).label().value().equals(",")) {
							if (t.getChild(0).getChild(1).label().value().equals("TO") || t.getChild(0).getChild(1).label().value().equals("IN") || t.getChild(0).getChild(0).label().value().equals("TO") || t.getChild(0).getChild(0).label().value().equals("IN")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
								String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
								
								String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " .";
								//System.out.println("This is " + phraseToCompare.trim());
								//System.out.println("pp " + PPphrase);
								
								boolean extract = true;
								ArrayList<Tree> a = coreContextSentence.getAttribution();
								for (Tree tr : a) {
									String attr = Sentence.listToString(tr.yield());
									//System.out.println("sfaf " + attr);
									
									String ph = Sentence.listToString(t.getChild(0).yield());
									//System.out.println("afsdf" + ph);
									if (attr.contains(ph)) {
										extract = false;
										//System.out.println("yay");
									}
								}
								//System.out.println(extract);
								
								
								if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
									SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
							if (t.getChild(0).getChild(0).label().value().equals("VBN") || (t.getChild(0).getChild(0).label().value().equals("VBG") && !t.getChild(0).getChild(0).getChild(0).label().value().equals("According"))) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String PPphrase = "This" + aux + "when " + Sentence.listToString(t.getChild(0).yield()) + " .";
								String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
								
								String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " .";
								//System.out.println(phraseToCompare);
								
								boolean extract = true;
								ArrayList<Tree> a = coreContextSentence.getAttribution();
								for (Tree tr : a) {
									String attr = Sentence.listToString(tr.yield());
									//System.out.println("sfaf " + attr);
									
									String ph = Sentence.listToString(t.getChild(0).yield());
									//System.out.println("afsdf" + ph);
									if (attr.contains(ph)) {
										extract = false;
										//System.out.println("yay");
									}
								}
								
								if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
									SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
							if (t.getChild(0).getChild(0).label().value().equals("ADVP") && (t.getChild(0).getChild(1).label().value().equals("VBN") || (t.getChild(0).getChild(1).label().value().equals("VBG") && !t.getChild(0).getChild(1).getChild(0).label().value().equals("According")))) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								String PPphrase = "This" + aux + "when " + Sentence.listToString(t.getChild(0).yield()) + " .";
								String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
								
								String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " .";
								//System.out.println(phraseToCompare);
								
								boolean extract = true;
								ArrayList<Tree> a = coreContextSentence.getAttribution();
								for (Tree tr : a) {
									String attr = Sentence.listToString(tr.yield());
									//System.out.println("sfaf " + attr);
									
									String ph = Sentence.listToString(t.getChild(0).yield());
									//System.out.println("afsdf" + ph);
									if (attr.contains(ph)) {
										extract = false;
										//System.out.println("yay");
									}
								}
								
								
								if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
									SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
						}
					}
					
					if (t.getChildrenAsList().size() >= 3) {
						if (t.getChild(0).label().value().equals("PP") && t.getChild(1).label().value().equals("NP") && t.getChild(2).label().value().equals(",")) {
							String aux = SentenceProcessor.setAux(true, isPresent);
							String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " .";
							String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " , ";
							
							String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " .";
							//System.out.println(phraseToCompare);
							
							boolean extract = true;
							ArrayList<Tree> a = coreContextSentence.getAttribution();
							for (Tree tr : a) {
								String attr = Sentence.listToString(tr.yield());
								//System.out.println("sfaf " + attr);
								
								String ph = Sentence.listToString(t.getChild(0).yield());
								//System.out.println("afsdf" + ph);
								if (attr.contains(ph)) {
									extract = false;
									//System.out.println("yay");
								}
							}
							
							if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
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
									
									String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " .";
									//System.out.println(phraseToCompare);
									
									
									boolean extract = true;
									ArrayList<Tree> a = coreContextSentence.getAttribution();
									for (Tree tr : a) {
										String attr = Sentence.listToString(tr.yield());
										//System.out.println("sfaf " + attr);
										
										String ph = Sentence.listToString(t.getChild(0).yield());
										//System.out.println("afsdf" + ph);
										if (attr.contains(ph)) {
											extract = false;
											//System.out.println("yay");
										}
									}
									
									if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
										SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
							}
							
							else if (t.getChild(0).getChild(1).label().value().equals("IN") && t.getChild(0).getChild(2).label().value().equals("S")) {
								if (t.getChild(0).getChild(1).getChild(0).label().value().equals("As") || t.getChild(0).getChild(1).getChild(0).label().value().equals("Before") || t.getChild(0).getChild(1).getChild(0).label().value().equals("After") || t.getChild(0).getChild(1).getChild(0).label().value().equals("Since") || t.getChild(0).getChild(1).getChild(0).label().value().equals("While")) {
									String aux = SentenceProcessor.setAux(true, isPresent);
									
									String PPphrase = "This" + aux + Sentence.listToString(t.getChild(0).yield()) + " .";
									String PPphraseToDelete = Sentence.listToString(t.getChild(0).yield()) + " , ";
									
									String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " .";
									//System.out.println(phraseToCompare);
									
									boolean extract = true;
									ArrayList<Tree> a = coreContextSentence.getAttribution();
									for (Tree tr : a) {
										String attr = Sentence.listToString(tr.yield());
										//System.out.println("sfaf " + attr);
										
										String ph = Sentence.listToString(t.getChild(0).yield());
										//System.out.println("afsdf" + ph);
										if (attr.contains(ph)) {
											extract = false;
											//System.out.println("yay");
										}
									}
									
									
									if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
										SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
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
									
									String phraseToCompare = Sentence.listToString(t.getChild(0).yield()) + " " + Sentence.listToString(t.getChild(1).yield()) + " .";
									//System.out.println(phraseToCompare);
									
									boolean extract = true;
									ArrayList<Tree> a = coreContextSentence.getAttribution();
									for (Tree tr : a) {
										String attr = Sentence.listToString(tr.yield());
										//System.out.println("sfaf " + attr);
										
										String ph = Sentence.listToString(t.getChild(0).yield());
										//System.out.println("afsdf" + ph);
										if (attr.contains(ph)) {
											extract = false;
											//System.out.println("yay");
										}
									}
									
									if (extract && !sentence.equals("This is " + phraseToCompare.trim()) && !sentence.equals("This was " + phraseToCompare.trim())) {
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
	
	
	public static boolean extractInfixPPs(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		 	
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
			
		for (Tree t : parse) {
			for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && (t.getChild(i+2).label().value().equals("NP") || t.getChild(i+2).label().value().equals("PP")) && t.getChild(i+3).label().value().equals(",")) {
					//if (t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("VBN") ||
							//t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
						
						
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						
						
					//}
				}
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("ADVP") && t.getChild(i+2).label().value().equals("PP") && t.getChild(i+3).label().value().equals(",")) {
					//if (t.getChild(i+2).getChild(0).label().value().equals("TO") || t.getChild(i+2).getChild(0).label().value().equals("IN") || t.getChild(i+2).getChild(0).label().value().equals("VBN") ||
						//	t.getChild(i+2).getChild(1).label().value().equals("TO") || t.getChild(i+2).getChild(1).label().value().equals("IN") || t.getChild(i+2).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
						
						
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						
						
					//}
				}
			}
			for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",")) {
					//if (t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("VBN") ||
						//	t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";
						
						
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						
						
					//}
				}
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR") && t.getChild(i+2).label().value().equals(",")) {
					if ((t.getChild(i+1).getChild(0).label().value().equals("IN") && t.getChild(i+1).getChild(1).label().value().equals("S") &&
							(t.getChild(i+1).getChild(0).getChild(0).label().value().equals("before") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("after") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("as") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("while") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("until"))) ||
							(t.getChild(i+1).getChild(1).label().value().equals("IN") && t.getChild(i+1).getChild(2).label().value().equals("S") &&
							(t.getChild(i+1).getChild(1).getChild(0).label().value().equals("before") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("after") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("as") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("while") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("until")))) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";

						
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						
						
					}
				}
					
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && (t.getChild(i+2).label().value().equals("NP") || t.getChild(i+2).label().value().equals("PP")) && i == t.getChildrenAsList().size()-3) {
					//if (t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("VBN") ||
						//	t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
						
						
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						
						
					//}
				}
				
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("ADVP") && t.getChild(i+2).label().value().equals("PP") && i == t.getChildrenAsList().size()-3) {
					//if (t.getChild(i+2).getChild(0).label().value().equals("TO") || t.getChild(i+2).getChild(0).label().value().equals("IN") || t.getChild(i+2).getChild(0).label().value().equals("VBN") ||
						//	t.getChild(i+2).getChild(1).label().value().equals("TO") || t.getChild(i+2).getChild(1).label().value().equals("IN") || t.getChild(i+2).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
							
						
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						
						
					//}
				}
			}
				
			for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("PP") && i == t.getChildrenAsList().size()-2) {
					//if (t.getChild(i+1).getChild(0).label().value().equals("TO") || t.getChild(i+1).getChild(0).label().value().equals("IN") || t.getChild(i+1).getChild(0).label().value().equals("VBN") ||
							//t.getChild(i+1).getChild(1).label().value().equals("TO") || t.getChild(i+1).getChild(1).label().value().equals("IN") || t.getChild(i+1).getChild(1).label().value().equals("VBN")) {
						String aux = SentenceProcessor.setAux(true, isPresent);
						
						String PPphrase = "This" + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
						String PPphraseToDelete = " , " + Sentence.listToString(t.getChild(i+1).yield());
						
							SentenceProcessor.updateSentence(PPphrase, PPphraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
					
						
					//}
				}
					
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR") && i == t.getChildrenAsList().size()-2) {
					if (t.getChild(i+1).getChildrenAsList().size() >= 2) {
						if ((t.getChild(i+1).getChild(0).label().value().equals("IN") && t.getChild(i+1).getChild(1).label().value().equals("S") &&
								(t.getChild(i+1).getChild(0).getChild(0).label().value().equals("before") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("after") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("as") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("while") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("until"))) ||
								(t.getChild(i+1).getChild(1).label().value().equals("IN") && t.getChild(i+1).getChild(2).label().value().equals("S") &&
								(t.getChild(i+1).getChild(1).getChild(0).label().value().equals("before") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("after") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("as") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("since") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("while") || t.getChild(i+1).getChild(1).getChild(0).label().value().equals("until")))) {
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
		}
		
		return isSplit;
	}
	
	
	public static boolean extractFromTo(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
	 	
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
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
	
	/**
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
						if (!t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",") && t.getChild(i+3).label().value().equals("PP")) {
							if (t.getChild(i+3).getChild(0).label().value().equals("VBG") && t.getChild(i+3).getChild(0).getChild(0).label().value().equals("including")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								
								String includingPhrase = "This" + aux + Sentence.listToString(t.getChild(i+3).yield()) + " .";
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
			
			
			if (t.label().value().equals("NP")) {
				if (t.getChildrenAsList().size() >= 4) {
					for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",") && t.getChild(i+3).label().value().equals("PP")) {
							if (t.getChild(i+3).getChild(0).label().value().equals("VBG") && t.getChild(i+3).getChild(0).getChild(0).label().value().equals("including")) {
								String aux = SentenceProcessor.setAux(true, isPresent);
								
								String includingPhrase = "This" + aux + Sentence.listToString(t.getChild(i+3).yield()) + " .";
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
								String aux = SentenceProcessor.setAux(true, isPresent);
								
								String includingPhrase = "This" + aux + Sentence.listToString(t.getChild(i+2).yield()) + " .";
								String includingPhraseToDelete = " , " + Sentence.listToString(t.getChild(i+2).yield());
								
								SentenceProcessor.updateSentence(includingPhrase, includingPhraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
		}
		
		return isSplit;
	}*/
	
	
	public static boolean extractAccording(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
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
	
	
	public static boolean extractToDo(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		String aux = SentenceProcessor.setAux(true, isPresent);
		if (!(sentence.startsWith("This is to") || sentence.startsWith("This was to"))) {
		for (Tree t : parse) {
			
			
			if (t.label().value().equals("S")) {
				if (t.getChildrenAsList().size() >= 2) {
					for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("VP")) {
							if (t.getChild(i).getChild(0).label().value().equals("DT") && t.getChild(i).getChild(0).getChild(0).label().value().equals("This")) {
								if (t.getChild(i+1).getChildrenAsList().size() >= 3) {
									if (t.getChild(i+1).getChild(0).label().value().equals("VBD") || t.getChild(i+1).getChild(0).label().value().equals("VBP") || t.getChild(i+1).getChild(0).label().value().equals("VBZ")) {
										if (t.getChild(i+1).getChild(0).getChild(0).label().value().equals("is") || t.getChild(i+1).getChild(0).getChild(0).label().value().equals("was")) {
											if (t.getChild(i+1).getChild(1).label().value().equals("PP")) {
												if (t.getChild(i+1).getChild(2).label().value().equals("S")) {
													if (t.getChild(i+1).getChild(2).getChild(0).label().value().equals("VP")) {
														if (t.getChild(i+1).getChild(2).getChild(0).getChildrenAsList().size() >= 2) {
															if (t.getChild(i+1).getChild(2).getChild(0).getChild(0).label().value().equals("TO") && t.getChild(i+1).getChild(2).getChild(0).getChild(1).label().value().equals("VP")) {
																if (t.getChild(i+1).getChild(2).getChild(0).getChild(0).getChild(0).label().value().equals("to") && t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(0).label().value().equals("VB")) {
																	String phrase = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " +  Sentence.listToString(t.getChild(i+1).getChild(2).yield()) + " .";
																	String phraseToDelete = Sentence.listToString(t.getChild(i+1).getChild(1).yield());
																	
																	SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																	isSplit = true;
																}
																
																if (t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChildrenAsList().size() >= 3) {
																	if (t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(0).label().value().equals("VP") && t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(1).label().value().equals("CC") && t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(2).label().value().equals("VP")) {
																		if (t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(0).getChildrenAsList().size() >= 2) {
																			if (t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("VB") && t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(0).getChild(1).label().value().equals("NP")) {
																				if (t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(2).getChildrenAsList().size() >= 2) {
																					if (t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(2).getChild(0).label().value().equals("VB") && t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(2).getChild(1).label().value().equals("NP")) {
																						String phrase = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " +  Sentence.listToString(t.getChild(i+1).getChild(2).yield()) + " .";
																						String phraseToDelete = Sentence.listToString(t.getChild(i+1).getChild(1).yield());
																						
																						SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																						isSplit = true;
																					}
																				}
																			}
																			
																		}
																	}
																}
																if (t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChildrenAsList().size() >= 4) {
																	if (t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(0).label().value().equals("VB") &&
																			t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(1).label().value().equals("CC") &&
																			t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(2).label().value().equals("VB") &&
																			t.getChild(i+1).getChild(2).getChild(0).getChild(1).getChild(3).label().value().equals("NP")) {
																		String phrase = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(0).yield()) + " " +  Sentence.listToString(t.getChild(i+1).getChild(2).yield()) + " .";
																		String phraseToDelete = Sentence.listToString(t.getChild(i+1).getChild(1).yield());
																		
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
				}
			}
			
			if (t.label().value().equals("VP")) {
				
				if (t.getChildrenAsList().size() >= 3) {
					if (t.getChild(0).label().value().equals("VBD") || t.getChild(0).label().value().equals("VBP") || t.getChild(0).label().value().equals("VB") || t.getChild(0).label().value().equals("VBZ") || t.getChild(0).label().value().equals("VBN")) {
						if (!(t.getChild(0).getChild(0).label().value().equals("is") || t.getChild(0).getChild(0).label().value().equals("was") || t.getChild(0).getChild(0).label().value().equals("are") || t.getChild(0).getChild(0).label().value().equals("were"))) {
							for (int i = 2; i < t.getChildrenAsList().size(); i++) {
								if (t.getChild(i).label().value().equals("S") && !t.getChild(i-1).label().value().equals("PP")) {
									if (t.getChild(i).getChild(0).label().value().equals("VP")) {
										if (t.getChild(i).getChild(0).getChildrenAsList().size() >= 2) {
											if (t.getChild(i).getChild(0).getChild(0).label().value().equals("TO") && t.getChild(i).getChild(0).getChild(1).label().value().equals("VP")) {
												if (t.getChild(i).getChild(0).getChild(0).getChild(0).label().value().equals("to")) {
													if (t.getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 2) {
														if (t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VB") && t.getChild(i).getChild(0).getChild(1).getChild(1).label().value().equals("NP")) {
															String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
															String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
															
															SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
															isSplit = true;
														}
													}
													
													if (t.getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 3) {
														if (t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VP") &&
																t.getChild(i).getChild(0).getChild(1).getChild(1).label().value().equals("CC") &&
																t.getChild(i).getChild(0).getChild(1).getChild(2).label().value().equals("VP")) {
															if (t.getChild(i).getChild(0).getChild(1).getChild(0).getChildrenAsList().size() >= 2) {
																if (t.getChild(i).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("VB") &&
																		t.getChild(i).getChild(0).getChild(1).getChild(0).getChild(1).label().value().equals("NP")) {
																	if (t.getChild(i).getChild(0).getChild(1).getChild(2).getChildrenAsList().size() >= 2) {
																		if (t.getChild(i).getChild(0).getChild(1).getChild(2).getChild(0).label().value().equals("VB") &&
																				t.getChild(i).getChild(0).getChild(1).getChild(2).getChild(1).label().value().equals("NP")) {
																			String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
																			String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
																			
																			SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																			isSplit = true;
																		}
																	}
																}
															}
															
														}
													}
													
													if (t.getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 4) {
														if (t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VB") &&
																t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("CC") &&
																t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VB") &&
																t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("NP")) {
															String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
															String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
															
															SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
															isSplit = true;
														}
													}
												}
											}
										}
										
									}
								}
								if (t.getChild(i).label().value().equals("S") && t.getChild(i-1).label().value().equals("PP")) {
									
									
									List<LabeledWord> label = t.getChild(i-1).labeledYield();
									//System.out.println(label);
									for (LabeledWord l : label) {
										if (l.tag().value().equals("NNP") || l.tag().value().equals("NNPS") || l.tag().value().equals("CD")) {
											if (t.getChild(i).getChild(0).label().value().equals("VP")) {
												if (t.getChild(i).getChild(0).getChildrenAsList().size() >= 2) {
													if (t.getChild(i).getChild(0).getChild(0).label().value().equals("TO") && t.getChild(i).getChild(0).getChild(1).label().value().equals("VP")) {
														if (t.getChild(i).getChild(0).getChild(0).getChild(0).label().value().equals("to")) {
															if (t.getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 2) {
																if (t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VB") && t.getChild(i).getChild(0).getChild(1).getChild(1).label().value().equals("NP")) {
																	String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
																	String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
																	
																	SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																	isSplit = true;
																}
															}
															
															if (t.getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 3) {
																if (t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VP") &&
																		t.getChild(i).getChild(0).getChild(1).getChild(1).label().value().equals("CC") &&
																		t.getChild(i).getChild(0).getChild(1).getChild(2).label().value().equals("VP")) {
																	if (t.getChild(i).getChild(0).getChild(1).getChild(0).getChildrenAsList().size() >= 2) {
																		if (t.getChild(i).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("VB") &&
																				t.getChild(i).getChild(0).getChild(1).getChild(0).getChild(1).label().value().equals("NP")) {
																			if (t.getChild(i).getChild(0).getChild(1).getChild(2).getChildrenAsList().size() >= 2) {
																				if (t.getChild(i).getChild(0).getChild(1).getChild(2).getChild(0).label().value().equals("VB") &&
																						t.getChild(i).getChild(0).getChild(1).getChild(2).getChild(1).label().value().equals("NP")) {
																					String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
																					String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
																					
																					SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																					isSplit = true;
																				}
																			}
																		}
																	}
																	
																}
															}
															
															if (t.getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 4) {
																if (t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VB") &&
																		t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("CC") &&
																		t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VB") &&
																		t.getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("NP")) {
																	String phrase = "This" + aux + Sentence.listToString(t.getChild(i).yield()) + " .";
																	String phraseToDelete = Sentence.listToString(t.getChild(i).yield());
																	
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
				
				if (t.getChildrenAsList().size() >= 2) {
					
					if (t.getChild(0).label().value().equals("VBD") || t.getChild(0).label().value().equals("VBP") || t.getChild(0).label().value().equals("VB") || t.getChild(0).label().value().equals("VBZ") || t.getChild(0).label().value().equals("VBN")) {
						//if (!(t.getChild(0).getChild(0).label().value().equals("is") || t.getChild(0).getChild(0).label().value().equals("was") || t.getChild(0).getChild(0).label().value().equals("are") || t.getChild(0).getChild(0).label().value().equals("were"))) {
								
							for (int j = 1; j < t.getChildrenAsList().size(); j++) {
								//System.out.println(t.getChild(0).yield());
							
								if (t.getChild(j).label().value().equals("NP")) {
									//System.out.println(t.getChild(j).yield());
									for (int i = 0; i < t.getChild(j).getChildrenAsList().size(); i++) {
										//System.out.println(j + t.getChild(j).getChild(i).label().value());
										if (t.getChild(j).getChild(i).label().value().equals("S")) {
											//System.out.println("test");
											if (t.getChild(j).getChild(i).getChild(0).label().value().equals("VP")) {
												
												if (t.getChild(j).getChild(i).getChild(0).getChildrenAsList().size() >= 2) {
													if (t.getChild(j).getChild(i).getChild(0).getChild(0).label().value().equals("TO") && t.getChild(j).getChild(i).getChild(0).getChild(1).label().value().equals("VP")) {
														if (t.getChild(j).getChild(i).getChild(0).getChild(0).getChild(0).label().value().equals("to")) {
															if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 2) {
																if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VB") && t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(1).label().value().equals("NP")) {
																	String phrase = "This" + aux + Sentence.listToString(t.getChild(j).getChild(i).yield()) + " .";
																	String phraseToDelete = Sentence.listToString(t.getChild(j).getChild(i).yield());
																	
																	SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																	isSplit = true;
																}
															}
															if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 3) {
																if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VP") &&
																		t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(1).label().value().equals("CC") &&
																		t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(2).label().value().equals("VP")) {
																	if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(0).getChildrenAsList().size() >= 2) {
																		if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("VB") &&
																				t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(0).getChild(1).label().value().equals("NP")) {
																			if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(2).getChildrenAsList().size() >= 2) {
																				if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(2).getChild(0).label().value().equals("VB") &&
																						t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(2).getChild(1).label().value().equals("NP")) {
																					String phrase = "This" + aux + Sentence.listToString(t.getChild(j).getChild(i).yield()) + " .";
																					String phraseToDelete = Sentence.listToString(t.getChild(j).getChild(i).yield());
																					
																					SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																					isSplit = true;
																				}
																			}
																		}
																		
																	}
																	
																}
																
															}
															if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChildrenAsList().size() >= 4) {
																if (t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(0).label().value().equals("VB") &&
																		t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(1).label().value().equals("CC") &&
																		t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(2).label().value().equals("VB") &&
																		t.getChild(j).getChild(i).getChild(0).getChild(1).getChild(3).label().value().equals("NP")) {
																	String phrase = "This" + aux + Sentence.listToString(t.getChild(j).getChild(i).yield()) + " .";
																	String phraseToDelete = Sentence.listToString(t.getChild(j).getChild(i).yield());
																	
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
								}
							
							}
							
							
							
							
							
							if (t.getChild(1).label().value().equals("S")) {
								if (t.getChild(1).getChildrenAsList().size() >= 2) {
									if (t.getChild(1).getChild(0).label().value().equals("NP") && t.getChild(1).getChild(1).label().value().equals("VP")) {
										for (int k = 0; k < t.getChild(1).getChild(0).getChildrenAsList().size(); k++) {
											if (t.getChild(1).getChild(0).getChild(k).label().value().equals("NNP") || 
													t.getChild(1).getChild(0).getChild(k).label().value().equals("NNPS")) {
												if (t.getChild(1).getChild(1).getChildrenAsList().size() >= 2) {
													if (t.getChild(1).getChild(1).getChild(0).label().value().equals("TO") && t.getChild(1).getChild(1).getChild(1).label().value().equals("VP")) {
														if (t.getChild(1).getChild(1).getChild(0).getChild(0).label().value().equals("to")) {
															if (t.getChild(1).getChild(1).getChild(1).getChildrenAsList().size() >= 2) {
																if (t.getChild(1).getChild(1).getChild(1).getChild(0).label().value().equals("VB") && (t.getChild(1).getChild(1).getChild(1).getChild(1).label().value().equals("NP"))) {
																	String phrase = "This" + aux + Sentence.listToString(t.getChild(1).getChild(1).yield()) + " .";
																	String phraseToDelete = Sentence.listToString(t.getChild(1).getChild(1).yield());
																	
																	SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																	isSplit = true;
																}
															}
															if (t.getChild(1).getChild(1).getChild(1).getChildrenAsList().size() >= 3) {
																if (t.getChild(1).getChild(1).getChild(1).getChild(0).label().value().equals("VP") &&
																		t.getChild(1).getChild(1).getChild(1).getChild(1).label().value().equals("CC") &&
																		t.getChild(1).getChild(1).getChild(1).getChild(2).label().value().equals("VP")) {
																	if (t.getChild(1).getChild(1).getChild(1).getChild(0).getChildrenAsList().size() >= 2) {
																		if (t.getChild(1).getChild(1).getChild(1).getChild(0).getChild(0).label().value().equals("VB") &&
																				t.getChild(1).getChild(1).getChild(1).getChild(0).getChild(1).label().value().equals("NP")) {
																			if (t.getChild(1).getChild(1).getChild(1).getChild(2).getChildrenAsList().size() >= 2) {
																				if (t.getChild(1).getChild(1).getChild(1).getChild(2).getChild(0).label().value().equals("VB") &&
																						t.getChild(1).getChild(1).getChild(1).getChild(2).getChild(1).label().value().equals("NP")) {
																					String phrase = "This" + aux + Sentence.listToString(t.getChild(1).getChild(1).yield()) + " .";
																					String phraseToDelete = Sentence.listToString(t.getChild(1).getChild(1).yield());
																					
																					SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
																					isSplit = true;
																				}
																				
																			}
																		}
																	}
																}
															}
															if (t.getChild(1).getChild(1).getChild(1).getChildrenAsList().size() >= 4) {
																if (t.getChild(1).getChild(1).getChild(1).getChild(0).label().value().equals("VB") &&
																		t.getChild(1).getChild(1).getChild(1).getChild(1).label().value().equals("CC") &&
																		t.getChild(1).getChild(1).getChild(1).getChild(2).label().value().equals("VB") &&
																		t.getChild(1).getChild(1).getChild(1).getChild(3).label().value().equals("NP")) {
																	String phrase = "This" + aux + Sentence.listToString(t.getChild(1).getChild(1).yield()) + " .";
																	String phraseToDelete = Sentence.listToString(t.getChild(1).getChild(1).yield());
																	
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
			}
					
				
		}
		}
		return isSplit;
	}
	
	
	public static boolean extractFinalPPs(CoreContextSentence coreContextSentence, Tree parse) {
		
		String sentence = Sentence.listToString(parse.yield());
		String current = sentence;
		String[] sentenceTokens = current.split(" ");
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(coreContextSentence.getOriginal());
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		ArrayList<String> phrases = new ArrayList<String>();
		
		boolean isCompOrSup = false;
		
		for (Tree t : parse) {
			if (t.label().value().equals("RBS") || t.label().value().equals("RBR") ||
					t.label().value().equals("JJR") || t.label().value().equals("JJS")) {
				isCompOrSup = true;
			}
		}
		
		ArrayList<Tree> tree = new ArrayList<Tree>();
		if (!isCompOrSup) {
		for (Tree t : parse) {
			
			if (t.label().value().equals("PP") ) {
				
					
				//if (t.ancestor(1, parse).label().value().equals("VP")) {
					if (!t.ancestor(1, parse).getChild(0).label().value().equals("VBG")) {
						//System.out.println("...........");
						
						
						if (t.ancestor(1, parse).getChild(0).label().value().equals("VBN")) {
							if (t.ancestor(1, parse).ancestor(1, parse).label().value().equals("VP") && !t.ancestor(1, parse).ancestor(1, parse).getChild(0).label().value().equals("VB")) {
								tree.add(t);
							}
						} else {
							if (!(t.ancestor(1, parse).label().value().equals("NP") &&
									(t.getChild(0).getChild(0).label().value().equals("for") || t.getChild(0).getChild(0).label().value().equals("to")))) {
								tree.add(t);
								
								
							}
							if (t.ancestor(1, parse).label().value().equals("NP") &&
									t.getChild(0).getChild(0).label().value().equals("for")) {
								for (Tree tr : t.ancestor(1, parse)) {
									if (tr.label().value().equals("CD")) {
										System.out.println("xxxxxxx");
										tree.add(t);
									}
								}
							}
							
						}
						
					}
				//}
				
				//System.out.println(Sentence.listToString(t.yield()));
				
			}
		}
		}
		
	
		
		if (tree.size() > 0) {
		String pp = Sentence.listToString(tree.get(tree.size()-1).yield());
		
		String[] ppTokens = pp.split(" ");
		String tagged = tagger.tagString(pp);
		String[] taggedTokens = tagged.split(" ");
		
		String nerString = SentenceProcessor.ner(pp);
		String[] nerTokens = nerString.split(" ");
		
		if ((nerTokens[nerTokens.length-1].endsWith("/LOCATION") || taggedTokens[taggedTokens.length-1].endsWith("_CD")) &&
				!taggedTokens[0].startsWith("of")) {
			String phrase = "This " + aux + pp + " .";
			String phraseToDelete = pp;
			
			boolean contained = false;
			for (String s : phrases) {
				if (s.equals(pp)) {
					contained = true;
				}
			}
			
			if (!contained) {
				phrases.add(pp);
				coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
			}
			
			
			//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx " + pp);
			current = current.replace(phraseToDelete, "");
			sentenceTokens = current.split(" ");
			
		
			
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
			isSplit = true;
			
		} else {
			for (int i = 0; i < taggedTokens.length; i++) {
				if ((nerTokens[i].endsWith("/LOCATION") || nerTokens[i].endsWith("/ORGANIZATION")) &&
						!taggedTokens[0].startsWith("of")) {
					//System.out.println("yyyyyyyyyyyy " + pp);
					
					String phrase = "This " + aux + pp + " .";
					String phraseToDelete = pp;
					
					current = current.replace(phraseToDelete, "");
					sentenceTokens = current.split(" ");
					
					boolean contained = false;
					for (String s : phrases) {
						if (s.equals(pp)) {
							contained = true;
						}
					}
					
					if (!contained) {
						phrases.add(pp);
						coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
					}
					
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
					isSplit = true;
					
				} else if (nerTokens[i].endsWith("/PERSON") &&
						!taggedTokens[0].startsWith("of")) {
					//System.out.println("here: " + ppTokens[taggedTokens.length-1]);
					//System.out.println("here: " + sentenceTokens[sentenceTokens.length-2]);
					
					if (ppTokens[taggedTokens.length-1].equals(sentenceTokens[sentenceTokens.length-2])) {
					//System.out.println("zzzzzzzzzzz " + pp);
					
					
					String phrase = "This " + aux + pp + " .";
					String phraseToDelete = pp;
					
					boolean contained = false;
					for (String s : phrases) {
						if (s.equals(pp)) {
							contained = true;
						}
					}
					
					if (!contained) {
						phrases.add(pp);
						coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
					}
					
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
					
					current = current.replace(phraseToDelete, "");
					sentenceTokens = current.split(" ");
					
					isSplit = true;
					}
					
				} else if (taggedTokens[taggedTokens.length-1].endsWith("_NNP") &&
						!taggedTokens[0].startsWith("of")) {
					if (ppTokens[taggedTokens.length-1].equals(sentenceTokens[sentenceTokens.length-2])) {
					//System.out.println("vvvvvvvvv " + pp);
					
					
					String phrase = "This " + aux + pp + " .";
					String phraseToDelete = pp;
					
					boolean contained = false;
					for (String s : phrases) {
						if (s.equals(pp)) {
							contained = true;
						}
					}
					
					if (!contained) {
						phrases.add(pp);
						coreContextSentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(phrase)));
					}
					
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
					
					current = current.replace(phraseToDelete, "");
					sentenceTokens = current.split(" ");
					
					isSplit = true;
					
				}
					
				}
				
			}
		}
		
		
		
		}
		
		//System.out.println(tree.get(tree.size()-1));
		//System.out.println(Sentence.listToString(tree.get(tree.size()-1).yield()));
		
		return isSplit;
		
		}
		

}
