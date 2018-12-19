/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

/**
 *
 * @param <T>
 * @author caronyn
 */
public interface EditableTableModel {

	void insertNewRow(int row);
	void removeRow(int row);
	void moveRow(int start, int stop, int to);

	Object getValueAt(int rowIndex, int columnIndex);
	void setValueAt(Object aValue, int rowIndex, int columnIndex);
}
