package com.paradise.ddpath.parser;


public class Scanner implements Lexer{

	private Token token;
	private int pos;
	private int endPos;
	private int prevEndPos;
	private Name name;
	/** A character buffer for literals.
     */
    private char[] sbuf = new char[128];
    private int sp;
    
    /** The input buffer, index of next chacter to be read,
     *  index of one past last character in buffer.
     */
    private char[] buf;
    private int bp;
    private int buflen;
    private int eofPos;
    
    /** The current character.
     */
    private char ch;
	public int endPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Name name() {
		// TODO Auto-generated method stub
		return null;
	}

	public void nextToken() {
		// TODO Auto-generated method stub
		
	}

	public void pos() {
		// TODO Auto-generated method stub
		
	}

	public void token() {
		// TODO Auto-generated method stub
		
	}

	public void token(Token token) {
		// TODO Auto-generated method stub
		
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
