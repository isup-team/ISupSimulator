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
public abstract class AbstractTokenizer {

    protected boolean charsAre(TokenizeContext ctx, CharSequence cs) {
	boolean ret = true;
	if (ctx.getLength() >= ctx.i + cs.length()) {
	    for (int j = 0; j < cs.length() && ret == true; j++) {
		if (ctx.charAt(ctx.i + j) != cs.charAt(j) && cs.charAt(j) != getMatchChar()) {
		    ret = false;
		}
	    }
	} else {
	    ret = false;
	}
	return ret;
    }

    protected int findChars(TokenizeContext ctx, CharSequence[] css) {
	int ret = -1;
	int ics = 0;

	while (ics < css.length && ret == -1) {
	    if (charsAre(ctx, css[ics])) {
		ret = css[ics].length();
	    }
	    ics++;
	}

	return ret;
    }

    protected int findOperator(TokenizeContext ctx) {
	return findChars(ctx, getOperators());
    }

    protected int findKeyword(TokenizeContext ctx) {
	return findChars(ctx, getKeywords());
    }

    protected void whileToken(TokenizeContext ctx, char c, TokenType type) {
	addToken(ctx, TokenType.Null);
	ctx.next();
	while (ctx.i < ctx.getLength() && ctx.charAt(ctx.i) != c) {
	    ctx.next();
	}
	ctx.next();
	addToken(ctx, type);
    }

    protected void whileToken(TokenizeContext ctx, char c, char escape, TokenType type) {
	addToken(ctx, TokenType.Null);
	ctx.next();
	while (ctx.i < ctx.getLength()
		&& (ctx.charAt(ctx.i) != c || (ctx.charAt(ctx.i - 1) == escape && ctx.charAt(ctx.i - 2) != escape))) {
	    ctx.next();
	}
	ctx.next();
	addToken(ctx, type);
    }

    protected void whileToken(TokenizeContext ctx, CharSequence cs, TokenType type) {
	addToken(ctx, TokenType.Null);
	ctx.next();
	while (ctx.i < ctx.getLength() && !charsAre(ctx, cs)) {
	    ctx.next();
	}
	ctx.i += cs.length();
	addToken(ctx, type);
    }

    protected void whileNumeric(TokenizeContext ctx, char c, TokenType type) {
	addToken(ctx, TokenType.Null);
	ctx.next();
	while (ctx.i < ctx.getLength() && Character.isDigit(ctx.charAt(ctx.i))) {
	    ctx.next();
	}
	addToken(ctx, type);
    }

    protected void addToken(TokenizeContext ctx, TokenType type) {
	if (ctx.last < ctx.i) {
	    ctx.tokens.add(new Token(type, ctx.i - ctx.last));
	    ctx.tokenEnd();
	}
    }

    protected void addWordToken(TokenizeContext ctx, TokenType type, int len) {
	addToken(ctx, TokenType.Null);
	ctx.i += len;
	addToken(ctx, type);
	ctx.tokenEnd();
    }

    public TokenizeContext tokenize(Segment text) {
	TokenizeContext ctx = new TokenizeContext(text);
	tokenize(ctx);
	return ctx;
    }

    /** ATTENTION : Valeur à mettre à jours */
    public abstract int getTokenMaxlength();

    public abstract char getMatchChar();

    public abstract CharSequence[] getOperators();

    public abstract CharSequence[] getKeywords();

    public abstract void tokenize(TokenizeContext ctx);
}
