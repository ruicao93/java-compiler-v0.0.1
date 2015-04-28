package com.paradise.ddpath.parser;

import static com.paradise.ddpath.util.LayoutCharacters.*;

import java.nio.CharBuffer;
public class Scanner implements Lexer{

	private Token token;
	private int pos;
	private int endPos;
	/** The last character position of the previous token.
     */
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
    
    public Scanner(CharBuffer buffer){
    	this(buffer.toString().toCharArray(),buffer.limit());
    }
    
    /**
     * Create a scanner from the input array.  This method might
     * modify the array.  To avoid copying the input array, ensure
     * that {@code inputLength < input.length} or
     * {@code input[input.length -1]} is a white space character.
     * @param input
     * @param inputLength
     */
    public Scanner(char[] input, int inputLength){
    	eofPos = inputLength;
    	if(input.length == inputLength){
    		if(input.length > 0 && Character.isWhitespace(input[inputLength-1])){
    			inputLength--;
    		}else{
    			char[] newInput = new char[inputLength+1];
    			System.arraycopy(input, 0, newInput, 0, inputLength);
    			input = newInput;
    		}
    	}
    	buf = input;
    	buflen = inputLength;
    	buf[inputLength] = EOI;
    	bp=-1;
    	scanChar();
    }
    
    /** Read next character.
     */
    private void scanChar() {
        ch = buf[++bp];
        if (ch == '\\') {
            //TODO convertUnicode();
        }
    }
    
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
