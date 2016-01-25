package sentenceCompression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;


public class CoreContextApp {
	
		
	public static void main(String[] args) throws IOException, ClassCastException, ClassNotFoundException {
		
		
		/** superset for intra-sentential attribution
		//construct URL to WordNet Dictionary directory on the computer
        String wordNetDirectory = "WordNet-3.0";
        String path = wordNetDirectory + File.separator + "dict";
        URL url = new URL("file", null, path);      

        //construct the Dictionary object and open it
        IDictionary dict = new Dictionary(url);
        dict.open();
        
        Properties props = new Properties(); 
        props.put("annotators", "tokenize, ssplit, pos, lemma"); 
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        String text = "disclosed"; 
        Annotation document = pipeline.process(text);  
        String lemma = "";
        for(CoreMap sentence: document.get(SentencesAnnotation.class))
        {    
            for(CoreLabel token: sentence.get(TokensAnnotation.class))
            {       
                String word = token.get(TextAnnotation.class);      
                lemma = token.get(LemmaAnnotation.class); 
                System.out.println("word: " + word);
                System.out.println("lemmatized version :" + lemma);
            }
        }
        
        
        // look up first sense of the word "dog "
        IIndexWord idxWord = dict.getIndexWord (lemma, POS.VERB );
        IWordID wordID = idxWord.getWordIDs().get(0) ;
        IWord word = dict.getWord (wordID);         
        System.out.println("Id = " + wordID);
        System.out.println(" Lemma = " + word.getLemma());
        System.out.println(" Gloss = " + word.getSynset().getGloss()); 
		
       
        ISynset synset = word.getSynset();
        String LexFileName = synset.getLexicalFile().getName();
        System.out.println("Lexical Name : "+ LexFileName);
		
       */
       
        
		
		String input = args[0];//"data/Wikipedia/Eval/baseball/bugs";
		String output = args[1];//"data/Wikipedia/Eval/baseball/baseballResultBugs";
		
		ArrayList<CoreContextSentence> sen = new ArrayList<CoreContextSentence>();
		
		FileOperator fo = new FileOperator();
		ArrayList<String> sentences = new ArrayList<String>();
		MaxentTagger tagger = new MaxentTagger("tagger/english-left3words-distsim.tagger");
	
		
		
		try {
			sentences = fo.readFile(new File(input));
			
			File f = new File("data/Wikipedia/Eval/Mandela/MandelaBugParsed");
			PrintWriter pw = new PrintWriter(f);
			TreePrint print = new TreePrint("penn");
			int i = 0;
			
			for (String s : sentences) {
				Core.getStart().clear();
				Core.getEnd().clear();
				Core.getPrefix().clear();
				Core.getPostfix().clear();
				String inputS = s;
				
				if (s.contains("-LRB-") && s.contains("-RRB-")) {
					
					s = s.replaceAll("-LRB-.*?-RRB-", "");
					s = s.replaceAll("  ,", " ,");
					//System.out.println("short: " + s);
				}
				
				List<CoreLabel> tokens = SentenceProcessor.tokenize(s);
				Tree parse = SentenceProcessor.parse(tokens);
				
				
				boolean keep = true;
				
				if (s.contains(" : ") || s.contains(" ; ")) {
					//System.out.println("contains: " + s);
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
				//System.out.println(keep);
				
				
				/** eliminating long NPs
				String taggedOriginal = tagger.tagString(s);
				String[] taggedOriginalTokens = taggedOriginal.split(" ");
				//System.out.println(taggedOriginal);
				
				
				for (int nOriginal = 0; nOriginal < taggedOriginalTokens.length-6; nOriginal++) {
					if ((taggedOriginalTokens[nOriginal].endsWith("_NN") || taggedOriginalTokens[nOriginal].endsWith("_NNS") ||
							taggedOriginalTokens[nOriginal].endsWith("_NNP") || taggedOriginalTokens[nOriginal].endsWith("_NNPS")) &&
							taggedOriginalTokens[nOriginal+1].endsWith("_,")) {
						for (int nOriginal2 = nOriginal+3; nOriginal2 < nOriginal+6; nOriginal2++) {
							if (taggedOriginalTokens[nOriginal2].endsWith("_,") &&
									(taggedOriginalTokens[nOriginal2-1].endsWith("_NN") || taggedOriginalTokens[nOriginal2-1].endsWith("_NNS") ||
											taggedOriginalTokens[nOriginal2-1].endsWith("_NNP") || taggedOriginalTokens[nOriginal2-1].endsWith("_NNPS"))) {
								for (int nOriginal3 = nOriginal2+3; nOriginal3 < nOriginal2+5; nOriginal3++) {
									if (nOriginal3 < taggedOriginalTokens.length) {
										if (taggedOriginalTokens[nOriginal3].endsWith("_,") &&
												(taggedOriginalTokens[nOriginal3-1].endsWith("_NN") || taggedOriginalTokens[nOriginal3-1].endsWith("_NNS") ||
														taggedOriginalTokens[nOriginal3-1].endsWith("_NNP") || taggedOriginalTokens[nOriginal3-1].endsWith("_NNPS"))) {
											for (int nOriginal4 = nOriginal3+1; nOriginal4 < nOriginal3+4; nOriginal4++) {
												if (nOriginal4 < taggedOriginalTokens.length) {
													if (taggedOriginalTokens[nOriginal4].endsWith("_NN") || taggedOriginalTokens[nOriginal4].endsWith("_NNS") ||
															taggedOriginalTokens[nOriginal4].endsWith("_NNP") || taggedOriginalTokens[nOriginal4].endsWith("_NNPS")) {
														System.out.println("sssssssssssssssss " + s);
														keep = false;
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
				*/
				
				
				String taggedOriginalGerundPP = tagger.tagString(s);
				String[] taggedOriginalTokensGerundPP = taggedOriginalGerundPP.split(" ");
				String pString = "";
				String ppString = "";
				boolean printPP = false;
				for (int numGerundPP = 0; numGerundPP < taggedOriginalTokensGerundPP.length; numGerundPP++) {
					for (int num3 = 0; num3 < 3; num3++) {
						if (taggedOriginalTokensGerundPP[num3].endsWith("_VBG") || taggedOriginalTokensGerundPP[num3].endsWith("_VBG") || taggedOriginalTokensGerundPP[num3].endsWith("_VBG") ||
							taggedOriginalTokensGerundPP[num3].endsWith("_VBN") || taggedOriginalTokensGerundPP[num3].endsWith("_VBN") || taggedOriginalTokensGerundPP[num3].endsWith("_VBN")) {
						
							if (num3 == 0 || !taggedOriginalTokensGerundPP[num3-1].endsWith("_,")) {
								if (num3 < 2 || (num3 == 2 && !(taggedOriginalTokensGerundPP[num3-1].endsWith("_VBD") || taggedOriginalTokensGerundPP[num3-1].endsWith("_VBP") || taggedOriginalTokensGerundPP[num3-1].endsWith("_VBZ")))) {
									for (int num4 = num3+1; num4 < taggedOriginalTokensGerundPP.length-1; num4++) {
										if (taggedOriginalTokensGerundPP[num4].endsWith("_,")) {
											if (taggedOriginalTokensGerundPP[num4+1].endsWith("_IN")) {
												pString = s;
												printPP = true;
												//System.out.println("ppppppppppppppppppppppppppppppppp " + s);
												
												boolean test = true;
												for (Tree t : parse) {
													if (test) {
														//if (t.label().value().equals("VP")) {
															if (t.getChildrenAsList().size() >= 2) {
																for (int numCommaPP = 0; numCommaPP < t.getChildrenAsList().size()-1; numCommaPP++) {
																	if (t.getChild(numCommaPP).label().value().equals(",") &&
																			t.getChild(numCommaPP+1).label().value().equals("PP")) {
																		ppString = Sentence.listToString(t.getChild(numCommaPP+1).yield());
																		test = false;
																	}
																}
															}
		 												//}
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
				String contextPP = "";
				if (printPP && !ppString.equals("")) {
					System.out.println("ppppppppppppppppppppppppppppppppp " + pString);
					//System.out.println(ppString);
					boolean tense = SentenceProcessor.isPresent(parse);
					String aux = SentenceProcessor.setAux(true, tense);
					contextPP = "This" + aux + ppString + " .";
					s = s.replace(ppString, "");
					s = s.replace("  ", " ");
					s = s.replace(",  ", "");
					s = s.replace(", , ", ",");
					tokens = SentenceProcessor.tokenize(s);
					parse = SentenceProcessor.parse(tokens);
					System.out.println(contextPP);
					System.out.println(s);
				}
				
				
				
				if (keep) {
					CoreContextSentence sentence = new CoreContextSentence();
					if (!contextPP.equals("")) {
						sentence.getContext().add(SentenceProcessor.parse(SentenceProcessor.tokenize(contextPP)));
					}
					sentence.setInput(inputS);
					sentence.setOriginal(parse);
					boolean[] delete = new boolean[tokens.size()];
					sentence.setDelete(delete);
					
					print.printTree(sentence.getOriginal(),pw); 
					
					
					System.out.println(i + " " + Sentence.listToString(sentence.getOriginal().yield()));
					i++;
					
					
					PrepositionalPhraseExtractor.extractToDo(sentence, sentence.getOriginal(), true, -1);
					
					RelativeClauseExtractor.extractNonRestrictiveRelativeClauses(sentence, sentence.getOriginal(), true, -1);
					
					PrepositionalPhraseExtractor.extractInitialPPs(sentence, sentence.getOriginal(), true, -1);
					PrepositionalPhraseExtractor.extractInfixPPs(sentence, sentence.getOriginal(), true, -1);
					PrepositionalPhraseExtractor.extractFromTo(sentence, sentence.getOriginal(), true, -1);
					PrepositionalPhraseExtractor.extractAccording(sentence, sentence.getOriginal(), true, -1);
					//PrepositionalPhraseExtractor.extractIncluding(sentence, sentence.getOriginal(), true, -1);
					
					
					
					ParticipialPhraseExtractor.extractPresentAndPastParticiples(sentence, sentence.getOriginal(), true, -1);
					
					InitialNounPhraseExtractor.extractInitialParentheticalNounPhrases(sentence, sentence.getOriginal(), true, -1);
					
					AdjectiveAdverbPhraseExtractor.extractAdjectivePhrases(sentence, sentence.getOriginal(), true, -1);
					
					
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
					ConjoinedClausesExtractor.infixAndOrButSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.infixCommaAndOrButSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.ifSplit(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.extractWhilePlusParticiple(sentence, sentence.getOriginal(), true, -1);
					ConjoinedClausesExtractor.extractSo(sentence, sentence.getOriginal(), true, -1);
					//ConjoinedClausesExtractor.or(sentence, sentence.getOriginal(), true, -1);
					
					Punctuation.splitAtColon(sentence, sentence.getOriginal(), true, -1);
					Punctuation.extractParentheses(sentence, sentence.getOriginal(), true, -1);
					Punctuation.removeBrackets(sentence, sentence.getOriginal(), true, -1);
					
					
					
					
					boolean isContextPruned = SentenceProcessor.pruneContextSentences(sentence);
					
					while (isContextPruned) {
						isContextPruned = SentenceProcessor.pruneContextSentences(sentence);
					}
				
				
				boolean isCorePruned = SentenceProcessor.pruneCoreSentences(sentence);
				//System.out.println(sentence.getCoreNew());
				
				int num = 0;
				while (isCorePruned && num < 20) {
					if (sentence.getCoreNew().size() > 0) {
						isCorePruned = SentenceProcessor.pruneCoreSentences(sentence);
						num++;
					}
				}
				
				//AdjectiveAdverbPhraseExtractor.extractAdverbPhrases(sentence, sentence.getOriginal(), true, -1);
				
				ArrayList<String> coreSen = new ArrayList<String>();
				for (Tree tree : sentence.getCoreNew()) {
					String str = Sentence.listToString(tree.yield());
					coreSen.add(str);
				}
				
				int n = 0;
				
				for (String str : coreSen) {
					//System.out.println("to compare: " + str);
					for (int j = n+1; j < coreSen.size(); j++) {
						//System.out.println("compared: " + coreSen.get(j));
						if (str.equals(coreSen.get(j))) {
							sentence.getCoreNew().remove(n);
						}
					}
					n++;
				}
				
				
				ArrayList<String> contextSen = new ArrayList<String>();
				int v = 0;
				for (Tree tree : sentence.getContext()) {
					String str = Sentence.listToString(tree.yield());
					
					if (str.endsWith(", .")) {
						str = str.replace(", .", ".");
						sentence.getContext().set(v, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
					}
					if (str.endsWith(", . ''")) {
						str = str.replace(", . ''", ".");
						sentence.getContext().set(v, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
					}
					if (str.endsWith(": .")) {
						str = str.replace(": .", ".");
						sentence.getContext().set(v, SentenceProcessor.parse(SentenceProcessor.tokenize(str)));
					}
					if (str.startsWith("This is .") || str.startsWith("This was .")) {
						sentence.getContext().set(v, null);
					}
					contextSen.add(str);
					//System.out.println(str);
					v++;
				}
				
				
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
					//System.out.println(cur);
					//System.out.println("cur: " + cur);
					for (int u = z+1; u < contextSen.size(); u++) {
						
						//System.out.println("comp: " + contextSen.get(u));
						if (contextSen.get(u).contains(cur)) {
							sentence.getContext().set(z, null);
						}
						
					}
					
					
					z++;
				}
				
				
				
				Tree original = sentence.getOriginal();
				String inputSentence = Sentence.listToString(original.yield());
				String[] tokensOriginal = inputSentence.split(" ");
				String core = "";
				boolean[] deleteCore = sentence.getDelete();
				
				
				if (!Core.getStart().isEmpty()) {
					
					int y = 0;
					for (Integer start : Core.getStart()) {
						//core = "";
						core = Core.getPrefix().get(y);
						for (int x = start; x <= Core.getEnd().get(y); x++) {
							
							if (deleteCore[x] == false) {
								core = core + " " + tokensOriginal[x];
								
							}
							
						}
						if (!core.isEmpty()) {		
							if (!core.matches(" .")) {
								
									if (core.endsWith(" .")) {
										sentence.getCoreNew().add(SentenceProcessor.parse(SentenceProcessor.tokenize(core.trim())));
									} else if (core.endsWith(",")) {
										core = core.replace(" ,", "");
										sentence.getCoreNew().add(SentenceProcessor.parse(SentenceProcessor.tokenize(core.trim() + " .")));
									} 
									else {
										sentence.getCoreNew().add(SentenceProcessor.parse(SentenceProcessor.tokenize(core.trim() + " .")));
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
					//System.out.println(string);
					if (string.startsWith(", ")) {
						string = string.replaceFirst(",", "");
						
					} 
					c2.add(SentenceProcessor.parse(SentenceProcessor.tokenize(string.trim())));
				}
			
				sentence.setCore(c2);
				
				ArrayList<Tree> contextSentences = sentence.getContext();
				//System.out.println(contextSentences);
				ArrayList<String> contextString = new ArrayList<String>();
				
				for (Tree tCon : contextSentences) {
					if (tCon != null) {
						//System.out.println(Sentence.listToString(tCon.yield()));
						String stringC = Sentence.listToString(tCon.yield());
						contextString.add(stringC);
					}
				}
				
				ArrayList<Tree> coreSentencesC = sentence.getCoreNew();
				ArrayList<String> coreStringC = new ArrayList<String>();
				
				for (Tree tCore : coreSentencesC) {
					if (tCore != null) {
						//System.out.println(Sentence.listToString(tCore.yield()));
						String stringC = Sentence.listToString(tCore.yield());
						coreStringC.add(stringC);
					}
				}
				//System.out.println(coreStringC);
				
				for (String coreS : coreStringC) {
					int o = 0;
					for (String coreS2 : coreStringC) {
						if (!coreS2.equals(coreS)) {
							if (coreS2.contains(coreS)) {
								
								coreS2 = coreS2.replace(coreS, "") + ".";
								//System.out.println(coreS2);
								if (coreS2.endsWith(", .")) {
									coreS2 = coreS2.replace(", .", ".");
								}
								sentence.getCoreNew().set(o, SentenceProcessor.parse(SentenceProcessor.tokenize(coreS2)));
							}
							
						}
						o++;
					}
				}
				
				for (String sCon : contextString) {
					ArrayList<Tree> coreSentences = sentence.getCoreNew();
					ArrayList<String> coreString = new ArrayList<String>();
					
					for (Tree tCore : coreSentences) {
						if (tCore != null) {
							//System.out.println(Sentence.listToString(tCon.yield()));
							String stringC = Sentence.listToString(tCore.yield());
							coreString.add(stringC);
						}
					}
					//System.out.println(coreString);
					
					String taggedString = tagger.tagString(sCon);
					String[] taggedStringTokens = taggedString.split(" ");
					String[] tokensCon = sCon.split(" ");
					
					//System.out.println("toCompare: " + toCompare);
					//System.out.println(taggedString);
					if (taggedStringTokens[2].endsWith("_IN")) {
						String toCompare = "";
						for (int p = 2; p < tokensCon.length-1; p++) {
							toCompare = toCompare + tokensCon[p] + " ";
						}
						int number = 0;
						for (String coreS : coreString) {
							if (coreS.contains(toCompare)) {
								//System.out.println(toCompare);
								String comma = toCompare + ",";
								if (coreS.contains(comma)) {
									coreS = coreS.replace(comma, "");
								} else {
									coreS = coreS.replace(toCompare, "");
								}
								if (coreS.endsWith(", .")) {
									coreS = coreS.replace(", .", ".");
								}
								sentence.getCoreNew().set(number, SentenceProcessor.parse(SentenceProcessor.tokenize(coreS)));
							}
							number++;
						}
					}
					if (taggedStringTokens[2].endsWith("_WRB")) {
						String toCompare = "";
						for (int p = 3; p < tokensCon.length-1; p++) {
							toCompare = toCompare + tokensCon[p] + " ";
							
						}
						//System.out.println(toCompare);
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
								sentence.getCoreNew().set(number, SentenceProcessor.parse(SentenceProcessor.tokenize(coreS)));
							}
							number++;
						}
					}
					
					if (taggedStringTokens.length > 4 && !taggedStringTokens[2].endsWith("_WRB") && !taggedStringTokens[2].endsWith("_IN")) {
						String toCompare = "";
						for (int p = 2; p < tokensCon.length-1; p++) {
							toCompare = toCompare + tokensCon[p] + " ";
						}
						//System.out.println(toCompare);
						
						int number = 0;
						for (String coreS : coreString) {
							//System.out.println("core " + coreS);
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
								sentence.getCoreNew().set(number, SentenceProcessor.parse(SentenceProcessor.tokenize(coreS)));
							}
							number++;
						}
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
			//System.out.println("counterNull: " + counterNull);
			
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
				//System.out.println("scontext: " + sContext);
				
				int q = 0;
				for (String sContext2 : contextStrings2) {
					//System.out.println("sContext2: " + sContext2);
					if (sContext2.contains(sContext) && !(sContext2.equals("This is " + sContext) || sContext2.equals("This was " + sContext) || sContext2.equals("This is when " + sContext) || sContext2.equals("This was when " + sContext)) && !sContext2.equals(sContext)) {
						//System.out.println("done: " + sContext2);
						sContext2 = sContext2.replace(sContext, "") + ".";
						if (sContext2.endsWith(", .")) {
							sContext2 = sContext2.replace(", .", ".");
						}
						sentence.getContext().set(q, SentenceProcessor.parse(SentenceProcessor.tokenize(sContext2)));
					}
					
					q++;
				}
				
				
			}
				
			
			int r2 = 0;
			for (String sContext : contextStrings) {
				
				
				String taggedString = tagger.tagString(sContext);
				String[] taggedStringTokens = taggedString.split(" ");
				String[] tokensCon = sContext.split(" ");
				
				String originalSen = Sentence.listToString(sentence.getOriginal().yield());
				String[] tokensO = originalSen.split(" ");
				int coun = 0;
				//System.out.println("con: " + tokensCon[2]);
				for (int r = 0; r < tokensO.length; r++) {
					
					if (tokensO[r].equals(tokensCon[2])) {
						//System.out.println("original: " + tokensO[r]);
						coun++;
					}
					
				}
				
				
				if (coun == 1 && (sContext.startsWith("This is") || sContext.startsWith("This was"))) {
					if (taggedStringTokens[2].endsWith("RB")) {
						if (tokensCon.length == 4) {
							
							for (String sContext2 : contextStrings2) {
								
								if (sContext2.contains(tokensCon[2]) && !sContext2.equals("This is " + tokensCon[2] + " .") && !sContext2.equals("This was " + tokensCon[2] + " .")) {
									//System.out.println("sContext: " + sContext);
									//contextNew.add(sContext);
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
				//System.out.println(stringC);
				sentence.getCoreNew().set(r3, SentenceProcessor.parse(SentenceProcessor.tokenize(stringC)));
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
				String taggedCore = tagger.tagString(s1);
				String[] taggedCoreTokens = taggedCore.split(" ");
				if ((taggedCoreTokens[0].endsWith("_VBG") || taggedCoreTokens[0].endsWith("VBN"))) {
					System.out.println("............................");
					sentence.setInput("");
				}
			}
			
			for (String s2 : coreNewFinalString) {
				String taggedCore = tagger.tagString(s2);
				String[] taggedCoreTokens = taggedCore.split(" ");
				if ((taggedCoreTokens[0].endsWith("_VBG") || taggedCoreTokens[0].endsWith("VBN"))) {
					System.out.println("............................");
					sentence.setInput("");
				}
			}
			
			for (Tree t : coreFinal) {
				IntraSententialAttribution.extractIntraSententialAttributions(sentence, t);
				
			}
			
			for (Tree t : coreNewFinal) {
				IntraSententialAttribution.extractIntraSententialAttributions(sentence, t);
				
			}
			
			ArrayList<Tree> coreFinal2 = sentence.getCore();
			ArrayList<Tree> coreNewFinal2 = sentence.getCoreNew();
			
			for (Tree t : coreFinal2) {
				//System.out.println("xx1: " + Sentence.listToString(t.yield()));
				AdjectiveAdverbPhraseExtractor.extractAdverbPhrases(sentence, t);
			}
			
			for (Tree t : coreNewFinal2) {
				//System.out.println("xx2: " + Sentence.listToString(t.yield()));
				AdjectiveAdverbPhraseExtractor.extractAdverbPhrases(sentence, t);
			}
			
			
			if (!sentence.getInput().equals("")) {
				sen.add(sentence);
			}
			
		}
			fo.writeFile(sen, new File(output));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
