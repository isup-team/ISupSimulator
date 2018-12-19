/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.network.service.socket.MessageFormater;
import ch.skyguide.pvss.network.persistency.xstreamConverter.ServiceXStreamFormater;
import ch.skyguide.pvss.network.persistency.xstreamConverter.SimulatorXStreamFormater;
import ch.skyguide.pvss.network.persistency.dpl.DPExportable;
import ch.skyguide.pvss.network.persistency.dpl.DplUtils;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.socket.SocketThread;
import ch.skyguide.pvss.network.service.socket.MessageDelimiter;
import ch.skyguide.pvss.network.service.socket.SocketUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caronyn
 */
public class XmlClient extends ServiceLeaf implements XmlService, DPExportable {

    // inner class
    private class XmlClientThread extends SocketThread {

        // constant
        public static final int TIMEOUT = 2000;
        private static final String VERSION = "1.0";
        private static final String ENCODING = "UTF-8";
        // attributes
        private int identity;
        private String name;
        private String address;
        private int port;
        private long period;
        private XStream xstream;
        private MessageFormater formater;
        private boolean keepAlive;

        private Socket connect() throws UnknownHostException, IOException {
            Socket client = new Socket();

            InetAddress inetAddress = InetAddress.getByName(address);
            InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, port);

            client.connect(socketAddress, TIMEOUT);
            client.setSoTimeout(TIMEOUT);

            return client;
        }

        @Override
        public MessageFormater getFormater() {
            return formater;
        }

        @Override
        public void statusChanged(boolean isRunning) {
            setStatus();
        }

        @Override
        public synchronized void run() {
            log("Thread Started for %s on %s:%s.", name, address, port);
            setRunning(true);
            Long time = 0L;
            Socket client = null;

            while (!this.isInterrupted()) {
                try {
                    try {
                        time = System.currentTimeMillis();
                        if (client == null) {
                            client = connect();
                        }

                        String xml = xstream.toXML(new ISupStatus(system));
                        send(client, xml);
                        log("Status sent\n%s", getFormater().removeSpecialChar(xml));

                        XmlClient.this.setActiveChain(this.identity);
                        String msg = receive(client);
                        log("Ack received\n%s", getFormater().removeSpecialChar(msg));
                        // TODO verifier le ack

                        if (!keepAlive) {
                            disconnect(client);
                            client = null;
                        }
                        SocketUtil.waitRest(this, period, time);
                    } catch (IOException ex) {
                        if (XmlClient.this.getActiveChain() == this.identity || XmlClient.this.getActiveChain() == -1) {
                            log(Level.WARNING, ex.getMessage() + " for %s on %s:%s.", name, address, port);
                            XmlClient.this.setActiveChain(-1);
                        }
                        SocketUtil.waitRest(this, period, time);
                    }
                } catch (InterruptedException ex) {
                    if (forceInterupt) {
                        this.interrupt();
                    }
                }

            }
            try {
                disconnect(client);
            } catch (IOException ex) {
                Logger.getLogger(XmlClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            setRunning(false);
            log("Thread Stopped for %s on %s:%s.", name, address, port);

        }

        public XmlClientThread(int identity, String name, String address, int port, long period, boolean keepAlive) {
            this.identity = identity;
            this.name = name;
            this.address = address;
            this.port = port;
            this.period = period;
            this.keepAlive = keepAlive;

            xstream = new XStream(new DomDriver(ENCODING));
            new ServiceXStreamFormater(new SimulatorXStreamFormater()).configureXml(xstream);
            this.formater = new XmlMessageFormater(VERSION, ENCODING, XmlClient.this.delimiter);
        }
    }
    // constant
    // attribute
    private LinkedList<XmlClientThread> threads;
    private int activeChain;
    private XmlClientService parent;
    // serializable
    private long period;
    private MessageDelimiter delimiter;
    private ISupSystem system;
    private int driverID;
    private String driverName;
    private String pollGroup;
    private int agentID;
    private String dpPrefix;

    // property
    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    public int getActiveChain() {
        return activeChain;
    }

    public void setActiveChain(int activeChain) {
        this.activeChain = activeChain;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public MessageDelimiter getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(MessageDelimiter delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public ISupSystem getSystem() {
        return system;
    }

    @Override
    public void setSystem(ISupSystem system) {
        this.system = system;
    }

    public XmlClientService getParent() {
        return parent;
    }

    public void setParent(XmlClientService parent) {
        this.parent = parent;
    }

    public void setStatus() {
        boolean areAllStarted = true;
        boolean areAllStopped = true;

        if (threads == null) {
            setStatus(Status.STOPPED);
        } else {
            for (XmlClientThread thread : threads) {
                if (thread.isRunning()) {
                    areAllStopped = false;
                } else {
                    areAllStarted = false;
                }
            }
        }

        if (areAllStarted) {
            setStatus(Status.STARTED);
        } else if (areAllStopped) {
            setStatus(Status.STOPPED);
        } else {
            setStatus(Status.MITIGATED);
        }
    }

    @Override
    public int getAgentID() {
        return agentID;
    }

    @Override
    public void setAgentID(int AgentID) {
        this.agentID = AgentID;
    }

    @Override
    public String getDriverName() {
        return driverName;
    }

    @Override
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Override
    public String getPollGroup() {
        return pollGroup;
    }

    @Override
    public void setPollGroup(String pollGroup) {
        this.pollGroup = pollGroup;
    }

    @Override
    public String getDpPrefix() {
        return dpPrefix;
    }

    @Override
    public void setDpPrefix(String dpPrefix) {
        this.dpPrefix = dpPrefix;
    }

    @Override
    public int getDriverID() {
        return driverID;
    }

    @Override
    public void setDriverID(int DriverID) {
        this.driverID = DriverID;
    }

    // methode implementation
    @Override
    protected void startAgent() {
        // lazy initialization
        this.threads = new LinkedList<XmlClientThread>();

        int identity = 0;
        for (TcpHost host : parent.getHosts()) {
            threads.add(new XmlClientThread(identity, this.getName(), host.getAddress(), host.getPort(), period, parent.isKeptAlive()));
            identity++;
        }

        for (XmlClientThread thread : threads) {
            thread.start();
        }
    }

    @Override
    protected void stopAgent() {
        if (threads != null) {
            for (XmlClientThread thread : threads) {
                thread.forceInterrupt();
            }
        }
    }

    @Override
    protected void restartAgent() {
        if (threads != null) {
            for (XmlClientThread thread : threads) {
                thread.interrupt();
            }
        }
    }

    @Override
    public void buildDpl(Database db) {
        NodeType type = DplUtils.createType(db, dpPrefix);
        NodeType sgFwSystem = DplUtils.xmlGetSgFwSystemType();

        DplUtils.xmlCreateComponents(this.system.getSubSystems(), type, sgFwSystem);
        DplUtils.xmlBuildAddress(db, type, this.system.getSubSystems(), dpPrefix, this.system.getName(), this.getPollGroup(), driverID, agentID, driverName);
    }

    // constructor
    private XmlClient() {
        super("");
    }

    public XmlClient(String name, long period, MessageDelimiter delimiter, ISupSystem system) {
        super(name);
        this.period = period;
        this.delimiter = delimiter;
        this.system = system;
        this.activeChain = -1;
    }
}
