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
public class LiteralItem extends Item {

    private String literal;

    @Override
    public boolean isOperator() {
        return false;
    }

    public String getLiteral() {
        return literal;
    }

    public LiteralItem(String literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return literal;
    }

    @Override
    public void evalDP(DP dp, int pos, EvalContext context) {
        context.incrementEvalNumber();

        int end = pos + literal.length();
        if (dp.getName().indexOf(literal, pos) == pos) {
            if (!hasNext()) {
                if (dp.getName().length() == end) {
                    context.addDp(dp);
                }
            } else {
                getNext().evalDP(dp, pos + literal.length(), context);
            }
        }
    }

    @Override
    public String chainSring() {
	String ret = literal;
	if (hasNext()) ret += getNext().chainSring();
	return ret;
    }

}
