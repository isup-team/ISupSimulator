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
public interface Evaluable {

    void evalDP(DP dp, int pos, EvalContext context);

}
