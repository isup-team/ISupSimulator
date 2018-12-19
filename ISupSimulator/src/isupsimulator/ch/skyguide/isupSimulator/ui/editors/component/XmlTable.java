/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * XmlTable.java
 *
 * Created on 27 sept. 2011, 07:01:15
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.editors.component;

import ch.skyguide.common.event.EventDispatcher;
import ch.skyguide.pvss.network.service.convertTable.SystemStatus;
import ch.skyguide.pvss.network.service.xml.ISupSubSystem;
import ch.skyguide.pvss.network.service.xml.ISupSubSystem.Mode;
import ch.skyguide.pvss.network.service.xml.ISupSystem;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.jComboBox.SystemStatusComboRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ListAdapter;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.SystemStatusTableRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.TableColumnDefinition;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author caronyn
 */
public class XmlTable extends javax.swing.JPanel {

    // attributes
    ISupSystem datasource;
    protected EventDispatcher<DataListener<ISupSystem>> dataChangedDispatcher = new EventDispatcher<DataListener<ISupSystem>>();
    TableColumnDefinition[] columnDefinitions = {
        new TableColumnDefinition("mode") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getSubSystem(rowIndex).getMode();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                String v = String.valueOf(value);
                if (value == null) {
                    v = Mode.R.name();
                }
                datasource.getSubSystem(rowIndex).setMode(Mode.valueOf(v));
            }

            @Override
            public boolean isEditable() {
                return true;
            }

            @Override
            public TableCellEditor getTableCellEditor() {
                JComboBox combo = new JComboBox(Mode.values());
                return new DefaultCellEditor(combo);
            }

            @Override
            public Class getColumnClass() {
                return Mode.class;
            }
        },
        new TableColumnDefinition("Name") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getSubSystem(rowIndex).getName();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getSubSystem(rowIndex).setName(String.valueOf(value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("DP Name") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getSubSystem(rowIndex).getDpName();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getSubSystem(rowIndex).setDpName(String.valueOf(value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("Status") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getSubSystem(rowIndex).getStatus().toString();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getSubSystem(rowIndex).setStatus(SystemStatus.valueOf(String.valueOf(value)));
            }

            @Override
            public TableCellEditor getTableCellEditor() {
                JComboBox combo = new JComboBox(SystemStatus.values());
                combo.setRenderer(new SystemStatusComboRenderer());
                return new DefaultCellEditor(combo);
            }

            @Override
            public Class getColumnClass() {
                return SystemStatus.class;
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("Label 1") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getSubSystem(rowIndex).getLabel1();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getSubSystem(rowIndex).setLabel1(String.valueOf(value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("Label 2") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getSubSystem(rowIndex).getLabel2();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getSubSystem(rowIndex).setLabel2(String.valueOf(value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("Label 3") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getSubSystem(rowIndex).getLabel3();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getSubSystem(rowIndex).setLabel3(String.valueOf(value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },
        new TableColumnDefinition("Message") {

            @Override
            public Object getField(int rowIndex) {
                return datasource.getSubSystem(rowIndex).getMessage();
            }

            @Override
            public void setField(int rowIndex, Object value) {
                datasource.getSubSystem(rowIndex).setMessage(String.valueOf(value));
            }

            @Override
            public boolean isEditable() {
                return true;
            }
        },};

    // event
    public void AddDataChangedListener(DataListener<ISupSystem> listener) {
        dataChangedDispatcher.addListener(listener);
    }

    public void fireDataChangedEvent() {
        dataChangedDispatcher.invoke("changed", new DataMVPEvent<ISupSystem>(this, datasource));
    }

    // attributes
    public ISupSystem getDatasource() {
        return datasource;
    }

    public void setDatasource(ISupSystem datasource) {
        this.datasource = datasource;
    }

    // methode implementation
    public void registerEvents() {
        // TODO une seul fois
        table.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                fireDataChangedEvent();
                TableColumnDefinition.adjustColumnWidth(table);
            }
        });
    }

    public void displayDataModel() {
        // table
        table.setModel(new ListAdapter(columnDefinitions, datasource.getSubSystems()) {

            @Override
            public Object getElementInstance() {
                return new ISupSubSystem("", SystemStatus.OPS);
            }
        });
        table.setDefaultRenderer(SystemStatus.class, new SystemStatusTableRenderer());
        TableColumnDefinition.applyEditors(table, columnDefinitions);
        TableColumnDefinition.adjustColumnWidth(table);
    }

    /** Creates new form XmlTable */
    public XmlTable() {
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

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();

        setName("Form"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

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
        table.setSurrendersFocusOnKeystroke(true);
        table.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))
        );

        tableManager.setName("tableManager"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tableManager, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager tableManager;
    // End of variables declaration//GEN-END:variables
}
