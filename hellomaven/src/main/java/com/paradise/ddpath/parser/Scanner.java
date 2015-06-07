package com.paradise.ddpath.parser;

import static com.paradise.ddpath.util.LayoutCharacters.*;
import static com.paradise.ddpath.parser.TokenType.*;

import java.nio.CharBuffer;
import java.util.List;

public class Scanner implements Lexer{

	private Token token;
	private int pos;
	private int endPos;
	/** The last character position of the previous token.
     */
	private int prevEndPos;
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
    private int line;
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
    	line = 0;
    	scanChar();
    }
    
    /** Read next character.
     */
    private void scanChar() {
        ch = buf[++bp];
        if (ch == LF) {
        	line++;
            scanChar();
        }
        if (ch == '\\') {
            //TODO convertUnicode();
        }
    }
    
    /**
     * 将源代码转变为Token序列
     * @return
     */
    public List<Token>  scan(){
    	return null;
    }
    
	public int endPos() {
		// TODO Auto-generated method stub
		return 0;
	}


	
	/**
	 * 取下一个Token
	 */
	public void nextToken() {
		prevEndPos = endPos;
        sp = 0;

        while (true) {
            pos = bp;
            switch (ch) {
            //识别格式化字符
            case ' ': //  空格
            case '\t': // tab
            case FF: // 换页符
                do {
                    scanChar();
                } while (ch == ' ' || ch == '\t' || ch == FF);
                endPos = bp;
                // 处理空白 processWhiteSpace();
                break;
            case LF: // 换行
                scanChar();
                endPos = bp;
                // 处理换行 processLineTerminator();
                break;
            case CR: // 回车
                scanChar();
                if (ch == LF) {
                    scanChar();
                }
                endPos = bp;
                //processLineTerminator();
                break;
                //是被标示符/关键字
            case 'A': case 'B': case 'C': case 'D': case 'E':
            case 'F': case 'G': case 'H': case 'I': case 'J':
            case 'K': case 'L': case 'M': case 'N': case 'O':
            case 'P': case 'Q': case 'R': case 'S': case 'T':
            case 'U': case 'V': case 'W': case 'X': case 'Y':
            case 'Z':
            case 'a': case 'b': case 'c': case 'd': case 'e':
            case 'f': case 'g': case 'h': case 'i': case 'j':
            case 'k': case 'l': case 'm': case 'n': case 'o':
            case 'p': case 'q': case 'r': case 's': case 't':
            case 'u': case 'v': case 'w': case 'x': case 'y':
            case 'z':
            case '$': case '_':
            	//字母，下划线开头为标识符或关键字
                scanIdent();
                endPos = bp;
                return;
            //识别10进制数字
            case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                scanNumber(10);
                endPos = bp;
                return;
            //识别标点符号、界符等
            case ',':
                token = new Token(TokenType.COMMA, ch,line);scanChar();  return;
            case ';':
                token = new Token(TokenType.SEMI, ch,line); scanChar(); return;
            case '(':
                token = new Token(TokenType.LPAREN, ch,line) ; scanChar(); return;
            case ')':
                token = new Token(TokenType.RPAREN, ch,line); scanChar(); return;
            case '[':
                token = new Token(TokenType.LBRACKET, ch,line); scanChar(); return;
            case ']':
                token = new Token(TokenType.RBRACKET, ch,line); scanChar(); return;
            case '{':
                token = new Token(TokenType.LBRACE, ch,line); scanChar(); return;
            case '}':
                token = new Token(TokenType.RBRACE, ch,line); scanChar(); return;
            //识别注释
            case '/':
                scanChar();
                if (ch == '/') {  //行注释
                    do {
                        scanCommentChar();
                    } while (ch != CR && ch != LF && bp < buflen);
                    endPos = bp;
                    break;
                } else if (ch == '*') { //段注释
                	scanDocComment();
                } else if (ch == '=') {
                    token = new Token(SLASHEQ);
                    scanChar();
                    endPos = bp;
                    break;
                } else {
                    token = new Token(SLASH);
                    scanChar();
                    endPos = bp;
                    break;
                }
                break;
            /*识别单引号
            case '\'':
                scanChar();
                if (ch == '\'') {
                    lexError("empty.char.lit");
                } else {
                    if (ch == CR || ch == LF)
                        lexError(pos, "illegal.line.end.in.char.lit");
                    scanLitChar();
                    if (ch == '\'') {
                        scanChar();
                        token = CHARLITERAL;
                    } else {
                        lexError(pos, "unclosed.char.lit");
                    }
                }
                return;
            //识别双引号
             */
            default:
            	//识别其他特殊符号，如算符等
                if (isSpecial(ch)) {
                    scanOperator(); 
                } else if(bp == buflen || ch == EOI && bp+1 == buflen){
                	token = new Token(TokenType.EOF);
                	pos = endPos = bp;
                }else {
                	token = Token.key(ch,line);
                	scanChar();
                }
                return;
            }
        }
		
	}
	
	public char[] getRawCharacters(int beginIndex, int endIndex){
		int length = endIndex - beginIndex;
		char[] chars = new char[length];
		System.arraycopy(buf, beginIndex, chars, 0, length);
		return chars;
	}
	
	public String getStringFromChars(char[] chars,int start, int length){
		char[] result = new  char[length];
    	System.arraycopy(chars, 0, result, start, length);
    	return result.toString();
	}
	/**
	 * Read an  identifier
	 */
	public void scanIdent(){
		do{
			//储存上一个字符
			if(sp == sbuf.length) putChar(ch); else sbuf[sp++] = ch;
			scanChar();
			switch(ch){
			case 'A': case 'B': case 'C': case 'D': case 'E':
            case 'F': case 'G': case 'H': case 'I': case 'J':
            case 'K': case 'L': case 'M': case 'N': case 'O':
            case 'P': case 'Q': case 'R': case 'S': case 'T':
            case 'U': case 'V': case 'W': case 'X': case 'Y':
            case 'Z':
            case 'a': case 'b': case 'c': case 'd': case 'e':
            case 'f': case 'g': case 'h': case 'i': case 'j':
            case 'k': case 'l': case 'm': case 'n': case 'o':
            case 'p': case 'q': case 'r': case 's': case 't':
            case 'u': case 'v': case 'w': case 'x': case 'y':
            case 'z':
            case '$': case '_':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            	break;
            default:
            		String name = char2String(sbuf, 0, sp);
            		token = Token.key(name,line);
            		return;
			}
			
		}while(true);
	}
	
	/**
	 * 识别数字 目前只支持10进制，默认为10进制
	 * @param radix
	 */
	public void scanNumber(int radix){
		do{
			if(Character.isDigit(ch)){
				putChar(ch);
				scanChar();
			}else{
				String name = char2String(sbuf, 0, sp);
				token = new Token(TokenType.NUMBER, name,line);
				return;
			}
		}while(true);
	}
	
	public void scanCommentChar(){
		scanChar();
	}
	
	/**
	 * 扫描段注释
	 */
	public void scanDocComment(){
		scanChar();
		do{
			switch(ch){
			case'*':
				scanChar();
				if('/' == ch){
					return;
				}
				break;
			default:
				scanChar();
			}
		}while(true);
	}
	/** Return true if ch can be part of an operator.
     */
    private boolean isSpecial(char ch) {
        switch (ch) {
        case '!': case '%': case '&': case '*': case '?':
        case '+': case '-': case ':': case '<': case '=':
        case '>': case '^': case '|': case '~':
        case '@':
            return true;
        default:
            return false;
        }
    }
    
    /** Read longest possible sequence of special characters and convert
     *  to token.
     */
    private void scanOperator(){
    	while(true){
    		putChar(ch);
    		String name = char2String(sbuf, 0, sp);
    		token  = Token.key(name,line);
    		scanChar();
    		if(!isSpecial(ch)) break;
    	}
    }
	public void pos() {
		// TODO Auto-generated method stub
		
	}

	public Token token() {
		return token;
	}

	public void token(Token token) {
		// TODO Auto-generated method stub
		
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	public void putChar(char ch){
		if(sp == sbuf.length){
			char[] chars = new char[sbuf.length*2];
			System.arraycopy(sbuf, 0, chars, 0, sbuf.length);
			sbuf = chars;
		}
		sbuf[sp++] = ch;
	}
	
	private String char2String(char[] chars,int start, int length){
		if(null == chars){
			return null;
		}
    	return new String(chars, 0, length);
	}

}
