/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.ui.editor;

import ch.skyguide.common.event.Listener;

/**
 * The open file action listener.
 * @author caronyn
 */
public interface EditorChangedListener<T> extends Listener {

	/**
	 * The observer methode.
	 * @param event
	 */
	public void changed(EditorChangedEvent<T> event);

}
