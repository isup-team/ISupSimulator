/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.valueManager;

import ch.skyguide.common.text.SyntaxTokenizer;
import ch.skyguide.common.text.SyntaxTokenizer.Token;
import java.util.Vector;

/**
 *
 * @author caronyn
 */
public class DynManager<T> extends AbstractManager<Vector<T>> {

    public static final String DYN_SEPARATOR = ", ";
    private AbstractManager<T> manager;

    public DynManager(AbstractManager<T> manager) {
	this.manager = manager;
    }
    
    @Override
    public Vector<T> fromDpl(String string) {
	Vector<T> ret = new Vector<T>();

	SyntaxTokenizer tokenizer = new SyntaxTokenizer(string, new String[]{DYN_SEPARATOR});
	for (Token token : tokenizer) {
	    if (!token.isToken) {
		ret.add(manager.fromDpl(token.value));
	    }
	}

	return ret;
    }

    @Override
    public String toDpl(Vector<T> value) {
	StringBuffer sb = new StringBuffer();

	for (T v : value) {
	    if (sb.length() > 0) {
		sb.append(DYN_SEPARATOR);
	    }
	    sb.append(manager.toDpl(v));
	}

	return sb.toString();
    }

}
