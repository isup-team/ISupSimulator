/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.pattern.item;

import ch.skyguide.pvss.data.dataPointElement.DP;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author CyaNn
 */
public class MultiItem extends Item implements Iterable<Item> {

    private static final String FORMAT = "{%s}";
    private static final String SEPARATOR = ",";

    List<Item> items = new Vector<Item>();

    @Override
    public boolean isOperator() {
        return true;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Iterator<Item> iterator() {
        return items.iterator();
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer();
	for (Item item : items) {
	    if (sb.length() > 0) sb.append(SEPARATOR);
	    sb.append(item.chainSring());
	}

	return String.format(FORMAT, sb.toString());
    }

    public void evalDP(DP dp, int pos, EvalContext context) {
        context.incrementEvalNumber();

        for(Item item : items) {
            item.evalDP(dp, pos, context);
        }

    }

}
