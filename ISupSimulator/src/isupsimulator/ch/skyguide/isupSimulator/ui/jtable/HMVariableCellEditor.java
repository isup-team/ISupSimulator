/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import ch.skyguide.pvss.network.service.homemade.HMTypeEnum;
import isupsimulator.ch.skyguide.isupSimulator.ui.jComboBox.HMStatusComboRenderer;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

/**
 *
 * @author caronyn
 */
public class HMVariableCellEditor extends DefaultCellEditor {

	// attribute
	String typeColumnName;
	Component editor;

	// methode implementation
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		TableColumn typeColumn = table.getColumn(typeColumnName);
		HMTypeEnum type = (HMTypeEnum) table.getModel().getValueAt(row, typeColumn.getModelIndex());

		if (type.getValues() != null) {
			JComboBox combo = new JComboBox(type.getValues());

			if (type.equals(HMTypeEnum.STATUS)) {
				combo.setRenderer(new HMStatusComboRenderer());
			}
			editor = combo;
		} else {
			editor = table.getDefaultEditor(value.getClass()).getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		return editor;
	}

	@Override
	public Object getCellEditorValue() {
		if (editor instanceof JComboBox) {
			return ((JComboBox) editor).getSelectedItem().toString();
		} else {
			return ((JTextField) editor).getText();
		}
	}

	// constructor
	public HMVariableCellEditor(String typeColumnName) {
		super(new JTextField());
		this.typeColumnName = typeColumnName;
	}
}
