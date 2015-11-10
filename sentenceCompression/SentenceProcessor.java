package sentenceCompression;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

public class SentenceProcessor {
	
	private static final String singularPresent = " is ";
	private static final String singularPast = " was ";
	private static final String pluralPresent = " are ";
	private static final String pluralPast = " were ";
	
	private static TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");  
	private static LexicalizedParser parser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	
	
	public static List<CoreLabel> tokenize(String sentence) {
		
		Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(sentence));
		List<CoreLabel> tokens = tok.tokenize();
		
		return tokens;
	}
	
	
	public static Tree parse(List<CoreLabel> tokens) {
		
		Tree parse = parser.apply(tokens);
		
		return parse;
		
	}
	
	
	public static int[] matchSentences(String initialSentence, String clauseToDelete) {
		
		//System.out.println("original: " + initialSentence);
		//System.out.println("to delete: " + clauseToDelete);
		
		int[] startEndOfDelete = new int[2];
		String[] clauseTokens = clauseToDelete.split(" ");
		String[] originalTokens = initialSentence.split(" ");
		int start = 0;
		int end = 0;
		
		int i = 0;
		int endOfString = 0;
		int startOfString = 0;
		
		while (i < originalTokens.length) {
			
			if (originalTokens[i].equals(clauseTokens[0])) {
				start = i;
				int j = 0;
				while (j < clauseTokens.length && originalTokens[i+j].equals(clauseTokens[j])) {
					//System.out.println("tokenOriginal: " + originalTokens[i+j]);
					//System.out.println("tokenDeleteClause: " + clauseTokens[j]);
					end = i + j;
					j++;
					
				}
				if (end - start == clauseTokens.length-1) {
					endOfString = end;
					startOfString = start;
				}
			}
			i++;
		}
		//System.out.println("start: " + startOfString);
		//System.out.println("end: " + endOfString);
		
		startEndOfDelete[0] = startOfString;
		startEndOfDelete[1] = endOfString;
		
		return startEndOfDelete;
	}


	public static void updateSentence(String relClause, String clauseToDelete, String sentence, CoreContextSentence coreContextSentence, boolean isOriginal, int contextNumber) {
		
		if (isOriginal == true) {
			if (contextNumber < 0) {
				int[] del = SentenceProcessor.matchSentences(sentence, clauseToDelete);
				if (relClause != null) {
					SentenceProcessor.updateContext(relClause, coreContextSentence);
				}
				
				SentenceProcessor.updateDelete(del, coreContextSentence);
			} else {
				sentence = sentence.replace(clauseToDelete, "");
				
				Tree contextTree = SentenceProcessor.parse(SentenceProcessor.tokenize(sentence));
				coreContextSentence.getCoreNew().remove(contextNumber);
				if (!sentence.isEmpty()) {
					coreContextSentence.getCoreNew().add(contextNumber, contextTree);
				}
				
			}
		} else {
			sentence = sentence.replace(clauseToDelete, "");
			
			Tree contextTree = SentenceProcessor.parse(SentenceProcessor.tokenize(sentence));
			coreContextSentence.getContext().remove(contextNumber);
			if (!sentence.isEmpty()) {
				coreContextSentence.getContext().add(contextNumber, contextTree);
			}
		}
	}

	
	public static void updateContext(String clause, CoreContextSentence sentence) {
		
		List<CoreLabel> tokens = SentenceProcessor.tokenize(clause);
		Tree parse = SentenceProcessor.parse(tokens);
		sentence.getContext().add(parse);
		
	}


	public static void updateDelete(int[] del, CoreContextSentence sentence) {
		
		boolean[] delete = sentence.getDelete();
		for (int j = 0; j < delete.length; j++) {
			if (delete[j]==false && del[0]<=j && j<=del[1]) {
				delete[j] = true;
			}
		}
		sentence.setDelete(delete);
	}
	
	
	public static void addCore(String phrase, CoreContextSentence sen) {
		phrase = phrase.replace(". .", ".");
		phrase = phrase.replace("...", ".");
		Tree t = SentenceProcessor.parse(tokenize(phrase));
		//sen.getCoreNew().add(t);
		
		ArrayList<String> coreSen = new ArrayList<String>();
		for (Tree tree : sen.getCoreNew()) {
			String s = Sentence.listToString(tree.yield());
			coreSen.add(s);
		}
		
		//System.out.println("phrase: " + phrase);
		boolean included = false;
		for (String s : coreSen) {
			//System.out.println("to compare: " + s);
			if (s.equals(phrase)) {
				included = true;
				break;
			}
		}
		
		if(!included) {
			sen.getCoreNew().add(t);
		}
		
	}
	
	
	public static void produceCore(boolean[] delete, CoreContextSentence sentence) {
		
		Tree original = sentence.getOriginal();
		String inputSentence = Sentence.listToString(original.yield());
		String[] tokensOriginal = inputSentence.split(" ");
		String core = "";
		
		for (int i = 0; i < delete.length; i++) {
			if (delete[i] == false) {
				core = core + " " + tokensOriginal[i];
			}
		}
		
		if (!core.isEmpty()) {
			
			if (!core.matches(" .")) {
				//System.out.println(core);
				if (core.endsWith(",")) {
					core = core.replace(",", "");
				}
				else if (core.endsWith(", .")) {
					core = core.replace(", .", ".");
				}
				sentence.getCore().add(parse(tokenize(core.trim())));
			}
			
		}
		
	}
	
	
	public static void deleteTokenInOriginal(int n, CoreContextSentence sentence) {
		boolean[] del = sentence.getDelete();
		del[n] = true;
		sentence.setDelete(del);
	}
	
	
	public static boolean pruneContextSentences(CoreContextSentence sentence) {
		
		int n = 0;
		ArrayList<Tree> currentContext = sentence.getContext();
		boolean[] isPruned = new boolean[currentContext.size()];
		boolean prune = false;
		
		boolean isPrunedRelClauseNonRestrictive = false;
		boolean isPrunedInitialPPs = false;
		boolean isPrunedAppositivePPs = false;
		boolean isPrunedADJPappositives = false;
		boolean isPrunedNPappositives = false;
		boolean isPrunedVPappositives = false;
		boolean isPrunedAccording = false;
		boolean isPrunedGerundAfterNNP = false;
		boolean isPrunedIncluding = false;
		boolean isPrunedProperNouns = false;
		boolean isPrunedInfixWhen = false;
		boolean isPrunedInfixAsSince = false;
		boolean isPrunedInfixCommaPPAfterBefore = false;
		boolean isPrunedInfixPPAfterBefore = false;
		boolean isPrunedInfixPPSAfterBefore = false;
		boolean isPrunedSBARAfterBefore = false;
		boolean isPrunedInitialWhen = false;
		boolean isPrunedInitialThoughAlthoughBecause = false;
		boolean isPrunedInfixBecauseThoughAlthough = false;
		boolean isPrunedInfixAndOrBut = false;
		boolean isPrunedCommaAndOrBut = false;
		boolean isPrunedADVPappositives = false;
		boolean isPrunedNPPappositives = false;
		boolean isPrunedGerundAfterWhile = false;
		boolean isPrunedGerundAfterComma = false;
		boolean isPrunedGerundSentenceStart = false;
		boolean isPrunedByGerund = false;
		boolean isPrunedColon = false;
		boolean isPrunedParentheses = false;
		boolean isPrunedBrackets = false;
		boolean isPrunedIf = false;
		boolean isPrunedFromTo = false;
		boolean isPrunedSo = false;
		boolean isPrunedToDo = false;
		
		while (n < currentContext.size()) {
			
			if (n < sentence.getContext().size()) {	
				isPrunedRelClauseNonRestrictive = RelativeClauseExtractor.extractNonRestrictiveRelativeClauses(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {		
				isPrunedInfixWhen = ConjoinedPhrasesExtractor.infixWhenSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedInfixAsSince = ConjoinedPhrasesExtractor.infixAsSinceSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedInfixCommaPPAfterBefore = ConjoinedPhrasesExtractor.infixCommaPPAfterBeforeSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedInfixPPAfterBefore = ConjoinedPhrasesExtractor.infixPPAfterBeforeSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedInfixPPSAfterBefore = ConjoinedPhrasesExtractor.infixPPSAfterBeforeSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedSBARAfterBefore = ConjoinedPhrasesExtractor.infixSBARAfterBeforeSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedInitialWhen = ConjoinedPhrasesExtractor.initialWhenSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedInitialThoughAlthoughBecause = ConjoinedPhrasesExtractor.initialThoughAlthoughBecauseSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedInfixBecauseThoughAlthough = ConjoinedPhrasesExtractor.infixBecauseThoughAlthoughSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedInfixAndOrBut = ConjoinedPhrasesExtractor.infixAndOrButSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedCommaAndOrBut = ConjoinedPhrasesExtractor.infixCommaAndOrButSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedIf = ConjoinedPhrasesExtractor.ifSplit(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedSo = ConjoinedPhrasesExtractor.extractSo(sentence, currentContext.get(n), false, n);
			}
				
			if (n < sentence.getContext().size()) {	
				isPrunedAccording = PrepositionalPhraseExtractor.extractAccording(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedGerundAfterWhile = ConjoinedPhrasesExtractor.extractWhilePlusParticiple(sentence, currentContext.get(n), false, n);
			}

			if (n < sentence.getContext().size()) {	
				isPrunedToDo = PrepositionalPhraseExtractor.extractToDo(sentence, currentContext.get(n), false, n);
			}
			//if (n < sentence.getContext().size()) {	
				//isPrunedInitialPPs = PrepositionalPhraseExtractor.extractInitialPPs(sentence, currentContext.get(n), false, n);
			//}
			if (n < sentence.getContext().size()) {	
				isPrunedAppositivePPs = PrepositionalPhraseExtractor.extractInfixPPs(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedFromTo = PrepositionalPhraseExtractor.extractFromTo(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedADJPappositives = AdjectiveAdverbPhraseExtractor.extractAdjectivePhrases(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedNPappositives = InitialNounPhraseExtractor.extractInitialParentheticalNounPhrases(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedVPappositives = ParticipialPhraseExtractor.extractPresentAndPastParticiples(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {
				isPrunedADVPappositives = AdjectiveAdverbPhraseExtractor.extractAdverbPhrases(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedNPPappositives = AppositivePhraseExtractor.extractNonRestrictiveAppositives(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedGerundAfterNNP = ParticipialPhraseExtractor.extractPresentParticiplesAfterNNP(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedIncluding = PrepositionalPhraseExtractor.extractIncluding(sentence, currentContext.get(n), false, n);
			}
			
			if (n < sentence.getContext().size()) {	
				isPrunedProperNouns = AppositivePhraseExtractor.extractRestrictiveAppositives(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedColon = Punctuation.splitAtColon(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedParentheses = Punctuation.extractParentheses(sentence, currentContext.get(n), false, n);
			}
			if (n < sentence.getContext().size()) {	
				isPrunedBrackets = Punctuation.removeBrackets(sentence, currentContext.get(n), false, n);
			}
			
			if (isPrunedRelClauseNonRestrictive || isPrunedInitialPPs || isPrunedAppositivePPs || isPrunedADJPappositives ||
					isPrunedNPappositives || isPrunedVPappositives || isPrunedAccording || isPrunedGerundAfterNNP || isPrunedIncluding || isPrunedProperNouns ||
					isPrunedInfixWhen || isPrunedInfixAsSince || isPrunedInfixCommaPPAfterBefore || isPrunedInfixPPAfterBefore || isPrunedInfixPPSAfterBefore ||
					isPrunedSBARAfterBefore || isPrunedInitialWhen || isPrunedInitialThoughAlthoughBecause || isPrunedInfixBecauseThoughAlthough || 
					isPrunedInfixAndOrBut || isPrunedCommaAndOrBut || isPrunedADVPappositives || isPrunedNPPappositives || isPrunedGerundAfterWhile || 
					isPrunedGerundAfterComma || isPrunedGerundSentenceStart || isPrunedByGerund || isPrunedColon || isPrunedParentheses || isPrunedBrackets || isPrunedIf
					|| isPrunedFromTo || isPrunedSo || isPrunedToDo) {
				isPruned[n] = true;
			}
			
			
			n++;
		}
		
		
		
		for (int i = 0; i < isPruned.length; i++) {
			if (isPruned[i] == true) {
				prune = true;
			}
		}
		
		return prune;
		
	}
	
	
	public static boolean isPresent(Tree tree) {
		
		for (Tree t : tree) {
			if (t.label().value().equals("S")) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+1).label().value().equals("VP")) {
						if (t.getChild(i+1).getChild(0).label().value().equals("VBP") || t.getChild(i+1).getChild(0).label().value().equals("VBZ")) {
							return true;
						}
					}
				}
				for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
					if (t.getChild(i).label().value().equals("NP") && t.getChild(i+2).label().value().equals("VP")) {
						if (t.getChild(i+2).getChild(0).label().value().equals("VBP") || t.getChild(i+2).getChild(0).label().value().equals("VBZ")) {
							return true;
						}
					}
				}
			}
			else if (t.label().value().equals("SINV")) {
				for (int i = 0; i < t.getChildrenAsList().size()-1; i++) {
					if (t.getChild(i).label().value().equals("VP") && t.getChild(i+1).label().value().equals("NP")) {
						if (t.getChild(i).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(0).label().value().equals("VBZ")) {
							return true;
						}
					}
				}
				for (int i = 0; i < t.getChildrenAsList().size()-2; i++) {
					if (t.getChild(i).label().value().equals("VP") && t.getChild(i+2).label().value().equals("NP")) {
						if (t.getChild(i).getChild(0).label().value().equals("VBP") || t.getChild(i).getChild(0).label().value().equals("VBZ")) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	
	public static boolean isSingular(LabeledWord label) {
		
		if (!label.tag().value().equals("NN") && !label.tag().value().equals("NNP") && !label.tag().value().equals("PRP")) {
			return false;
		}
		
		return true;
	}

	
	public static String setAux(boolean singular, boolean present) {
		String aux = "";
		
		if (singular && present) {
			aux = singularPresent;
		} else if (singular && !present) {
			aux = singularPast;
		} else if (!singular && present) {
			aux = pluralPresent;
		} else if (!singular && !present) {
			aux = pluralPast;
		}
		
		return aux;
	}


	/**
	public static boolean pruneCoreSentences(CoreContextSentence sentence) {
		int n = 0;
		//ArrayList<Tree> currentCore = sentence.getCoreNew();
		boolean[] isPruned = new boolean[sentence.getCoreNew().size()];
		boolean prune = false;
		
		boolean isPrunedRelClauseNonRestrictive = false;
		boolean isPrunedRelClauseRestrictive = false;
		boolean isPrunedInitialPPs = false;
		boolean isPrunedAppositivePPs = false;
		boolean isPrunedADJPappositives = false;
		boolean isPrunedNPappositives = false;
		boolean isPrunedVPappositives = false;
		boolean isPrunedAccording = false;
		boolean isPrunedGerundAfterNNP = false;
		boolean isPrunedIncluding = false;
		boolean isPrunedProperNouns = false;
		boolean isPrunedInfixWhen = false;
		boolean isPrunedInfixAsSince = false;
		boolean isPrunedInfixCommaPPAfterBefore = false;
		boolean isPrunedInfixPPAfterBefore = false;
		boolean isPrunedInfixPPSAfterBefore = false;
		boolean isPrunedSBARAfterBefore = false;
		boolean isPrunedInitialWhen = false;
		boolean isPrunedInitialThoughAlthoughBecause = false;
		boolean isPrunedInfixBecauseThoughAlthough = false;
		boolean isPrunedInfixAndOrBut = false;
		boolean isPrunedCommaAndOrBut = false;
		boolean isPrunedADVPappositives = false;
		boolean isPrunedNPPappositives = false;
		boolean isPrunedGerundAfterWhile = false;
		boolean isPrunedGerundAfterComma = false;
		boolean isPrunedGerundSentenceStart = false;
		boolean isPrunedByGerund = false;
		boolean isPrunedColon = false;
		boolean isPrunedParentheses = false;
		boolean isPrunedBrackets = false;
		boolean isPrunedIf = false;
		boolean isPrunedFromTo = false;
		
		while (n < sentence.getCoreNew().size()) {
			//String str = Sentence.listToString(currentContext.get(n).yield());
			//String[] toks = str.split(" ");
			
			//System.out.println(toks[0]);
			//System.out.println(toks[1]);
			
			//if (!(toks[0].equals("It") && toks[1].equals("is/was"))) {
			if (n < sentence.getCoreNew().size()) {
				isPrunedRelClauseNonRestrictive = RelativeClauseExtractor.extractNonRestrictiveRelativeClauses(sentence, sentence.getCoreNew().get(n), true, n);
			}
				//isPrunedRelClauseRestrictive = RelativeClauseExtractor.extractRestrictiveRelativeClauses(sentence, currentContext.get(n), false, n);
		//	}
			
			//if (!(toks[0].equals("This") && (toks[1].equals("is/was") || toks[1].equals("is") || toks[1].equals("was") || toks[1].equals("are") || toks[1].equals("were")))) {
			if (n < sentence.getCoreNew().size()) {
				isPrunedInfixWhen = ConjoinedPhrasesExtractor.infixWhenSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedInfixAsSince = ConjoinedPhrasesExtractor.infixAsSinceSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedInfixCommaPPAfterBefore = ConjoinedPhrasesExtractor.infixCommaPPAfterBeforeSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedInfixPPAfterBefore = ConjoinedPhrasesExtractor.infixPPAfterBeforeSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedInfixPPSAfterBefore = ConjoinedPhrasesExtractor.infixPPSAfterBeforeSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedSBARAfterBefore = ConjoinedPhrasesExtractor.infixSBARAfterBeforeSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedInitialWhen = ConjoinedPhrasesExtractor.initialWhenSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedInitialThoughAlthoughBecause = ConjoinedPhrasesExtractor.initialThoughAlthoughBecauseSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedInfixBecauseThoughAlthough = ConjoinedPhrasesExtractor.infixBecauseThoughAlthoughSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedInfixAndOrBut = ConjoinedPhrasesExtractor.infixAndOrButSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedCommaAndOrBut = ConjoinedPhrasesExtractor.infixCommaAndOrButSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
			if (n < sentence.getCoreNew().size()) {
				isPrunedIf = ConjoinedPhrasesExtractor.ifSplit(sentence, sentence.getCoreNew().get(n), true, n);
			}
				
				if (n < sentence.getCoreNew().size()) {
					isPrunedAccording = GerundClauseExtractor.extractAccording(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedGerundAfterWhile = GerundClauseExtractor.extractGerundAfterWhile(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedGerundAfterComma = GerundClauseExtractor.extractGerundAfterComma(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedByGerund = GerundClauseExtractor.extractByGerund(sentence, sentence.getCoreNew().get(n), true, n);
				}
			//}
				if (n < sentence.getCoreNew().size()) {
					isPrunedInitialPPs = PrepositionalPhraseExtractor.extractInitialPPs(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedFromTo = PrepositionalPhraseExtractor.extractFromTo(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedAppositivePPs = PrepositionalPhraseExtractor.extractAppositivePPs(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedADJPappositives = AppositivePhraseExtractor.extractAdjectivePhraseAppositives(sentence, sentence.getCoreNew().get(n), true, n);
				}
				
			//isPrunedNPappositives = AppositivePhraseExtractor.extractNounPhraseAppositives(sentence, currentContext.get(n), false, n);
				if (n < sentence.getCoreNew().size()) {
					isPrunedVPappositives = AppositivePhraseExtractor.extractVerbPhraseAppositives(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedADVPappositives = AppositivePhraseExtractor.extractAdverbialPhraseAppositives(sentence, sentence.getCoreNew().get(n), false, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedNPPappositives = AppositivePhraseExtractor.extractProperNounPhraseAppositives(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedGerundAfterNNP = GerundClauseExtractor.extractGerundAfterNNP(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedIncluding = GerundClauseExtractor.extractIncluding(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedGerundSentenceStart = GerundClauseExtractor.extractGerundSentenceStart(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedProperNouns = ProperNounsExtractor.extractAdjectivesNounsNNPs(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedColon = Punctuation.splitAtColon(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedParentheses = Punctuation.parentheses(sentence, sentence.getCoreNew().get(n), true, n);
				}
				if (n < sentence.getCoreNew().size()) {
					isPrunedBrackets = Punctuation.removeBrackets(sentence, sentence.getCoreNew().get(n), true, n);
				}
			
			if (isPrunedRelClauseNonRestrictive || isPrunedRelClauseRestrictive || isPrunedInitialPPs || isPrunedAppositivePPs || isPrunedADJPappositives ||
					isPrunedNPappositives || isPrunedVPappositives || isPrunedAccording || isPrunedGerundAfterNNP || isPrunedIncluding || isPrunedProperNouns ||
					isPrunedInfixWhen || isPrunedInfixAsSince || isPrunedInfixCommaPPAfterBefore || isPrunedInfixPPAfterBefore || isPrunedInfixPPSAfterBefore ||
					isPrunedSBARAfterBefore || isPrunedInitialWhen || isPrunedInitialThoughAlthoughBecause || isPrunedInfixBecauseThoughAlthough || 
					isPrunedInfixAndOrBut || isPrunedCommaAndOrBut || isPrunedADVPappositives || isPrunedNPPappositives || isPrunedGerundAfterWhile || 
					isPrunedGerundAfterComma || isPrunedGerundSentenceStart || isPrunedByGerund || isPrunedColon || isPrunedParentheses || isPrunedBrackets || isPrunedIf
					|| isPrunedFromTo) {
				isPruned[n] = true;
			}
			
			
			n++;
		}
		
		
		
		for (int i = 0; i < isPruned.length; i++) {
			if (isPruned[i] == true) {
				prune = true;
			}
		}
		
		return prune;
		
	}
	*/
	
	
	public static String collapseWhitespace(String value) {
		// Replace all whitespace blocks with single spaces.
		return value.replaceAll("\\s+", " ");
	}
	
	
}
