/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.persistency;

import ch.skyguide.pvss.network.service.snmp.SNMPConvertTable;
import ch.skyguide.pvss.network.service.xml.ISupSystem;
import ch.skyguide.pvss.network.service.convertTable.Entry;
import ch.skyguide.pvss.network.service.convertTable.SystemStatus;
import ch.skyguide.pvss.network.persistency.xstreamConverter.ServiceXStreamFormater;
import ch.skyguide.pvss.network.service.snmp.SNMPAgentService;
import java.lang.reflect.InvocationTargetException;
import ch.skyguide.pvss.network.service.ServiceComposite;
import ch.skyguide.pvss.network.service.homemade.HMStatus;
import ch.skyguide.pvss.network.service.homemade.HMStatusEnum;
import ch.skyguide.pvss.network.service.homemade.HomemadeService;
import ch.skyguide.pvss.network.service.homemade.HMSubSystem;
import ch.skyguide.pvss.network.service.modbus.Coil;
import ch.skyguide.pvss.network.service.modbus.WagoConvertTable;
import ch.skyguide.pvss.network.service.modbus.WagoService;
import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import ch.skyguide.pvss.network.service.snmp.SNMPAgentService.Version;
import ch.skyguide.pvss.network.service.snmp.SNMPMIB;
import ch.skyguide.pvss.network.service.snmp.SNMPTrapService;
import ch.skyguide.pvss.network.service.snmp.builder.ColumnBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.RowBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.ScalarBuilder;
import ch.skyguide.pvss.network.service.snmp.builder.TableBuilder;
import ch.skyguide.pvss.network.service.xml.ISupSubSystem;
import ch.skyguide.pvss.network.service.xml.XmlClient;
import ch.skyguide.pvss.network.service.xml.TcpHost;
import ch.skyguide.pvss.network.service.xml.XmlClientService;
import ch.skyguide.pvss.network.service.xml.XmlServerService;
import ch.skyguide.pvss.network.service.socket.MessageDelimiter;
import com.thoughtworks.xstream.XStream;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.junit.Before;
import org.junit.Test;
import org.snmp4j.smi.Opaque;
import static org.junit.Assert.*;

/**
 *
 * @author caronyn
 */
public class XmlFilePersistenceStrategieTest {

	private ServiceComposite root1 = null;
	private ServiceComposite root2 = null;
	private XStream xstream = null;

	public XmlFilePersistenceStrategieTest() {
	}

	@Before
	public void setUp() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		xstream = new XStream();
		new ServiceXStreamFormater().configureXml(xstream);

