package sentenceCompression;

import java.util.ArrayList;

import edu.stanford.nlp.trees.Tree;

public class CoreContextSentence {

	private Tree original;
	private ArrayList<Tree> core = new ArrayList<Tree>();
	private ArrayList<Tree> context = new ArrayList<Tree>();
	private ArrayList<Tree> coreNew = new ArrayList<Tree>();
	
	//private String originalYield;
	//private ArrayList<String> coreYield = new ArrayList<String>();
	//private ArrayList<String> contextYield = new ArrayList<String>();
	
	
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
	
	public void setCore(ArrayList<Tree> core) {
		this.core = core;
	}
	
	public void setContext(ArrayList<Tree> context) {
		this.context = context;
	}
	
	public void setCoreNew(ArrayList<Tree> coreNew) {
		this.coreNew = coreNew;
	}
	
	public Tree getOriginal() {
		return this.original;
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
}
