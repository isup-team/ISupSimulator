/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * HomemadeEditor.java
 *
 * Created on 30 sept. 2011, 14:44:43
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.editors;

import ch.skyguide.pvss.network.service.modbus.BaudRate;
import ch.skyguide.pvss.network.service.modbus.Coil;
import ch.skyguide.pvss.network.service.modbus.DataBits;
import ch.skyguide.pvss.network.service.modbus.Encoding;
import ch.skyguide.pvss.network.service.modbus.Parity;
import ch.skyguide.pvss.network.service.modbus.Register;
import ch.skyguide.pvss.network.service.modbus.StopBits;
import ch.skyguide.pvss.network.service.modbus.Utils;
import ch.skyguide.pvss.network.service.modbus.WagoConvertTable;
import ch.skyguide.pvss.network.service.modbus.WagoService;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.CoilCellRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ListAdapter;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.RegisterCellEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.TableColumnDefinition;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author caronyn
 */
public class WagoEditor extends Editor<WagoService> {

	// constant
	public static final String CONVERT_TABLE_COLUMN_NAME = "Convert table";
	// attributes
	TableColumnDefinition[] coilsColumnDefinitions = {
		new TableColumnDefinition("ID") {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getCoils().get(rowIndex).getId();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				if (value instanceof String) {
					datasource.getCoils().get(rowIndex).setId(Integer.valueOf((String) value));
				} else {
					datasource.getCoils().get(rowIndex).setId((Integer) value);
				}
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition("Name") {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getCoils().get(rowIndex).getName();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				datasource.getCoils().get(rowIndex).setName((String) value);
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition(CONVERT_TABLE_COLUMN_NAME) {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getCoil(rowIndex).getConvertTable();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				WagoConvertTable ct = null;

				if (value instanceof String) {
					ct = ((WagoService) datasource).getConvertTable((String) value);
				} else {
					ct = (WagoConvertTable) value;
				}

				Coil coil = datasource.getCoil(rowIndex);
				coil.setConvertTable(ct);

			}

			@Override
			public TableCellEditor getTableCellEditor() {
				JComboBox combo = new JComboBox();
				combo.addItem(null);
				for (WagoConvertTable ct : datasource.getConvertTables()) {
					if (ct.getFirst() != null && ct.getFirst().getValue() instanceof Boolean) {
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
			public Object getField(int rowIndex) {
				return datasource.getCoils().get(rowIndex).getValue();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				if (value instanceof String) {
					datasource.getCoils().get(rowIndex).setValue(Boolean.valueOf((String) value));
				} else {
					datasource.getCoils().get(rowIndex).setValue((Boolean) value);
				}
			}

			@Override
			public Class getColumnClass() {
				return Boolean.class;
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		}
	};
	TableColumnDefinition[] registrersColumnDefinitions = {
		new TableColumnDefinition("ID") {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getRegisters().get(rowIndex).getId();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				if (value instanceof String) {
					datasource.getRegisters().get(rowIndex).setId(Integer.valueOf((String) value));
				} else {
					datasource.getRegisters().get(rowIndex).setId((Integer) value);
				}
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition("Name") {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getRegisters().get(rowIndex).getName();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				datasource.getRegisters().get(rowIndex).setName((String) value);
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		},
		new TableColumnDefinition(CONVERT_TABLE_COLUMN_NAME) {

			@Override
			public Object getField(int rowIndex) {
				return datasource.getReg(rowIndex).getConvertTable();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				WagoConvertTable ct = null;

				if (value instanceof String) {
					ct = ((WagoService) datasource).getConvertTable((String) value);
				} else {
					ct = (WagoConvertTable) value;
				}

				Register reg = datasource.getReg(rowIndex);
				reg.setConvertTable(ct);
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				JComboBox combo = new JComboBox();
				combo.addItem(null);
				for (WagoConvertTable ct : datasource.getConvertTables()) {
					if (ct.getFirst() != null && ct.getFirst().getValue() instanceof Integer) {
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
			public Object getField(int rowIndex) {
				return datasource.getRegisters().get(rowIndex).getValue();
			}

			@Override
			public void setField(int rowIndex, Object value) {
				if (value instanceof String) {
					datasource.getRegisters().get(rowIndex).setValue(Integer.parseInt((String) value));
				} else {
					datasource.getRegisters().get(rowIndex).setValue((Integer) value);
				}
			}

			RegisterCellEditor editor;

			@Override
			public TableCellRenderer getTableCellRenderer() {
				return new RegisterCellEditor(new JSlider(4000, 20000), CONVERT_TABLE_COLUMN_NAME);
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				return new RegisterCellEditor(new JSlider(4000, 20000), CONVERT_TABLE_COLUMN_NAME);
			}

			@Override
			public boolean isEditable() {
				return true;
			}
		}
	};

	// methode implementation
	@Override
	public boolean canEdit(Class type) {
		return WagoService.class.equals(type);
	}

	@Override
	public void registerEvents() {
		registerLazyTextChangedEvent(nameField);
		registerLazyComboChangedEvent(portCombo);
		registerLazySpinnerChangedEvent(idSpinner);
		registerLazyComboChangedEvent(baudRateCombo);
		registerLazyComboChangedEvent(dataBitsCombo);
		registerLazyComboChangedEvent(parityCombo);
		registerLazyComboChangedEvent(stopBitsCombo);
		registerLazyCheckBoxChangedEvent(echoCheck);
		registerLazyComboChangedEvent(encodingCombo);

		// TODO une seul fois
		coilsTable.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				fireDataChangedEvent();
				coilsTable.repaint();
			}
		});
		registersTable.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				fireDataChangedEvent();
				registersTable.repaint();
			}
		});

	}

	@Override
	public void displayDataModel() {
		nameField.setText(datasource.getName());
		portCombo.setSelectedItem(datasource.getPort());
		idSpinner.setValue(datasource.getId());
		baudRateCombo.setSelectedItem(datasource.getBaudRate().getRate());
		dataBitsCombo.setSelectedItem(datasource.getDataBits().getBit());
		parityCombo.setSelectedItem(datasource.getParity());
		stopBitsCombo.setSelectedItem(datasource.getStopBits().getBit());
		echoCheck.setSelected(datasource.isEcho());
		encodingCombo.setSelectedItem(datasource.getEncoding());

		// table
		coilsTable.setModel(new ListAdapter<Coil>(coilsColumnDefinitions, datasource.getCoils()) {

			@Override
			public Coil getElementInstance() {
				// TODO
				return new Coil(0, "New Coil", Boolean.FALSE);
			}
		});
		TableColumnDefinition.applyEditors(coilsTable, coilsColumnDefinitions);
		((JComponent) coilsTable.getDefaultRenderer(Boolean.class)).setOpaque(true);
		coilsTable.setDefaultRenderer(Boolean.class, new CoilCellRenderer(CONVERT_TABLE_COLUMN_NAME));

		registersTable.setModel(new ListAdapter<Register>(registrersColumnDefinitions, datasource.getRegisters()) {

			@Override
			public Register getElementInstance() {
				return new Register(0, "", 4000);
			}
		});
		TableColumnDefinition.applyEditors(registersTable, registrersColumnDefinitions);
		//registersTable.setDefaultEditor(Register.class, new RegisterCellEditor());
	}

	@Override
	public void storeDataModel() {
		datasource.setName(nameField.getText());
		datasource.setPort(portCombo.getSelectedItem().toString());
		datasource.setId((Integer) idSpinner.getValue());
		datasource.setBaudRate(BaudRate.valueOf((Integer) baudRateCombo.getSelectedItem()));
		datasource.setDataBits(DataBits.valueOf((Integer) dataBitsCombo.getSelectedItem()));
		datasource.setParity((Parity) parityCombo.getSelectedItem());
		datasource.setStopBits(StopBits.valueOf((Integer) stopBitsCombo.getSelectedItem()));
		datasource.setEcho(echoCheck.isSelected());
		datasource.setEncoding((Encoding) encodingCombo.getSelectedItem());
	}

	private void initialize() {
		coilsTableManager.setTable(coilsTable);
		registersTableManager.setTable(registersTable);

		// port combo
		portCombo.setModel(new DefaultComboBoxModel(Utils.getComPorts()));

		// id
		idSpinner.setModel(new SpinnerNumberModel(1, 1, 99, 1));

		// baud rate
		baudRateCombo.setModel(new DefaultComboBoxModel(BaudRate.rateValues()));

		// data bits
		dataBitsCombo.setModel(new DefaultComboBoxModel(DataBits.bitsValues()));

		// parity
		parityCombo.setModel(new DefaultComboBoxModel(Parity.values()));

		// stop bits
		stopBitsCombo.setModel(new DefaultComboBoxModel(StopBits.bitsValues()));

		// encoding
		encodingCombo.setModel(new DefaultComboBoxModel(Encoding.values()));
	}

	/** Creates new form HomemadeEditor */
	public WagoEditor() {
		initComponents();
		initialize();
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
        nameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        coilsTable = new javax.swing.JTable();
        coilsTableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        registersTable = new javax.swing.JTable();
        registersTableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();
        jLabel5 = new javax.swing.JLabel();
        portCombo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        parityCombo = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        echoCheck = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        encodingCombo = new javax.swing.JComboBox();
        idSpinner = new javax.swing.JSpinner();
        dataBitsCombo = new javax.swing.JComboBox();
        stopBitsCombo = new javax.swing.JComboBox();
        baudRateCombo = new javax.swing.JComboBox();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(WagoEditor.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        nameField.setName("nameField"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane3.setAutoscrolls(true);
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        coilsTable.setModel(new javax.swing.table.DefaultTableModel(
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
        coilsTable.setName("coilsTable"); // NOI18N
        jScrollPane3.setViewportView(coilsTable);

        coilsTableManager.setName("coilsTableManager"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
            .addComponent(coilsTableManager, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(coilsTableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane4.setAutoscrolls(true);
        jScrollPane4.setName("jScrollPane4"); // NOI18N

        registersTable.setModel(new javax.swing.table.DefaultTableModel(
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
        registersTable.setName("registersTable"); // NOI18N
        jScrollPane4.setViewportView(registersTable);

        registersTableManager.setName("registersTableManager"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(registersTableManager, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 753, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registersTableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        portCombo.setActionCommand(resourceMap.getString("portCombo.actionCommand")); // NOI18N
        portCombo.setName("portCombo"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        parityCombo.setName("parityCombo"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        echoCheck.setMinimumSize(new java.awt.Dimension(29, 21));
        echoCheck.setName("echoCheck"); // NOI18N
        echoCheck.setPreferredSize(new java.awt.Dimension(21, 22));

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        encodingCombo.setName("encodingCombo"); // NOI18N

        idSpinner.setName("idSpinner"); // NOI18N

        dataBitsCombo.setName("dataBitsCombo"); // NOI18N

        stopBitsCombo.setName("stopBitsCombo"); // NOI18N

        baudRateCombo.setName("baudRateCombo"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(portCombo, 0, 175, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dataBitsCombo, 0, 101, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(parityCombo, 0, 90, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stopBitsCombo, 0, 94, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(echoCheck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel10))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(encodingCombo, 0, 112, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(baudRateCombo, 0, 98, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(portCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(idSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(baudRateCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(encodingCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(echoCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox baudRateCombo;
    private javax.swing.JTable coilsTable;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager coilsTableManager;
    private javax.swing.JComboBox dataBitsCombo;
    private javax.swing.JCheckBox echoCheck;
    private javax.swing.JComboBox encodingCombo;
    private javax.swing.JSpinner idSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JComboBox parityCombo;
    private javax.swing.JComboBox portCombo;
    private javax.swing.JTable registersTable;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager registersTableManager;
    private javax.swing.JComboBox stopBitsCombo;
    // End of variables declaration//GEN-END:variables
}
