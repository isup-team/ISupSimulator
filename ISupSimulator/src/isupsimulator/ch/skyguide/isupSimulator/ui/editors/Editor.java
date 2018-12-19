package isupsimulator.ch.skyguide.isupSimulator.ui.editors;

import ch.skyguide.common.event.EventDispatcher;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataMVPEvent;
import isupsimulator.ch.skyguide.isupSimulator.ui.event.DataListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The editor interface enabled to edit any datasource.
 * @param <T> The type to edit.
 * @author caronyn
 */
public abstract class Editor<T> extends JPanel {

    // propertie
    protected EventDispatcher<DataListener<T>> dataChangedDispatcher = new EventDispatcher<DataListener<T>>();
    T datasource;
    private boolean eventActive = true;

    public void activeEvent() {
        eventActive = true;
    }

    public void inactiveEvent() {
        eventActive = false;
    }

    // static methodes
    /**
     * Register text component key press event, if not already registered.
     * @param component Text component to register.
     */
    public void registerLazyTextChangedEvent(JTextField component) {
        if (component.getActionListeners().length == 0) {
            component.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireDataChangedEvent();
                }
            });

            component.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(FocusEvent e) {
                    super.focusLost(e);
                    fireDataChangedEvent();
                }
            });

            component.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getModifiers() == e.CTRL_MASK) {
                        fireDataChangedEvent();
                    }
                }
            });
        }
    }

    public void registerLazyTextChangedEvent(JTextArea component) {
        if (component.getKeyListeners().length == 0) {
            component.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(FocusEvent e) {
                    super.focusLost(e);
                    fireDataChangedEvent();
                }
            });

            component.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    fireDataChangedEvent();
                }
            });
        }
    }

    public void registerLazySpinnerChangedEvent(JSpinner component) {
        // TODO voir pourquoi
        if (component.getChangeListeners().length <= 1) {
            component.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    fireDataChangedEvent();
                }
            });
        }
    }

    public void registerLazyComboChangedEvent(JComboBox component) {
        if (component.getActionListeners().length == 0) {
            component.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    fireDataChangedEvent();
                }
            });
        }
    }

    public void registerLazyCheckBoxChangedEvent(JCheckBox component) {
        if (component.getChangeListeners().length <= 1) {
            component.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    fireDataChangedEvent();
                }
            });
        }
    }

    /**
     * Add the data changed listener.
     * @param listener The listener.
     */
    public void AddDataChangedListener(DataListener<T> listener) {
        dataChangedDispatcher.addListener(listener);
    }

    /**
     * Raise the changed event.
     * @param src The object source.
     */
    public void fireDataChangedEvent() {
        if (eventActive) {
            dataChangedDispatcher.invoke("changed", new DataMVPEvent<T>(this, datasource));
        }
    }

    /**
     * Return if this is the appropriate editor.
     * @param type Type accepted.
     * @return Return true if it can edit the type.
     */
    public abstract boolean canEdit(Class type);

    /**
     * Set the data model.
     * @param datasource Data model.
     */
    public void setDataModel(T datasource) {
        this.datasource = datasource;
    }

    /**
     * Register the datachange events.
     */
    public abstract void registerEvents();

    /**
     * Data model to view.
     */
    public abstract void displayDataModel();

    /**
     * View to Datamodel.
     */
    public abstract void storeDataModel();
}
