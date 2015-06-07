package com.paradise.ddpath.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.paradise.ddpath.graph.CFGGraph;
import com.paradise.ddpath.graph.CFGNode;
import com.paradise.ddpath.nio.FileObject;

import static com.paradise.ddpath.parser.TokenType.*;

/**
 * 解析源程序的类，
 * @author 睿
 *
 */
public class CFGParser {
	private String filePath;
	private static ScannerFactory scannerFactory = null;
	private List<Token> tokenList;
	private Token token = null;
	private Stack<CFGNode> cycleStack = new Stack<CFGNode>();
	private Stack<List<CFGNode>> cycleSucListStack = new Stack<List<CFGNode>>();
	private Stack<CFGNode> doWhileStack = new Stack<CFGNode>();
	private boolean error = false;
	private String lastError;
	private FileObject fileObject;
	/**
	 * Current pointer
	 */
	private int cp = 0;
	private CFGGraph cfgGraph;
	private List<CFGNode> nodeList ;
	public CFGParser(String filePath){
		this.filePath = filePath;
	}
	public CFGParser(){
		cfgGraph = new CFGGraph();
		nodeList = cfgGraph.getNodeList();
	}
	public void compile(){
		
	}
	public void parseAll(FileObject fileObject){
		this.fileObject = fileObject;
		parseFile(fileObject);
		nextToken();
		parseTokens(cfgGraph.getNodeList());
	}
	
	public void parseFile(FileObject fileObject){
		parse(fileObject);
	}
	
