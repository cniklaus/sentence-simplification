package analysis;

import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;

/**
 * Class for converting an input NL sentence into a variety of representations that the transformation module can work with.
 * 
 * This class constitutes "stage 2" of the simplification framework.
 * 
 * @author christina
 *
 */
public class RepresentationGenerator {

	private static TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");  
	private static LexicalizedParser parser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	static MaxentTagger tagger = new MaxentTagger("tagger/english-left3words-distsim.tagger");
	private static String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
	@SuppressWarnings("rawtypes")
	private static AbstractSequenceClassifier classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	
	
	/**
	 * tokenizes the input sentence
	 * 
	 * @param sentence
	 * @return tokenized sentence
	 */
	public static List<CoreLabel> tokenize(String sentence) {
		
		Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(sentence));
		List<CoreLabel> tokens = tok.tokenize();
		
		return tokens;
	}
	
	
	/**
	 * creates the constituency-based parse tree of the input sentence
	 * 
	 * @param tokenized sentence
	 * @return parse tree
	 */
	public static Tree parse(List<CoreLabel> tokens) {
		
		Tree parse = parser.apply(tokens);
		
		return parse;
		
	}
	
	
	/**
	 * creates a named entity tagged version of the input sentence
	 * 
	 * @param sentence
	 * @return NE-tagged sentence
	 */
	public static String ner(String s) {
		return classifier.classifyToString(s);
	}
	
	
	/**
	 * POS tags the input sentence
	 * 
	 * @param sentence
	 * @return POS-tagged sentence
	 */
	public static String posTag(String s) {
		return tagger.tagString(s);
	}
}
