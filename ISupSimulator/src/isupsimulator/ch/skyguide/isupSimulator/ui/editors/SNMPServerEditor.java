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

import ch.skyguide.pvss.network.service.snmp.SNMPAgentService;
import ch.skyguide.pvss.network.service.snmp.SNMPAgentService.Version;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import org.snmp4j.smi.OctetString;

/**
 *
 * @author caronyn
 */
public final class SNMPServerEditor extends Editor<SNMPAgentService> {

    // methode implementation
    /**
     * Return if this is the appropriate editor.
     * @param type Type accepted.
     * @return Return true if it can edit the type.
     */
    @Override
    public boolean canEdit(Class type) {
        return SNMPAgentService.class.equals(type);
    }

    /**
     * Register the data change events.
     */
    @Override
    public void registerEvents() {
        registerLazyTextChangedEvent(nameTextField);
        registerLazyTextChangedEvent(portTextField);
        registerLazyTextChangedEvent(contextTextField);
        registerLazyComboChangedEvent(versionCombobox);

        // SNMP v3
        registerLazyTextChangedEvent(usmTextField);
        registerLazyComboChangedEvent(levelCombobox);
        registerLazyComboChangedEvent(authAlgo);
        registerLazyTextChangedEvent(authTextField);
        registerLazyComboChangedEvent(privAlgo);
        registerLazyTextChangedEvent(privTextField);
    }

    /**
     * Data model to view.
     */
    @Override
    public void displayDataModel() {
        nameTextField.setText(datasource.getName());
        portTextField.setText(String.valueOf(datasource.getPort()));
        versionCombobox.setSelectedItem(datasource.getVersion());
        contextTextField.setText(getNullableToString(datasource.getContext()));

        // SNMP v3
        usmTextField.setText(getNullableToString(datasource.getUsmName()));
        levelCombobox.setSelectedItem(datasource.getLevel());
        authAlgo.setSelectedItem(datasource.getAuthAlgo());
        authTextField.setText(getNullableToString(datasource.getAuthPassword()));
        privAlgo.setSelectedItem(datasource.getPrivAlgo());
        privTextField.setText(getNullableToString(datasource.getPrivPassword()));

        if (levelCombobox.getSelectedIndex() == -1) {
            levelCombobox.setSelectedIndex(1);
        }
        if (authAlgo.getSelectedIndex() == -1) {
            authAlgo.setSelectedIndex(1);
        }
        if (privAlgo.getSelectedIndex() == -1) {
            privAlgo.setSelectedIndex(1);
        }

    }

    /**
     * View to Datamodel.
     */
    @Override
    public void storeDataModel() {
        SNMPAgentService.Version version = SNMPAgentService.Version.valueOf(versionCombobox.getSelectedItem().toString());

        datasource.setName(nameTextField.getText());
        datasource.setPort(Integer.valueOf(portTextField.getText()));
        datasource.setVersion(version);
        datasource.setContext(new OctetString(contextTextField.getText()));

        // SNMP v3
        if (version == SNMPAgentService.Version.v3) {
            datasource.setUsmName(new OctetString(usmTextField.getText()));
            datasource.setLevel(SNMPAgentService.SecurityLevel.valueOf(levelCombobox.getSelectedItem().toString()));
            datasource.setAuthAlgo(SNMPAgentService.AuthAlgorithm.valueOf(authAlgo.getSelectedItem().toString()));
            datasource.setAuthPassword(new OctetString(authTextField.getText()));
            datasource.setPrivAlgo(SNMPAgentService.PrivAlgorithm.valueOf(privAlgo.getSelectedItem().toString()));
            datasource.setPrivPassword(new OctetString(privTextField.getText()));
        } else {
            datasource.setUsmName(null);
            datasource.setLevel(null);
            datasource.setAuthAlgo(null);
            datasource.setAuthPassword(null);
            datasource.setPrivAlgo(null);
            datasource.setPrivPassword(null);
        }

    }

