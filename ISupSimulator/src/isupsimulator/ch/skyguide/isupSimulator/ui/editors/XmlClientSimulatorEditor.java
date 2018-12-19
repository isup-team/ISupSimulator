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

import ch.skyguide.pvss.network.service.xml.ISupSystem;
import ch.skyguide.pvss.network.service.xml.XmlClient;
import ch.skyguide.pvss.network.service.socket.MessageDelimiter;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataListener;

/**
 *
 * @author caronyn
 */
public final class XmlClientSimulatorEditor extends Editor<XmlClient> {

    @Override
    public void setDataModel(XmlClient datasource) {
        super.setDataModel(datasource);
        dpManager.setDatasource(datasource);
    }

    // methode implementation
    /**
     * Return if this is the appropriate editor.
     * @param type Type accepted.
     * @return Return true if it can edit the type.
     */
    @Override
    public boolean canEdit(Class type) {
        return XmlClient.class.equals(type);
    }

    /**
     * Register the data change events.
     */
    @Override
    public void registerEvents() {
        dpManager.registerEvents(this);

        xmlTable.registerEvents();
        xmlTable.AddDataChangedListener(new DataListener<ISupSystem>() {

            public void changed(DataMVPEvent<ISupSystem> event) {
                fireDataChangedEvent();
            }
        });

        registerLazyTextChangedEvent(nameField);
        registerLazyTextChangedEvent(systemNameField);
        registerLazyTextChangedEvent(sourceField);
        registerLazyTextChangedEvent(periodField);
        registerLazyComboChangedEvent(delimiterCombo);
    }

    /**
     * Data model to view.
     */
    @Override
    public void displayDataModel() {
        dpManager.displayDataModel();

        xmlTable.setDatasource(datasource.getSystem());
        xmlTable.displayDataModel();

        nameField.setText(datasource.getName());
        systemNameField.setText(datasource.getSystem().getName());
        sourceField.setText(datasource.getSystem().getSource());
        periodField.setText(String.valueOf(datasource.getPeriod()));
        delimiterCombo.setSelectedItem(datasource.getDelimiter());
    }

    /**
     * View to Datamodel.
     */
    @Override
    public void storeDataModel() {
        dpManager.storeDataModel();

        datasource.setName(nameField.getText());
        datasource.getSystem().setName(systemNameField.getText());
        datasource.getSystem().setSource(sourceField.getText());
        datasource.setPeriod(Integer.valueOf(periodField.getText()));
        datasource.setDelimiter((MessageDelimiter) delimiterCombo.getSelectedItem());
    }

    // constructor
    /** Creates new form SNMPServerEditor */
    public XmlClientSimulatorEditor() {
        initComponents();
                
        for (MessageDelimiter delimiter : MessageDelimiter.values()) {
            delimiterCombo.addItem(delimiter);
        }
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
        sourceField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        periodField = new javax.swing.JTextField();
        xmlTable = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.XmlTable();
        jLabel8 = new javax.swing.JLabel();
        delimiterCombo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        systemNameField = new javax.swing.JTextField();
        dpManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.DPManager();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(XmlClientSimulatorEditor.class);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        sourceField.setName("sourceField"); // NOI18N

        jLabel3.setName("jLabel3"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        nameField.setName("nameField"); // NOI18N

        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        periodField.setName("periodField"); // NOI18N

        xmlTable.setName("xmlTable"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        delimiterCombo.setName("delimiterCombo"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        systemNameField.setName("systemNameField"); // NOI18N

        dpManager.setName("dpManager"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7))
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(periodField, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(systemNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourceField, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                    .addComponent(delimiterCombo, 0, 195, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(161, 161, 161)))
                .addContainerGap())
            .addComponent(dpManager, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
            .addComponent(xmlTable, javax.swing.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))
                            .addComponent(sourceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(systemNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delimiterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(periodField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addComponent(dpManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(xmlTable, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox delimiterCombo;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.DPManager dpManager;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField periodField;
    private javax.swing.JTextField sourceField;
    private javax.swing.JTextField systemNameField;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.XmlTable xmlTable;
    // End of variables declaration//GEN-END:variables
}