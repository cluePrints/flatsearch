package com.soboleiv.flatsearch.server.util;

public class Integers {
	public static int fromString(String input) {
		if (input == null)
			return -1;
		
		String cleared = dropNonNumericCharacters(input);
		if (cleared.length() == 0)
			return -1; 
		
		return Integer.parseInt(cleared);
	}

	private static String dropNonNumericCharacters(String input) {
		char[] buffer = new char[input.length()];
		int bPos = 0;
		for (int i=0; i<input.length(); i++) {
			char currChar = input.charAt(i);
			if (currChar >= '0' && currChar <= '9') {
				buffer[bPos] = currChar;
				bPos++;
			}
		}
		String cleared = new String(buffer, 0, bPos);
		return cleared;
	}
}
