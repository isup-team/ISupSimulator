/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.convertTable;

/**
 * Contain the correspondance between value and status.
 * @param <T> Value type.
 * @author caronyn
 */
public class Entry<T> {

	private SystemStatus status;
	private T value;

	/**
	 * Status setter.
	 * @return The status.
	 */
	public SystemStatus getStatus() {
		return status;
	}

	/**
	 * Value getter.
	 * @return Return the value.
	 */
	public T getValue() {
		return value;
	}

	public void setStatus(SystemStatus status) {
		this.status = status;
	}

	public void setValue(T value) {
		this.value = value;
	}

	private Entry() {
	}
	
	/**
	 * Default constructor.
	 * @param status Set immutable status.
	 * @param value Set immutable value.
	 */
	public Entry(SystemStatus status, T value) {
		this.status = status;
		this.value = value;
	}

}
