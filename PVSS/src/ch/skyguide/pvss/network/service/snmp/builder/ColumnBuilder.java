/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.snmp.builder;

import ch.skyguide.pvss.network.service.snmp.SMIAccess;
import ch.skyguide.pvss.network.service.snmp.SMISyntax;
import ch.skyguide.pvss.network.service.snmp.SNMPConvertTable;
import java.io.Serializable;
import org.snmp4j.agent.mo.MOColumn;
import org.snmp4j.smi.Variable;

/**
 *
 * @author caronyn
 */
public class ColumnBuilder implements Builder<MOColumn>, Serializable {

	private String name;
	private int index;
	protected SMIAccess access;
	private SMISyntax syntax;
	private SNMPConvertTable<? extends Variable> convertTable;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SMIAccess getAccess() {
		return access;
	}

	public void setAccess(SMIAccess access) {
		this.access = access;
	}

	public SMISyntax getSyntax() {
		return syntax;
	}

	public void setSyntax(SMISyntax syntax) {
		this.syntax = syntax;
	}

	public SNMPConvertTable<? extends Variable> getConvertTable() {
		return convertTable;
	}

	public void setConvertTable(SNMPConvertTable<? extends Variable> convertTable) {
		this.convertTable = convertTable;
	}

	public void removeConvertTable () {
		convertTable = null;
	}
	
	@Override
	public MOColumn getObject() {
		return new MOColumn(index, syntax.getSmi());
	}

	public ColumnBuilder(String name, int index, SMIAccess access, SMISyntax syntax) {
		this.name = name;
		this.index = index;
		this.access = access;
		this.syntax = syntax;
	}

	public ColumnBuilder(String name, int index, SMIAccess access, SMISyntax syntax, SNMPConvertTable convertTable) {
		this.name = name;
		this.index = index;
		this.access = access;
		this.syntax = syntax;
		this.convertTable = convertTable;
	}
	
}
