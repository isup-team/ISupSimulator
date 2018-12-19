/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.ui.documentStyleManager.scheme;

import java.awt.Color;
import ch.skyguide.common.ui.documentStyleManager.token.TokenType;
import java.awt.Graphics;

/**
 *
 * @author caronyn
 */
public abstract class StyleScheme {

    public static final Color COLOR_DARK_GREEN = new Color(0x008000);
    public static final Color COLOR_DARK_BLUE = new Color(0x0000E6);
    public static final Color COLOR_DARK_RED = new Color(0xA31515);
    public static final Color COLOR_BROWN = new Color(0xCE7B00);

    public abstract void applyCommon(Graphics g);
    public abstract void applyNull(Graphics g);
    public abstract void applyComment1(Graphics g);
    public abstract void applyComment2(Graphics g);
    public abstract void applyLiteral1(Graphics g);
    public abstract void applyLiteral2(Graphics g);
    public abstract void applyLabel(Graphics g);
    public abstract void applyKeyword1(Graphics g);
    public abstract void applyKeyword2(Graphics g);
    public abstract void applyKeyword3(Graphics g);
    public abstract void applyOperator(Graphics g);
    public abstract void applyInvalid(Graphics g);

    public void applyToken(TokenType tokenType, Graphics g) {
	applyCommon(g);
	
	switch (tokenType) {
	    case Null :
		applyNull(g);
		break;
	    case Comment1 :
		applyComment1(g);
		break;
	    case Comment2 :
		applyComment2(g);
		break;
	    case Literal1 :
		applyLiteral1(g);
		break;
	    case Literal2 :
		applyLiteral2(g);
		break;
	    case Label :
		applyLabel(g);
		break;
	    case Keyword1 :
		applyKeyword1(g);
		break;
	    case Keyword2 :
		applyKeyword2(g);
		break;
	    case Keyword3 :
		applyKeyword3(g);
		break;
	    case Operator :
		applyOperator(g);
		break;
	    case Invalid :
		applyInvalid(g);
		break;
	}
    }

}
