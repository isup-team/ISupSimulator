/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency.xstreamConverter;

import ch.skyguide.pvss.network.service.convertTable.SystemStatus;
import ch.skyguide.pvss.network.service.xml.ISupCommand;
import ch.skyguide.pvss.network.service.xml.ISupLabel;
import ch.skyguide.pvss.network.service.xml.ISupStatus;
import ch.skyguide.pvss.network.service.xml.ISupSubSystem;
import ch.skyguide.pvss.network.service.xml.ISupSystem;
import ch.skyguide.pvss.persistency.XStreamFormater;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author caronyn
 */
public class SimulatorXStreamFormater extends XStreamFormater {

    @Override
    protected void configure(XStream xstream) {

        // ISup status
        xstream.aliasType("ISupStatus", ISupStatus.class);
        xstream.useAttributeFor(ISupStatus.class, "version");
        xstream.aliasAttribute(ISupStatus.class, "version", "Version");
        xstream.aliasField("System", ISupStatus.class, "system");

        // ISup command
        xstream.aliasType("ISupCommand", ISupCommand.class);
        xstream.useAttributeFor(ISupCommand.class, "version");
        xstream.aliasAttribute(ISupCommand.class, "version", "Version");
        xstream.aliasField("System", ISupCommand.class, "system");

        // ISup System
        xstream.aliasAttribute(ISupSystem.class, "name", "Name");
        xstream.aliasAttribute(ISupSystem.class, "source", "Source");
        xstream.omitField(ISupSystem.class, "source");

        // ISup sub system
        xstream.aliasType("SubSystem", ISupSubSystem.class);
        xstream.omitField(ISupSubSystem.class, "mode");
        xstream.omitField(ISupSubSystem.class, "dpName");
        xstream.aliasAttribute(ISupSubSystem.class, "name", "Name");
        xstream.aliasAttribute(ISupSubSystem.class, "status", "Status");
        xstream.aliasField("Message", ISupSubSystem.class, "message");

        // Label
        xstream.aliasType("Label", ISupLabel.class);

        // Converters
        xstream.registerConverter(new LabelConverter());
        xstream.registerConverter(new EnumInstanceConverter<SystemStatus>(SystemStatus.class, SystemStatus.U_S, "U/S"));
    }

    public SimulatorXStreamFormater() {
        super();
    }

    public SimulatorXStreamFormater(XStreamFormater formater) {
        super(formater);
    }
}
