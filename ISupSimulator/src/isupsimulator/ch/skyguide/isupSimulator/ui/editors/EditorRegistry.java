/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.editors;

import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataListener;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author caronyn
 */
public class EditorRegistry {

	// attribute
	static private EditorRegistry singleton;
	private List<Editor> editors;

	// function
	private void registerEditorEvent(Editor editor) {
		editor.AddDataChangedListener(new DataListener() {

			public void changed(DataMVPEvent event) {
				try {
					((Editor) event.getSource()).storeDataModel();
				} catch (Exception ex) {
					Editor editor = ((Editor) event.getSource());
					editor.inactiveEvent();
					editor.displayDataModel();
					editor.activeEvent();
					
				}

			}
		});
	}

	// methode
	public void registerEditor(Editor editor) {
		editors.add(editor);
		registerEditorEvent(editor);
	}

	public Editor getEditor(Object object) {
		for (Editor editor : editors) {
			if (editor.canEdit(object.getClass())) {
				editor.setDataModel(object);
				editor.inactiveEvent();
				editor.displayDataModel();
				editor.activeEvent();
				editor.registerEvents();
				return editor;
			}
		}
		return null;
	}

	/**
	 * Add the data changed listener for event handling.
	 * @param listener The listener.
	 */
	public void AddDataChangedListener(DataListener<SNMPMIB> listener) {
		for (Editor editor : editors) {
			editor.AddDataChangedListener(listener);
		}
	}

	// constructor
	private EditorRegistry() {
		editors = new LinkedList<Editor>();
	}

	static public EditorRegistry getInstance() {
		if (singleton == null) {
			singleton = new EditorRegistry();
		}
		return singleton;
	}
}