    // constructor
    /** Creates new form SNMPServerEditor */
    public SNMPServerEditor() {
        initComponents();
        versionCombobox.addItem(SNMPAgentService.Version.v1);
        versionCombobox.addItem(SNMPAgentService.Version.v2c);
        versionCombobox.addItem(SNMPAgentService.Version.v3);

        levelCombobox.addItem(SNMPAgentService.SecurityLevel.noAuth_noPriv);
        levelCombobox.addItem(SNMPAgentService.SecurityLevel.auth_noPriv);
        levelCombobox.addItem(SNMPAgentService.SecurityLevel.auth_priv);

        authAlgo.addItem(SNMPAgentService.AuthAlgorithm.SHA);
        authAlgo.addItem(SNMPAgentService.AuthAlgorithm.MD5);

        privAlgo.addItem(SNMPAgentService.PrivAlgorithm.AES128);
        privAlgo.addItem(SNMPAgentService.PrivAlgorithm.AES192);
        privAlgo.addItem(SNMPAgentService.PrivAlgorithm.AES256);
        privAlgo.addItem(SNMPAgentService.PrivAlgorithm.DES);
        privAlgo.addItem(SNMPAgentService.PrivAlgorithm.DES3);

        versionCombobox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                versionCombobox_onChanged(e);
            }
        });

        levelCombobox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                levelCombobox_onChanged(e);
            }
        });
    }

    private static String getNullableToString(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }

    private static <T> T cast(Object object, Class<T> cls) {
        if (object != null && object.getClass().isAssignableFrom(cls)) {
            return (T) object;
        } else {
            throw new ClassCastException(String.format("Object of type %s cannot be converted to %s !", object.getClass(), cls));
        }
    }

    private void versionCombobox_onChanged(ActionEvent e) {

        JComboBox combo = cast(e.getSource(), JComboBox.class);
        Version version = cast(combo.getSelectedItem(), Version.class);

        if (version == Version.v3) {
            contextLabel.setVisible(false);
            contextTextField.setVisible(false);
            usmPanel.setVisible(true);
        } else {
            contextLabel.setVisible(true);
            contextTextField.setVisible(true);
            usmPanel.setVisible(false);
        }

    }

    private void levelCombobox_onChanged(ActionEvent e) {
        JComboBox combo = cast(e.getSource(), JComboBox.class);

        SNMPAgentService.SecurityLevel level;
        if (combo.getSelectedItem() == null) {
            level = SNMPAgentService.SecurityLevel.noAuth_noPriv;
        } else {
            level = cast(combo.getSelectedItem(), SNMPAgentService.SecurityLevel.class);
        }

        authAlgo.setEnabled(true);
        authTextField.setEnabled(true);
        privAlgo.setEnabled(true);
        privTextField.setEnabled(true);

        if (level == SNMPAgentService.SecurityLevel.noAuth_noPriv || level == SNMPAgentService.SecurityLevel.auth_noPriv) {
            privAlgo.setEnabled(false);
            privTextField.setEnabled(false);

            privAlgo.setSelectedIndex(0);
            privTextField.setText("");
        }

        if (level == SNMPAgentService.SecurityLevel.noAuth_noPriv) {
            authAlgo.setEnabled(false);
            authTextField.setEnabled(false);

            authAlgo.setSelectedIndex(0);
            authTextField.setText("");
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
        portTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        contextTextField = new javax.swing.JTextField();
        versionCombobox = new javax.swing.JComboBox();
        usmPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        usmTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        levelCombobox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        authAlgo = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        authTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        privAlgo = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        privTextField = new javax.swing.JTextField();
        contextLabel = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(SNMPServerEditor.class);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        portTextField.setName("portTextField"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        nameTextField.setName("nameTextField"); // NOI18N

        contextTextField.setName("contextTextField"); // NOI18N

        versionCombobox.setName("versionCombobox"); // NOI18N

        usmPanel.setName("usmPanel"); // NOI18N
        usmPanel.setLayout(new java.awt.GridLayout(6, 2, 5, 20));

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N
        usmPanel.add(jLabel10);

        usmTextField.setName("usmTextField"); // NOI18N
        usmPanel.add(usmTextField);

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        usmPanel.add(jLabel5);

        levelCombobox.setName("levelCombobox"); // NOI18N
        usmPanel.add(levelCombobox);

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        usmPanel.add(jLabel6);

        authAlgo.setName("authAlgo"); // NOI18N
        usmPanel.add(authAlgo);

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N
        usmPanel.add(jLabel8);

        authTextField.setName("authTextField"); // NOI18N
        usmPanel.add(authTextField);

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N
        usmPanel.add(jLabel7);

        privAlgo.setName("privAlgo"); // NOI18N
        usmPanel.add(privAlgo);

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N
        usmPanel.add(jLabel9);

        privTextField.setName("privTextField"); // NOI18N
        usmPanel.add(privTextField);

        contextLabel.setText(resourceMap.getString("contextLabel.text")); // NOI18N
        contextLabel.setName("contextLabel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usmPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(contextLabel))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(contextTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(portTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                            .addComponent(versionCombobox, 0, 63, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contextTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(versionCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(contextLabel))
                .addGap(18, 18, 18)
                .addComponent(usmPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox authAlgo;
    private javax.swing.JTextField authTextField;
    private javax.swing.JLabel contextLabel;
    private javax.swing.JTextField contextTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox levelCombobox;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField portTextField;
    private javax.swing.JComboBox privAlgo;
    private javax.swing.JTextField privTextField;
    private javax.swing.JPanel usmPanel;
    private javax.swing.JTextField usmTextField;
    private javax.swing.JComboBox versionCombobox;
    // End of variables declaration//GEN-END:variables
}
