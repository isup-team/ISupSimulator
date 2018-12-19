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
public class DotItem extends Item {

    private static final String LITERAL = ".";

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public String toString() {
        return LITERAL;
    }

    @Override
    public void evalDP(DP dp, int pos, EvalContext context) {
        context.incrementEvalNumber();

	// Voir twice star
	if (hasNext() && dp instanceof DPNode && dp.getName().length() == pos) {
            DPNode dpNode = (DPNode) dp;
            for (DP child : dpNode.getChildren()) {
		getNext().evalDP(child, 0, context);
	    }
	}

    }

}
