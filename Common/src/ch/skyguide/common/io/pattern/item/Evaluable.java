/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.io.pattern.item;

/**
 *
 * @author caronyn
 */
public interface Evaluable {

    void evalDP(String path, int pos, EvalContext context);

}
