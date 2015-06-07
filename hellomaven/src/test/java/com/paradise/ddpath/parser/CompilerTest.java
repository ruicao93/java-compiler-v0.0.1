package com.paradise.ddpath.parser;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import com.paradise.ddpath.graph.CFGGraph;
import com.paradise.ddpath.ui.CFGGraphPainter;
import com.paradise.ddpath.ui.MainFrame2;
import com.paradise.ddpath.graph.DDpathGraph;
import com.paradise.ddpath.nio.FileObject;

public class CompilerTest {
	
	public static String graphFilePath = "F:\\大四\\毕业设计\\java绘图\\graphviz-2.38\\release\\bin\\1test.dot";
	public static String graphCommandPath = "F:\\大四\\毕业设计\\java绘图\\graphviz-2.38\\release\\bin\\1command.bat";
	
	//public static Logger LOG = Logger.getLogger(CompilerTest.class);
	
	@Test
	public void compilerTest(){
		String filePathStr = "d:\\test\\test.java";
		FileObject fileObject = new FileObject(filePathStr);
		CFGParser compiler = new CFGParser();
		compiler.parseAll(fileObject);
		compiler.printTokens(System.out);
		System.out.println(CFGGraphPainter.traversal(compiler.getCFGGraph()));
		System.out.println("lastErr: " + compiler.getLastError());
	}
	@Test
	public void compilerForTest(){
		String filePathStr = "d:\\test\\fortest.txt";
		FileObject fileObject = new FileObject(filePathStr);
		CFGParser compiler = new CFGParser();
		compiler.parseAll(fileObject);
		compiler.printTokens(System.out);
		//System.out.println(Graph.traversal(compiler.getCFGTree()));
		CFGGraphPainter.paintGraph(compiler.getCFGGraph(), graphFilePath, graphCommandPath);
		System.out.println("lastErr: " + compiler.getLastError());
	}
	@Test
	public void compilerSwitchTest(){
		String filePathStr = "d:\\test\\switchtest.txt";
		FileObject fileObject = new FileObject(filePathStr);
		CFGParser compiler = new CFGParser();
		compiler.parseAll(fileObject);
		compiler.printTokens(System.out);
		System.out.println(CFGGraphPainter.traversal(compiler.getCFGGraph()));
		System.out.println("lastErr: " + compiler.getLastError());
	}
	@Test
	public void compilerWhileTest(){
		String filePathStr = "d:\\test\\whiletest.txt";
		FileObject fileObject = new FileObject(filePathStr);
		CFGParser compiler = new CFGParser();
		compiler.parseAll(fileObject);
		compiler.printTokens(System.out);
		CFGGraphPainter.paintGraph(compiler.getCFGGraph(), graphFilePath, graphCommandPath);
		System.out.println("lastErr: " + compiler.getLastError());
		try {
			Runtime.getRuntime().exec("cmd /c");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void compilerDoWhileTest(){
		String filePathStr = "d:\\test\\dowhiletest.txt";
		FileObject fileObject = new FileObject(filePathStr);
		CFGParser compiler = new CFGParser();
		compiler.parseAll(fileObject);
		compiler.printTokens(System.out);
		CFGGraph cfgGraph = compiler.getCFGGraph();
		//CFGGraphPainter.paintGraph(cfgGraph, graphFilePath, graphCommandPath);
		DDpathParser cfgParser = new DDpathParser();
		DDpathGraph ddPpathGraph = cfgParser.parseCFGGraph(cfgGraph);
		System.out.println("lastErr: " + compiler.getLastError());
//		try {
//			Runtime.getRuntime().exec("cmd /c");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		MainFrame2 frame = new MainFrame2();
		frame.show(ddPpathGraph);
		//LOG.info("this is log");
	}
	
	public static void main(String[] args) {
		String filePathStr = "d:\\test\\triangletest.txt";
		FileObject fileObject = new FileObject(filePathStr);
		CFGParser compiler = new CFGParser();
		compiler.parseAll(fileObject);
		compiler.printTokens(System.out);
		CFGGraph cfgGraph = compiler.getCFGGraph();
		CFGGraphPainter.paintGraph(cfgGraph, graphFilePath, graphCommandPath);
		DDpathParser cfgParser = new DDpathParser();
		DDpathGraph ddPpathGraph = cfgParser.parseCFGGraph(cfgGraph);
		System.out.println("lastErr: " + compiler.getLastError());
//		try {
//			Runtime.getRuntime().exec("cmd /c");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		MainFrame2 frame = new MainFrame2();
		frame.show(ddPpathGraph);
	}
}
