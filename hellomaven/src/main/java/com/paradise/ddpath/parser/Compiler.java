package com.paradise.ddpath.parser;

import com.paradise.ddpath.file.FileObject;

public class Compiler {
	private String filePath;
	private static ScannerFactory scannerFactory = null;
	private static ParserFactory parserFactory = null;
	public Compiler(String filePath){
		this.filePath = filePath;
	}
	
	public void compile(){
		
	}
	private void compile(FileObject fileObject){
		
	}
	
	private void parseFile(FileObject fileObject){
		parse(fileObject);
	}
	
	/** Parse contents of file.
     *  @param fileObject     The name of the file to be parsed.
     */
	private void parse(FileObject fileObject){
		if(null != fileObject){
			parse(fileObject, fileObject.getCharContent());
		}
	}
	
	/** Parse contents of input stream.
     *  @param fileObject     The name of the file from which input stream comes.
     *  @param content        The input stream to be parsed.
     */
	private void parse(FileObject fileObject, CharSequence content){
		Scanner scanner = getScannerFactory().newScanner(content);
		Parser parser = getParserFactory().newParser(scanner);
		
	}
	
	/** Try to open input stream with given name.
     *  Report an error if this fails.
     *  @param fileobject  The file name of the input stream to be opened.
     */
	private CharSequence readSource(FileObject fileObject){
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
	
	private ParserFactory getParserFactory(){
		if(null == parserFactory){
			parserFactory = new ParserFactory();
		}
		return parserFactory;
	}
}
