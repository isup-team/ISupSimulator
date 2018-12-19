/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.modbus;

/**
 *
 * @author caronyn
 */
public class Register {


	private int id;
	private String name;
	private Integer value;
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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public WagoConvertTable getConvertTable() {
		return convertTable;
	}

	public void setConvertTable(WagoConvertTable convertTable) {
		this.convertTable = convertTable;
	}

	public void removeConvertTable () {
		convertTable = null;
	}
	
	private Register() {
	}
	
	public Register(int id, String name, Integer value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public Register(int id, String name, Integer value, WagoConvertTable convertTable) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.convertTable = convertTable;
	}

}
