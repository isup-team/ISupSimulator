/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SNMPServerEditor.java
 *
 * Created on 14 déc. 2010, 09:39:12
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.editors;

import ch.skyguide.pvss.network.service.convertTable.Entry;
import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import ch.skyguide.pvss.network.service.snmp.SNMPConvertTable;
import ch.skyguide.pvss.network.service.snmp.SNMPTrapService;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ColumnDefinedConvertTableTableRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ColumnDefinedVariableCellEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ScalarAdapter;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.TableColumnDefinition;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 *
 * @author caronyn
 */
public final class SNMPTrapEditor extends Editor<SNMPTrapService> {

	public static final String TYPE_COLUMN_NAME = "Type";
	public static final String CONVERT_TABLE_COLUMN_NAME = "Convert table";
	// attributes
	TableColumnDefinition[] columnDefinitions = {
		new TableColumnDefinition("Name") {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getBuilder(rowIndex).getName();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				datasource.getBuilder(rowIndex).setName((String) value);
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition("OID") {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getBuilder(rowIndex).getOID();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				datasource.getBuilder(rowIndex).setOID(new OID((String) value));
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition("Access") {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getBuilder(rowIndex).getAccess();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				SMIAccess access = null;

				if (value instanceof String) {
					access = SMIAccess.valueOf((String) value);
				} else {
					access = (SMIAccess) value;
				}

				datasource.getBuilder(rowIndex).setAccess(access);
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				JComboBox combo = new JComboBox(SMIAccess.values());
				return new DefaultCellEditor(combo);
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition(TYPE_COLUMN_NAME) {

			@Override
			public Object getField(int rowIndex) {
				ScalarBuilder builder = datasource.getBuilder(rowIndex);
				Variable value = null;
				if (builder.getConvertTable() != null) {
					value = builder.getConvertTable().getFirst().getValue();
				} else {
					value = builder.getValue();
				}
				return (SMISyntax.valueOf(value.getSyntax()));
			}

			@Override
			public void setField(int rowIndex, Object value) {
				SMISyntax syn = null;
				if (value instanceof String) {
					syn = SMISyntax.valueOf((String) value);
				} else {
					syn = (SMISyntax) value;
				}

				ScalarBuilder builder = datasource.getBuilder(rowIndex);
				try {
					builder.setValue(syn.toVariable(builder.getValue().toString()));
				} catch (Exception ex) {
					builder.setValue(syn.getDefaultVariable());
				}
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				JComboBox combo = new JComboBox(SMISyntax.values());
				return new DefaultCellEditor(combo);
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition(CONVERT_TABLE_COLUMN_NAME) {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getBuilder(rowIndex).getConvertTable();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				SNMPConvertTable ct = null;

				if (value instanceof String) {
					ct = datasource.getConvertTable((String) value);
				} else {
					ct = (SNMPConvertTable) value;
				}

				ScalarBuilder scalar = ((ScalarBuilder) datasource.getBuilder(rowIndex));
				scalar.setConvertTable(ct);

				if (ct != null) {
					boolean found = false;
					for (Object o : ct.getList()) {
						Entry mv = (Entry) o;
						if (mv.getValue().toString().equals(scalar.getValue().toString())) {
							found = true;
						}
					}
					if (!found) {
						scalar.setValue((Variable) ct.getFirst().getValue());
					}
				}
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				JComboBox combo = new JComboBox();
				combo.addItem(null);
				for (SNMPConvertTable ct : datasource.getConvertTables()) {
					if (ct.getFirst() != null) {
						combo.addItem(ct);
					}
				}
				return new DefaultCellEditor(combo);
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition("Value") {

			@Override
			public Class getColumnClass() {
				return String.class;
			}

			@Override
			public Object getField(int rowIndex) {
				ScalarBuilder scalar = datasource.getBuilder(rowIndex);
				SMISyntax syn = SMISyntax.valueOf(scalar.getValue().getSyntax());
				Object o = null;
				try {
					o = syn.toJava(datasource.getBuilder(rowIndex).getValue());
				} catch (Exception ex) {
					// TODO ajouter message d'erreur
					o = syn.getDefaultJava();
				}
				return o;
			}

			@Override
			public void setField(int rowIndex, Object value) {
				ScalarBuilder scalar = datasource.getBuilder(rowIndex);
				SMISyntax syn = SMISyntax.valueOf(scalar.getValue().getSyntax());
				try {
					scalar.setValue(syn.toVariable(value));
				} catch (Exception ex) {
					// TODO ajouter message d'erreur
				}
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				return new ColumnDefinedVariableCellEditor(CONVERT_TABLE_COLUMN_NAME);
			}

			@Override
			public boolean isEditable() {
				return true;
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
		return SNMPTrapService.class.equals(type);
	}

	/**
	 * Register the data change events.
	 */
	@Override
	public void registerEvents() {
		registerLazyTextChangedEvent(nameTextField);
		registerLazyTextChangedEvent(enterpriseOIDTextField);
		registerLazyTextChangedEvent(addressTextField);
		registerLazyTextChangedEvent(agentAddressTextField);
		registerLazyTextChangedEvent(portTextField);
		registerLazyTextChangedEvent(communityTextField);
		registerLazyTextChangedEvent(intervalTextField);

		table.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				fireDataChangedEvent();
				TableColumnDefinition.adjustColumnWidth(table);
			}
		});
	}

	/**
	 * Data model to view.
	 */
	@Override
	public void displayDataModel() {
		nameTextField.setText(datasource.getName());
		enterpriseOIDTextField.setText(datasource.getEnterpriseOID().toString());
		addressTextField.setText(datasource.getAddress());
		agentAddressTextField.setText(datasource.getSourceAddress());
		portTextField.setText(String.valueOf(datasource.getPort()));
		communityTextField.setText(datasource.getCommunity().toString());
		intervalTextField.setText(String.valueOf(datasource.getInterval()));

		// table
		table.setModel(new ScalarAdapter(columnDefinitions, datasource));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumnDefinition.applyEditors(table, columnDefinitions);
		table.setDefaultRenderer(String.class, new ColumnDefinedConvertTableTableRenderer(TYPE_COLUMN_NAME, CONVERT_TABLE_COLUMN_NAME));
		TableColumnDefinition.adjustColumnWidth(table);
	}

	/**
	 * View to Datamodel.
	 */
	@Override
	public void storeDataModel() {
		datasource.setName(nameTextField.getText());
		datasource.setEnterpriseOID(new OID(enterpriseOIDTextField.getText()));
		datasource.setAddress(addressTextField.getText());
		datasource.setAgentAddress(agentAddressTextField.getText());
		datasource.setPort(Integer.valueOf(portTextField.getText()));
		datasource.setCommunity(new OctetString(communityTextField.getText()));
		datasource.setInterval(Integer.valueOf(intervalTextField.getText()));
	}

	/** Creates new form SNMPServerEditor */
	public SNMPTrapEditor() {
		initComponents();
		tableManager.setTable(table);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        portTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        intervalTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        communityTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        addressTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        enterpriseOIDTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        agentAddressTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(SNMPTrapEditor.class);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        portTextField.setName("portTextField"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        intervalTextField.setName("intervalTextField"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        communityTextField.setName("communityTextField"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        addressTextField.setName("addressTextField"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        nameTextField.setName("nameTextField"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        enterpriseOIDTextField.setName("enterpriseOIDTextField"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        agentAddressTextField.setName("agentAddressTextField"); // NOI18N

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
        table.setName("table"); // NOI18N
        jScrollPane1.setViewportView(table);

        tableManager.setName("tableManager"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(communityTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(intervalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(addressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(enterpriseOIDTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(agentAddressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)))))
                .addContainerGap())
            .addComponent(tableManager, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(enterpriseOIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(addressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(agentAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(communityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intervalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(tableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressTextField;
    private javax.swing.JTextField agentAddressTextField;
    private javax.swing.JTextField communityTextField;
    private javax.swing.JTextField enterpriseOIDTextField;
    private javax.swing.JTextField intervalTextField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField portTextField;
    private javax.swing.JTable table;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager tableManager;
    // End of variables declaration//GEN-END:variables
}
