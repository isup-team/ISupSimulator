/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.pattern;

import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.Root;
import ch.skyguide.pvss.data.pattern.item.EvalContext;
import java.util.Vector;

/**
 *
 * @author CyaNn
 */
public class Pattern {

    private Path path;
    private int lastEvalNumber;

    public int getLastEvalNumber() {
        return lastEvalNumber;
    }

    public Pattern(String pattern) {
        path = new Path(PatternParser.parse(pattern));
    }

    public Vector<DP> find(Root root) {
        EvalContext context = new EvalContext();

        for (DP instance : root.getChildren()) {
            path.getFirstItem().evalDP(instance, 0, context);
        }

        lastEvalNumber = context.getEvalNumber();

        return context.getDps();
    }

    public DP findDP(Root root) {
        Vector<DP> dps = find(root);

        if (dps.size() == 1) {
            return dps.firstElement();
        } else if (dps.size() == 0) {
            throw new PatternException(String.format("Pattern [%s] return no value", toString()));
        }

        throw new PatternException(String.format("Pattern [%s] return more than one data point", toString()));

    }

    public DP findFirstDP(Root root) {
        Vector<DP> dps = find(root);

        if (dps.size() == 0) {
            throw new PatternException(String.format("Pattern [%s] return no value", toString()));
        }
        return dps.firstElement();

    }

    @Override
    public String toString() {
        return path.toString();
    }
}
