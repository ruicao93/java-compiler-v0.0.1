package com.paradise.ddpath.parser;

/**
 * 
 * @Description: 词法分析器接口
 *
 * @author caorui
 * @sine 2015年4月27日 下午4:59:06
 *
 */
public interface Lexer {
	
	int endPos();
	Name name();
	void nextToken();
	void pos();
	void token();
	void token(Token token);
	void reset();
}
