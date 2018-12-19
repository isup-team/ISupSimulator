/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.documentStyleManager;

import ch.skyguide.common.ui.documentStyleManager.scheme.StyleScheme;
import ch.skyguide.common.ui.documentStyleManager.token.Token;
import ch.skyguide.common.ui.documentStyleManager.update.UpdateStrategy;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.text.*;

/**
 *
 * @author caronyn
 */
public class SyntaxHighlightView extends WrappedPlainView implements TabExpander {

    private UpdateStrategy updater;
    private StyleScheme scheme;

    public SyntaxHighlightView(Element elem, boolean wordWrap, UpdateStrategy updater, StyleScheme scheme) {
	super(elem, wordWrap);
	this.updater = updater;
	this.scheme = scheme;
    }

    public SyntaxHighlightView(Element elem, UpdateStrategy updater, StyleScheme scheme) {
	super(elem);
	this.updater = updater;
	this.scheme = scheme;
    }

    // Syntax highlight
    @Override
    public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f) {
	super.insertUpdate(e, a, f);

	try {
	    updater.update(e);
	} catch (BadLocationException ex) {
	    Logger.getLogger(SyntaxHighlightView.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    @Override
    public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f) {
	super.removeUpdate(e, a, f);

	try {
	    updater.update(e);
	} catch (BadLocationException ex) {
	    Logger.getLogger(SyntaxHighlightView.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Override
    protected int drawUnselectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	Segment text = new Segment();
	Document doc = getDocument();
	doc.getText(0, doc.getLength(), text);

	int i = 0;
	int t0 = 0;
	int t1 = 0;

	while (t0 < p1 && i < updater.getCurrentContext().tokens.size()) {
	    Token t = updater.getCurrentContext().tokens.elementAt(i);
	    t1 =
		    t0 + t.getLength();

	    if (t1 > p0) {
		int s0 = Math.max(t0, p0);
		int s1 = Math.min(t1, p1);

		scheme.applyToken(t.getTokenType(), g2d);
		Segment sub = (Segment) text.subSequence(s0, s1);
		x = Utilities.drawTabbedText(sub, x, y, g2d, this, s0);
	    }

	    i++;
	    t0 = t1;
	}

	return x;

    }

    @Override
    protected int drawSelectedText(Graphics g, int x, int y, int p0, int p1) throws BadLocationException {
	return drawUnselectedText(g, x, y, p0, p1);
    }
}
