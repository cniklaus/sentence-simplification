package sentenceCompression;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

public class Punctuation {

	public static boolean splitAtColon(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		
		for (Tree t : parse) {
			if (t.getChildrenAsList().size() >= 3) {
				for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
					if (t.getChild(i).label().value().equals(":") && t.getChild(i+1).label().value().equals("CC") && t.getChild(i+2).label().value().equals("S")) {
						String phrase1 = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
						String phraseToDeleteForPhrase2 = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
						String phrase2 = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
						String delete = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
						
						
						if (isOriginal) {
							//SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							//SentenceProcessor.addCore(SentenceProcessor.collapseWhitespace(phrase1), coreContextSentence);
							//SentenceProcessor.addCore(SentenceProcessor.collapseWhitespace(phrase2), coreContextSentence);
							
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
					}
				}	
			}
			for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
				if (t.getChild(i).label().value().equals(":") && t.getChild(i+1).label().value().equals("S")) {
					String phrase1 = Sentence.listToString(t.getChild(i+1).yield());
					String phraseToDeleteForPhrase2 = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " .";	
					String phrase2 = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
					String delete = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
					
					if (isOriginal) {
						//SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						//SentenceProcessor.addCore(SentenceProcessor.collapseWhitespace(phrase1), coreContextSentence);
						//SentenceProcessor.addCore(SentenceProcessor.collapseWhitespace(phrase2), coreContextSentence);
						
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
				}
				
				if (t.getChild(i).label().value().equals(":") && t.getChild(i).getChild(0).label().value().equals(":") && (t.getChild(i+1).label().value().equals("UCP") || t.getChild(i+1).label().value().equals("NP"))) {
					
					TregexPattern patternMW = TregexPattern.compile("NP . /:/");
					// Run the pattern on one particular tree
					TregexMatcher matcher = patternMW.matcher(t.ancestor(1, parse));
					// Iterate over all of the subtrees that matched
					ArrayList<Tree> match = new ArrayList<Tree>();
					while (matcher.findNextMatchingNode()) {
						match.add(matcher.getMatch());
					} 
					//System.out.println(match.get(match.size()-1));
					
					List<LabeledWord> label = match.get(match.size()-1).labeledYield();
					boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
					String aux = SentenceProcessor.setAux(isSingular, isPresent);
					
					String phrase1 = Sentence.listToString(match.get(match.size()-1).yield()) + aux + Sentence.listToString(t.getChild(i+1).yield());
					String phraseToDeleteForPhrase2 = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " .";
					String phrase2 = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
					String delete = Sentence.listToString(t.yield()).replace(phraseToDeleteForPhrase2, "");
					
					if (isOriginal) {
						//SentenceProcessor.updateSentence(null, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						//SentenceProcessor.addCore(SentenceProcessor.collapseWhitespace(phrase1), coreContextSentence);
						//SentenceProcessor.addCore(SentenceProcessor.collapseWhitespace(phrase2), coreContextSentence);
						
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
				}
			}
		}
		
		return isSplit;
	}
	
	
	
	public static boolean parentheses(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("PRN")) {
				if (t.getChild(0).label().value().equals(":") && (t.getChild(1).label().value().equals("ADVP") || t.getChild(1).label().value().equals("PP"))) {
					String phrase = "This " + aux + Sentence.listToString(t.yield()) + " .";
					phrase = phrase.replace("--", "");
					String phraseToDelete = Sentence.listToString(t.yield());
							
					SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
					isSplit = true;
				}
				if (t.getChild(0).label().value().equals(":") && t.getChild(1).label().value().equals("SBAR")) {
					if (t.getChild(1).getChild(0).label().value().equals("WHADVP") && t.getChild(1).getChild(1).label().value().equals("S")) {
						if (t.getChild(1).getChild(0).getChild(0).label().value().equals("WRB") && t.getChild(1).getChild(0).getChild(0).getChild(0).label().value().equals("where")) {
							String phrase = "There " + Sentence.listToString(t.getChild(1).getChild(1).yield()) + " .";
							phrase = phrase.replace("--", "");
							String phraseToDelete = Sentence.listToString(t.yield());
									
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
				}
			}
			if (t.getChildrenAsList().size() >= 2) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					
					if (t.getChild(i).label().value().equals(":") && (t.getChild(i+1).label().value().equals("ADVP") || t.getChild(i+1).label().value().equals("PP"))) {
						ArrayList<Tree> tr = new ArrayList<Tree>();
						tr.add(0, t.getChild(i+1));
						tr.addAll(t.getChild(i+1).siblings(parse));
						for (int j = 0; j <= i; j++) {
							tr.remove(0);
						}
						String ph = "";
						for (Tree tree : tr) {
							ph = ph + Sentence.listToString(tree.yield()) + " ";
						}
						
						String phrase = "This " + aux + Sentence.listToString(t.getChild(i+1).yield()) + " " + ph + " .";
						phrase = phrase.replace("--", "");
						String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + ph.replace("--", "");
						
						SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
						
					}
					if (t.getChild(i).label().value().equals(":") && t.getChild(i+1).label().value().equals("ADJP")) {
						ArrayList<Tree> tr = new ArrayList<Tree>();
						tr.add(0, t.getChild(i+1));
						tr.addAll(t.getChild(i+1).siblings(parse));
						for (int j = 0; j <= i; j++) {
							tr.remove(0);
						}
						String ph = "";
						for (Tree tree : tr) {
							ph = ph + Sentence.listToString(tree.yield()) + " ";
						}
						
						String phrase = "This " + aux + "with " + Sentence.listToString(t.getChild(i+1).yield()) + " " + ph + " .";
						phrase = phrase.replace("--", "");
						String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + ph.replace("--", "");
						
						SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
						
					}
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PRN")) {
						if (t.getChild(i+1).getChild(0).label().value().equals(":") && t.getChild(i+1).getChild(1).label().value().equals("VP")) {
							List<LabeledWord> label = t.getChild(i).labeledYield();
							boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
							String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
							
							String phrase = Sentence.listToString(t.getChild(i).yield()) + aux2 + Sentence.listToString(t.getChild(i+1).yield()) + " .";
							phrase = phrase.replace("--", "");
							String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
							
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(i+1).getChild(0).label().value().equals(":") && t.getChild(i+1).getChild(1).label().value().equals("NP")) {
							List<LabeledWord> label = t.getChild(i).labeledYield();
							boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
							String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
							String pronoun = "";
							if (isSingular) {
								pronoun = "This ";
							} else {
								pronoun = "These ";
							}
							
							String phrase = pronoun + aux2 + Sentence.listToString(t.getChild(i+1).yield()) + " .";
							phrase = phrase.replace("--", "");
							String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
							
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(i+1).getChild(0).label().value().equals(":") && t.getChild(i+1).getChild(1).label().value().equals("SBAR")) {
							if (t.getChild(i+1).getChild(1).getChild(0).label().value().equals("WHNP") && t.getChild(i+1).getChild(1).getChild(1).label().value().equals("S")) {
								if (t.getChild(i+1).getChild(1).getChild(0).getChild(0).label().value().equals("WP") && (t.getChild(i+1).getChild(1).getChild(0).getChild(0).getChild(0).label().value().equals("who") || t.getChild(i+1).getChild(1).getChild(0).getChild(0).getChild(0).label().value().equals("which"))) {
									List<LabeledWord> label = t.getChild(i).labeledYield();
									boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
									String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
									String phrase = Sentence.listToString(t.getChild(i).yield()) + aux2 + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(1).yield()) + " .";
									String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
									
									SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
								if (t.getChild(i+1).getChild(1).getChild(0).getChildrenAsList().size() >= 2) {
									if (t.getChild(i+1).getChild(1).getChild(0).getChild(0).label().value().equals("DT") && t.getChild(i+1).getChild(1).getChild(0).getChild(1).label().value().equals("WHPP")) {
										String phrase = Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(1).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
								}
								
								if (t.getChild(i+1).getChild(1).getChild(0).getChild(0).label().value().equals("WHPP")) {
									String phrase = Sentence.listToString(t.getChild(i+1).getChild(1).getChild(0).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i+1).getChild(1).getChild(1).yield()) + " .";
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
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(":") && t.getChild(i+2).label().value().equals("NP") && i == t.getChildrenAsList().size()-3) {
						List<LabeledWord> label = t.getChild(i+2).labeledYield();
						boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
						String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
						
						String phrase = Sentence.listToString(t.getChild(i).yield()) + aux2 + Sentence.listToString(t.getChild(i+2).yield()) + " .";
						String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
						
						SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
			}
			if (t.getChildrenAsList().size() >= 5) {
				for (int i = 0; i < t.getChildrenAsList().size()-4; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(":") && t.getChild(i+2).label().value().equals("NP") && t.getChild(i+3).label().value().equals("CC") && t.getChild(i+4).label().value().equals("NP") && i == t.getChildrenAsList().size()-5) {
						String aux2 = SentenceProcessor.setAux(false, isPresent);
						
						String phrase = Sentence.listToString(t.getChild(i).yield()) + aux2 + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield()) + " .";
						String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield());
						
						SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
			}
		}
		
		return isSplit;
	}
	
	
	public static boolean removeBrackets(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		boolean isPresent = SentenceProcessor.isPresent(parse);
		String aux = SentenceProcessor.setAux(true, isPresent);
		
		for (Tree t : parse) {
			if (t.label().value().equals("PRN")) {
				if (t.getChildrenAsList().size() >= 3) {
					for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
						if (t.getChild(i).label().value().equals("-LRB-") && t.getChild(i+1).label().value().equals("S") && t.getChild(i+2).label().value().equals("-RRB-")) {
							String phrase = Sentence.listToString(t.getChild(i+1).yield()) + " .";
							String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
							
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(i).label().value().equals("-LRB-") && t.getChild(i+1).label().value().equals("CD") && t.getChild(i+2).label().value().equals("-RRB-")) {
							String phrase = "This " + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
							String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
							
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(i).label().value().equals("-LRB-") && (t.getChild(i+1).label().value().equals("ADVP") || t.getChild(i+1).label().value().equals("PP") || t.getChild(i+1).label().value().equals("VP")) && t.getChild(i+2).label().value().equals("-RRB-")) {
							String phrase = "This " + aux + Sentence.listToString(t.getChild(i+1).yield()) + " .";
							String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
							
							SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(i).label().value().equals("-LRB-") && t.getChild(i+1).label().value().equals("NP") && t.getChild(i+2).label().value().equals("-RRB-")) {
							if (t.getChild(i+1).getChild(0).label().value().equals("CD") && t.getChild(i+1).getChildrenAsList().size()==1) {
								String phrase = "This " + aux + " in " + Sentence.listToString(t.getChild(i+1).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield());
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}			
						}
					}
				}
			}
			
			
				if (t.getChildrenAsList().size() >= 5) {
					for (int i = 0; i < t.getChildrenAsList().size()-4; i++) {
						if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("-LRB-") && t.getChild(i+2).label().value().equals("CC") && t.getChild(i+3).label().value().equals("NP") && t.getChild(i+4).label().value().equals("-RRB-")) {
							if (t.getChild(i+2).getChild(0).label().value().equals("and")) {
								String phrase = "This " + aux + " as well as " + Sentence.listToString(t.getChild(i+3).yield()) + " .";
								String phraseToDelete = Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+2).yield()) + " " + Sentence.listToString(t.getChild(i+3).yield()) + " " + Sentence.listToString(t.getChild(i+4).yield());
								
								SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				
			}
			
			if (t.getChildrenAsList().size() >= 2) {
				for (int k = 0; k < t.getChildrenAsList().size()-1; k++) {
					if (t.getChild(k).label().value().equals("NP") && t.getChild(k+1).label().value().equals("PRN")) {
						if (t.getChild(k+1).getChildrenAsList().size() >= 3) {
							for (int i = 0; i < t.getChild(k+1).getChildrenAsList().size()-2; i++) {
								
								if (t.getChild(k+1).getChild(i).label().value().equals("-LRB-") && t.getChild(k+1).getChild(i+1).label().value().equals("NP") && t.getChild(k+1).getChild(i+2).label().value().equals("-RRB-")) {
									if (t.getChild(k+1).getChild(i+1).getChild(0).label().value().equals("NNP") || t.getChild(k+1).getChild(i+1).getChild(0).label().value().equals("NNPS")) {
										List<LabeledWord> label = t.getChild(k+1).getChild(i+1).labeledYield();
										boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
										String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
										
										String phrase = Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + aux2 + Sentence.listToString(t.getChild(k).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(k+1).getChild(i).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+2).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
									if (t.getChild(k+1).getChild(i+1).getChild(0).label().value().equals("QP") || t.getChild(k+1).getChild(i+1).getChild(0).label().value().equals("DT")) {
										List<LabeledWord> label = t.getChild(k+1).getChild(i+1).labeledYield();
										boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
										String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
										
										String phrase = Sentence.listToString(t.getChild(k).yield()) + aux2 + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(k+1).getChild(i).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+2).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}
									if (t.getChild(k+1).getChild(i+1).getChild(0).label().value().equals("NP")) {
										List<LabeledWord> label = t.getChild(k).labeledYield();
										if (label.get(label.size()-1).tag().value().equals("NNP") || label.get(label.size()-1).tag().value().equals("NNPS")) {
											boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
											String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
											
											String phrase = Sentence.listToString(t.getChild(k).yield()) + aux2 + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " .";
											String phraseToDelete = Sentence.listToString(t.getChild(k+1).getChild(i).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+2).yield());
											
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										}
										else {
											String phrase = "This " + aux + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " .";
											String phraseToDelete = Sentence.listToString(t.getChild(k+1).getChild(i).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+2).yield());
											
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										}
									}
									if (t.getChild(k+1).getChild(i+1).getChildrenAsList().size() >= 2) {
										if (t.getChild(k+1).getChild(i+1).getChild(0).label().value().equals("CD") && (t.getChild(k+1).getChild(i+1).getChild(1).label().value().equals("NN") || t.getChild(k+1).getChild(i+1).getChild(1).label().value().equals("NNS"))) {
											List<LabeledWord> label = t.getChild(k+1).getChild(i+1).labeledYield();
											boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
											String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
											
											String phrase = Sentence.listToString(t.getChild(k).yield()) + aux2 + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " .";
											String phraseToDelete = Sentence.listToString(t.getChild(k+1).getChild(i).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(k+1).getChild(i+2).yield());
											
											SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
											isSplit = true;
										}
									}
									/**
									if (!t.getChild(k+1).getChild(i+1).getChild(0).label().value().equals("CD") && !t.getChild(k+1).getChild(0).label().value().equals("NP") && !t.getChild(k+1).getChild(0).label().value().equals("QP") && !t.getChild(k+1).getChild(0).label().value().equals("DT") && !t.getChild(k+1).getChild(0).label().value().equals("NNP") && !t.getChild(k+1).getChild(0).label().value().equals("NNPS")) {
										List<LabeledWord> label = t.getChild(k).labeledYield();
										boolean isSingular = SentenceProcessor.isSingular(label.get(label.size()-1));
										String aux2 = SentenceProcessor.setAux(isSingular, isPresent);
										
										String phrase = Sentence.listToString(t.getChild(k).yield()) + aux2 + Sentence.listToString(t.getChild(k+1).getChild(i+1).yield()) + " .";
										String phraseToDelete = Sentence.listToString(t.getChild(k+1).yield());
										
										SentenceProcessor.updateSentence(phrase, phraseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
										isSplit = true;
									}*/
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
