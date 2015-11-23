package sentenceCompression;

import java.util.List;

import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

public class RelativeClauseExtractor {

	public static boolean extractNonRestrictiveRelativeClauses(CoreContextSentence coreContextSentence, Tree parse, boolean isOriginal, int contextNumber) {
		
		String sentence = Sentence.listToString(parse.yield());
		boolean isSplit = false;
		
		for (Tree t : parse) {
			
			for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
				
				if (t.getChild(i).label().value().equals(",") && t.getChild(i+1).label().value().equals("SBAR")) {
					if (t.getChild(i+1).getChild(0).label().value().equals("WHADVP") && t.getChild(i+1).getChild(0).getChild(0).label().value().equals("WRB") && t.getChild(i+1).getChild(1).label().value().equals("S") && t.getChild(i+1).getChild(0).getChild(0).getChild(0).label().value().equals("where")) {
						String relClause = "There " + Sentence.listToString(t.getChild(i+1).getChild(1).yield()) + " .";
						String relClauseToDelete = "";
						
						if (i == t.getChildrenAsList().size()-2) {
							relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield());
						}
						else if (t.getChild(i+2).label().value().equals(",")) {
							relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+1).yield()) + " ,";
						}
						
						SentenceProcessor.updateSentence(relClause, relClauseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
						isSplit = true;
					}
				}
			}
					
			for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
				
				if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals(",") && t.getChild(i+2).label().value().equals("SBAR")) {
					if (t.getChild(i+2).getChild(0).label().value().equals("WP$") && t.getChild(i+2).getChild(1).label().value().equals("S")) {
						if (t.getChild(i+2).getChild(0).getChild(0).label().value().equals("whose")) {
							String relClause = Sentence.listToString(t.getChild(i).yield()) + "'s " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " .";
							String relClauseToDelete = "";
							//System.out.println(att);
							
							if (i == t.getChildrenAsList().size()-3) {
								relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
							}
							else if (t.getChild(i+3).label().value().equals(",")) {
								relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
							}
							
							SentenceProcessor.updateSentence(relClause, relClauseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						
					}
					if (t.getChild(i+2).getChild(0).label().value().equals("WHNP") && t.getChild(i+2).getChild(1).label().value().equals("S")) {
						if ((t.getChild(i+2).getChild(0).getChild(0).label().value().equals("WDT") && t.getChild(i+2).getChild(0).getChild(0).getChild(0).label().value().equals("which")) ||
								(t.getChild(i+2).getChild(0).getChild(0).label().value().equals("WP") && (t.getChild(i+2).getChild(0).getChild(0).getChild(0).label().value().equals("who") || t.getChild(i+2).getChild(0).getChild(0).getChild(0).label().value().equals("whom")))) {
								
							List<LabeledWord> label = t.getChild(i).labeledYield();
							String att = "";
							if (label.get(label.size()-1).tag().value().equals("CD")) {
								att = Sentence.listToString(t.getChild(i).ancestor(3, parse).yield());
							} else {
								att = Sentence.listToString(t.getChild(i).yield());
							}
								
							String relClause = att + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " .";
							String relClauseToDelete = "";
							//System.out.println(att);
							
							if (i == t.getChildrenAsList().size()-3) {
								relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
							}
							else if (t.getChild(i+3).label().value().equals(",")) {
								relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
							}
							
							SentenceProcessor.updateSentence(relClause, relClauseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
						if (t.getChild(i+2).getChild(0).getChild(0).label().value().equals("DT") && t.getChild(i+2).getChild(0).getChild(1).label().value().equals("WHPP")) {
							if (t.getChild(i+2).getChild(0).getChild(1).getChild(1).label().value().equals("WHNP")) {
								if (t.getChild(i+2).getChild(0).getChild(1).getChild(1).getChild(0).label().value().equals("WP") && t.getChild(i+2).getChild(0).getChild(1).getChild(1).getChild(0).getChild(0).label().value().equals("whom")) {
									String relClause = Sentence.listToString(t.getChild(i+2).getChild(0).yield()) + " the " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " .";
									relClause = relClause.replace("whom", "");
									String relClauseToDelete = "";
		
									
									if (i == t.getChildrenAsList().size()-3) {
										relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
									}
									else if (t.getChild(i+3).label().value().equals(",")) {
										relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
									}
									
									//System.out.println(Sentence.listToString(t.getChild(i+2).getChild(0).getChild(1).getChild(1).yield()));
									//System.out.println(relClause);
									
									SentenceProcessor.updateSentence(relClause, relClauseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
									isSplit = true;
								}
							}
							
						}
						
						if (t.getChild(i+2).getChild(0).getChild(0).label().value().equals("WP$") && t.getChild(i+2).getChild(0).getChild(0).getChild(0).label().value().equals("whose")) {
							
							String relClause = Sentence.listToString(t.getChild(i).yield()) + "'s " + Sentence.listToString(t.getChild(i+2).yield()) + " .";
							relClause = relClause.replace("whose", "");
							String relClauseToDelete = "";
							//System.out.println(att);
							
							if (i == t.getChildrenAsList().size()-3) {
								relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
							}
							else if (t.getChild(i+3).label().value().equals(",")) {
								relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
							}
							
							SentenceProcessor.updateSentence(relClause, relClauseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}
					}
							
					if (t.getChild(i+2).getChild(0).label().value().equals("WHPP") && t.getChild(i+2).getChild(1).label().value().equals("S")) {
						if ((t.getChild(i+2).getChild(0).getChild(0).label().value().equals("IN") || t.getChild(i+2).getChild(0).getChild(0).label().value().equals("TO")) && t.getChild(i+2).getChild(0).getChild(1).label().value().equals("WHNP")) {
							if ((t.getChild(i+2).getChild(0).getChild(1).getChild(0).label().value().equals("WDT") && t.getChild(i+2).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("which")) ||
									(t.getChild(i+2).getChild(0).getChild(1).getChild(0).label().value().equals("WP") && (t.getChild(i+2).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("who") || t.getChild(i+2).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("whom")))) {
								
								String relClause = Sentence.listToString(t.getChild(i+2).getChild(0).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+2).getChild(1).yield()) + " .";
								String relClauseToDelete = "";
	
								
								if (i == t.getChildrenAsList().size()-3) {
									relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield());
								}
								else if (t.getChild(i+3).label().value().equals(",")) {
									relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+2).yield()) + " ,";
								}
								
								SentenceProcessor.updateSentence(relClause, relClauseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
					
			for (int i = 0; i < t.getChildrenAsList().size()-3; i++) {
				if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("PP") && t.getChild(i+2).label().value().equals(",") && t.getChild(i+3).label().value().equals("SBAR")) {
					if (t.getChild(i+3).getChild(0).label().value().equals("WHNP") && t.getChild(i+3).getChild(1).label().value().equals("S")) {
						if ((t.getChild(i+3).getChild(0).getChild(0).label().value().equals("WDT") && t.getChild(i+3).getChild(0).getChild(0).getChild(0).label().value().equals("which")) ||
								(t.getChild(i+3).getChild(0).getChild(0).label().value().equals("WP") && (t.getChild(i+3).getChild(0).getChild(0).getChild(0).label().value().equals("who") || t.getChild(i+3).getChild(0).getChild(0).getChild(0).label().value().equals("whom")))) {
									
							String relClause = Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+1).yield()) + " " + Sentence.listToString(t.getChild(i+3).getChild(1).yield()) + " .";
							String relClauseToDelete = "";	
							
							if (i == t.getChildrenAsList().size()-4) {
								relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+3).yield());
							}
							else if (t.getChild(i+4).label().value().equals(",")) {
								relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+3).yield()) + " ,";
							}
							
							SentenceProcessor.updateSentence(relClause, relClauseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
							isSplit = true;
						}	
					}
							
					else if (t.getChild(i+3).getChild(0).label().value().equals("WHPP") && t.getChild(i+3).getChild(1).label().value().equals("S")) {
						if (t.getChild(i+3).getChild(0).getChild(0).label().value().equals("IN") && t.getChild(i+3).getChild(0).getChild(1).label().value().equals("WHNP")) {
							if ((t.getChild(i+3).getChild(0).getChild(1).getChild(0).label().value().equals("WDT") && t.getChild(i+3).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("which")) ||
									(t.getChild(i+3).getChild(0).getChild(1).getChild(0).label().value().equals("WP") && (t.getChild(i+3).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("who") || t.getChild(i+3).getChild(0).getChild(1).getChild(0).getChild(0).label().value().equals("whom")))) {
										
								String relClause = Sentence.listToString(t.getChild(i+3).getChild(0).getChild(0).yield()) + " " + Sentence.listToString(t.getChild(i).yield()) + " " + Sentence.listToString(t.getChild(i+3).getChild(1).yield()) + " .";
								String relClauseToDelete = "";
								
								if (i == t.getChildrenAsList().size()-4) {
									relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+3).yield());
								}
								else if (t.getChild(i+4).label().value().equals(",")) {
									relClauseToDelete = ", " + Sentence.listToString(t.getChild(i+3).yield()) + " ,";
								}
								
								SentenceProcessor.updateSentence(relClause, relClauseToDelete.trim(), sentence, coreContextSentence, isOriginal, contextNumber);
								isSplit = true;
							}
						}
					}
				}
			}
		}
		
		return isSplit;
	}

}
