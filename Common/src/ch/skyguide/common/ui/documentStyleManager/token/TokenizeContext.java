/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.documentStyleManager.token;

import javax.swing.text.Segment;

/**
 *
 * @author CyaNn
 */
public class TokenizeContext {

    public int i;
    public int last;
    public Segment txt;
    public TokenStack tokens = new TokenStack();

    public TokenizeContext(Segment txt) {
	this.txt = txt;
	i = 0;
	last = 0;
    }
    
    public void next() {
	i++;
    }

    public int getLength(){
	return txt.length();
    }

    public char charAt(int i) {
	return txt.charAt(i);
    }

    public void tokenEnd() {
	last = i;
    }

    @Override
    public String toString() {
	String ret = "";
	ret += super.toString() + " [" + i + "] [" + last + "]\n";
	ret += txt.toString() + "\n";
	ret += tokens.toString();
	return ret;
    }

}
