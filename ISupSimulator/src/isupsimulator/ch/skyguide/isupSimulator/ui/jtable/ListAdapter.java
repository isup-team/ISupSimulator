/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtable;

import java.util.LinkedList;
import java.util.List;
import javax.swing.event.TableModelEvent;

/**
 *
 * @param <T>
 * @author caronyn
 */
public abstract class ListAdapter<T> extends ColumnDefinedTableModel {

	// attribute
	protected List<T> elements;

	// property
	public int getRowCount() {
		if (elements == null) {
			return 0;
		} else {
			return elements.size();
		}
	}

	// abstract methode
	public abstract T getElementInstance();

	// methode implementation
	public void insertNewRow(int row) {
		initializeList();
		elements.add(row, getElementInstance());
		this.fireTableRowsInserted(row, row);
	}

	public void removeRow(int row) {
		initializeList();
		elements.remove(row);
		this.fireTableRowsDeleted(row, row);
	}

	public void moveRow(int start, int stop, int to) {
		initializeList();
		this.fireTableChanged(new TableModelEvent(this));
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void initializeList() {
		if (elements == null) {
			elements = new LinkedList<T>();
		}
	}

	// constructor
	public ListAdapter(TableColumnDefinition[] columnDefinitions, Iterable<T> elements) {
		super(columnDefinitions);
		this.elements = (List<T>) elements;
	}
}
