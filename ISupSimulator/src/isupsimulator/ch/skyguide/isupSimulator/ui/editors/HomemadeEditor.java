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

import ch.skyguide.pvss.network.service.homemade.HMStatus;
import ch.skyguide.pvss.network.service.homemade.HMStatusEnum;
import ch.skyguide.pvss.network.service.homemade.HMSubSystem;
import ch.skyguide.pvss.network.service.homemade.HMTypeEnum;
import ch.skyguide.pvss.network.service.homemade.HMVariant;
import ch.skyguide.pvss.network.service.homemade.HomemadeService;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.HMStatusTableRenderer;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.HMVariableCellEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.ListAdapter;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtable.TableColumnDefinition;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author caronyn
 */
public class HomemadeEditor extends Editor<HomemadeService> {

	// attributes
	TableColumnDefinition[] columnDefinitions = {
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
		new TableColumnDefinition("Type") {

			@Override
			public Object getField(int rowIndex) {
				HMVariant variant = datasource.getSubSystem(rowIndex).getValue();
				HMTypeEnum type = variant.getHMType();
				return type;
			}

			@Override
			public void setField(int rowIndex, Object value) {
				if (value instanceof String) {
					value = HMTypeEnum.valueOf((String) value);
				}

				HMTypeEnum type = (HMTypeEnum) value;
				HMSubSystem subSystem = datasource.getSubSystem(rowIndex);
				try {
					subSystem.setValue(type.toVariant(value));
				} catch (Exception ex) {
					subSystem.setValue(type.getDefaultType());
				}
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				JComboBox combo = new JComboBox(HMTypeEnum.values());
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
				HMVariant variant = datasource.getSubSystem(rowIndex).getValue();
				HMTypeEnum type = variant.getHMType();
				Object o = null;

				try {
					o = type.toJava(variant);
				} catch (Exception ex) {
					// TODO ajouter message d'erreur
					o = type.getDefaultJava();
				}

				return o;

			}

			@Override
			public void setField(int rowIndex, Object value) {
				HMSubSystem subSystem = datasource.getSubSystem(rowIndex);
				HMTypeEnum type = subSystem.getValue().getHMType();

				try {
					subSystem.setValue(type.toVariant(value));
				} catch (Exception e) {
					// TODO ajouter message d'erreur
				}
			}

			@Override
			public TableCellEditor getTableCellEditor() {
				return new HMVariableCellEditor("Type");
			}

			@Override
			public Class getColumnClass() {
				return String.class;
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
		return HomemadeService.class.equals(type);
	}

	@Override
	public void registerEvents() {
		registerLazyTextChangedEvent(nameField);
		registerLazyTextChangedEvent(
				portField);
		registerLazyTextChangedEvent(
				heartbeatPeriodField);
		registerLazyTextChangedEvent(
				statusPeriodField);

		// TODO une seul fois
		table.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				fireDataChangedEvent();
				table.repaint();


			}
		});



	}

	@Override
	public void displayDataModel() {
		nameField.setText(datasource.getName());
		portField.setText(String.valueOf(datasource.getPort()));
		heartbeatPeriodField.setText(String.valueOf(datasource.getHeartbeatPeriod()));
		statusPeriodField.setText(String.valueOf(datasource.getStatusPeriod()));

		// table
		table.setModel(new ListAdapter<HMSubSystem>(columnDefinitions, datasource.getSubSystems()) {

			@Override
			public HMSubSystem getElementInstance() {
				return new HMSubSystem("Sub system", new HMStatus());


			}
		});
		TableColumnDefinition.applyEditors(table, columnDefinitions);
		table.setDefaultRenderer(String.class, new HMStatusTableRenderer());
	}

	@Override
	public void storeDataModel() {
		datasource.setName(nameField.getText());
		datasource.setPort(Integer.valueOf(portField.getText()));
		datasource.setHeartbeatPeriod(Integer.valueOf(heartbeatPeriodField.getText()));
		datasource.setStatusPeriod(Integer.valueOf(statusPeriodField.getText()));
	}

	/** Creates new form HomemadeEditor */
	public HomemadeEditor() {
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
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        nameField = new javax.swing.JTextField();
        tableManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager();
        jLabel2 = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        heartbeatPeriodField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        statusPeriodField = new javax.swing.JTextField();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(HomemadeEditor.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

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

        nameField.setName("nameField"); // NOI18N

        tableManager.setName("tableManager"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        portField.setName("portField"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        heartbeatPeriodField.setName("heartbeatPeriodField"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        statusPeriodField.setName("statusPeriodField"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableManager, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(heartbeatPeriodField, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(statusPeriodField, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(heartbeatPeriodField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusPeriodField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField heartbeatPeriodField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField portField;
    private javax.swing.JTextField statusPeriodField;
    private javax.swing.JTable table;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TableManager tableManager;
    // End of variables declaration//GEN-END:variables
}
