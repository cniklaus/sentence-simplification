package transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import analysis.RepresentationGenerator;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;

/**
 * Class for simplifying a sequence of input sentences by splitting each of them - one after another - into core and associated context sentences.
 * 
 * This class constitutes "stage 3" of the simplification framework.
 * 
 * @author christina
 *
 */
public class Transformer {
		
	public static ArrayList<Integer> eliminatedSentences = new ArrayList<Integer>();
	
	/**
	 * simplifies a list of NL sentences 
	 * by making various calls to transformation classes 
	 * which specify simplification rules for selected grammatical constituents
	 * 
	 * @param list of sentences
	 * @param output file
	 * @throws IOException
	 */
	public static void simplify(ArrayList<String> sentences, String output) throws IOException {
		
		ArrayList<CoreContextSentence> sen = new ArrayList<CoreContextSentence>();
		FileOperator fo = new FileOperator();
	
		try {
			
			int i = 0;
			ArrayList<Boolean> severalCores = new ArrayList<Boolean>();
			int counterSentences = 0;
			
			//simplify one sentence after another by splitting it into a set of core and affiliated context sentences
			for (String s : sentences) {
				severalCores.add(false);
				SentenceProcessor.positions.clear();
				SentenceProcessor.loc.clear();
				
				Core.getStart().clear();
				Core.getEnd().clear();
				Core.getPrefix().clear();
				Core.getPostfix().clear();
				String inputS = s;
				
				String nerString = RepresentationGenerator.ner(s);
				String[] sentenceTokens = s.split(" "); 
				String[] nerTokens = nerString.split(" ");
				
				//transform input sentence to lower case
				if (!nerTokens[0].endsWith("/ORGANIZATION") && !nerTokens[0].endsWith("/PERSON") && !nerTokens[0].endsWith("/LOCATION") &&
						!sentenceTokens[0].equals("I")) {
					sentenceTokens[0] = sentenceTokens[0].toLowerCase();
					
					String sentenceNew = "";
					for (int counter = 0; counter < sentenceTokens.length; counter++) {
						sentenceNew = sentenceNew + " " + sentenceTokens[counter];
					}
					s = sentenceNew.trim();
				}
				
				for (int locCounter = 0; locCounter < nerTokens.length-2; locCounter++) {
					if (nerTokens[locCounter].endsWith("/LOCATION") && sentenceTokens[locCounter+1].equals(",") && nerTokens[locCounter+2].endsWith("/LOCATION")) {
						SentenceProcessor.loc(sentenceTokens[locCounter]);
						SentenceProcessor.loc(sentenceTokens[locCounter+2]);	
					}
				}
					
				//delete bracketed content
				if (s.contains("-LRB-") && s.contains("-RRB-")) {
					s = s.replaceAll("-LRB-.*?-RRB-", "");
					s = s.replaceAll("  ,", " ,");
				}
				
				List<CoreLabel> tokens = RepresentationGenerator.tokenize(s);
				Tree parse = RepresentationGenerator.parse(tokens);
				boolean keep = true;
				
				//check if an input sentence containing ":" or ";" connects two full sentences (if false then the input sentence is not processed)
				if (s.contains(" : ") || s.contains(" ; ")) {
					keep = false;
					for (Tree t : parse) {
						if (t.label().value().equals("S") && t.getChildrenAsList().size() >= 3) {
							for (int i2 = 0; i2 < t.getChildrenAsList().size()-2; i2++) {
								if (t.getChild(i2).label().value().equals("S") && t.getChild(i2+1).label().value().equals(":") && t.getChild(i2+2).label().value().equals("S")) {
									keep = true;	
								} 
							}
						}
					}
				}
				
				//check if input sentence starts with a participial phrase succeeded by a comma which is followed by a PP
				String taggedOriginalGerundPP = RepresentationGenerator.posTag(s);
				String[] taggedOriginalTokensGerundPP = taggedOriginalGerundPP.split(" ");
				String ppString = "";
				boolean printPP = false;
				for (int numGerundPP = 0; numGerundPP < taggedOriginalTokensGerundPP.length; numGerundPP++) {
					if (taggedOriginalTokensGerundPP.length>=3) {
						for (int num3 = 0; num3 < 3; num3++) {
							if (taggedOriginalTokensGerundPP[num3].endsWith("_VBG") || taggedOriginalTokensGerundPP[num3].endsWith("_VBG") || taggedOriginalTokensGerundPP[num3].endsWith("_VBG") ||
								taggedOriginalTokensGerundPP[num3].endsWith("_VBN") || taggedOriginalTokensGerundPP[num3].endsWith("_VBN") || taggedOriginalTokensGerundPP[num3].endsWith("_VBN")) {
							
								if (num3 == 0 || !taggedOriginalTokensGerundPP[num3-1].endsWith("_,")) {
									if (num3 < 2 || (num3 == 2 && !(taggedOriginalTokensGerundPP[num3-1].endsWith("_VBD") || taggedOriginalTokensGerundPP[num3-1].endsWith("_VBP") || taggedOriginalTokensGerundPP[num3-1].endsWith("_VBZ")))) {
										for (int num4 = num3+1; num4 < taggedOriginalTokensGerundPP.length-1; num4++) {
											if (taggedOriginalTokensGerundPP[num4].endsWith("_,")) {
												if (taggedOriginalTokensGerundPP[num4+1].endsWith("_IN")) {
													printPP = true;
												
													boolean test = true;
													for (Tree t : parse) {
														if (test) {
															if (t.getChildrenAsList().size() >= 2) {
																for (int numCommaPP = 0; numCommaPP < t.getChildrenAsList().size()-1; numCommaPP++) {
																	if (t.getChild(numCommaPP).label().value().equals(",") &&
																			t.getChild(numCommaPP+1).label().value().equals("PP")) {
																		ppString = Sentence.listToString(t.getChild(numCommaPP+1).yield());
																		test = false;
																	}
																}
															}
														}
													}
													
												} else {
													num4 = taggedOriginalTokensGerundPP.length;
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
				
				String contextPP = "";
				if (printPP && !ppString.equals("")) {
					boolean tense = SentenceProcessor.isPresent(parse);
					String aux = SentenceProcessor.setAux(true, tense);
					contextPP = "This" + aux + ppString + " .";
					s = s.replace(ppString, "");
					s = s.replace("  ", " ");
					s = s.replace(",  ", "");
					s = s.replace(", , ", ",");
					tokens = RepresentationGenerator.tokenize(s);
					parse = RepresentationGenerator.parse(tokens);
				}
				
				if (!keep) {
					eliminatedSentences.add(counterSentences);
				}
					
				
				if (keep) {
					CoreContextSentence sentence = new CoreContextSentence();
					if (!contextPP.equals("")) {
						sentence.getContext().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(contextPP)));
					}
					sentence.setInput(inputS);
					System.out.println(i + " " + inputS);
					i++;
					
					sentence.setOriginal(parse);
					boolean[] delete = new boolean[tokens.size()];
					sentence.setDelete(delete);
					
					//split the input sentence into several cores, if appropriate
					int counterSplit = 0;
					while (counterSplit < 5) {
						Punctuation.splitAtColon(sentence, sentence.getOriginal(), true, -1);
						ConjoinedClausesExtractor.infixAndOrButSplit(sentence, sentence.getOriginal(), true, -1);
						ConjoinedClausesExtractor.infixCommaAndOrButSplit(sentence, sentence.getOriginal(), true, -1);
						
						counterSplit++;
					}
					
					Set<Integer> setItems = new LinkedHashSet<Integer>(SentenceProcessor.positions);
					SentenceProcessor.positions.clear();
					SentenceProcessor.positions.addAll(setItems);
					
					Collections.sort(SentenceProcessor.positions);
					
					ArrayList<String> delPhrase = IntraSententialAttribution.extractIntraSententialAttributions(sentence, sentence.getOriginal(), true, -1);
						
					for (Tree trAttr : sentence.getAttribution()) {
						sentence.getContext().add(trAttr);
					}
					 
					//extract incidental pieces of information and transform them into stand-alone context sentences
					PrepositionalPhraseExtractor.extractToDo(sentence, sentence.getOriginal(), true, -1);
					
					RelativeClauseExtractor.extractNonRestrictiveRelativeClauses(sentence, sentence.getOriginal(), true, -1);
					
					PrepositionalPhraseExtractor.extractInitialPPs(sentence, sentence.getOriginal(), true, -1);
					PrepositionalPhraseExtractor.extractInfixPPs(sentence, sentence.getOriginal(), true, -1);
					PrepositionalPhraseExtractor.extractFromTo(sentence, sentence.getOriginal(), true, -1);
					PrepositionalPhraseExtractor.extractAccording(sentence, sentence.getOriginal(), true, -1);
						
					ParticipialPhraseExtractor.extractPresentAndPastParticiples(sentence, sentence.getOriginal(), true, -1);
					
					InitialNounPhraseExtractor.extractInitialParentheticalNounPhrases(sentence, sentence.getOriginal(), true, -1);
					
					AdjectiveAdverbPhraseExtractor.extractAdjectivePhrases(sentence, sentence.getOriginal(), true, -1);
					AdjectiveAdverbPhraseExtractor.extractAdverbPhrases(sentence, sentence.getOriginal(), true, -1);
					
					AppositivePhraseExtractor.extractNonRestrictiveAppositives(sentence, sentence.getOriginal(), true, -1);
					AppositivePhraseExtractor.extractRestrictiveAppositives(sentence, sentence.getOriginal(), true, -1);
					
					ConjoinedClausesExtractor.infixWhenSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.infixAsSinceSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.infixCommaPPAfterBeforeSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.infixPPAfterBeforeSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.infixPPSAfterBeforeSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.infixSBARAfterBeforeSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.initialWhenSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.initialThoughAlthoughBecauseSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.infixBecauseThoughAlthoughSplit(sentence, sentence.getOriginal(), true, -1);
					
					
					ConjoinedClausesExtractor.ifSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.extractWhilePlusParticiple(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.extractSo(sentence, sentence.getOriginal(), true, -1);
						
					Punctuation.extractParentheses(sentence, sentence.getOriginal(), true, -1);
					Punctuation.removeBrackets(sentence, sentence.getOriginal(), true, -1);
					
					//simplify extracted context sentences, if appropriate
					boolean isContextPruned = SentenceProcessor.pruneContextSentences(sentence);
					
					while (isContextPruned) {
						isContextPruned = SentenceProcessor.pruneContextSentences(sentence);
					}
				
					//further simplify core sentences
					boolean isCorePruned = SentenceProcessor.pruneCoreSentences(sentence);
				
					int num = 0;
					while (isCorePruned && num < 20) {
						if (sentence.getCoreNew().size() > 0) {
							isCorePruned = SentenceProcessor.pruneCoreSentences(sentence);
							num++;
						}
					}
				
					ArrayList<String> coreSen = new ArrayList<String>();
					for (Tree tree : sentence.getCoreNew()) {
						String str = Sentence.listToString(tree.yield());
						coreSen.add(str);
					}
				
					int n = 0;
				
					for (String str : coreSen) {
						for (int j = n+1; j < coreSen.size(); j++) {
							if (str.equals(coreSen.get(j))) {
								sentence.getCoreNew().remove(n);
							}
						}
						n++;
					}
				
					//clean punctuation
					ArrayList<String> contextSen = new ArrayList<String>();
					int v = 0;
					for (Tree tree : sentence.getContext()) {
						String str = Sentence.listToString(tree.yield());
					
						if (str.endsWith(", .")) {
							str = str.replace(", .", ".");
							sentence.getContext().set(v, RepresentationGenerator.parse(RepresentationGenerator.tokenize(str)));
						}
						if (str.endsWith(", . ''")) {
							str = str.replace(", . ''", ".");
							sentence.getContext().set(v, RepresentationGenerator.parse(RepresentationGenerator.tokenize(str)));
						}
						if (str.endsWith(": .")) {
							str = str.replace(": .", ".");
							sentence.getContext().set(v, RepresentationGenerator.parse(RepresentationGenerator.tokenize(str)));
						}
						if (str.startsWith("This is .") || str.startsWith("This was .")) {
							sentence.getContext().set(v, null);
						}
						contextSen.add(str);
						v++;
					}
				
					//delete duplicate context sentences
					int m = 0;
					for (String str : contextSen) {
					
						for (int j = m+1; j < contextSen.size(); j++) {
							if (str.equals(contextSen.get(j))) {
								sentence.getContext().set(m, null);
							}
						}
						m++;
					}
			
					int z = 0;
					for (String str : contextSen) {
						String[] toks = str.split(" ");
						String cur = "";
						for (int t = 2; t < toks.length; t++) {
							cur = cur + toks[t] + " ";
						}
						cur = cur.trim();
						for (int u = z+1; u < contextSen.size(); u++) {
							if (contextSen.get(u).contains(cur)) {
								sentence.getContext().set(z, null);
							}
						}
						z++;
					}
					
					//create core sentence by deleting extracted components
					Tree original = sentence.getOriginal();
					String inputSentence = Sentence.listToString(original.yield());
					String[] tokensOriginal = inputSentence.split(" ");
					String core = "";
					boolean[] deleteCore = sentence.getDelete();
					
					if (!Core.getStart().isEmpty()) {
						int y = 0;
						for (Integer start : Core.getStart()) {
							core = Core.getPrefix().get(y);
							for (int x = start; x <= Core.getEnd().get(y); x++) {
								if (deleteCore[x] == false) {
									core = core + " " + tokensOriginal[x];	
								}
							}
							if (!core.isEmpty()) {		
								if (!core.matches(" .")) {
									if (core.endsWith(" .")) {
										sentence.getCoreNew().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(core.trim())));
									} else if (core.endsWith(",")) {
										core = core.replace(" ,", "");
										sentence.getCoreNew().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(core.trim() + " .")));
									} 
									else {
										sentence.getCoreNew().add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(core.trim() + " .")));
									}
								}			
							}
									
							int[] setToFalse = new int[2];
							setToFalse[0] = start;
							setToFalse[1] = Core.getEnd().get(y) + 1;
							SentenceProcessor.updateDelete(setToFalse, sentence);
	
							y++;
						}
					}
					
					SentenceProcessor.produceCore(sentence.getDelete(), sentence);
					
					ArrayList<Tree> c = sentence.getCore();
					ArrayList<Tree> c2 = new ArrayList<Tree>();
					ArrayList<String> st = new ArrayList<String>();
					for (Tree t : c) {
						String string = Sentence.listToString(t.yield());
						st.add(string);
					}
					
					for (String string : st) {
						if (string.startsWith(", ")) {
							string = string.replaceFirst(",", "");
							
						} 
						c2.add(RepresentationGenerator.parse(RepresentationGenerator.tokenize(string.trim())));
					}
				
					sentence.setCore(c2);
					
					//add context sentences
					ArrayList<Tree> contextSentences = sentence.getContext();
					ArrayList<String> contextString = new ArrayList<String>();
					
					for (Tree tCon : contextSentences) {
						if (tCon != null) {
							String stringC = Sentence.listToString(tCon.yield());
							contextString.add(stringC);
						}
					}
					
					ArrayList<Tree> coreSentencesC = sentence.getCoreNew();
					ArrayList<String> coreStringC = new ArrayList<String>();
					
					for (Tree tCore : coreSentencesC) {
						if (tCore != null) {
							String stringC = Sentence.listToString(tCore.yield());
							coreStringC.add(stringC);
						}
					}
					
					//cleanup punctuation
					for (String coreS : coreStringC) {
						int o = 0;
						for (String coreS2 : coreStringC) {
							if (!coreS2.equals(coreS)) {
								if (coreS2.contains(coreS)) {
									
									coreS2 = coreS2.replace(coreS, "") + ".";
									if (coreS2.endsWith(", .")) {
										coreS2 = coreS2.replace(", .", ".");
									}
									sentence.getCoreNew().set(o, RepresentationGenerator.parse(RepresentationGenerator.tokenize(coreS2)));
								}
							}
							o++;
						}
					}
					
					//remove duplicate context sentences
					for (String sCon : contextString) {
						ArrayList<Tree> coreSentences = sentence.getCoreNew();
						ArrayList<String> coreString = new ArrayList<String>();
						
						for (Tree tCore : coreSentences) {
							if (tCore != null) {
								String stringC = Sentence.listToString(tCore.yield());
								coreString.add(stringC);
							}
						}
						
						String taggedString = RepresentationGenerator.posTag(sCon);
						String[] taggedStringTokens = taggedString.split(" ");
						String[] tokensCon = sCon.split(" ");
						
						if (taggedStringTokens[2].endsWith("_IN")) {
							String toCompare = "";
							for (int p = 2; p < tokensCon.length-1; p++) {
								toCompare = toCompare + tokensCon[p] + " ";
							}
							int number = 0;
							for (String coreS : coreString) {
								if (coreS.contains(toCompare)) {
									String comma = toCompare + ",";
									if (coreS.contains(comma)) {
										coreS = coreS.replace(comma, "");
									} else {
										coreS = coreS.replace(toCompare, "");
									}
									if (coreS.endsWith(", .")) {
										coreS = coreS.replace(", .", ".");
									}
									sentence.getCoreNew().set(number, RepresentationGenerator.parse(RepresentationGenerator.tokenize(coreS)));
								}
								number++;
							}
						}
						if (taggedStringTokens[2].endsWith("_WRB")) {
							String toCompare = "";
							for (int p = 3; p < tokensCon.length-1; p++) {
								toCompare = toCompare + tokensCon[p] + " ";
								
							}
							int number = 0;
							for (String coreS : coreString) {
								if (coreS.contains(toCompare)) {
									
									String comma = toCompare + ",";
									if (coreS.contains(comma)) {
										coreS = coreS.replace(comma, "");
									} else {
										coreS = coreS.replace(toCompare, "");
									}
									if (coreS.endsWith(", .")) {
										coreS = coreS.replace(", .", ".");
									}
									sentence.getCoreNew().set(number, RepresentationGenerator.parse(RepresentationGenerator.tokenize(coreS)));
								}
								number++;
							}
						}
						
						if (taggedStringTokens.length > 4 && !taggedStringTokens[2].endsWith("_WRB") && !taggedStringTokens[2].endsWith("_IN")) {
							String toCompare = "";
							for (int p = 2; p < tokensCon.length-1; p++) {
								toCompare = toCompare + tokensCon[p] + " ";
							}
							
							int number = 0;
							for (String coreS : coreString) {
								if (coreS.contains(toCompare)) {
									
									String comma = toCompare + ",";
									if (coreS.contains(comma)) {
										coreS = coreS.replace(comma, "");
									} else {
										coreS = coreS.replace(toCompare, "");
									}
									if (coreS.endsWith(", .")) {
										coreS = coreS.replace(", .", ".");
									}
									sentence.getCoreNew().set(number, RepresentationGenerator.parse(RepresentationGenerator.tokenize(coreS)));
								}
								number++;
							}
						}
					}
					
					//extract selected prepositional phrases representing the last one in the input
					boolean pps = true;
					while (pps) {
						ArrayList<Tree> coreFinal2 = sentence.getCore();
						ArrayList<Tree> coreNewFinal2 = sentence.getCoreNew();
						
						for (Tree t : coreFinal2) {
							pps = PrepositionalPhraseExtractor.extractFinalPPs(sentence, t);
						}
						
						for (Tree t : coreNewFinal2) {
							pps = PrepositionalPhraseExtractor.extractFinalPPs(sentence, t);
						}
					}
					
						
					
					ArrayList<Tree> contextTrees = sentence.getContext();
					ArrayList<String> contextStrings = new ArrayList<String>();
					ArrayList<String> contextStrings2 = new ArrayList<String>();
					int counterNull = 0;
				
					for (Tree contextTree : contextTrees) {
						if (contextTree != null) {
							String stringC = Sentence.listToString(contextTree.yield());
							contextStrings.add(stringC);
							contextStrings2.add(stringC);
						} else {
							counterNull++;
						}
					}
				
					for (String sContext : contextStrings) {
						
						if (sContext.startsWith("This is when")) {
							sContext = sContext.replace("This is when ", "");
						} else if (sContext.startsWith("This was when")) {
							sContext = sContext.replace("This was when ", "");
						} else if (sContext.startsWith("This is")) {
							sContext = sContext.replace("This is ", "");
						} else if (sContext.startsWith("This was")) {
							sContext = sContext.replace("This was ", "");
						}
						
						int q = 0;
						for (String sContext2 : contextStrings2) {
							if (sContext2.contains(sContext) && !(sContext2.equals("This is " + sContext) || sContext2.equals("This was " + sContext) || sContext2.equals("This is when " + sContext) || sContext2.equals("This was when " + sContext)) && !sContext2.equals(sContext)) {
								sContext2 = sContext2.replace(sContext, "") + ".";
								if (sContext2.endsWith(", .")) {
									sContext2 = sContext2.replace(", .", ".");
								}
								sentence.getContext().set(q, RepresentationGenerator.parse(RepresentationGenerator.tokenize(sContext2)));
							}
							q++;
						}
					}
						
					int r2 = 0;
					for (String sContext : contextStrings) {
						String taggedString = RepresentationGenerator.posTag(sContext);
						String[] taggedStringTokens = taggedString.split(" ");
						String[] tokensCon = sContext.split(" ");
						
						String originalSen = Sentence.listToString(sentence.getOriginal().yield());
						String[] tokensO = originalSen.split(" ");
						int coun = 0;
						for (int r = 0; r < tokensO.length; r++) {
							if (tokensO[r].equals(tokensCon[2])) {
								coun++;
							}
						}
						
						if (coun == 1 && (sContext.startsWith("This is") || sContext.startsWith("This was"))) {
							if (taggedStringTokens[2].endsWith("RB")) {
								if (tokensCon.length == 4) {
									for (String sContext2 : contextStrings2) {
										if (sContext2.contains(tokensCon[2]) && !sContext2.equals("This is " + tokensCon[2] + " .") && !sContext2.equals("This was " + tokensCon[2] + " .")) {
											sentence.getContext().set(r2+counterNull, null);
										}
									}
								}
							}
						} 
						r2++;
					}
					
					ArrayList<Tree> core2 = sentence.getCoreNew();
					ArrayList<String> coreString2 = new ArrayList<String>();
					int r3 = 0;
					for (Tree t2 : core2) {
						String stringC = Sentence.listToString(t2.yield());
						if (stringC.endsWith(", .")) {
							stringC = stringC.replace(", .", ".");
						}
						coreString2.add(stringC);
						sentence.getCoreNew().set(r3, RepresentationGenerator.parse(RepresentationGenerator.tokenize(stringC)));
						r3++;
					}
					
					ArrayList<Tree> coreFinal = sentence.getCore();
					ArrayList<Tree> coreNewFinal = sentence.getCoreNew();
					ArrayList<String> coreFinalString = new ArrayList<String>();
					ArrayList<String> coreNewFinalString = new ArrayList<String>();
					
					for (Tree t : coreFinal) {
						coreFinalString.add(Sentence.listToString(t.yield()));
					}
					
					for (Tree t : coreNewFinal) {
						coreNewFinalString.add(Sentence.listToString(t.yield()));
					}
					
					for (String s1 : coreFinalString) {
						String taggedCore = RepresentationGenerator.posTag(s1);
						String[] taggedCoreTokens = taggedCore.split(" ");
						if ((taggedCoreTokens[0].endsWith("_VBG") || taggedCoreTokens[0].endsWith("VBN"))) {
							sentence.setInput("");
						}
					}
					
					for (String s2 : coreNewFinalString) {
						String taggedCore = RepresentationGenerator.posTag(s2);
						String[] taggedCoreTokens = taggedCore.split(" ");
						if ((taggedCoreTokens[0].endsWith("_VBG") || taggedCoreTokens[0].endsWith("VBN"))) {
							sentence.setInput("");
						}
					}
					
					int r4 = 0;
					for (String s1 : coreFinalString) {
						for (String s3 : delPhrase) {
							if (s1.contains(s3)) {
								s1 = s1.replace(s3, "");
								sentence.getCoreNew().set(r4, RepresentationGenerator.parse(RepresentationGenerator.tokenize(s1)));
							}
						}
						r4++;
					}
					
					int r5 = 0;
					for (String s2 : coreNewFinalString) {
						for (String s3 : delPhrase) {
							if (s2.contains(s3)) {
								s2 = s2.replace(s3, "");
								sentence.getCoreNew().set(r5, RepresentationGenerator.parse(RepresentationGenerator.tokenize(s2)));
							}
						}
						r5++;
					}
					
					ArrayList<Tree> contextFinal = sentence.getContext();
					ArrayList<String> contextFinalString = new ArrayList<String>();
					int[] contexts = new int[contextFinal.size()];
					
					if (SentenceProcessor.positions.isEmpty() && !sentence.getInput().equals("")) {
						for (Tree treeCon : contextFinal) {
							if (treeCon != null) {
								contextFinalString.add(Sentence.listToString(treeCon.yield()));
							}
						}
						
						for (String stringCon : contextFinalString) {
							stringCon = stringCon + " 0";
							sentence.getConWithNumber().add(stringCon);
						}
					}
					
					if (!SentenceProcessor.positions.isEmpty() && !sentence.getInput().equals("")) {
						for (Tree treeCon : contextFinal) {
							if (treeCon != null) {
								contextFinalString.add(Sentence.listToString(treeCon.yield()));
							}
						}
						
						String originalString = Sentence.listToString(sentence.getOriginal().yield());
						String[] tokensOriginal2 = originalString.split(" ");
						
						int tracker = 0;
						Integer posStart = 0;
						ArrayList<String> cores = new ArrayList<String>();
						SentenceProcessor.positions.add(tokensOriginal2.length);
						
						for (Integer positions : SentenceProcessor.positions) {
							String phr = "";
								
							for (int c4 = posStart; c4 < positions; c4++) {
								phr = phr + " " + tokensOriginal2[c4];
								tracker = c4;
							}
							if (!phr.isEmpty()) {
								cores.add(phr);
							}
							posStart = tracker+2;
						}
							
						int counterCon = 0;
						
						for (String stringCon : contextFinalString) {
							String[] stringConTokens = stringCon.split(" ");
							int length = stringConTokens.length;
							int len = 0;
							if (length > 7) {
								len = 3;
							} else if (length > 6) {
								len = 2;
							} else {
								len = 1;
							}
							String rest = "";
							ArrayList<String> restSen = new ArrayList<String>();
							for (int le = length-1-len; le < length-1; le++) {
								rest = rest + " " + stringConTokens[le];
							
							}
							restSen.add(rest);
								
							int counterContext = 0;
							for (String strCores : cores) {
								for (String r : restSen) {
									if (strCores.contains(r)) {
										contexts[counterCon] = counterContext;
									}
								}
								counterContext++;
							}
							sentence.getConWithNumber().add(stringCon + " " + contexts[counterCon]);
							counterCon++;
						}	
					}
							
					if (!sentence.getInput().equals("")) {
						sen.add(sentence);
					}
					
				}
				counterSentences++;	
			}
			fo.writeFile(sen, new File(output));
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
		
}