/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.documentStyleManager.update;

import ch.skyguide.common.ui.documentStyleManager.token.AbstractTokenizer;
import ch.skyguide.common.ui.documentStyleManager.token.TokenizeContext;
import java.awt.Component;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Segment;

/**
 *
 * @author CyaNn
 */
public abstract class UpdateStrategy {

    protected TokenizeContext currentContext;
    protected AbstractTokenizer tokenizer;
    private Component component = null;

    public Component getComponent() {
	return component;
    }

    public void setComponent(Component component) {
	this.component = component;
    }

    public TokenizeContext getCurrentContext() {
	return currentContext;
    }

    public void tokenize(DocumentEvent e) throws BadLocationException {

	// TODO : TEST
	//long time = Calendar.getInstance().getTimeInMillis();

	Segment text = new Segment();
	e.getDocument().getText(0, e.getDocument().getLength(), text);
	currentContext =
		tokenizer.tokenize(text);

	// TODO : TEST
	//System.out.println("Time Tokenize : " + (Calendar.getInstance().getTimeInMillis() - time));

    }

    public void repaintComponent() {
	if (component != null) {
	    component.repaint();
	}
    }

    public abstract void update(DocumentEvent e) throws BadLocationException;

    public UpdateStrategy(AbstractTokenizer tokenizer) {
	this.tokenizer = tokenizer;
    }
}
