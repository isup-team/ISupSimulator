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
public class StarItem extends Item {

    private static final String LITERAL = "*";

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

        if (!hasNext()) {
            context.addDp(dp);
        } else {
            for (int newPos = pos + 1; newPos <= dp.getName().length(); newPos++) {
                getNext().evalDP(dp, newPos, context);
            }
        }
    }

}
