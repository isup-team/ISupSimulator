/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.text;

import ch.skyguide.common.text.SyntaxTokenizer.Token;
import java.util.Iterator;

/**
 *
 * @author CyaNn
 */
public class SyntaxTokenizer implements Iterable<Token> {

	// inner class
	public class Token {

		public final boolean isToken;
		public final String value;

		Token(boolean isToken, String value) {
			this.isToken = isToken;
			this.value = value;
		}
	}
	// variables
	private String string;
	private String[] delims;

	// constructor
	public SyntaxTokenizer(String string, String[] delims) {
		this.string = string;
		this.delims = delims;
	}

	public Iterator<Token> iterator() {
		return new ReadableIterator<Token>() {

			private int pos = 0;

			public boolean hasNext() {
				return (pos < string.length());
			}

			private Token findNext(boolean pop) {
				int end = -1;

				for (String delim : delims) {
					int i = string.indexOf(delim, pos);
					if (i != -1 && (end == -1 || i < end)) {
						end = i;

						if (end == pos) {
							if (pop) {
								pos = pos + delim.length();
							}
							return new Token(true, delim);
						}
					}
				}

				if (end == -1) {
					end = string.length();
				}

				Token token = new Token(false, string.substring(pos, end));
				if (pop) {
					pos = end;
				}
				return token;
			}

			public Token next() {
				return findNext(true);
			}

			public void remove() {
				next();
			}

			public Token read() {
				return findNext(false);
			}
		};
	}
}
