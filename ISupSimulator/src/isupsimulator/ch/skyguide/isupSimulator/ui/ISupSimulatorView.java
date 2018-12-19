package isupsimulator.ch.skyguide.isupSimulator.ui;

import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.FileMVPListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.ServiceMVPListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 * ISup simulator frame's view interface.
 * @author caronyn
 */
public interface ISupSimulatorView extends View {

	// event handling
	/**
	 * Add the open file listener used for specific file.
	 * @param listener The open file listener instance.
	 */
	void AddOpenFileListener(FileMVPListener listener);

	/**
	 * Add the save file listener used for specific file.
	 * @param listener The save file listener instance.
	 */
	void AddSaveFileListener(FileMVPListener listener);

	/**
	 * Add the start service event listener.
	 * @param listener The listener.
	 */
	void AddStartServiceListener(ServiceMVPListener listener);

	/**
	 * Add the stop service event listener.
	 * @param listener The listener.
	 */
	void AddStopServiceListener(ServiceMVPListener listener);

	/**
	 * Add the refresh service event listener.
	 * @param listener The listener.
	 */
	void AddRefreshServiceListener(ServiceMVPListener listener);

	/**
	 * Add the data changed listener for event handling.
	 * @param listener The listener.
	 */
	void AddDataChangedListener(DataListener listener);

	// methodes
	/**
	 * Set the frame title.
	 * @param title the title
	 */
	void setTitle(String title);

	/**
	 * Get the tree selected node.
	 * @return Return the selected node.
	 */
	DefaultMutableTreeNode getSelectedNode();

	/**
	 * Set the service tree model to draw the tree of services.
	 * @param model Service tree model.
	 */
	void setServiceTreeModel(TreeModel model);

	/**
	 * Set the service tree cell renderer.
	 * @param renderer Service tree cell renderer.
	 */
	void setServiceTreeCellRenderer(TreeCellRenderer renderer);

	/**
	 * Refresh the frame tree.
	 */
	void refreshTree();

	void applyServicesStatus();

	void registerLoggers();

}
