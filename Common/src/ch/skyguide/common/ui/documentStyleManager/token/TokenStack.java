/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.documentStyleManager.token;

import java.util.Stack;

/**
 *
 * @author CyaNn
 */
public class TokenStack extends Stack<Token> {

    public Token getTokenAtOffset(int offset) {
	Token ret = null;

	int id = 0;
	int t0 = 0;
	int t1 = 0;
	while (ret == null && id < this.size()) {
	    Token t = this.elementAt(id);
	    t1 = t0 + t.getLength();
	    if (offset >= t0 && offset <= t1) {
		ret = t;
	    }

	    t0 = t1;
	    id++;
	}

	return ret;
    }

    public void addCharAtOffset(int offset) {
	Token t = getTokenAtOffset(offset);
	t.setLength(t.getLength() + 1);
    }

    public void removeCharAtOffset(int offset) {
	Token t = getTokenAtOffset(offset);
	t.setLength(t.getLength() - 1);
	if (t.getLength() == 0) {
	    this.remove(t);
	}
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();
	for (Token token : this) {
	    buffer.append(token.toString());
	    buffer.append('\n');
	}
	return buffer.toString();
    }
}
