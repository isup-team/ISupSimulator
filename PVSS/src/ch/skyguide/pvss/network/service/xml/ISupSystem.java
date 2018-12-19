/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.network.service.xml;

import ch.skyguide.pvss.network.service.ListContainer;
import java.util.LinkedList;

/**
 *
 * @author caronyn
 */
public final class ISupSystem implements ListContainer<ISupSubSystem>{

	private String name;
	private String source;
	private LinkedList<ISupSubSystem> subSystems;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public void lazyInitializeSubSystems() {
		if (subSystems == null) {
			subSystems = new LinkedList<ISupSubSystem>();
		}
	}

	@Override
	public boolean addSubSystem(ISupSubSystem e) {
		lazyInitializeSubSystems();
		return subSystems.add(e);
	}

	@Override
	public void addSubSystem(int index, ISupSubSystem e) {
		lazyInitializeSubSystems();
		subSystems.add(index, e);
	}

	@Override
	public boolean removeSubSystem(ISupSubSystem o) {
		lazyInitializeSubSystems();
		return subSystems.remove(o);
	}

	@Override
	public ISupSubSystem removeSubSystem(int i) {
		lazyInitializeSubSystems();
		return subSystems.remove(i);
	}

	@Override
	public ISupSubSystem getSubSystem(int index) {
		lazyInitializeSubSystems();
		return subSystems.get(index);
	}

	@Override
	public Iterable<ISupSubSystem> getSubSystems() {
		lazyInitializeSubSystems();
		return subSystems;
	}

	private ISupSystem() {
		lazyInitializeSubSystems();
	}
	
	public ISupSystem(String name, String source) {
		this.name = name;
		this.source = source;
		lazyInitializeSubSystems();
	}
}
