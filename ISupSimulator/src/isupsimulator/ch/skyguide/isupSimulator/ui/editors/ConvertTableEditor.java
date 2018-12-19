/*
 * SNMPMibEditor.java
 *
 * Created on 10 déc. 2010, 10:22:43
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.editors;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.convertTable.Entry;
import ch.skyguide.pvss.network.service.convertTable.EnumConverter;
import ch.skyguide.pvss.network.service.convertTable.SystemStatus;
import isupsimulator.ch.skyguide.isupSimulator.ui.jComboBox.SystemStatusComboRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.BooleanCellRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ListAdapter;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.SystemStatusTableRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.TableColumnDefinition;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import org.snmp4j.smi.Variable;

/**
 *
 * @author caronyn
 */
public final class ConvertTableEditor extends Editor<ConvertTable> {

	public static final String VALUE_COLUMN_NAME = "Value";
	// attributes
	TableColumnDefinition[] columnDefinitions = {
		new TableColumnDefinition(VALUE_COLUMN_NAME) {

			@Override
			public Object getField(int rowIndex) {
				EnumConverter syn = (EnumConverter) typeCombo.getSelectedItem();

				try {
					return syn.toJava(datasource.get(rowIndex).getValue());
				} catch (Exception ex) {
					return syn.getDefaultJava();
				}
			}

			@Override
			public void setField(int rowIndex, Object value) {
				EnumConverter syn = (EnumConverter) typeCombo.getSelectedItem();

				try {
					datasource.get(rowIndex).setValue(syn.toVariable(value));
				} catch (Exception e) {
					datasource.get(rowIndex).setValue(syn.getDefaultVariable());
					System.out.println(e);
				}
			}

			@Override
			public boolean isEditable() {
				return true;
			}

			@Override
			public Class getColumnClass() {
				EnumConverter syn = (EnumConverter) typeCombo.getSelectedItem();
				return syn.getDefaultJava().getClass();
			}

		},
		new TableColumnDefinition("Status") {

			@Override
			public Object getField(int rowIndex) {
				return datasource.get(rowIndex).getStatus();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				SystemStatus status = null;
				if (value instanceof String) {
					status = SystemStatus.valueOf((String) value);
				} else {
					status = (SystemStatus) value;
				}

				datasource.get(rowIndex).setStatus(status);
			}

			@Override
			public Class getColumnClass() {
				return SystemStatus.class;
			}

			@Override
			public boolean isEditable() {
				return true;
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				JComboBox combo = new JComboBox(SystemStatus.values());
				combo.setRenderer(new SystemStatusComboRenderer());
				return new DefaultCellEditor(combo);
			}
		}
	};

	// methode implementation
	/**
	 * Return if this is the appropriate editor.
	 * @param type Type accepted.
	 * @return Return true if it can edit the type.
	 */
	@Override
	public boolean canEdit(Class type) {
		return (type.getSuperclass().equals(ConvertTable.class));
	}

	/**
	 * Register the data change events.
	 */
	@Override
	public void registerEvents() {
		registerLazyTextChangedEvent(nameTextField);

		// TODO une seul fois
		table.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				fireDataChangedEvent();
				TableColumnDefinition.adjustColumnWidth(table);
			}
		});
	}

	/**
	 * From data model to view.
	 */
	@Override
	public void displayDataModel() {
		typeCombo.setModel(new DefaultComboBoxModel(datasource.getConverter().allValues()));

		nameTextField.setText(datasource.getName());

		// table
		table.setModel(new ListAdapter<Entry<Variable>>(columnDefinitions, datasource.getList()) {

			@Override
			public Entry getElementInstance() {
				return new Entry(SystemStatus.OPS, datasource.getConverter().getDefaultVariable());
			}
		});
		table.setDefaultRenderer(SystemStatus.class, new SystemStatusTableRenderer());
		table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
		TableColumnDefinition.applyEditors(table, columnDefinitions);
		TableColumnDefinition.adjustColumnWidth(table);

		if (datasource.getFirst() != null) {
			typeCombo.setSelectedItem(datasource.getConverter().find(datasource.getFirst().getValue()));
		} else {
			typeCombo.setSelectedIndex(0);
		}
	}

	/**
	 * From view to data model.
	 */
	public void storeDataModel() {
		datasource.setName(nameTextField.getText());
	}

	// event handling
	private void typeCombo_actionPerformed(ActionEvent e) {
		TableModel model = table.getModel();
		int col = table.getColumn(VALUE_COLUMN_NAME).getModelIndex();

		for (int row = 0; row < model.getRowCount(); row++) {
			model.setValueAt(model.getValueAt(row, col), row, col);
			;
		}
	}

	// constructor
	/** Creates new form SNMPMibEditor */
	public ConvertTableEditor() {
		initComponents();
		tableManager.setTable(table);

		typeCombo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				typeCombo_actionPerformed(e);
			}
		});
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();
        jLabel2 = new javax.swing.JLabel();
        typeCombo = new javax.swing.JComboBox();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(ConvertTableEditor.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        nameTextField.setName("nameTextField"); // NOI18N

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.setCellSelectionEnabled(true);
        table.setName("table"); // NOI18N
        jScrollPane1.setViewportView(table);

        tableManager.setName("tableManager"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        typeCombo.setName("typeCombo"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableManager, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTable table;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager tableManager;
    private javax.swing.JComboBox typeCombo;
    // End of variables declaration//GEN-END:variables
}
