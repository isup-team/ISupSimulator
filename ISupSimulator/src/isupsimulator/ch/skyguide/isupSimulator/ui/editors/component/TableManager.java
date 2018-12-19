/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TableManager.java
 *
 * Created on 27 sept. 2011, 12:01:45
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.editors.component;

import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.EditableTableModel;
import isupsimulator.ch.skyguide.isupSimulator.util.StringTransfert;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTable;
import org.jdesktop.application.Action;

/**
 *
 * @author caronyn
 */
public class TableManager extends javax.swing.JPanel {

	// attribute
	StringTransfert transfert;
	JTable table;

	// attributes
	public void setTable(JTable table) {
		this.table = table;

		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);

				if (e.getModifiers() == KeyEvent.CTRL_MASK) {
					String key = KeyEvent.getKeyText(e.getKeyCode());
					if ("C".equals(key)) {
						copy();
					} else if ("V".equals(key)) {
						paste();
					}
				}
			}
		});

	}

	private EditableTableModel getModel() {
		if (table.getModel() instanceof EditableTableModel) {
			return (EditableTableModel) table.getModel();
		} else {
			throw new RuntimeException("Table model does not implement EditableTableModel interface !\n");
		}
	}

	// function
	private int getLastSelectedRow() {
		int count = table.getSelectedRowCount();

		if (count == 0) {
			return -1;
		} else {
			return table.getSelectedRows()[table.getSelectedRowCount() - 1];
		}
	}

	private int getSelectionCount() {
		int count = table.getSelectedRowCount();

		if (count == 0) {
			count = 1;
		}

		return count;
	}

	// action
	@Action
	public void insert() {
		int index = table.getSelectedRow();
		int count = getSelectionCount();

		for (int i = index; i < index + count; i++) {

			if (i != -1) {
				getModel().insertNewRow(index);
			} else {
				getModel().insertNewRow(0);
			}
		}

		if (index == -1) {
			index = 0;
		}
		table.setRowSelectionInterval(index, index + count - 1);
		table.setColumnSelectionInterval(0, table.getColumnCount() - 1);

	}

	@Action
	public void add() {
		int index = getLastSelectedRow();
		int count = getSelectionCount();

		for (int i = index; i < index + count; i++) {
			if (i != -1) {
				getModel().insertNewRow(i + 1);
			} else {
				getModel().insertNewRow(table.getRowCount());
			}
		}

		if (index == -1) {
			index = table.getRowCount() - 2;
		}
		table.setRowSelectionInterval(index + 1, index + count);
		table.setColumnSelectionInterval(0, table.getColumnCount() - 1);

	}

	@Action
	public void remove() {
		for (int i = table.getSelectedRowCount() - 1; i >= 0; i--) {
			getModel().removeRow(table.getSelectedRows()[i]);
		}
	}

	private void setValueAt(Object value, int row, int col) {

		//try {
		if (col >= 0 && col < table.getColumnCount()
				&& row >= 0 && row < table.getRowCount()) {

			getModel().setValueAt(value, row, col);
		}
		/*} catch (Exception ex) {
		// do nothing
		}*/
	}

	private Object getValueAt(int row, int col) {

		try {
			if (col >= 0 && col < table.getColumnCount()
					&& row >= 0 && row < table.getRowCount()) {

				Object ret = getModel().getValueAt(row, col);
				if (ret == null) {
					ret = "";
				}
				return ret;
			}
		} catch (Exception ex) {
			// do nothing
		}
		return "";
	}

	private void trySetRowSelectionInterval(int index0, int index1) {
		try {
			table.setRowSelectionInterval(index0, index1);
		} catch (Exception ex) {
			// do nothing
		}
	}

	private void trySetColumnSelectionInterval(int index0, int index1) {
		try {
			table.setColumnSelectionInterval(index0, index1);
		} catch (Exception ex) {
			// do nothing
		}
	}

	@Action
	public void copy() {
		StringBuilder sb = new StringBuilder();

		for (int r = 0; r < table.getSelectedRowCount(); r++) {
			StringBuilder line = new StringBuilder();

			for (int c = 0; c < table.getSelectedColumnCount(); c++) {
				int row = table.getSelectedRows()[r];
				int col = table.getSelectedColumns()[c];

				if (line.length() != 0) {
					line.append("\t");
				}
				line.append(getValueAt(row, col));
			}
			if (sb.length() != 0) {
				sb.append("\n");
			}
			sb.append(line);
		}

		transfert.setClipboardContent(sb.toString());
	}

	private void pasteLine(String line, int l, int row, int col) {
		trySetRowSelectionInterval(row, row + l);

		if (!line.contains("\t")) {
			setValueAt(line, row + l, col);
			trySetColumnSelectionInterval(col, col);
		} else {
			String[] tokens = line.split("\t");

			for (int t = 0; t < tokens.length; t++) {
				setValueAt(tokens[t], row + l, col + t);
				trySetColumnSelectionInterval(col, col + t);
			}
		}
	}

	@Action
	public void paste() {
		String content = transfert.getClipboardContent();

		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();

		if (row == -1) {
			row = 0;
		}

		if (col == -1) {
			col = 0;
		}

		if (!content.contains("\n")) {
			pasteLine(content, 0, row, col);
		} else {
			String[] lines = content.split("\n");

			for (int l = 0; l < lines.length; l++) {
				pasteLine(lines[l], l, row, col);
			}
		}
	}

	/** Creates new form TableManager */
	public TableManager() {
		initComponents();
		transfert = new StringTransfert();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setName("Form"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getActionMap(TableManager.class, this);
        jButton1.setAction(actionMap.get("insert")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(TableManager.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton3.setAction(actionMap.get("add")); // NOI18N
        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N

        jButton2.setAction(actionMap.get("remove")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jButton5.setAction(actionMap.get("copy")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N

        jButton4.setAction(actionMap.get("paste")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton1)
                    .addComponent(jButton5)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    // End of variables declaration//GEN-END:variables
}
