package ch.skyguide.pvss.data.gui;


import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.DPNode;
import javax.swing.tree.DefaultMutableTreeNode;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author CyaNn
 */
public class TreeNodeDPAdapter extends DefaultMutableTreeNode {

    public TreeNodeDPAdapter(DP dp) {
	super(dp);

	if (dp instanceof DPNode) {
	    DPNode node = (DPNode) dp;
	    for (DP child : node.getChildren()) {
		super.add(new TreeNodeDPAdapter(child));
	    }
	}
    }

}
