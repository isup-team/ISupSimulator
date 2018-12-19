/*
 * SNMPMibEditor.java
 *
 * Created on 10 déc. 2010, 10:22:43
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.editors;

import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import ch.skyguide.pvss.network.service.convertTable.Entry;
import ch.skyguide.pvss.network.service.snmp.SNMPConvertTable;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ColumnDefinedConvertTableTableRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ColumnDefinedVariableCellEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.TableColumnDefinition;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ScalarAdapter;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

/**
 *
 * @author caronyn
 */
public final class SNMPMibEditor extends Editor<SNMPMIB> {

    public static final String TYPE_COLUMN_NAME = "Type";
    public static final String CONVERT_TABLE_COLUMN_NAME = "Convert table";
    // attributes
    TableColumnDefinition[] columnDefinitions = {
        new TableColumnDefinition("Name") {

            @Override
            public Object getField(int rowIndex) {
                return ((ScalarBuilder) datasource.getBuilder(rowIndex)).getName();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                ((ScalarBuilder) datasource.getBuilder(rowIndex)).setName((String) value);
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("DP Name") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getBuilder(rowIndex).getDpName();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getBuilder(rowIndex).setDpName(String.valueOf(value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("OID") {

            @Override
            public Object getField(int rowIndex) {
                return ((ScalarBuilder) datasource.getBuilder(rowIndex)).getOID();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                ((ScalarBuilder) datasource.getBuilder(rowIndex)).setOID(new OID((String) value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("Access") {

            @Override
            public Object getField(int rowIndex) {
                return ((ScalarBuilder) datasource.getBuilder(rowIndex)).getAccess();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                SMIAccess access = null;

                if (value instanceof String) {
                    access = SMIAccess.valueOf((String) value);
                } else {
                    access = (SMIAccess) value;
                }

                ((ScalarBuilder) datasource.getBuilder(rowIndex)).setAccess(access);
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
                ScalarBuilder builder = ((ScalarBuilder) datasource.getBuilder(rowIndex));
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
                SMISyntax syntax = null;

                // get syntax
                if (value instanceof String) {
                    syntax = SMISyntax.valueOf((String) value);
                } else {
                    syntax = (SMISyntax) value;
                }

                // get builder
                ScalarBuilder builder = ((ScalarBuilder) datasource.getBuilder(rowIndex));

                // reset convert table
                builder.setConvertTable(null);

                // try to convert type
                try {
                    builder.setValue(syntax.toVariable(builder.getValue().toString()));
                } catch (Exception ex) {
                    builder.setValue(syntax.getDefaultVariable());
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
                return ((ScalarBuilder) datasource.getBuilder(rowIndex)).getConvertTable();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                SNMPConvertTable convertTable = null;

                // get convert table
                if (value instanceof String) {
                    convertTable = ((SNMPMIB) datasource).getConvertTable((String) value);
                } else {
                    convertTable = (SNMPConvertTable) value;
                }

                ScalarBuilder scalar = ((ScalarBuilder) datasource.getBuilder(rowIndex));
                scalar.setConvertTable(convertTable);

                if (convertTable != null) {
                    boolean found = false;
                    for (Object o : convertTable.getList()) {
                        Entry mv = (Entry) o;
                        if (mv.getValue().toString().equals(scalar.getValue().toString())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        scalar.setValue((Variable) convertTable.getFirst().getValue());
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
                ScalarBuilder scalar = ((ScalarBuilder) datasource.getBuilder(rowIndex));
                SMISyntax syn = SMISyntax.valueOf(scalar.getValue().getSyntax());
                Object o = null;
                try {
                    o = syn.toJava(((ScalarBuilder) datasource.getBuilder(rowIndex)).getValue());
                } catch (Exception ex) {
                    // TODO ajouter message d'erreur
                    o = syn.getDefaultJava();
                }
                return o;
            }

            @Override
            public void setField(int rowIndex, Object value) {
                ScalarBuilder scalar = ((ScalarBuilder) datasource.getBuilder(rowIndex));
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
    @Override
    public void setDataModel(SNMPMIB datasource) {
        super.setDataModel(datasource);
        dpManager.setDatasource(datasource);
    }

    /**
     * Return if this is the appropriate editor.
     * @param type Type accepted.
     * @return Return true if it can edit the type.
     */
    @Override
    public boolean canEdit(Class type) {
        return SNMPMIB.class.equals(type);
    }

    /**
     * Register the data change events.
     */
    @Override
    public void registerEvents() {
        dpManager.registerEvents(this);

        registerLazyTextChangedEvent(nameTextField);
        registerLazyTextChangedEvent(oidPrefixTextField);

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
        dpManager.displayDataModel();

        // TODO ajouter les communities
        nameTextField.setText(datasource.getName());
        oidPrefixTextField.setText(datasource.getPrefixOID().toString());

        // table
        table.setModel(new ScalarAdapter(columnDefinitions, datasource));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumnDefinition.applyEditors(table, columnDefinitions);
        table.setDefaultRenderer(String.class, new ColumnDefinedConvertTableTableRenderer(TYPE_COLUMN_NAME, CONVERT_TABLE_COLUMN_NAME));
        TableColumnDefinition.adjustColumnWidth(table);
    }

    /**
     * From view to data model.
     */
    public void storeDataModel() {
        dpManager.storeDataModel();

        datasource.setName(nameTextField.getText());
        datasource.setPrefixOID(new OID(oidPrefixTextField.getText()));
    }

    // constructor
    /** Creates new form SNMPMibEditor */
    public SNMPMibEditor() {
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

        jLabel1 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        oidPrefixTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();
        dpManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.DPManager();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(SNMPMibEditor.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        nameTextField.setText(resourceMap.getString("nameTextField.text")); // NOI18N
        nameTextField.setName("nameTextField"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        oidPrefixTextField.setText(resourceMap.getString("oidPrefixTextField.text")); // NOI18N
        oidPrefixTextField.setName("oidPrefixTextField"); // NOI18N

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
        table.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("table.columnModel.title0")); // NOI18N
        table.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("table.columnModel.title1")); // NOI18N
        table.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("table.columnModel.title2")); // NOI18N
        table.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("table.columnModel.title3")); // NOI18N

        tableManager.setName("tableManager"); // NOI18N

        dpManager.setName("dpManager"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableManager, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
            .addComponent(dpManager, javax.swing.GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 827, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(oidPrefixTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(oidPrefixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(dpManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.DPManager dpManager;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField oidPrefixTextField;
    private javax.swing.JTable table;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager tableManager;
    // End of variables declaration//GEN-END:variables
}
