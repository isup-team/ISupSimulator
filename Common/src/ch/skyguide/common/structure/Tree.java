/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.structure;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author caronyn
 */
public class Tree<E> {

    private static class Entry<E> {
	E element;
    }

    private static class EntryNode<E> extends Entry<E> {
	List<Entry<E>> children;
    }

    public E element;
    public List<Tree<E>> children;

    // constructor
    public Tree() {
    }

}
