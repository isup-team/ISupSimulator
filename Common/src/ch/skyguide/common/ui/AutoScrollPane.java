/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.common.ui;

import java.awt.Adjustable;
import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JScrollPane;

/**
 *
 * @author caronyn
 */
public class AutoScrollPane extends JScrollPane implements AdjustmentListener, MouseWheelListener {

    boolean isLocked = true;
    int currentPos = 0;
    int currentMax = 0;

    // function
    private void init() {
	this.getVerticalScrollBar().addAdjustmentListener(this);
	this.addMouseWheelListener(this);
    }
    
    // methode
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
	Adjustable adj = e.getAdjustable();
	int end = adj.getMaximum() - adj.getVisibleAmount();

	if (e.getValueIsAdjusting()) {
	    if (e.getValue() == end) {
		isLocked = true;
	    } else {
		isLocked = false;
	    }
	} else
	    if (isLocked) {
	    adj.setValue(end);
	}

	currentPos = adj.getValue();
	currentMax = end;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
	if (e.getWheelRotation() > 0 && currentPos == currentMax) {
	    isLocked = true;
	} else {
	    isLocked = false;
	}
    }

    // constructor
    public AutoScrollPane() {
	super();
	init();
    }

    public AutoScrollPane(int vsbPolicy, int hsbPolicy) {
	super(vsbPolicy, hsbPolicy);
	init();
    }

    public AutoScrollPane(Component view) {
	super(view);
	init();
    }

    public AutoScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
	super(view, vsbPolicy, hsbPolicy);
	init();
    }
    
}