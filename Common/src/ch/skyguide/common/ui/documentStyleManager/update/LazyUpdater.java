/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.documentStyleManager.update;

import ch.skyguide.common.ui.documentStyleManager.token.AbstractTokenizer;
import ch.skyguide.common.ui.documentStyleManager.token.Token;
import ch.skyguide.common.ui.documentStyleManager.token.TokenizeContext;
import java.awt.Component;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;

/**
 *
 * @author CyaNn
 */
public class LazyUpdater extends UpdateStrategy {

    private TokenizeContext currentContextPart = null;

    public enum TokenChange {

	All, CurrentToken, NextToken
    }

    private TokenChange tokenChange(DocumentEvent e) throws BadLocationException {
	TokenChange ret = TokenChange.All;

	Document doc = e.getDocument();

	int off = Math.max(0, e.getOffset() - tokenizer.getTokenMaxlength());
	int len = Math.min(doc.getLength() - off, tokenizer.getTokenMaxlength() * 2);
	Segment text = new Segment();
	doc.getText(off, len, text);

	TokenizeContext ctx = new TokenizeContext(text);
	tokenizer.tokenize(ctx);

	if (e.getLength() == 1) {

	    if (currentContextPart != null) {
		Token curT0 = currentContextPart.tokens.getTokenAtOffset((e.getOffset() - off) + 1);
		Token curTL = currentContextPart.tokens.lastElement();
		Token newT0 = ctx.tokens.getTokenAtOffset((e.getOffset() - off) + 1);
		Token newTL = ctx.tokens.lastElement();
		Token curMainT0 = currentContext.tokens.getTokenAtOffset(e.getOffset() + 1);

		if (curT0.getTokenType() == newT0.getTokenType() && curTL.getTokenType() == newTL.getTokenType() && ctx.tokens.size() == currentContextPart.tokens.size()) {
		    if (curT0.getTokenType() == curMainT0.getTokenType()) {
			ret = TokenChange.NextToken;
		    } else {
			ret = TokenChange.CurrentToken;
		    }
		}
	    }
	} else {
	    ret = TokenChange.All;
	}

	currentContextPart = ctx;

	return (ret);
    }

    @Override
    public void update(DocumentEvent e) throws BadLocationException {
	if (e.getType() == DocumentEvent.EventType.INSERT) {
	    switch (tokenChange(e)) {
		case All:
		    tokenize(e);
		    repaintComponent();
		    break;
		case CurrentToken:
		    currentContext.tokens.addCharAtOffset(e.getOffset());
		    break;
		case NextToken:
		    currentContext.tokens.addCharAtOffset(e.getOffset() + 1);
		    break;
	    }
	} else {
	    if (tokenChange(e) == TokenChange.All) {
		tokenize(e);
		repaintComponent();
	    } else {
		currentContext.tokens.removeCharAtOffset(e.getOffset() + 1);
	    }
	}

    }

    public LazyUpdater(AbstractTokenizer tokenizer) {
	super(tokenizer);
    }
}
