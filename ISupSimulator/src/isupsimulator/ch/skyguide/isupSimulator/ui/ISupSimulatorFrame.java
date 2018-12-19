/*
 * ISupSimulatorView.java
 */
package isupsimulator.ch.skyguide.isupSimulator.ui;

import ch.skyguide.common.event.EventDispatcher;
import ch.skyguide.common.ui.logger.TextFieldHandler;
import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.io.DatabaseWriter;
import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.modbus.ModbusSerialListener;
import ch.skyguide.pvss.network.service.snmp.SNMPAgent;
import ch.skyguide.pvss.network.service.snmp.SNMPAgentService;
import ch.skyguide.pvss.network.service.snmp.SNMPTrapService;
import ch.skyguide.pvss.network.service.socket.SocketThread;
import isupsimulator.ISupSimulatorApp;
import isupsimulator.ch.skyguide.isupSimulator.conf.Config;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.ConvertTableEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.Editor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.EditorRegistry;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.HomemadeEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.LiteralEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.SNMPMibEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.SNMPServerEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.SNMPTableEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.SNMPTrapEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.ServiceCompositeEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.WagoEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.XmlClientEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.XmlClientSimulatorEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.XmlServerEditor;
import isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.DPManager;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.FileMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.FileMVPListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.ServiceMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.ServiceMVPListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.fileFilter.DplFileFilter;
import isupsimulator.ch.skyguide.isupSimulator.ui.fileFilter.SimFileFilter;
import isupsimulator.ch.skyguide.isupSimulator.util.GuiUtil;
import isupsimulator.ch.skyguide.isupSimulator.util.LoggerStyledFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FilenameUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;

/**
 * The application's main frame.
 */
public class ISupSimulatorFrame extends FrameView implements ISupSimulatorView {

    // resources
    private ResourceMap resourceMap = Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(isupsimulator.ISupSimulatorApp.class);
    // attributes
    private ISupSimulatorPresenter presenter;
    private EditorRegistry registry;
    private AboutBoxFrame aboutBox = null;
    private ServiceStatusEnum currentStatus = ServiceStatusEnum.STOPPED;
    // event dispatchers
    private EventDispatcher<FileMVPListener> openFileDispatcher = new EventDispatcher<FileMVPListener>();
    private EventDispatcher<FileMVPListener> saveFileDispatcher = new EventDispatcher<FileMVPListener>();
    private EventDispatcher<ServiceMVPListener> startServiceDispatcher = new EventDispatcher<ServiceMVPListener>();
    private EventDispatcher<ServiceMVPListener> stopServiceDispatcher = new EventDispatcher<ServiceMVPListener>();
    private EventDispatcher<ServiceMVPListener> refreshServiceDispatcher = new EventDispatcher<ServiceMVPListener>();

    // function
    private void registerEditor() {
        registry = EditorRegistry.getInstance();

        registry.registerEditor(new ServiceCompositeEditor());
        registry.registerEditor(new LiteralEditor());
        registry.registerEditor(new SNMPMibEditor());
        registry.registerEditor(new SNMPServerEditor());
        registry.registerEditor(new SNMPTrapEditor());
        registry.registerEditor(new SNMPTableEditor());
        registry.registerEditor(new WagoEditor());
        registry.registerEditor(new XmlClientEditor());
        registry.registerEditor(new XmlClientSimulatorEditor());
        registry.registerEditor(new XmlServerEditor());
        registry.registerEditor(new HomemadeEditor());
        registry.registerEditor(new ConvertTableEditor());
    }

    private void setEditor(TreeNode selectedNode) {
        if (presenter != null) {
            Editor editor = null;
            editorPanel.removeAll();

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedNode;
            editor = EditorRegistry.getInstance().getEditor(node.getUserObject());

            if (editor != null) {
                editorPanel.add(editor);
            }
            editorPanel.revalidate();
            editorPanel.repaint();
        }
    }

