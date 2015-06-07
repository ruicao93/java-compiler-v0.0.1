package com.paradise.ddpath.parser;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;

public class Token {
	
	private TokenType tokenType;
	private String name;
	/**
	 * 行号
	 */
	private int line;
	
	public static final EnumSet<TokenType>  enumSet = EnumSet.allOf(TokenType.class);
	
	public Token(TokenType tokenType,String name,int line){
		this.tokenType = tokenType;
		this.name = name;
		this.line = line;
	}
	
	public Token(TokenType tokenType,char name,int line){
		this.tokenType = tokenType;
		this.name = String.valueOf(name);
		this.line = line;
	}
	
	public Token(TokenType tokenType){
		this.tokenType = tokenType;
		this.name = String.valueOf(tokenType.typeName);
	}
	
	public static Token key(String name,int line){
		TokenType type = null;
    	for(TokenType tokenType : enumSet){
    		if(StringUtils.isNotBlank(tokenType.typeName)){
    			if(tokenType.typeName .equals(name)){
    				type = tokenType;
    				break;
    			}
    		}
    	}
    	if(null == type){
    		type = TokenType.IDENTIFIER;
    	}
    	return new Token(type, name,line);
    }
	
	public static Token key(char name,int line){
		TokenType type = null;
    	for(TokenType tokenType : enumSet){
    		if(StringUtils.isNotBlank(tokenType.typeName)){
    			if(tokenType.typeName .equals(name)){
    				type = tokenType;
    				break;
    			}
    		}
    	}
    	if(null == type){
    		type = TokenType.IDENTIFIER;
    	}
    	return new Token(type, name,line);
    }
	
	public TokenType getTokenType() {
		return tokenType;
	}
	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	
	public String toString(){
		return "name" + getName() + "tokenType:" + getTokenType();
	}
	
}
