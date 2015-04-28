package com.paradise.ddpath.parser;


public class ParserFactory {
	
	public static ParserFactory instance = null;
	
	public ParserFactory instance(){
		if(null == instance){
			instance = new ParserFactory();
		}
		return instance;
	}
	protected ParserFactory(){
		
	}
	
	public Parser newParser(Lexer S){
		return new Parser(S);
	}
}
