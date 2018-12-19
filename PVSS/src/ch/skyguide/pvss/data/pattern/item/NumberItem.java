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
public class NumberItem extends Item {

    private static final String FORMAT = "[%d]";
    int index;

    public NumberItem(int index) {
	this.index = index;
    }

    @Override
    public boolean isOperator() {
	return true;
    }

    @Override
    public String toString() {
	return String.format(FORMAT, index + 1);
    }

    public void evalDP(DP dp, int pos, EvalContext context) {
        context.incrementEvalNumber();

	if (dp instanceof DPNode && ((DPNode)dp).getChildren().size() > index && dp.getName().length() == pos) {
	    DP child = ((DPNode) dp).getChildren().elementAt(index);
	    if (!hasNext()) {
		context.addDp(child);
	    } else {
		getNext().evalDP(child, child.getName().length(), context);
	    }
	}
    }
}
