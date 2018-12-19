/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.homemade;

import java.io.Serializable;

/**
 *
 * @param <T>
 * @author caronyn
 */
public abstract class HMVariant<T> implements Cloneable, Serializable {

	private T value;

	// abstract
	public abstract HMTypeEnum getHMType();

	// property
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	// methode implementation
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HMVariant other = (HMVariant) obj;
		if (this.value != other.getValue()) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + (this.value != null ? this.value.hashCode() : 0);
		return hash;
	}

	// constructor
	public HMVariant(T value) {
		this.value = value;
	}

}
