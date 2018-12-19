/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.convertTable.Entry;
import isupsimulator.ch.skyguide.isupSimulator.ui.jComboBox.ConvertTableCombolRenderer;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

/**
 *
 * @author caronyn
 */
public abstract class VariableCellEditor extends DefaultCellEditor {

	Component editor;

	public VariableCellEditor() {
		super(new JTextField());
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

		ConvertTable convertTable = getConvertTable(table, row, column);

		if (convertTable == null) {
			editor = table.getDefaultEditor(Object.class).getTableCellEditorComponent(table, value, isSelected, row, column);
		} else {
			JComboBox combo = new JComboBox();
			combo.setRenderer(new ConvertTableCombolRenderer(convertTable));
			for (Object o : convertTable.getList()) {
				Entry vm = (Entry) o;
				combo.addItem(vm.getValue());

				// select value
				if (vm.getValue().toString().equals(String.valueOf(value))) {
					combo.setSelectedIndex(combo.getItemCount()-1);
				}
			}
			editor = combo;
		}
		return editor;
	}

	public abstract ConvertTable getConvertTable(JTable table, int row, int column);

	@Override
	public Object getCellEditorValue() {
		if (editor instanceof JComboBox) {
			return ((JComboBox) editor).getSelectedItem().toString();
		} else {
			return ((JTextField) editor).getText();
		}
	}
}
