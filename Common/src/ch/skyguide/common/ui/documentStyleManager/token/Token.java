/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.ui.documentStyleManager.token;

/**
 *
 * @author CyaNn
 */
public class Token {

    private TokenType tokenType;
    private int length;

    public int getLength() {
	return length;
    }

    public void setLength(int length) {
	this.length = length;
    }

    public TokenType getTokenType() {
	return tokenType;
    }

    public Token(TokenType tokenType, int length) {
	this.tokenType = tokenType;
	this.length = length;
    }

    @Override
    public String toString() {
	return tokenType + " " + length;
    }

}
