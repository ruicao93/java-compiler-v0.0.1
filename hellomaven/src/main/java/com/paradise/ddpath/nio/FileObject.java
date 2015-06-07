package com.paradise.ddpath.nio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

public class FileObject implements JavaFileObject{
	private String filePathStr = null;
	private String fileName = null;
	private Path filePath = null;
	private CharBuffer cb = null;
	private String[] linesContent = null;
	private ByteBuffer cached;
	public  FileObject(String  filePathStr){
		setFilePath(filePathStr);
	}
	
	private boolean setFilePath(String  filePathStr){
		this.filePathStr = filePathStr;
		filePath = Paths.get(filePathStr);
		fileName = filePath.getFileName().toString();
		return true;
	}
	
	public CharBuffer getCharContent() throws IOException{
		if (cb == null) {
            InputStream in = openInputStream();
            try {
                ByteBuffer bb = makeByteBuffer(in);
                Charset cs = Charset.forName("UTF-8");
                cb = cs.decode(bb);
            } finally {
                in.close();
            }
        }
        return cb;
	}
	
	public String getContentLines(int startLine,int endLine){
		if(cb == null){
			try {
				cb = getCharContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		if(linesContent == null) linesContent = cb.toString().split("\n");
		String str = "";
		for(int i=startLine; i<=endLine; i++){
			str += linesContent[i];
			str += "\n";
		}
//		return  startLine + "行---->" + endLine + "行";
		return str;
	}
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	public URI toUri() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream openInputStream() throws IOException {
		 return Files.newInputStream(filePath);
	}

	public OutputStream openOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Writer openWriter() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLastModified() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}

	public Kind getKind() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isNameCompatible(String simpleName, Kind kind) {
		// TODO Auto-generated method stub
		return false;
	}

	public NestingKind getNestingKind() {
		// TODO Auto-generated method stub
		return null;
	}

	public Modifier getAccessLevel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
     * Make a byte buffer from an input stream.
     */
    public ByteBuffer makeByteBuffer(InputStream in)
        throws IOException {
        int limit = in.available();
        if (limit < 1024) limit = 1024;
        ByteBuffer result = getCacheByteBuffer(limit);
        int position = 0;
        while (in.available() != 0) {
            if (position >= limit)
                // expand buffer
                result = ByteBuffer.
                    allocate(limit <<= 1).
                    put((ByteBuffer)result.flip());
            int count = in.read(result.array(),
                position,
                limit - position);
            if (count < 0) break;
            result.position(position += count);
        }
        return (ByteBuffer)result.flip();
    }
    
    private ByteBuffer getCacheByteBuffer(int capacity) {
        if (capacity < 20480) capacity = 20480;
        ByteBuffer result =
            (cached != null && cached.capacity() >= capacity)
            ? (ByteBuffer)cached.clear()
            : ByteBuffer.allocate(capacity + capacity>>1);
        cached = null;
        return result;
    }
}
