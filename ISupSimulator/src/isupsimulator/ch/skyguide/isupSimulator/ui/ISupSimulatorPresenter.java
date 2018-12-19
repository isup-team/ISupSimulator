package isupsimulator.ch.skyguide.isupSimulator.ui;

import ch.skyguide.common.event.Event;
import ch.skyguide.pvss.persistency.PersistenceStrategy;
import ch.skyguide.pvss.persistency.XmlFilePersistenceStrategy;
import ch.skyguide.pvss.network.persistency.xstreamConverter.ServiceXStreamFormater;
import ch.skyguide.pvss.network.service.Service;
import ch.skyguide.pvss.network.service.ServiceComposite;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.ServiceListener;
import ch.skyguide.pvss.network.service.ServiceManager;
import isupsimulator.ch.skyguide.isupSimulator.conf.Config;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtree.TreeCellRendererService;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.FileMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.FileMVPListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.ServiceMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.ServiceMVPListener;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtree.CompositeServiceAdapter;
import isupsimulator.ch.skyguide.isupSimulator.ui.jtree.JTreeNodeAdapterFactory;
import java.io.File;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *	Main JFrame presenter.
 * @author caronyn
 */
public class ISupSimulatorPresenter implements Presenter {

	// constants
	private static final String TITLE = "ISup Simulator v %s%s";
	// attributes
	private ResourceMap resourceMap = Application.getInstance(isupsimulator.ISupSimulatorApp.class).getContext().getResourceMap(isupsimulator.ISupSimulatorApp.class);
	private PersistenceStrategy<Service> persistence = null;
	private ISupSimulatorView view;
	private ServiceComposite root;
	private String filename;
	private boolean isSaved;

	// event handling
	private void openFile_actionPerformed(Object src, String fileName) {
		loadFile(fileName);
	}

	private void saveFile_actionPerformed(Object src, String fileName) {
		saveFile(fileName);
	}

	private void startService_actionPerformed(Object src, Service service) {
		service.start();
	}

	private void stopService_actionPerformed(Object src, Service service) {
		service.stop();
	}

	private void refreshService_actionPerformed(Object src, Service service) {
		service.restart();
	}

	private void editor_changed(Object src, Object data) {
		isSaved = false;
		createTitle();
	}

	// function
	private void init() {
		view.setServiceTreeCellRenderer(new TreeCellRendererService());
	}

	public void loadLastFile() {
		// load last
		String last = Config.getInstance().getLastServiceFile();
		if (last != null && !last.equals("")) {
			loadFile(last);
		} else {
			newFile();
		}

	}

	private void createTitle() {
		String name = "";
		if (filename != null) {
			File file = new File(filename);

			if (file.isFile()) {
				name = file.getName();

				if (!name.equals("")) {
					name = " - " + name;
				}

				if (!isSaved) {
					name = name + " *";
				}
			}
		}

		String t = String.format(TITLE, resourceMap.getString("Application.version"), name);
		view.setTitle(t);
	}

	private void newFile() {
		root = new ServiceComposite("root");
		registerRoot();
	}

	private void loadFile(String filename) {
		if (root != null) {
			root.stop();
		}

		this.filename = filename;
		Config.getInstance().setLastServiceFile(filename);
		Config.getInstance().addRecentServiceFile(filename);
		persistence = new XmlFilePersistenceStrategy(new ServiceXStreamFormater(), filename);
		root = (ServiceComposite) persistence.retrieveObjectTree();
		registerRoot();
	}

	private void registerRoot() {
		registerListeners(root);
		ServiceManager.getInstance().setRoot(root);
		applyServiceTreeNode();
		isSaved = true;
		createTitle();
		view.registerLoggers();
	}

	private void saveFile(String filename) {
		this.filename = filename;
		if (!filename.substring(filename.length() - 4).equals("." + Config.SIM_EXTENSION)) {
			filename += "." + Config.SIM_EXTENSION;
		}

		persistence = new XmlFilePersistenceStrategy(new ServiceXStreamFormater(), filename);
		persistence.storeObjectTree(ServiceManager.getInstance().getRoot());
		isSaved = true;
		createTitle();
		Config.getInstance().setLastServiceFile(filename);
		Config.getInstance().addRecentServiceFile(filename);
	}

	private void applyServiceTreeNode() {
		JTreeNodeAdapterFactory adapter = JTreeNodeAdapterFactory.valueOf(ServiceManager.getInstance().getRoot());
		TreeNode treeNode = adapter.getTreeNodeInstance(root);
		view.setServiceTreeModel(new DefaultTreeModel(treeNode));
	}

	private void registerListeners(Service node) {

		if (node instanceof ServiceLeaf) {
			ServiceLeaf leaf = (ServiceLeaf) node;
			leaf.addServiceListener(new ServiceListener() {

				@Override
				public void serviceStarted(Event<ServiceLeaf> event) {
				}

				@Override
				public void serviceStopped(Event<ServiceLeaf> event) {
				}

				public void serviceChanged(Event<ServiceLeaf> event) {
					view.refreshTree();

					SwingUtilities.invokeLater(new Runnable() {

						public void run() {
							view.applyServicesStatus();
						}
					});
				}
			});
		} else if (node instanceof ServiceComposite) {
			for (Service child : (ServiceComposite<Service>) node) {
				// recursive to throw depth first tree
				registerListeners(child);
			}
		}

	}

	// property
	public ServiceComposite getRoot() {
		return root;
	}

	/**
	 * Last service file from config delegation getter.
	 * @return Return the last loaded file.
	 */
	public String getLastServiceFile() {
		return Config.getInstance().getLastServiceFile();
	}

	/**
	 * Last 10 service files from config delegation getter.
	 * @return Return the last loaded files.
	 */
	public List<String> getLastServiceFiles() {
		return Config.getInstance().getRecentServiceFile();
	}

	// interface implementation
	/**
	 * Getter of the presented view.
	 * @return Return the view.
	 */
	public View getView() {
		return (View) view;
	}

	// constructor
	/**
	 * Default constructor.
	 * @param view the view to manage.
	 */
	public ISupSimulatorPresenter(ISupSimulatorFrame view) {
		this.view = view;
		init();

		// event handling
		view.AddOpenFileListener(new FileMVPListener() {

			public void actionPerformed(FileMVPEvent event) {
				openFile_actionPerformed(event.getSource(), event.getFileName());
			}
		});

		view.AddSaveFileListener(new FileMVPListener() {

			public void actionPerformed(FileMVPEvent event) {
				saveFile_actionPerformed(event.getSource(), event.getFileName());
			}
		});

		view.AddStartServiceListener(new ServiceMVPListener() {

			public void actionPerformed(ServiceMVPEvent event) {
				startService_actionPerformed(event.getSource(), event.getService());
			}
		});

		view.AddStopServiceListener(new ServiceMVPListener() {

			public void actionPerformed(ServiceMVPEvent event) {
				stopService_actionPerformed(event.getSource(), event.getService());
			}
		});

		view.AddRefreshServiceListener(new ServiceMVPListener() {

			public void actionPerformed(ServiceMVPEvent event) {
				refreshService_actionPerformed(event.getSource(), event.getService());
			}
		});

		view.AddDataChangedListener(new DataListener() {

			public void changed(DataMVPEvent event) {
				editor_changed(event.getSource(), event.getData());
			}
		});

	}
}
