/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.modbus;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.network.service.ServiceLeaf;
import ch.skyguide.pvss.network.service.convertTable.ConvertTableListable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.procimg.DigitalOut;
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.SimpleDigitalOut;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.SerialParameters;

/**
 *
 * @author caronyn
 */
public class WagoService extends ServiceLeaf implements ProcessImageContainer, ConvertTableListable<WagoConvertTable> {

    // constant
    public static final int COILS_NUMBER = 512;
    public static final int REGISTERS_NUMBER = 32;
    // attributes
    private String port;
    private int id;
    private BaudRate baudRate;
    private DataBits dataBits;
    private Parity parity;
    private StopBits stopBits;
    private boolean echo;
    private Encoding encoding;
    private List<WagoConvertTable> convertTables;
    private LinkedList<Coil> coils;
    private LinkedList<Register> registers;
    private Thread thread;

    // inner class
    private class ModbusThread extends Thread {

        private ModbusSerialListener listener;

        @Override
        public void run() {
            try {
                ModbusCoupler.getReference().setProcessImage(new DynamicProcessImage(WagoService.this));
                ModbusCoupler.getReference().setMaster(false);
                ModbusCoupler.getReference().setUnitID(id);

                SerialParameters params = new SerialParameters();
                params.setPortName(port);
                params.setBaudRate(baudRate.getRate());
                params.setDatabits(dataBits.getBit());
                params.setParity(parity.toString());
                params.setStopbits(stopBits.getBit());
                params.setEcho(echo);
                params.setEncoding(encoding.getValue());

                listener = new ModbusSerialListener(params);
                WagoService.this.setStatus(Status.STARTED);
                listener.setListening(true);
                WagoService.this.setStatus(Status.STOPPED);

                listener = null;
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        @Override
        public void interrupt() {
            listener.setListening(false);
        }
    }

    // function
    private void initialize() {
        if (coils == null) {
            coils = new LinkedList<Coil>();
        }

        if (registers == null) {
            registers = new LinkedList<Register>();
        }
    }

    // property
    public BaudRate getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(BaudRate baudRate) {
        this.baudRate = baudRate;
    }

    public DataBits getDataBits() {
        return dataBits;
    }

    public void setDataBits(DataBits dataBits) {
        this.dataBits = dataBits;
    }

    public boolean isEcho() {
        return echo;
    }

    public void setEcho(boolean echo) {
        this.echo = echo;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Parity getParity() {
        return parity;
    }

    public void setParity(Parity parity) {
        this.parity = parity;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public StopBits getStopBits() {
        return stopBits;
    }

    public void setStopBits(StopBits stopBits) {
        this.stopBits = stopBits;
    }

    public Coil getCoil(int index) {
        return coils.get(index);
    }

    public Register getReg(int index) {
        return registers.get(index);
    }

    public WagoConvertTable getConvertTable(String name) {
        for (WagoConvertTable table : convertTables) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public LinkedList<Coil> getCoils() {
        return coils;
    }

    public LinkedList<Register> getRegisters() {
        return registers;
    }

    public void addCoil(Coil c) {
        coils.add(c);
    }

    public boolean addRegister(Register e) {
        return registers.add(e);
    }

    // methode implementation
    @Override
    public void addConvertTable(WagoConvertTable e) {
        if (convertTables == null) {
            convertTables = new ArrayList<WagoConvertTable>();
        }
        convertTables.add(e);
    }

    @Override
    public void removeConvertTable(WagoConvertTable e) {
        // integrity check
        for (Coil c : coils) {
            if (e.equals(c.getConvertTable())) {
                c.removeConverTable();
            }
        }

        for (Register r : registers) {
            if (e.equals(r.getConvertTable())) {
                r.removeConvertTable();
            }
        }

        convertTables.remove(e);
    }

    @Override
    public List<WagoConvertTable> getConvertTables() {
        if (convertTables == null) {
            convertTables = new ArrayList<WagoConvertTable>();
        }
        return convertTables;
    }

    @Override
    public DigitalOut getDigitalOut(int ref) throws IllegalAddressException {
        for (Coil c : coils) {
            if (c.getId() == ref + 1) {
                return new SimpleDigitalOut(c.getValue());
            }
        }

        return new SimpleDigitalOut(false);
    }

    @Override
    public net.wimpi.modbus.procimg.Register getRegister(int ref) throws IllegalAddressException {
        for (Register r : registers) {
            if (r.getId() == ref + 1) {
                int i = ((r.getValue() - 4000) * 32768 / 16000);
                return new SimpleRegister(i);
            }
        }
        return new SimpleRegister(0);
    }

    @Override
    public void startAgent() {
        thread = new ModbusThread();
        thread.start();
    }

    @Override
    public void stopAgent() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void restartAgent() {
    }

    @Override
    public void buildDpl(Database db) {
        // do nothing yet
    }

    // constructor
        private WagoService() {
        super("");
        initialize();
    }

    public WagoService(String name, String port, int id, BaudRate baudRate, DataBits dataBits, Parity parity, StopBits stopBits, boolean echo, Encoding encoding) {
        super(name);
        this.port = port;
        this.id = id;
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.parity = parity;
        this.stopBits = stopBits;
        this.echo = echo;
        this.encoding = encoding;
        initialize();
    }

    public WagoService(String name, String port, int id) {
        super(name);
        this.port = port;
        this.id = id;
        this.baudRate = BaudRate.BR_19200;
        this.dataBits = DataBits.B_8;
        this.parity = Parity.NONE;
        this.stopBits = StopBits.B_1;
        this.echo = false;
        this.encoding = Encoding.ASCII;
        initialize();
    }
}
