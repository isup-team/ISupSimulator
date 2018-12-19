/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isupsimulator.ch.skyguide.isupSimulator.ui.jtree;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.network.service.Service;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author caronyn
 */
public abstract class TreeNodeServiceAdapter extends DefaultMutableTreeNode implements Service {

    protected Service getService() {
        return (Service) super.getUserObject();
    }

    public TreeNodeServiceAdapter(Service service) {
        super(service);
    }

    @Override
    public String toString() {
        Service s = (Service) super.getUserObject();
        return s.getName();
    }

    @Override
    public Status getStatus() {
        return getService().getStatus();
    }

    @Override
    public String getName() {
        return getService().getName();
    }

    @Override
    public void setName(String name) {
        getService().setName(name);
    }

    @Override
    public void start() {
        getService().start();
    }

    @Override
    public void stop() {
        getService().stop();
    }

    @Override
    public void restart() {
        getService().restart();
    }
            
    @Override
    public void buildDpl(Database db) {
        getService().buildDpl(db);
    }

}
