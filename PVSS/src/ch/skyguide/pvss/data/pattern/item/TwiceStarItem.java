/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.pattern.item;

import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.DPNode;

/**
 *
 * @author CyaNn
 */
public class TwiceStarItem extends Item {

    private static final String LITERAL = "**";

    @Override
    public boolean isOperator() {
	return false;
    }

    @Override
    public String toString() {
	return LITERAL;
    }

    public void evalDP(DP dp, int pos, EvalContext context) {
	context.incrementEvalNumber();

	if (!hasNext()) {
	    context.addDp(dp);
	} else {
	    for (int newPos = pos; newPos <= dp.getName().length(); newPos++) {
		getNext().evalDP(dp, newPos, context);
	    }
	}

	if (dp instanceof DPNode) {
	    DPNode node = (DPNode) dp;
	    for (DP child : node.getChildren()) {
		evalDP(child, 0, context);
	    }
	}
    }
}
