/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.pattern.item;

import ch.skyguide.pvss.data.dataPointElement.DP;
import java.util.Vector;

/**
 *
 * @author CyaNn
 */
public class EvalContext {

    Vector<DP> dps = new Vector<DP>();
    int evalNumber;

    public int getEvalNumber() {
        return evalNumber;
    }

    public void incrementEvalNumber() {
        evalNumber++;
    }

    public void addDp(DP dp) {
        dps.add(dp);
    }

    public Vector<DP> getDps() {
        return dps;
    }

}
