/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

import ch.skyguide.pvss.network.service.ServiceComposite;
import java.util.LinkedList;

/**
 *
 * @author caronyn
 */
public class XmlClientService extends ServiceComposite<XmlClient> {

    private boolean keepAlive;
    private LinkedList<TcpHost> hosts;

    @Override
    public void addService(XmlClient service) {
        service.setParent(this);
        super.addService(service);
    }

    public boolean addHost(TcpHost e) {
        return hosts.add(e);
    }

    public void addHost(int index, TcpHost element) {
        hosts.add(index, element);
    }

    public boolean removeHost(TcpHost o) {
        return hosts.remove(o);
    }

    public TcpHost removeHost(int index) {
        return hosts.remove(index);
    }

    public TcpHost getHost(int index) {
        return hosts.get(index);
    }

    public Iterable<TcpHost> getHosts() {
        return hosts;
    }

    public boolean isKeptAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    // constructor
    private XmlClientService() {
        super("");
        this.hosts = new LinkedList<TcpHost>();
    }

    public XmlClientService(String name) {
        super(name);
        this.hosts = new LinkedList<TcpHost>();
    }
}
