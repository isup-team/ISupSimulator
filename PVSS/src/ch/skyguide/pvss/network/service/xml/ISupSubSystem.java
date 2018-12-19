/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

import ch.skyguide.pvss.network.service.convertTable.SystemStatus;
import java.util.LinkedList;

/**
 *
 * @author caronyn
 */
public class ISupSubSystem {

    public enum Mode {

        R(true, false), W(false, true), RW(true, true);
        private boolean readable, writable;

        public boolean isReadable() {
            return readable;
        }

        public boolean isWritable() {
            return writable;
        }

        private Mode(boolean readable, boolean writable) {
            this.readable = readable;
            this.writable = writable;
        }
    }
    private Mode mode;
    private String dpName;
    private String name;
    private SystemStatus status;
    private LinkedList<ISupLabel> labels;
    private String message;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SystemStatus getStatus() {
        return status;
    }

    public void setStatus(SystemStatus status) {
        this.status = status;
    }

    private void setLabel(int index, String value) {
        if (labels == null) {
            labels = new LinkedList<ISupLabel>();
        }
        if (labels.size() < index + 1) {
            for (int i = labels.size(); i < index + 1; i++) {
                labels.add(new ISupLabel("Label" + (i + 1), ""));
            }
        }

        labels.get(index).setValue(value);
        cleanLabels();
    }

    private void cleanLabels() {
        for (int i = labels.size() - 1; i >= 0; i--) {
            if ("".equals(labels.get(i).getValue())) {
                labels.remove(i);
            } else {
                return;
            }
        }
    }

    private String getLabel(int index) {
        try {
            return labels.get(index).getValue();
        } catch (Exception ex) {
            return "";
        }
    }

    public void setLabel1(String value) {
        setLabel(0, value);
    }

    public String getLabel1() {
        return getLabel(0);
    }

    public void setLabel2(String value) {
        setLabel(1, value);
    }

    public String getLabel2() {
        return getLabel(1);
    }

    public void setLabel3(String value) {
        setLabel(2, value);
    }

    public String getLabel3() {
        return getLabel(2);
    }

    private ISupSubSystem() {
    }

    public ISupSubSystem(String name, SystemStatus status) {
        this.mode = Mode.R;
        this.name = name;
        this.status = status;
    }
}
