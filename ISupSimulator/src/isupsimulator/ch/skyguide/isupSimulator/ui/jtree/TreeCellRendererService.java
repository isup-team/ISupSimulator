/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.network.service.Service;
import isupsimulator.ch.skyguide.isupSimulator.ui.ServiceStatusEnum;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author caronyn
 */
public class TreeCellRendererService extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

		if (value instanceof Service) {
			Service service = (Service) value;

			ServiceStatusEnum servSt = ServiceStatusEnum.valueOf(service.getStatus());
			setIcon(servSt.getImageIcon());
		}

		return this;
	}
}