	/** Parse contents of file.
     *  @param fileObject     The name of the file to be parsed.
     */
	private void parse(FileObject fileObject){
		if(null != fileObject){
			try {
				parse(fileObject, fileObject.getCharContent());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/** Parse contents of input stream.
     *  @param fileObject     The name of the file from which input stream comes.
     *  @param content        The input stream to be parsed.
     */
	private void parse(FileObject fileObject, CharSequence content){
		Scanner scanner = getScannerFactory().newScanner(content);
		tokenList = new ArrayList<Token>();
		scanner.nextToken();
		while(scanner.token().getTokenType() != TokenType.EOF){
			tokenList.add(scanner.token());
			scanner.nextToken();
		}
		//printTokens(System.out);
	}
	
	/** Try to open input stream with given name.
     *  Report an error if this fails.
     *  @param fileobject  The file name of the input stream to be opened.
	 * @throws IOException 
     */
	private CharSequence readSource(FileObject fileObject) throws IOException{
		CharSequence cs = null;
		if(fileObject != null){
			cs = fileObject.getCharContent();
		}
		return cs;
	}
	
	private ScannerFactory getScannerFactory(){
		if(null == scannerFactory){
			scannerFactory = new ScannerFactory();
		}
		return scannerFactory;
	}
	
	public List<Token> getTokens(){
		return tokenList;
	}
	
	public void printTokens(OutputStream out){
		PrintWriter printer = new PrintWriter(out);
		int index = 0;
		if(null != tokenList){
			for(Token token : tokenList){
				System.out.println(++index + "." + token.getName() + "--" + token.getTokenType().toString());
			}
		}
	}
	
	
	
	/**
	 * 解析单词序列，构成程序图
	 * @param root
	 */
	private List<CFGNode> parseTokens(CFGNode node){
		List<CFGNode> noSucList = new ArrayList<CFGNode>();
		noSucList.add(node);
		do{
			if(token == null || token.getTokenType() == EOF){
				return noSucList;
			}
			switch(token.getTokenType()){
			case IF:
				noSucList = parseIf(noSucList);
				break;
			case IDENTIFIER:
				noSucList =  parseStatement(noSucList);
				break;
			case LBRACE:
				//nextToken();
				noSucList = parseCompoundStatement(noSucList);
				break;
			case FOR:
				noSucList = parseFor(noSucList);
				break;
			case SWITCH:
				noSucList = parseSwitch(noSucList);
				break;
			case WHILE:
				noSucList = parseWhile(noSucList);
				break;
			case DO:
				noSucList = parseDoWhile(noSucList);
				break;
			case BREAK:
				noSucList = parseBreak(noSucList);
				break;
			case CONTINUE:
				noSucList = parseContinue(noSucList);
				break;
			default:
//				if(token == null || token.getTokenType() == EOF) {
//					nodeStack.clear();
//					return rootList;
//				}
				return noSucList;
			}
		}while(true);
	}
	/**
	 * 解析单词序列，构成程序图
	 * @param root
	 */
	private List<CFGNode> parseTokens(List<CFGNode> rootList){
		List<CFGNode> noSucList = new ArrayList<CFGNode>(rootList);
		do{
			if(token == null || token.getTokenType() == EOF){
				return noSucList;
			}
			switch(token.getTokenType()){
			case IF:
				noSucList = parseIf(noSucList);
				break;
			case IDENTIFIER:
				noSucList =  parseStatement(noSucList);
				break;
			case LBRACE:
				//nextToken();
				noSucList = parseCompoundStatement(noSucList);
				break;
			case FOR:
				noSucList = parseFor(noSucList);
				break;
			case SWITCH:
				noSucList = parseSwitch(noSucList);
				break;
			case WHILE:
				noSucList = parseWhile(noSucList);
				break;   
			case DO:
				noSucList = parseDoWhile(noSucList);
				break;
			case BREAK:
				noSucList = parseBreak(noSucList);
				break;
			case CONTINUE:
				noSucList = parseContinue(noSucList);
				break;
			default:
//				if(token == null || token.getTokenType() == EOF) {
//					nodeStack.clear();
//					return rootList;
//				}
				return noSucList;
			}
		}while(true);
	}
	private void nextToken(){
		if(null == tokenList || tokenList.size() <=0) {
			token = null;
			return;
		}
		if(cp < tokenList.size()){
			token = tokenList.get(cp++);
			System.out.println(token.getName());
			return;
		}
		token = null;
	}
	
	private void backToken(){
		cp--;
		if(null == tokenList || tokenList.size() <=0) {
			token = null;
			return;
		}
		if(cp < tokenList.size()){
			token = tokenList.get(cp);
			System.out.println(token.getName());
			return;
		}
		token = null;
	}
	
	/**
	 * 处理if
	 * @param root
	 */
	private List<CFGNode> parseIf(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		CFGNode node = newCFGNode();
		node.addToken(token);
		link(noSucNodeList, node);
		//扫描if控制语句
		nextToken();
		if(token.getTokenType() != LPAREN){
			error = true;
			lastError = "if 语句错误";
			return null ;
		}
		//扫描if条件语句
		scanPartenPair(node);
//		int count = 0;
//		//nextToken();
//		do{
//			
//			switch(token.getTokenType()){
//			case LPAREN:
//				count++;
//				node.addToken(token);
//				nextToken();
//				break;
//			case RPAREN:
//				count--;
//				node.addToken(token);
//				break;
//			default :
//				node.addToken(token);
//				nextToken();
//			}
//		}while(count > 0);
		//扫描true条件分支
		noSucNodeList.clear();
		noSucNodeList.add(node);
		//nextToken();
		List<CFGNode> trueList = new ArrayList<CFGNode>();
		if(token.getTokenType() == LBRACE){
			trueList = parseCompoundStatement(noSucNodeList);
		}else{
			trueList = parseStatement(noSucNodeList);
		}
		//扫描flase(else)条件分支
		
		List<CFGNode> falseList = new ArrayList<CFGNode>();
		if(token.getTokenType() == ELSE){
			falseList = parseElse(noSucNodeList);
		}else{
			falseList.add(node);
		}
		trueList.addAll(falseList);
		noSucNodeList = trueList;
		return noSucNodeList;
	}
	/**
	 * 解析简单语句
	 * @param root
	 */
	private List<CFGNode> parseStatement(CFGNode root){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>();
		noSucNodeList.add(root);
		switch(token.getTokenType()){
		case BREAK:
			return parseBreak(noSucNodeList);
		case CONTINUE:
			return parseContinue(noSucNodeList);
		}
		CFGNode node = newCFGNode();
		node.addToken(token);
		link(noSucNodeList, node);
		nextToken();
		while(token.getTokenType() != SEMI){
			node.addToken(token);
			nextToken();
			if(token.getTokenType() == null){
				error = true;
				lastError = "简单语句识别出错";
				return null;
			}
		}
		node.addToken(token);
		nextToken();
		noSucNodeList.clear();
		noSucNodeList.add(node);
		return noSucNodeList;
	}
	
	/**
	 * 解析简单语句
	 * @param root
	 */
	private List<CFGNode> parseStatement(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		switch(token.getTokenType()){
		case BREAK:
			return parseBreak(noSucNodeList);
		case CONTINUE:
			return parseContinue(noSucNodeList);
		}
		CFGNode node = newCFGNode();
		node.addToken(token);
		link(noSucNodeList, node);
		nextToken();
		while(token.getTokenType() != SEMI){
			node.addToken(token);
			nextToken();
			if(token.getTokenType() == null){
				error = true;
				lastError = "简单语句识别出错";
				return null;
			}
		}
		node.addToken(token);
		nextToken();
		noSucNodeList.clear();
		noSucNodeList.add(node);
		return noSucNodeList;
	}
	/**
	 * 解析复合语句
	 * @param node
	 */
	private List<CFGNode> parseCompoundStatement(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		nextToken();
		noSucNodeList = parseTokens(noSucNodeList);
		//nextToken();
		if(token.getTokenType() == RBRACE){
			nextToken();
			return noSucNodeList;
		}else{
			error = true;
			lastError = "解析复合语句出错" + token.getName();
			return noSucNodeList;
		}
	}
	/**
	 * 解析复合语句
	 * @param node
	 */
	private List<CFGNode> parseCompoundStatement(CFGNode root){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>();
		noSucNodeList.add(root);
		nextToken();
		noSucNodeList = parseTokens(noSucNodeList);
		//nextToken();
		if(token.getTokenType() == RBRACE){
			nextToken();
			return noSucNodeList;
		}else{
			error = true;
			lastError = "解析复合语句出错" + token.getName();
			return noSucNodeList;
		}
	}
	private List<CFGNode> parseElse(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		nextToken();
		switch(token.getTokenType()){
		case IF:
			noSucNodeList = parseIf(noSucNodeList);
			break;
		case LBRACE:
			noSucNodeList = parseCompoundStatement(noSucNodeList);
			break;
		default:
			noSucNodeList = parseStatement(noSucNodeList);
		}
		return noSucNodeList;
	}
	
	private List<CFGNode> parseFor(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		//scan 'For' Block
		CFGNode node = newCFGNode();
		node.addToken(token);
		nextToken();
		if(token == null || token.getTokenType() != LPAREN){
			error("解析for条件语句出错");
			return null;
		}
		//扫描for条件语句
		scanPartenPair(node);
		link(noSucNodeList, node);
		//将条件语句入循环条件节点栈
		pushCycleNode(node);
		//新建待后继节点列表栈
		pushSucList(new ArrayList<CFGNode>());
		if(token.getTokenType() == LBRACE){
			noSucNodeList = parseCompoundStatement(node);
		}else{
			noSucNodeList = parseStatement(node);
		}
		//noSucNodeList = parseTokens(node);
		link(noSucNodeList, node);
		noSucNodeList.clear();
		//将for条件语句也作为待后继节点
		noSucNodeList.add(node);
		//将所有待后继节点列表栈加入待后继节点列表
		noSucNodeList.addAll(peekSucList());
		//nextToken();
		//弹出当前循环条件节点
		popCycleNode();
		//弹出当前待后继节点列表
		popSucList();
		return noSucNodeList;
	}
	
	private List<CFGNode> parseSwitch(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		//scan 'Switch' Block
		CFGNode node = newCFGNode();
		node.addToken(token);
		nextToken();
		if(token == null || token.getTokenType() != LPAREN){
			error("解析switch条件语句出错");
			return null;
		}
		//扫描for条件语句
		scanPartenPair(node);
		link(noSucNodeList, node);
		//将条件语句入循环条件节点栈
		pushCycleNode(node);
		//新建待后继节点列表栈
		pushSucList(new ArrayList<CFGNode>());
		//解析switch体
		if(token.getTokenType() != LBRACE){
			error("解析switch体语句出错");
			return null;
		}
		//解析case和default
		nextToken();
		noSucNodeList.clear();
		boolean hasDefault = false;
		while(token.getTokenType() == CASE || token.getTokenType() == DEFAULT){
			if(token.getTokenType() == DEFAULT) {
				hasDefault = true;
			}else{
				//条件
				nextToken();
			}
			//冒号
			nextToken();
			nextToken();
			noSucNodeList.add(peekCycleNode());
			noSucNodeList = parseTokens(noSucNodeList);
		}
		//闭合switch体
		if(token.getTokenType() != RBRACE){
			error("解析switch体语句出错");
			return null;
		}
		nextToken();
		//noSucNodeList = parseTokens(node);
		//link(noSucNodeList, node);
		//noSucNodeList.clear();
		//将switch条件语句也作为待后继节点
		if(!hasDefault){
			noSucNodeList.add(node);
		}
		//将所有待后继节点列表栈加入待后继节点列表
		noSucNodeList.addAll(peekSucList());
		//nextToken();
		//弹出当前循环条件节点
		popCycleNode();
		//弹出当前待后继节点列表
		popSucList();
		return noSucNodeList;
	}
	
	private List<CFGNode> parseCase(List<CFGNode> rootList){
		nextToken();
		nextToken();
		if(token == null || token.getTokenType() != COLON){
			error("解析case出错,token:" + token);
			return null;
		}
		nextToken();
		parseTokens(peekCycleNode());
		return rootList;
	}
	
	private List<CFGNode> parseBreak(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		CFGNode node = newCFGNode();
		node.addToken(token);
		link(noSucNodeList, node);
		peekSucList().add(node);
		nextToken();
		if(token == null || token.getTokenType() != SEMI){
			error("解析break出错");
			return null;
		}
		node.addToken(token);
		nextToken();
		noSucNodeList.clear();
		//noSucNodeList.add(node);
		return noSucNodeList;
	}
	
	private List<CFGNode> parseContinue(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		CFGNode node = newCFGNode();
		node.addToken(token);
		link(noSucNodeList, node);
		link(node,peekCycleNode());
		nextToken();
		if(token == null || token.getTokenType() != SEMI){
			error("解析break出错");
			return null;
		}
		node.addToken(token);
		nextToken();
		noSucNodeList.clear();
		//noSucNodeList.add(node);
		return noSucNodeList;
	}
	
	private List<CFGNode> parseWhile(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		//scan 'While' Block
		CFGNode node = newCFGNode();
		node.addToken(token);
		nextToken();
		if(token == null || token.getTokenType() != LPAREN){
			error("解析while条件语句出错");
			return null;
		}
		//扫描while条件语句
		scanPartenPair(node);
		link(noSucNodeList, node);
		//将条件语句入循环条件节点栈
		pushCycleNode(node);
		//新建待后继节点列表栈
		pushSucList(new ArrayList<CFGNode>());
		if(token.getTokenType() == LBRACE){
			noSucNodeList = parseCompoundStatement(node);
		}else{
			noSucNodeList = parseStatement(node);
		}
		//noSucNodeList = parseTokens(node);
		link(noSucNodeList, node);
		noSucNodeList.clear();
		//将while条件语句也作为待后继节点
		noSucNodeList.add(node);
		//将所有待后继节点列表栈加入待后继节点列表
		noSucNodeList.addAll(peekSucList());
		//nextToken();
		//弹出当前循环条件节点
		popCycleNode();
		//弹出当前待后继节点列表
		popSucList();
		return noSucNodeList;
	}
	
	private List<CFGNode> parseDoWhile(List<CFGNode> rootList){
		List<CFGNode> noSucNodeList = new ArrayList<CFGNode>(rootList);
		//scan 'do' Block
		//first statement node
		CFGNode node = null;
		//while node
		CFGNode whileNode = newCFGNode();
		nextToken();
		if(token == null || token.getTokenType() != LBRACE){
			error("解析do语句体出错");
			return null;
		}
		//扫描do循环体的第一条语句
		//scanPartenPair(node);
		nextToken();
		if(token.getTokenType() == LBRACE){
			noSucNodeList = parseCompoundStatement(noSucNodeList);
			int index1 = rootList.size();
			int index2 = rootList.get(index1-1).getSucList().size();
			node = rootList.get(index1-1).getSucList().get(index2-1);
		}else{
			noSucNodeList = parseStatement(noSucNodeList);
			node = noSucNodeList.get(0);
		}
		//将do循环体的第一条语句入循环节点栈
		pushCycleNode(whileNode);
		//处理do while循环体的其他语句
		//while语句
		
		//pushDoWhileNode(whileNode);
		link(whileNode, node);
		
		//新建待后继节点列表栈
		pushSucList(new ArrayList<CFGNode>());
		if(token.getTokenType() != RBRACE){
			backToken();
			noSucNodeList = parseCompoundStatement(noSucNodeList);
		}else{
			noSucNodeList.clear();
			noSucNodeList.add(node);
			//take '}'
			nextToken();
		}
		
		if(token.getTokenType() != WHILE){
			error("解析while条件语句出错:" + token);
			return null;
		}
		whileNode.addToken(token);
		nextToken();
		//scan while条件语句
		scanPartenPair(whileNode);
		link(noSucNodeList, whileNode);
		//link(peekSucList(), node);
		nextToken();
		//link(noSucNodeList, node);
		//noSucNodeList = parseTokens(node);
		noSucNodeList.clear();
		//将while条件语句也作为待后继节点
		noSucNodeList.add(whileNode);
		//将所有待后继节点列表栈加入待后继节点列表
		noSucNodeList.addAll(peekSucList());
		//nextToken();
		//弹出当前循环条件节点
		popCycleNode();
		//弹出当前待后继节点列表
		popSucList();
		return noSucNodeList;
	}
	
	/**
	 * 扫描圆括号对
	 * @param node
	 */
	private void scanPartenPair(CFGNode node){
		node.addToken(token);
		if(token == null || token.getTokenType() != LPAREN){
			error("括号解析出错");
			return;
		}
		int count=1;
		nextToken();
		do{
			if(token == null){
				error("括号解析出错");
				return;
			}
			switch (token.getTokenType()) {
			case LPAREN:
				count++;
				node.addToken(token);
				nextToken();
				break;
			case RPAREN:
				count--;
				node.addToken(token);
				nextToken();
				break;
			default:
				node.addToken(token);
				nextToken();
				break;
			}
		}while(count>0);
	}
	
	private void pushDoWhileNode(CFGNode node){
		cycleStack.push(node);
	}
	
	private CFGNode popDoWhileNode(){
		return cycleStack.pop();
	}
	
	private CFGNode peekDoWhileNode(){
		return cycleStack.peek();
	}
	private void clearDoWhileNodeStack(){
		cycleStack.clear();
	}
	
	private void pushCycleNode(CFGNode node){
		cycleStack.push(node);
	}
	
	private CFGNode popCycleNode(){
		return cycleStack.pop();
	}
	
	private CFGNode peekCycleNode(){
		return cycleStack.peek();
	}
	private void clearCycleNodeStack(){
		cycleStack.clear();
	}
	
	private List<CFGNode> peekSucList(){
		return cycleSucListStack.peek();
	}
	private void pushSucList(List<CFGNode> sucList){
		cycleSucListStack.push(sucList);
	}
	private List<CFGNode> popSucList(){
		return cycleSucListStack.pop();
	}
	
	private void link(List<CFGNode> rootList,CFGNode node){
		if(null == rootList){
			return;
		}
		for(CFGNode root : rootList){
			cfgGraph.link(root, node);
		}
	}
	
	private void link(CFGNode preNode,CFGNode sucNode){
		cfgGraph.link(preNode, sucNode);
	}
	
	public CFGGraph getCFGGraph(){
		//initCFGNodeContent(cfgGraph);
		return cfgGraph;
	}
	
	@Deprecated
	private void initCFGNodeContent(CFGGraph cfgGraph){
	}
	
	private void initCFGNodeContent2(CFGGraph cfgGraph){
		for(CFGNode node : cfgGraph.getNodeList()){
			int minLine = node.getMinLine();
			int maxLine = node.getMaxLine();
			node.setContent(fileObject.getContentLines(minLine, maxLine));
		}
	}
	
	public String getLastError(){
		return lastError;
	}
	
	private void error(String error){
		this.error = true;
		lastError = error;
	}
	
	private void clearError(){
		this.error = false;
	}
	
	private CFGNode newCFGNode(){
		return cfgGraph.newCFGNode();
	}
}
