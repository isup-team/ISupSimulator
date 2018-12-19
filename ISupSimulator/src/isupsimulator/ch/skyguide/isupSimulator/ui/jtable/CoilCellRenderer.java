/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.modbus.WagoConvertTable;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author caronyn
 */
public class CoilCellRenderer extends BooleanCellRenderer {

	String convertTableColumnName;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		WagoConvertTable ct = (WagoConvertTable) table.getModel().getValueAt(row, table.getColumn(convertTableColumnName).getModelIndex());

		if (ct != null) {
			Color color = ct.find(value).getStatus().getColor();
			if (isSelected) {
				color = color.darker();
			}

			renderer.setBackground(color);
		}

		return renderer;
	}

	public CoilCellRenderer(String convertTableColumnName) {
		this.convertTableColumnName = convertTableColumnName;
	}
}
