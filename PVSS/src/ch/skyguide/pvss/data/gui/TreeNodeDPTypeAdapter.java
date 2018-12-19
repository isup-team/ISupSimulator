/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.gui;

import ch.skyguide.pvss.data.dataPointType.DPType;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author CyaNn
 */
public class TreeNodeDPTypeAdapter extends DefaultMutableTreeNode {

    public TreeNodeDPTypeAdapter(DPType dp) {
	super(dp);

	if (dp instanceof NodeType) {
	    NodeType node = (NodeType) dp;
	    for (DPType child : node) {
		super.add(new TreeNodeDPTypeAdapter(child));
	    }
	}
    }

}
