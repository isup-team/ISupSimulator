/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.pattern.item;

import ch.skyguide.pvss.data.dataPointElement.DP;

/**
 *
 * @author CyaNn
 */
public class WildCardItem extends Item {

    private static final String LITERAL = "_";
    private static final String FORMAT = LITERAL + "%s";

    @Override
    public boolean isOperator() {
	return false;
    }

    @Override
    public String toString() {
	return LITERAL;
    }

    @Override
    public void evalDP(DP dp, int pos, EvalContext context) {
	context.incrementEvalNumber();

	int end = pos + 1;
	if (!hasNext()) {
	    if (dp.getName().length() == end) {
		context.addDp(dp);
	    }
	} else {
	    if (dp.getName().length() >= end) {
		getNext().evalDP(dp, end, context);
	    }
	}
    }

}
