/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jComboBox;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.snmp4j.smi.Variable;

/**
 *
 * @author caronyn
 */
public class ConvertTableCombolRenderer implements ListCellRenderer {

	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private ConvertTable convertTable;

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);

		Color color = convertTable.find(value).getStatus().getColor();

		if (isSelected) {
			renderer.setBackground(color.darker());
		} else {
			renderer.setBackground(color);
		}
		return renderer;
	}

	public ConvertTableCombolRenderer(ConvertTable valueList) {
		this.convertTable = valueList;
	}
}
