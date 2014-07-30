import java.io.*;
import java.util.*;

public class Char2Pinyin {
	public static void main(String[] args) throws IOException {
		
		BufferedReader uni2pinyin = new BufferedReader(new FileReader("doc/Uni2Pinyin"));
		Map<String, List<String>> dict_with_tone = new HashMap<String, List<String>>();
		Map<String, List<String>> dict_without_tone = new HashMap<String, List<String>>();
		
		String tone = args[0];
		String input = "";
		int sent_len = args.length;
		for (int j = 1; j < sent_len; j ++) {
			input += args[j] + " ";
		}
		String output = "";
		
		String line = "";
		while ((line = uni2pinyin.readLine()) != null) {
			String[] tokens = line.split("\t");
			if (tokens.length < 2) {
				continue;
			} else {
				String unicode = "\\u" + tokens[0];
				unicode = unicode.toLowerCase();
				List<String> pinyin_with_tone = new ArrayList<String>();
				List<String> pinyin_without_tone = new ArrayList<String>();
				if (tokens.length == 2) {			
					pinyin_with_tone.add(tokens[1]);
					String token_without_tone = tokens[1].substring(0, tokens[1].length() - 1);
					pinyin_without_tone.add(token_without_tone);
				} else {
					for (int i = 1; i < tokens.length; i ++) {
						pinyin_with_tone.add(tokens[i]);
						String token_without_tone = tokens[1].substring(0, tokens[1].length() - 1);
						pinyin_without_tone.add(token_without_tone);
					}
				}
				dict_with_tone.put(unicode, pinyin_with_tone);
				dict_without_tone.put(unicode, pinyin_without_tone);
			}
		}
		uni2pinyin.close();
		
		input = input.replaceAll("[\"]", "");
		char[] charArray = input.toCharArray();
		for (char c : charArray) {
			String charUnicode = toUnicode(c);
			if (c == ' ') {
				output += " ";
			} else if (tone.equals("tone")) {
				if (dict_with_tone.containsKey(charUnicode)) {
					output += dict_with_tone.get(charUnicode).get(0);
				} else {
					output += "MISSED!";
				}
			} else {
				if (dict_without_tone.containsKey(charUnicode)) {
					output += dict_without_tone.get(charUnicode).get(0);
				} else {
					output += "MISSED!";
				}
			} 
		}
		System.out.println(output);
	}
	
	private static String toUnicode(char ch) {
	    return String.format("\\u%04x", (int) ch);
	}
}
