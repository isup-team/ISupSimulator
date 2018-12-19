/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.network.persistency.xstreamConverter.ServiceXStreamFormater;
import ch.skyguide.pvss.network.persistency.xstreamConverter.SimulatorXStreamFormater;
import ch.skyguide.pvss.network.persistency.dpl.DPExportable;
import ch.skyguide.pvss.network.persistency.dpl.DplUtils;
import ch.skyguide.pvss.network.service.socket.MessageFormater;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.socket.ServerThread;
import ch.skyguide.pvss.network.service.socket.MessageDelimiter;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author caronyn
 */
public class XmlServerService extends ServiceLeaf implements XmlService, DPExportable {

    // inner class
    private class XmlServerThread extends ServerThread {

        private static final String VERSION = "1.0";
        private static final String ENCODING = "UTF-8";
        MessageFormater formater;
        private XStream xstream;

        @Override
        public MessageFormater getFormater() {
            return formater;
        }

        @Override
        public String getHeartbeatMessage() {
            return xstream.toXML(new ISupStatus(new ISupSystem(XmlServerService.this.getName(), XmlServerService.this.system.getSource())));
        }

        @Override
        public String getStatusMessage() {
            return xstream.toXML(new ISupStatus(getSendableSystem()));
        }

        @Override
        public void messageReceived(String msg) {
            // Do nothing
            // TODO interpreter le message reçu.
        }

        @Override
        public void statusChanged(boolean isRunning) {
            if (isRunning) {
                XmlServerService.this.setStatus(Status.STARTED);
            } else {
                XmlServerService.this.setStatus(Status.STOPPED);
            }
        }

        public XmlServerThread(String name, int port, long heartbeatPeriod, long statusPeriod, MessageDelimiter delimiter) {
            super(name, port, heartbeatPeriod, statusPeriod);
            this.formater = new XmlMessageFormater(VERSION, ENCODING, delimiter);
            this.xstream = new XStream();
            new ServiceXStreamFormater(new SimulatorXStreamFormater()).configureXml(xstream);

        }
    }
    //attributes
    private XmlServerThread thread;
    // serializable
    private ISupSystem system;
    private int port;
    private long heartbeatPeriod;
    private long statusPeriod;
    private MessageDelimiter delimiter;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ISupSystem getSendableSystem() {
        ISupSystem sys = new ISupSystem(system.getName(), system.getSource());

        for (ISupSubSystem subSystem : system.getSubSystems()) {
            if (subSystem.getMode().isReadable()) {
                sys.addSubSystem(subSystem);
            }
        }

        return sys;
    }

    public long getHearbeatPeriod() {
        return heartbeatPeriod;
    }

    public void setHeartbeatPeriod(long heartbeatPeriod) {
        this.heartbeatPeriod = heartbeatPeriod;
    }

    public long getStatusPeriod() {
        return statusPeriod;
    }

    public void setStatusPeriod(long statusPeriod) {
        this.statusPeriod = statusPeriod;
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
        this.thread = new XmlServerThread(this.getName(), this.getPort(), heartbeatPeriod, statusPeriod, delimiter);
        this.thread.start();
    }

    @Override
    protected void stopAgent() {
        this.thread.forceInterrupt();
    }

    @Override
    protected void restartAgent() {
        this.thread.restartSender();
    }

    @Override
    public void buildDpl(Database db) {
        NodeType type = DplUtils.createType(db, dpPrefix);
        NodeType sgFwSystem = DplUtils.xmlGetSgFwSystemType();

        DplUtils.xmlCreateComponents(this.system.getSubSystems(), type, sgFwSystem);
        DplUtils.xmlBuildAddress(db, type, this.system.getSubSystems(), dpPrefix, this.system.getName(), this.getPollGroup(), driverID, agentID, driverName);
    }

    // constructor
    private XmlServerService() {
        super("");
    }

    public XmlServerService(ISupSystem system, int port, long heartbeatPeriod, long statusPeriod, MessageDelimiter delimiter) {
        super(system.getName());

        this.system = system;
        this.port = port;
        this.heartbeatPeriod = heartbeatPeriod;
        this.statusPeriod = statusPeriod;
        this.delimiter = delimiter;
    }
}
