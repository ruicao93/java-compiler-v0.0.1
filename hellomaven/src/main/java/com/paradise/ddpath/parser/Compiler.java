package com.paradise.ddpath.parser;

import com.paradise.ddpath.file.FileObject;

public class Compiler {
	private String filePath;
	private static ScannerFactory scannerFactory = null;
	public Compiler(String filePath){
		this.filePath = filePath;
	}
	
	public void compile(){
		
	}
	private void compile(FileObject fileObject){
		
	}
	
	private void parseFile(FileObject fileObject){
		
	}
	
	private void parse(FileObject fileObject, CharSequence content){
		Scanner scanner = getScannerFactory().newScanner(content);
		
	}
	
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
}
