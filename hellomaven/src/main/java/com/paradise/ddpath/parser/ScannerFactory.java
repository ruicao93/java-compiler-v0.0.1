package com.paradise.ddpath.parser;

import java.nio.CharBuffer;


public class ScannerFactory {
	private static ScannerFactory instance = null;
	
	public ScannerFactory instance(){
		if(null == instance){
			instance = new ScannerFactory();
		}
		return instance;
	}
	
	protected ScannerFactory(){
		
	}
	
	public Scanner newScanner(CharSequence input){
		if(null == input){
			return null;
		}
		if(input instanceof CharBuffer){
			return new Scanner((CharBuffer)input);
		}else{
			char[] array = input.toString().toCharArray();
			return newScanner(array,array.length);
		}
	}
	public Scanner newScanner(char[] input, int inputLength){
		return new Scanner(input, inputLength);
	}
}