    private void loadRecentFiles() {
        // load rencents
        openRecentMenu.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                JMenu menu = (JMenu) e.getSource();
                menu.removeAll();

                GuiUtil.buildRecentMenu(presenter.getLastServiceFiles(), openRecentMenu, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String file = e.getActionCommand();
                        openFileDispatcher.invoke("actionPerformed", new FileMVPEvent((JComponent) e.getSource(), file));
                    }
                });
            }
        });
    }

    private void expandTree(int level) {
        for (int i = 0; i < serviceTree.getRowCount(); i++) {
            TreePath path = serviceTree.getPathForRow(i);

            if (level <= 0 || path.getPathCount() <= level) {
                serviceTree.expandPath(serviceTree.getPathForRow(i));
            }
        }
    }

    private void selectFirstNode() {
        serviceTree.setSelectionRow(0);
    }

    // interface implementation
    /**
     * Get the view presenter.
     * @return Return the presenter.
     */
    public Presenter getPresenter() {
        return presenter;
    }

    /**
     * Add the open file listener used for specific file.
     * @param listener The open file listener instance.
     */
    public void AddOpenFileListener(FileMVPListener listener) {
        openFileDispatcher.addListener(listener);
    }

    /**
     * Add the save file listener used for specific file.
     * @param listener The save file listener instance.
     */
    public void AddSaveFileListener(FileMVPListener listener) {
        saveFileDispatcher.addListener(listener);
    }

    /**
     * Add the start service event listener.
     * @param listener The listener.
     */
    public void AddStartServiceListener(ServiceMVPListener listener) {
        startServiceDispatcher.addListener(listener);
    }

    /**
     * Add the stop service event listener.
     * @param listener The listener.
     */
    public void AddStopServiceListener(ServiceMVPListener listener) {
        stopServiceDispatcher.addListener(listener);
    }

    /**
     * Add the refresh service event listener.
     * @param listener The listener.
     */
    public void AddRefreshServiceListener(ServiceMVPListener listener) {
        refreshServiceDispatcher.addListener(listener);
    }

    /**
     * Add the data changed listener for event handling.
     * @param listener The listener.
     */
    public void AddDataChangedListener(DataListener listener) {
        registry.AddDataChangedListener(listener);
    }

    /**
     * Set the frame title.
     * @param title the title
     */
    public void setTitle(String title) {
        this.getFrame().setTitle(title);
    }

    /**
     * Get the tree selected node.
     * @return Return the selected node.
     */
    public DefaultMutableTreeNode getSelectedNode() {
        return (DefaultMutableTreeNode) serviceTree.getSelectionPath().getLastPathComponent();
    }

    /**
     * Set the service tree model to draw the tree of services.
     * @param model Service tree model.
     */
    public void setServiceTreeModel(TreeModel model) {
        serviceTree.setModel(model);
        expandTree(resourceMap.getInteger("Data.expandTreeLevel"));
        selectFirstNode();
    }

    /**
     * Set the service tree cell renderer.
     * @param renderer Service tree cell renderer.
     */
    public void setServiceTreeCellRenderer(TreeCellRenderer renderer) {
        serviceTree.setCellRenderer(renderer);
    }

    /**
     * Refresh the frame tree.
     */
    public void refreshTree() {
        serviceTree.repaint();
    }

    public void applyServicesStatus() {
        Service.Status status = presenter.getRoot().getStatus();
        ServiceStatusEnum servSt = ServiceStatusEnum.valueOf(status);

        if (servSt == ServiceStatusEnum.MITIGATED) {
            servSt = ServiceStatusEnum.STARTED;
        }

        if (!servSt.equals(currentStatus)) {

            getFrame().setIconImage(servSt.getImageIcon().getImage());
        }
        currentStatus = servSt;
    }

    public void registerLoggers() {
        Handler handler = new TextFieldHandler(new LoggerStyledFormatter(logger, new SimpleFormatter()));
        handler.setLevel(Level.ALL);

        lazyRegister(SNMPAgentService.class.getName(), handler);
        lazyRegister(SNMPTrapService.class.getName(), handler);
        lazyRegister(SNMPAgent.class.getName(), handler);
        lazyRegister(SocketThread.class.getName(), handler);
        lazyRegister(ModbusSerialListener.class.getName(), handler);
    }

    private void lazyRegister(String className, Handler handler) {
        // force to load to class loader to run
        try {
            this.getClass().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ISupSimulatorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean found = false;
        Handler[] handlers = Logger.getLogger(className).getHandlers();
        for (int i = 0; i < handlers.length && !found; i++) {
            if (handlers[i].equals(handler)) {
                found = true;
            }
        }

        if (!found) {
            Logger.getLogger(className).addHandler(handler);
        }

    }

    /**
     * Open file.
     */
    @Action
    public void open() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new SimFileFilter());

        fc.setCurrentDirectory(new File(presenter.getLastServiceFile()));
        int ret = fc.showOpenDialog(this.getFrame());

        if (ret == JFileChooser.APPROVE_OPTION) {
            openFileDispatcher.invoke("actionPerformed", new FileMVPEvent(
                    openMenu, fc.getSelectedFile().getAbsolutePath()));
        }

    }

    /**
     * Show about box.
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ISupSimulatorApp.getApplication().getMainFrame();
            aboutBox = new AboutBoxFrame(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ISupSimulatorApp.getApplication().show(aboutBox);
    }

    /**
     * Refresh file.
     */
    @Action
    public void refresh() {
        openFileDispatcher.invoke("actionPerformed", new FileMVPEvent(
                openMenu, presenter.getLastServiceFile()));
    }

    /**
     * Save file.
     */
    @Action
    public void save() {
        if ("".equals(presenter.getLastServiceFile())) {
            saveAs();
        } else {
            saveFileDispatcher.invoke("actionPerformed", new FileMVPEvent(
                    saveMenu, presenter.getLastServiceFile()));
        }
    }

    /**
     * Save as file.
     */
    @Action
    public void saveAs() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new SimFileFilter());

        fc.setCurrentDirectory(new File(presenter.getLastServiceFile()));
        int ret = fc.showSaveDialog(this.getFrame());

        if (ret == JFileChooser.APPROVE_OPTION) {
            saveFileDispatcher.invoke("actionPerformed", new FileMVPEvent(
                    saveMenu, fc.getSelectedFile().getAbsolutePath()));
        }
    }

    /**
     * Save as file.
     */
    @Action
    public void exportConfig() {
        Database db = buildDatabase();

        if (db != null) {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File(Config.getInstance().getLastExportFile()));

            fc.addChoosableFileFilter(new DplFileFilter());
            int ret = fc.showOpenDialog(this.getFrame());

            if (ret == JFileChooser.APPROVE_OPTION) {
                Config.getInstance().setLastExportFile(fc.getSelectedFile().getParent());

                // filename
                String fileName = fc.getSelectedFile().getAbsolutePath();
                if (!FilenameUtils.getExtension(fileName).equals(Config.DPL_EXTENSION)) {
                    fileName = fileName + '.' + Config.DPL_EXTENSION;
                }

                saveDpl(fileName, db);
            }
        }
    }

    /*@Action
    public void exportConfig() {
    Database db = buildDatabase();
    saveDpl("", db);
    }*/
    public Database buildDatabase() {
        Database db = null;
        TreeNode node = findNextService(getSelectedNode());

        Service service = (Service) node;
        db = new Database();

        service.buildDpl(db);

        return db;
    }

    public void saveDpl(String fileName, Database db) {

        FileOutputStream fos = null;
        try {

            //OutputStreamWriter osw = new OutputStreamWriter(System.out);
            fos = new FileOutputStream(new File(fileName));
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            DatabaseWriter dw = new DatabaseWriter(osw);

            //dw.writeType(db);
            //dw.writeInstance(db);

            dw.writeHeader(db);
            dw.writeDistrib(db);
            dw.writeAddress(db);

            osw.flush();

        } catch (IOException ex) {
            Logger.getLogger(DPManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(DPManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Quit application.
     */
    @Action
    public void quit() {
    }

    private TreeNode findNextService(TreeNode node) {
        if (node instanceof Service) {
            return node;
        }

        return findNextService(node.getParent());
    }

    /**
     * Start the selected service.
     */
    @Action
    public void startService() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) findNextService(getSelectedNode());
        startServiceDispatcher.invoke("actionPerformed", new ServiceMVPEvent(startServiceMenu, (Service) node));
    }

    /**
     * Stop the selected service.
     */
    @Action
    public void stopService() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) findNextService(getSelectedNode());
        stopServiceDispatcher.invoke("actionPerformed", new ServiceMVPEvent(startServiceMenu, (Service) node));
    }

    /**
     * Stop all services.
     */
    @Action
    public void stopAllService() {
        stopServiceDispatcher.invoke("actionPerformed", new ServiceMVPEvent(startServiceMenu, presenter.getRoot()));
    }

    @Action
    public void apply() {
        refreshServiceDispatcher.invoke("actionPerformed", new ServiceMVPEvent(applyMenu, presenter.getRoot()));
    }

    /**
     * Clear the log list.
     */
    @Action
    public void clearList() {
        try {
            logger.getDocument().remove(0, logger.getDocument().getLength());


        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);


        }
    }

    /**
     * Default constructor.
     * @param app the application object.
     */
    public ISupSimulatorFrame(SingleFrameApplication app) {
        super(app);
        initComponents();

        getFrame().setIconImage(currentStatus.getImageIcon().getImage());

        // set menu names (JDesktop bug)
        //openRecentMenu.setText("Open Recent ...");
        //actionMenu.setText("Action");

        treeManager.setTree(serviceTree);
        registerEditor();
        presenter = new ISupSimulatorPresenter(this);
        loadRecentFiles();

        try {
            presenter.loadLastFile();
        } catch (Exception ex) {
            ex.printStackTrace();
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

        mainPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        rightPanel = new javax.swing.JPanel();
        editorPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        serviceTree = new javax.swing.JTree();
        treeManager = new isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TreeManager();
        autoScrollPane1 = new ch.skyguide.common.ui.AutoScrollPane();
        logger = new javax.swing.JTextPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        openMenu = new javax.swing.JMenuItem();
        openRecentMenu = new javax.swing.JMenu();
        refreshMenu = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        saveMenu = new javax.swing.JMenuItem();
        saveAsMenu = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        quitMenu = new javax.swing.JMenuItem();
        actionMenu = new javax.swing.JMenu();
        startServiceMenu = new javax.swing.JMenuItem();
        stopServiceMenu = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        applyMenu = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        clearListMenu = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutBoxMenu = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(1024, 768));
        mainPanel.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(500);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.7);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jSplitPane2.setDividerLocation(300);
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        rightPanel.setName("rightPanel"); // NOI18N
        rightPanel.setLayout(new java.awt.BorderLayout());

        editorPanel.setName("editorPanel"); // NOI18N
        editorPanel.setLayout(new java.awt.BorderLayout());
        rightPanel.add(editorPanel, java.awt.BorderLayout.CENTER);

        jSplitPane2.setRightComponent(rightPanel);

        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        serviceTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        serviceTree.setName("serviceTree"); // NOI18N
        serviceTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                serviceTreeValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(serviceTree);

        treeManager.setName("treeManager"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeManager, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(treeManager, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane2.setLeftComponent(jPanel1);

        jSplitPane1.setLeftComponent(jSplitPane2);

        autoScrollPane1.setName("autoScrollPane1"); // NOI18N

        logger.setName("logger"); // NOI18N
        autoScrollPane1.setViewportView(logger);

        jSplitPane1.setRightComponent(autoScrollPane1);

        mainPanel.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(ISupSimulatorFrame.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getActionMap(ISupSimulatorFrame.class, this);
        openMenu.setAction(actionMap.get("open")); // NOI18N
        openMenu.setText(resourceMap.getString("openMenu.text")); // NOI18N
        openMenu.setName("openMenu"); // NOI18N
        fileMenu.add(openMenu);

        openRecentMenu.setText(resourceMap.getString("openRecentMenu.text")); // NOI18N
        openRecentMenu.setName("openRecentMenu"); // NOI18N
        fileMenu.add(openRecentMenu);

        refreshMenu.setAction(actionMap.get("refresh")); // NOI18N
        refreshMenu.setText(resourceMap.getString("refreshMenu.text")); // NOI18N
        refreshMenu.setName("refreshMenu"); // NOI18N
        fileMenu.add(refreshMenu);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        saveMenu.setAction(actionMap.get("save")); // NOI18N
        saveMenu.setText(resourceMap.getString("saveMenu.text")); // NOI18N
        saveMenu.setName("saveMenu"); // NOI18N
        fileMenu.add(saveMenu);

        saveAsMenu.setAction(actionMap.get("saveAs")); // NOI18N
        saveAsMenu.setText(resourceMap.getString("saveAsMenu.text")); // NOI18N
        saveAsMenu.setName("saveAsMenu"); // NOI18N
        fileMenu.add(saveAsMenu);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        jMenuItem2.setAction(actionMap.get("exportConfig")); // NOI18N
        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText(resourceMap.getString("exportConfig.text")); // NOI18N
        jMenuItem2.setToolTipText(resourceMap.getString("exportConfig.toolTipText")); // NOI18N
        jMenuItem2.setName("exportConfig"); // NOI18N
        fileMenu.add(jMenuItem2);

        jSeparator4.setName("jSeparator4"); // NOI18N
        fileMenu.add(jSeparator4);

        quitMenu.setAction(actionMap.get("quit")); // NOI18N
        quitMenu.setText(resourceMap.getString("quitMenu.text")); // NOI18N
        quitMenu.setName("quitMenu"); // NOI18N
        fileMenu.add(quitMenu);

        menuBar.add(fileMenu);

        actionMenu.setText(resourceMap.getString("actionMenu.text")); // NOI18N

        startServiceMenu.setAction(actionMap.get("startService")); // NOI18N
        startServiceMenu.setText(resourceMap.getString("startServiceMenu.text")); // NOI18N
        startServiceMenu.setName("startServiceMenu"); // NOI18N
        actionMenu.add(startServiceMenu);

        stopServiceMenu.setAction(actionMap.get("stopService")); // NOI18N
        stopServiceMenu.setText(resourceMap.getString("stopServiceMenu.text")); // NOI18N
        stopServiceMenu.setName("stopServiceMenu"); // NOI18N
        actionMenu.add(stopServiceMenu);

        jMenuItem1.setAction(actionMap.get("stopAllService")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        actionMenu.add(jMenuItem1);

        applyMenu.setAction(actionMap.get("apply")); // NOI18N
        applyMenu.setText(resourceMap.getString("applyMenu.text")); // NOI18N
        applyMenu.setName("applyMenu"); // NOI18N
        actionMenu.add(applyMenu);

        jSeparator3.setName("jSeparator3"); // NOI18N
        actionMenu.add(jSeparator3);

        clearListMenu.setAction(actionMap.get("clearList")); // NOI18N
        clearListMenu.setText(resourceMap.getString("clearListMenu.text")); // NOI18N
        clearListMenu.setName("clearListMenu"); // NOI18N
        actionMenu.add(clearListMenu);

        menuBar.add(actionMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutBoxMenu.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutBoxMenu.setText(resourceMap.getString("aboutBoxMenu.text")); // NOI18N
        aboutBoxMenu.setName("aboutBoxMenu"); // NOI18N
        helpMenu.add(aboutBoxMenu);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

	private void serviceTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_serviceTreeValueChanged

            setEditor((TreeNode) evt.getPath().getLastPathComponent());
	}//GEN-LAST:event_serviceTreeValueChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutBoxMenu;
    private javax.swing.JMenu actionMenu;
    private javax.swing.JMenuItem applyMenu;
    private ch.skyguide.common.ui.AutoScrollPane autoScrollPane1;
    private javax.swing.JMenuItem clearListMenu;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextPane logger;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenu;
    private javax.swing.JMenu openRecentMenu;
    private javax.swing.JMenuItem quitMenu;
    private javax.swing.JMenuItem refreshMenu;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JMenuItem saveAsMenu;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JTree serviceTree;
    private javax.swing.JMenuItem startServiceMenu;
    private javax.swing.JMenuItem stopServiceMenu;
    private isupsimulator.ch.skyguide.isupSimulator.ui.editors.component.TreeManager treeManager;
    // End of variables declaration//GEN-END:variables
}
