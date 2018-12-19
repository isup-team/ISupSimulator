/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.modbus;

/**
 *
 * @author caronyn
 */
public class Coil {

	private int id;
	private String name;
	private Boolean value;
	private WagoConvertTable convertTable;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	public WagoConvertTable getConvertTable() {
		return convertTable;
	}

	public void setConvertTable(WagoConvertTable convertTable) {
		this.convertTable = convertTable;
	}

	public void removeConverTable() {
		this.convertTable = null;
	}
	
	private Coil() {
	}
	
	public Coil(int id, String name, Boolean value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public Coil(int id, String name, Boolean value, WagoConvertTable convertTable) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.convertTable = convertTable;
	}

}
