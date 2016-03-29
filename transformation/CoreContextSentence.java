package transformation;

import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;

/**
 * Class representing a core and its associated context sentences
 * 
 * @author christina
 *
 */
public class CoreContextSentence {

	private Tree original;
	private String input;
	private ArrayList<Tree> core = new ArrayList<Tree>();
	private ArrayList<Tree> context = new ArrayList<Tree>();
	private ArrayList<Tree> coreNew = new ArrayList<Tree>();
	private ArrayList<Tree> attribution = new ArrayList<Tree>();
	private ArrayList<String> contextWithNumber = new ArrayList<String>();
		
	private boolean[] delete;
	
	public void setDelete(boolean[] delete) {
		this.delete = delete;
	}
	
	public boolean[] getDelete() {
		return delete;
	}
	
	public void setOriginal(Tree original) {
		this.original = original;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public void setCore(ArrayList<Tree> core) {
		this.core = core;
	}
	
	public void setContext(ArrayList<Tree> context) {
		this.context = context;
	}
	
	public void setCoreNew(ArrayList<Tree> coreNew) {
		this.coreNew = coreNew;
	}
	
	public void setAttribution(ArrayList<Tree> attribution) {
		this.attribution = attribution;
	}
	
	public void setConWithNumber(ArrayList<String> contextWithNumber) {
		this.contextWithNumber = contextWithNumber;
	}
	
	public Tree getOriginal() {
		return this.original;
	}
	
	public String getInput() {
		return this.input;
	}
	
	public ArrayList<Tree> getCore() {
		return this.core;
	}
	
	public ArrayList<Tree> getContext() {
		return this.context;
	}
	
	public ArrayList<Tree> getCoreNew() {
		return this.coreNew;
	}
	
	public ArrayList<Tree> getAttribution() {
		return this.attribution;
	}
	
	public ArrayList<String> getConWithNumber() {
		return this.contextWithNumber;
	}
}
