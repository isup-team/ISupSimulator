/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.convertTable.Entry;
import ch.skyguide.pvss.network.service.modbus.WagoConvertTable;
import java.awt.Color;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author CARONYN
 */
public class RegisterCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	JSlider slider;
	String convertTableColumnName;

	private Color getColor(JTable table, Object value, boolean isSelected, int row) {
		WagoConvertTable ct = (WagoConvertTable) table.getModel().getValueAt(row, table.getColumn(convertTableColumnName).getModelIndex());

		Color color = table.getBackground();

		Integer v = (Integer) value;
		if (ct != null) {
			for (Object o : ct.getList()) {
				Entry e = (Entry)o;
				Integer i = (Integer)e.getValue();
				if (i <= v) {
					color = e.getStatus().getColor();
				}
			}

			if (isSelected) {
				color = color.darker();
			}
		} else {
			if (row % 2 == 1) {
				color = Color.WHITE;
			}

			if (isSelected) {
				color = table.getSelectionBackground();
			}
		}

		return color;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		slider.setBackground(getColor(table, value, isSelected, row));
		slider.setValue((Integer) value);
		slider.updateUI();

		return slider;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		slider.setBackground(getColor(table, value, isSelected, row));
		slider.setValue((Integer) value);

		return slider;
	}

	public Object getCellEditorValue() {
		return slider.getValue();
	}

	public RegisterCellEditor(final JSlider slider, String convertTableColumnName) {
		this.slider = slider;
		this.convertTableColumnName = convertTableColumnName;

		slider.setOpaque(true);
		slider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (!slider.getValueIsAdjusting()) {
					stopCellEditing();
				}
			}
		});
	}
}
