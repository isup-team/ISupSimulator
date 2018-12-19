/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.editor;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author caronyn
 */
public class EditorRegistry {

	// attributes
	static private EditorRegistry singleton;
	private List<Editor> editors;

	// methodes
	public void registerEditor(Editor editor) {
		editors.add(editor);
	}

	public Editor getEditor(Object object) {
		for (Editor editor : editors) {
			if (editor.canEdit(object.getClass())) {
				editor.setObject(object);
				editor.objectToFrame();
				return editor;
			}
		}
		return null;
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
