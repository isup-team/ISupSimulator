/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jComboBox;

import ch.skyguide.pvss.network.service.homemade.HMStatusEnum;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author caronyn
 */
public class HMStatusComboRenderer implements ListCellRenderer {

	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);

		Color color = HMStatusEnum.valueOf(String.valueOf(value)).getISupStatus().getColor();

		if (isSelected) {
			renderer.setBackground(color.darker());
		} else {
			renderer.setBackground(color);
		}
		return renderer;
	}

}
