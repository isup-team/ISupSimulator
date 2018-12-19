/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.ui.documentStyleManager.scheme;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author caronyn
 */
public class DefaultStyleScheme extends StyleScheme {

    @Override
    public void applyCommon(Graphics g) {
	g.setFont(g.getFont().deriveFont(Font.PLAIN));
    }

    public void applyNull(Graphics g) {
        g.setColor(Color.BLACK);
    }

    public void applyComment1(Graphics g) {
        g.setColor(StyleScheme.COLOR_DARK_GREEN);
    }

    public void applyComment2(Graphics g) {
        g.setColor(StyleScheme.COLOR_DARK_GREEN);
    }

    public void applyLiteral1(Graphics g) {
        g.setColor(StyleScheme.COLOR_BROWN);
    }

    public void applyLiteral2(Graphics g) {
        g.setColor(StyleScheme.COLOR_BROWN);
	g.setFont(g.getFont().deriveFont(Font.BOLD));
    }

    public void applyLabel(Graphics g) {
        g.setColor(Color.DARK_GRAY);
    }

    public void applyKeyword1(Graphics g) {
        g.setColor(StyleScheme.COLOR_DARK_BLUE);
    }

    public void applyKeyword2(Graphics g) {
        g.setColor(StyleScheme.COLOR_DARK_BLUE);
	g.setFont(g.getFont().deriveFont(Font.BOLD));
    }

    public void applyKeyword3(Graphics g) {
        g.setColor(StyleScheme.COLOR_DARK_BLUE);
	g.setFont(g.getFont().deriveFont(Font.BOLD));
    }

    public void applyOperator(Graphics g) {
        g.setColor(StyleScheme.COLOR_DARK_RED);
	//g.setFont(g.getFont().deriveFont(Font.BOLD));
    }

    public void applyInvalid(Graphics g) {
        g.setColor(Color.RED);
	g.setFont(g.getFont().deriveFont(Font.BOLD));
    }

}
