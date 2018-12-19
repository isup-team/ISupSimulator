/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author caronyn
 */
public class ConvertTableAdapter extends DefaultMutableTreeNode{

	public ConvertTableAdapter(ConvertTable convertTable) {
		super(convertTable);
	}

	@Override
	public String toString() {
		return ((ConvertTable) userObject).getName();
	}

}
