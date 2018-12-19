/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.text;

import java.util.Iterator;

/**
 *
 * @author CyaNn
 */
public interface ReadableIterator<T> extends Iterator<T> {

    public T read();

}
