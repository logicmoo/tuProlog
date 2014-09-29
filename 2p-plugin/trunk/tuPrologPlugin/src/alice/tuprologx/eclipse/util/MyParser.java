package alice.tuprologx.eclipse.util;

import java.io.*;

public class MyParser {

	public int readTerm() {
		while (true) {
			String temp = null;
			try {
				temp = buff.readLine();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				return -1;
			}
			if (temp == null)
				return -1;
			if (temp.lastIndexOf(":-") > 0) {
				end = temp.lastIndexOf(":-");
				nome = temp.substring(0, end).trim();
				start = offset;
				end = nome.length();
			}
			offset += temp.replaceAll("\t", "   ").length() + 1;
			if (temp.equals("\t") || temp.trim().endsWith(","))
				offset--;
			if (temp.equals(""))
				offset++;
			if (temp.trim().endsWith("."))
				break;
		}
		return end;
	}

	public String getTerm() {
		return nome;
	}

	public int getStart() {
		return start;
	}

	private int start;
	private int end;
	private int offset;
	private String nome;

	private BufferedReader buff = null;

	public MyParser(String text) {
		buff = new BufferedReader(new StringReader(text));
		start = 0;
		end = 0;
		offset = 0;
	}
}
