/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author CARONYN
 */
public class BooleanCellRenderer extends JCheckBox implements TableCellRenderer {

	public BooleanCellRenderer() {
		super();
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setBorderPainted(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		if (value != null) {
			this.setSelected((Boolean) value);
		}

		Color color = null;

		if (isSelected) {
			color = table.getSelectionBackground();
		} else {
			// Only to bypass Nimbus bug : see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524
			if (row % 2 == 1) {
				color = Color.WHITE;
			} else {
				color = table.getBackground();
			}
		}

		this.setOpaque(true);
		this.setBackground(color);

		return this;
	}
}