		compositeBuild();
	}

	private void compositeBuild() {
		// construct root
		root1 = new ServiceComposite("Simulator");

		ServiceComposite gva = new ServiceComposite("ISup-Z");
		root1.addService(gva);

		ServiceComposite optinetAgent = new ServiceComposite("optinet");
		gva.addService(optinetAgent);

		// SNMP
		SNMPAgentService snmpServer = new SNMPAgentService("SNMP Default", 161, Version.v2c, new OctetString("public"));
		SNMPMIB mib = new SNMPMIB("optinet", new OID("1.3.6.1.4.1.637.65"));

		// with value list
		SNMPConvertTable<Integer32> ct1 = new SNMPConvertTable<Integer32>("ConvertTable01");
		ct1.addItem(new Entry(SystemStatus.OPS, new Integer32(1)));
		ct1.addItem(new Entry(SystemStatus.SBY, new Integer32(2)));
		ct1.addItem(new Entry(SystemStatus.U_S, new Integer32(3)));
		ct1.addItem(new Entry(SystemStatus.DEG.UKN, new Integer32(4)));
		SNMPConvertTable<Integer32> ct2 = new SNMPConvertTable("ConvertTable02");
		ct2.addItem(new Entry(SystemStatus.U_S, new Integer32(-1)));
		ct2.addItem(new Entry(SystemStatus.OPS, new Integer32(0)));
		ct2.addItem(new Entry(SystemStatus.DEG, new Integer32(1)));
		mib.addConvertTable(ct1);
		mib.addConvertTable(ct2);

		SNMPConvertTable<Integer32> ct3 = new SNMPConvertTable("ConvertTable03");
		ct3.addItem(new Entry(SystemStatus.U_S, new Integer32(-1)));
		ct3.addItem(new Entry(SystemStatus.OPS, new Integer32(0)));
		ct3.addItem(new Entry(SystemStatus.DEG, new Integer32(1)));

		mib.addBuilder(new ScalarBuilder("unsolicitedEventsEnabled", new OID("1.1.1.1.0"), SMIAccess.READ_ONLY, new Integer32(1)));
		mib.addBuilder(new ScalarBuilder("descr", new OID("1.1.1.3.0"), SMIAccess.READ_ONLY, new OctetString("Optinet")));
		mib.addBuilder(new ScalarBuilder("t1", new OID("1.1.1.4.0"), SMIAccess.READ_ONLY, new Integer32(52), ct1));
		mib.addBuilder(new ScalarBuilder("t11", new OID("1.1.1.10.0"), SMIAccess.READ_ONLY, new Integer32(52), ct2));
		mib.addBuilder(new ScalarBuilder("t2", new OID("1.1.1.5.0"), SMIAccess.READ_ONLY, new Integer32(52)));
		mib.addBuilder(new ScalarBuilder("t3", new OID("1.1.1.7.0"), SMIAccess.READ_ONLY, new Counter64(52)));
		mib.addBuilder(new ScalarBuilder("t4", new OID("1.1.1.8.0"), SMIAccess.READ_ONLY, new Gauge32(52)));
		mib.addBuilder(new ScalarBuilder("t5", new OID("1.1.1.9.0.0"), SMIAccess.READ_ONLY, new TimeTicks(52)));
		mib.addBuilder(new ScalarBuilder("t7", new OID("1.1.1.9.1.0"), SMIAccess.READ_ONLY, new IpAddress("156.135.81.124")));
		mib.addBuilder(new ScalarBuilder("t8", new OID("1.1.1.9.2.0"), SMIAccess.READ_ONLY, new TcpAddress("156.135.81.124/161")));
		mib.addBuilder(new ScalarBuilder("t9", new OID("1.1.1.9.3.0"), SMIAccess.READ_ONLY, new UdpAddress("156.135.81.124/162")));
		mib.addBuilder(new ScalarBuilder("t10", new OID("1.1.1.9.4.0"), SMIAccess.READ_ONLY, new Null()));
		byte[] bytes = {0x0f, 0x52};
		mib.addBuilder(new ScalarBuilder("t12", new OID("1.1.1.9.5.0"), SMIAccess.READ_ONLY, new Opaque(bytes)));

		TableBuilder tb = new TableBuilder("alarmTable", new OID("1.1.1.2.1"), SMIAccess.READ_ONLY);
		tb.addColumnBuilder(new ColumnBuilder("currentAlarmId", 1, SMIAccess.READ_ONLY, SMISyntax.INTEGER));
		tb.addColumnBuilder(new ColumnBuilder("friendlyName", 2, SMIAccess.READ_ONLY, SMISyntax.OCTET_STRING));

		RowBuilder rowb1 = new RowBuilder(new OID("3647"));
		rowb1.addValue(new Integer32(3647));
		rowb1.addValue(new OctetString("Test 7"));

		RowBuilder rowb2 = new RowBuilder(new OID("3648"));
		rowb2.addValue(new Integer32(3648));
		rowb2.addValue(new OctetString("Test 8"));
		tb.addRowBuilder(rowb1);
		tb.addRowBuilder(rowb2);

		mib.addBuilder(tb);
		snmpServer.addMib(mib);
		optinetAgent.addService(snmpServer);

		SNMPTrapService trapService = new SNMPTrapService("ScNet", "156.135.81.124", "156.135.164.50", 162, 1000, new OID(".1.3.6.1.4.1.123"), new OctetString("public"), 6, 1);
		trapService.addConvertTable(ct3);
		trapService.addBuilder(new ScalarBuilder("Trap 1 ", new OID(".1.3.6.1.4.1.123.3.44.1.5.1"), SMIAccess.READ_ONLY, new OctetString("Test traps")));
		trapService.addBuilder(new ScalarBuilder("Trap 2 ", new OID(".1.3.6.1.4.1.123.3.44.1.5.2"), SMIAccess.READ_ONLY, new OctetString("Test traps 2")));
		trapService.addBuilder(new ScalarBuilder("Trap 3 ", new OID(".1.3.6.1.4.1.123.3.44.1.5.3"), SMIAccess.READ_ONLY, new Integer32(1), ct3));
		gva.addService(trapService);

		// XML Client
		XmlClientService xmlClient = new XmlClientService("xml client");
		gva.addService(xmlClient);

		xmlClient.addHost(new TcpHost("156.135.81.226", 5000));
		xmlClient.addHost(new TcpHost("156.135.81.227", 5000));

		// FDPZ
		ISupSystem fdpz = new ISupSystem("FDPZ", "");
		xmlClient.addService(new XmlClient("FDPZ", 10000, MessageDelimiter.ZERO, fdpz));

		ISupSubSystem mainServer_1 = new ISupSubSystem("MainServer_1", SystemStatus.OPS);
		mainServer_1.setLabel1("OPS");
		mainServer_1.setLabel2("Test");
		mainServer_1.setMessage("test message");

		ISupSubSystem mainServer_2 = new ISupSubSystem("MainServer_2", SystemStatus.U_S);
		fdpz.addSubSystem(mainServer_1);
		fdpz.addSubSystem(mainServer_2);

		// AFTN
		ISupSystem afps = new ISupSystem("AFPS", "");
		xmlClient.addService(new XmlClient("AFPS", 10000, MessageDelimiter.ZERO, afps));
		afps.addSubSystem(new ISupSubSystem("ct_service", SystemStatus.OPS));
		afps.addSubSystem(new ISupSubSystem("ct_idm", SystemStatus.OPS));
		afps.addSubSystem(new ISupSubSystem("ct_ruleengine", SystemStatus.OPS));
		afps.addSubSystem(new ISupSubSystem("ct_aftnio", SystemStatus.OPS));
		afps.addSubSystem(new ISupSubSystem("ct_servicebackup", SystemStatus.OPS));
		afps.addSubSystem(new ISupSubSystem("ct_idmbackup", SystemStatus.OPS));
		afps.addSubSystem(new ISupSubSystem("ct_ruleenginebackup", SystemStatus.OPS));
		afps.addSubSystem(new ISupSubSystem("ct_aftniobackup", SystemStatus.OPS));

		// XML Server
		// Crystal
		ISupSystem crystal = new ISupSystem("CRYSTAL", "");
		gva.addService(new XmlServerService(crystal, 5010, 3000, 5000, MessageDelimiter.PIPE));
		crystal.addSubSystem(new ISupSubSystem("broker_LSAS_A", SystemStatus.OPS));
		crystal.addSubSystem(new ISupSubSystem("broker_LSAS_B", SystemStatus.OPS));

		ISupSystem inch = new ISupSystem("INCH", "");
		gva.addService(new XmlServerService(inch, 5000, 3000, 5000, MessageDelimiter.PIPE));
		inch.addSubSystem(new ISupSubSystem("broker_LSGG_A", SystemStatus.OPS));
		inch.addSubSystem(new ISupSubSystem("broker_LSGG_B", SystemStatus.OPS));

		// Homemade
		HomemadeService homemade = new HomemadeService("CPR", 9069, 10000, 50000);
		homemade.addSubSystem(new HMSubSystem("ART_ARTAS", new HMStatus(HMStatusEnum.OPS)));
		homemade.addSubSystem(new HMSubSystem("ART_RMCDE", new HMStatus(HMStatusEnum.OPS)));
		gva.addService(homemade);

		// WAGO
		WagoService dole = new WagoService("Dole", "COM1", 1);
		WagoConvertTable<Boolean> wct = new WagoConvertTable<Boolean>("Direct table");
		wct.addItem(new Entry<Boolean>(SystemStatus.OPS, Boolean.TRUE));
		wct.addItem(new Entry<Boolean>(SystemStatus.U_S, Boolean.FALSE));
		dole.addConvertTable(wct);
		dole.addCoil(new Coil(1, "Alarm générale", Boolean.TRUE, wct));
		dole.addCoil(new Coil(2, "RDC onduleurs planchés", Boolean.TRUE));

		gva.addService(dole);

	}

	@Test
	public void testSerialization() {
		// serialize
		String xml1 = xstream.toXML(root1);

		// output
		System.out.println(xml1);

		// unit test
		assertNotNull(xml1);
		assertNotSame(xml1, "");
	}

	@Test
	public void testSerializationAndDeserialization() {
		// serialize
		String xml1 = xstream.toXML(root1);

		// deserialize
		root2 = (ServiceComposite) xstream.fromXML(xml1);
		String xml2 = xstream.toXML(root2);

		// unit test
		assertEquals(xml1, xml2);
	}
/*
	@Test
	public void testSystemXMLSerialization() {
	ServiceComposite gva = (ServiceComposite) root1.getService(0);
	XmlClientService xmlService = (XmlClientService) gva.getService("xml client");
	XmlMessageFormater formater = new XmlMessageFormater("1.0", "UTF-8", '\0', new ServiceXStreamFormater(new SimulatorXStreamFormater()));

	// FDPZ
	String xml1 = formater.format(new ISupStatus(xmlService.getService("FDPZ").getSystem()));
	System.out.println(xml1);

	assertNotSame(xml1, "");

	// AFPS
	String xml2 = formater.format(new ISupStatus(xmlService.getService("AFPS").getSystem()));
	System.out.println(xml2);

	assertNotSame(xml2, "");
	assertNotSame(xml1, xml2);

	}*/
}
