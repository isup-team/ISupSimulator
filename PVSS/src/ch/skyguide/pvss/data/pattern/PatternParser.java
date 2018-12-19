/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.pattern;

import ch.skyguide.common.text.ReadableIterator;
import ch.skyguide.common.text.SyntaxTokenizer;
import ch.skyguide.common.text.SyntaxTokenizer.Token;
import ch.skyguide.pvss.data.pattern.item.*;

/**
 *
 * @author CyaNn
 */
public class PatternParser {

    public static Item parse(String pattern) {
	SyntaxTokenizer tokenizer = new SyntaxTokenizer(pattern, Delimiter.getCodes());

	ReadableIterator<Token> iterator = (ReadableIterator<Token>) tokenizer.iterator();
	Item item = parse(iterator);
	return item;
    }

    private static Item parse(ReadableIterator<Token> iterator) {
	Item item = null;

	Token token = iterator.next();

	if (token.value.equals(Delimiter.BRACKET.getCode())) {
	    MultiItem mItem = new MultiItem();

	    while (iterator.hasNext() && !iterator.read().value.equals(Delimiter.ENDBRACKET.getCode())) {
		//iterator.next();
		Item child = parse(iterator);
		if (child != null) {
		    mItem.addItem(child);
		}
	    }
	    item = mItem;
	} else if (token.value.equals(Delimiter.SQUAREBRACKET.getCode())) {
	    if (iterator.hasNext()) {
		item = new NumberItem(Integer.valueOf(iterator.next().value) - 1);
		if (!iterator.hasNext() || !iterator.next().value.equals(Delimiter.ENDSQUAREBRACKET.getCode())) {
		    throw new PatternException("");
		}
	    } else {
		throw new PatternException("Number and ] attempts");
	    }
	} else if (token.value.equals(Delimiter.COMA.getCode()) || token.value.equals(Delimiter.ENDBRACKET.getCode()) || token.value.equals("")) {
	    return null;
	} else if (token.value.equals(Delimiter.DOT.getCode())) {
	    item = new DotItem();
	} else if (token.value.equals(Delimiter.WILDCARD.getCode())) {
	    item = new WildCardItem();
	} else if (token.value.equals(Delimiter.STAR.getCode())) {
	    item = new StarItem();
	} else if (token.value.equals(Delimiter.TWICESTAR.getCode())) {
	    item = new TwiceStarItem();
	} else {
	    item = new LiteralItem(token.value);
	}

	// recursive
	if (iterator.hasNext()) {
	    item.setNext(parse(iterator));
	}

	return item;
    }

}
