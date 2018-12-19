/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SNMPMibEditor.java
 *
 * Created on 10 déc. 2010, 10:22:43
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.editors;

import ch.skyguide.pvss.network.service.convertTable.ConvertTable;
import ch.skyguide.pvss.network.service.convertTable.EnumConverter;
import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import ch.skyguide.pvss.network.service.snmp.SNMPConvertTable;
import ch.skyguide.pvss.network.service.snmp.builder.ColumnBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.RowBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.TableBuilder;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ConvertTableTableRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ListAdapter;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.TableAdapter;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.TableColumnDefinition;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.VariableCellEditor;
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
public final class SNMPTableEditor extends Editor<TableBuilder> {

    public static final String CONVERT_TABLE_COLUMN_NAME = "Convert table";
    // attributes
    TableColumnDefinition[] columnDefinitions = {
        new TableColumnDefinition("Index") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getColumns().get(rowIndex).getIndex();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getColumns().get(rowIndex).setIndex(Integer.valueOf((String) value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("Name") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getColumns().get(rowIndex).getName();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getColumns().get(rowIndex).setName(String.valueOf(value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("Access") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getColumns().get(rowIndex).getAccess();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                SMIAccess access = null;

                if (value instanceof String) {
                    access = SMIAccess.valueOf((String) value);
                } else {
                    access = (SMIAccess) value;
                }

                datasource.getColumns().get(rowIndex).setAccess(access);
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
        new TableColumnDefinition("Type") {

            @Override
            public Object getField(int rowIndex) {
                SNMPConvertTable ct = datasource.getColumns().get(rowIndex).getConvertTable();

                if (ct != null && ct.getFirst() != null) {
                    SMISyntax syn = SMISyntax.valueOf(((Variable) ct.getFirst().getValue()).getSyntax());
                    return syn;
                } else {
                    return datasource.getColumns().get(rowIndex).getSyntax();
                }
            }

            @Override
            public void setField(int rowIndex, Object value) {
                SMISyntax syntax = null;
                if (value instanceof String) {
                    syntax = SMISyntax.valueOf((String) value);
                } else {
                    syntax = (SMISyntax) value;
                }

                datasource.getColumns().get(rowIndex).setSyntax(syntax);
            }

            @Override
            public boolean isEditable() {
                return true;
            }

            @Override
            public TableCellEditor getTableCellEditor() {
                JComboBox combo = new JComboBox(SMISyntax.values());
                return new DefaultCellEditor(combo);
            }
        },
        new TableColumnDefinition(CONVERT_TABLE_COLUMN_NAME) {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getColumns().get(rowIndex).getConvertTable();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                SNMPConvertTable convertTable = null;

                if (value instanceof String) {
                    convertTable = datasource.getParentMib().getConvertTable(String.valueOf(value));
                } else {
                    convertTable = (SNMPConvertTable) value;
                }

                datasource.getColumns().get(rowIndex).setConvertTable(convertTable);

                if (convertTable != null && convertTable.getFirst() != null) {
                    SMISyntax syn = SMISyntax.valueOf(((Variable) convertTable.getFirst().getValue()).getSyntax());
                    datasource.getColumns().get(rowIndex).setSyntax(syn);
                }
            }

            @Override
            public TableCellEditor getTableCellEditor() {
                JComboBox combo = new JComboBox();
                combo.addItem(null);
                for (Object o : datasource.getParentMib().getConvertTables()) {
                    ConvertTable ct = (ConvertTable) o;
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
        }
    };

    // function
    private void computeTableIntegrity(TableModelEvent e) {
        int rowIndex = e.getFirstRow();

        for (RowBuilder builder : datasource.getRows()) {

            switch (e.getType()) {
                case TableModelEvent.DELETE: {
                    builder.removeValue(rowIndex);
                    break;
                }
                case TableModelEvent.INSERT: {
                    SMISyntax syn = datasource.getColumns().get(rowIndex).getSyntax();
                    builder.addValue(rowIndex, syn.getDefaultVariable());
                    break;
                }
                case TableModelEvent.UPDATE: {
                    SMISyntax syn = datasource.getColumns().get(rowIndex).getSyntax();
                    try {
                        SMISyntax origSyn = SMISyntax.valueOf(builder.getValues().get(rowIndex).getSyntax());
                        builder.getValues().set(
                                rowIndex,
                                syn.toVariable(origSyn.toJava(builder.getValues().get(rowIndex))));

                    } catch (Exception ex) {
                        builder.getValues().set(rowIndex, syn.getDefaultVariable());
                    }
                    break;
                }
            }
        }

        ((TableAdapter) table.getModel()).fireTableStructureChanged();
    }

    // methode implementation
    /**
     * Return if this is the appropriate editor.
     * @param type Type accepted.
     * @return Return true if it can edit the type.
     */
    @Override
    public boolean canEdit(Class type) {
        return TableBuilder.class.equals(type);
    }

    /**
     * Register the data change events.
     */
    @Override
    public void registerEvents() {
        registerLazyTextChangedEvent(nameTextField);
        registerLazyTextChangedEvent(enterpriseOIDTextField);

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
        enterpriseOIDTextField.setText(datasource.getOID().toString());

        columnTable.setModel(new ListAdapter(columnDefinitions, datasource.getColumns()) {

            @Override
            public Object getElementInstance() {
                return new ColumnBuilder("Column", 0, SMIAccess.READ_ONLY, SMISyntax.NULL);
            }
        });
        columnTable.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                computeTableIntegrity(e);
            }
        });
        TableColumnDefinition.applyEditors(columnTable, columnDefinitions);
        TableColumnDefinition.adjustColumnWidth(columnTable);

        table.setModel(new TableAdapter(datasource));
        table.setDefaultEditor(String.class, new VariableCellEditor() {

            @Override
            public SNMPConvertTable getConvertTable(JTable table, int row, int col) {
                int definitionCol = columnTable.getColumn(CONVERT_TABLE_COLUMN_NAME).getModelIndex();

                if (col == 0) {
                    return null;
                } else {
                    return (SNMPConvertTable) columnTable.getValueAt(col - 1, definitionCol);
                }
            }
        });
        table.setDefaultRenderer(String.class, new ConvertTableTableRenderer() {

            @Override
            protected SNMPConvertTable getConvertTable(JTable table, int row, int column) {
                return datasource.getColumns().get(column - 1).getConvertTable();
            }

            @Override
            protected EnumConverter getEnumConverter(JTable table, int row, int column) {
                return datasource.getColumns().get(column - 1).getSyntax();
            }

            @Override
            protected void setConvertTable(JTable table, int row, int column, ConvertTable convertTable) {
                datasource.getColumns().get(column - 1).setConvertTable((SNMPConvertTable) convertTable);
            }
        });
        TableColumnDefinition.adjustColumnWidth(table);

    }

    /**
     * View to Datamodel.
     */
    @Override
    public void storeDataModel() {
        datasource.setName(nameTextField.getText());
        datasource.setOID(new OID(enterpriseOIDTextField.getText()));
    }

    /** Creates new form SNMPMibEditor */
    public SNMPTableEditor() {
        initComponents();
        columnTableManager.setTable(columnTable);
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
        enterpriseOIDTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        columnTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        columnTableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();
        tableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(SNMPTableEditor.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        nameTextField.setText(resourceMap.getString("nameTextField.text")); // NOI18N
        nameTextField.setName("nameTextField"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        enterpriseOIDTextField.setText(resourceMap.getString("enterpriseOIDTextField.text")); // NOI18N
        enterpriseOIDTextField.setName("enterpriseOIDTextField"); // NOI18N

        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        columnTable.setModel(new javax.swing.table.DefaultTableModel(
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
        columnTable.setName("columnTable"); // NOI18N
        jScrollPane1.setViewportView(columnTable);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

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
        jScrollPane2.setViewportView(table);

        columnTableManager.setName("columnTableManager"); // NOI18N

        tableManager.setName("tableManager"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(enterpriseOIDTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                    .addComponent(columnTableManager, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tableManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(enterpriseOIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(columnTableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(tableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable columnTable;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager columnTableManager;
    private javax.swing.JTextField enterpriseOIDTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTable table;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager tableManager;
    // End of variables declaration//GEN-END:variables
}
