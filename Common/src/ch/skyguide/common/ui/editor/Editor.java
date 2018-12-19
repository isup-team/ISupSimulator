/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui.editor;

import ch.skyguide.common.event.EventDispatcher;
import javax.swing.JPanel;

/**
 *
 * @author caronyn
 */
public abstract class Editor<T> extends JPanel {

    protected EventDispatcher<EditorChangedListener<T>> changeDispatcher = new EventDispatcher<EditorChangedListener<T>>();

    public void addEditorChangedListener(EditorChangedListener<T> listener) {
        changeDispatcher.addListener(listener);
    }

    protected void fireEditorChanged(T value) {
        changeDispatcher.invoke("changed", new EditorChangedEvent<T>(this, value));
    }

    public abstract boolean canEdit(Class type);

    public abstract void setObject(T object);

    public abstract void objectToFrame();

    public abstract void frameToObject();
}
