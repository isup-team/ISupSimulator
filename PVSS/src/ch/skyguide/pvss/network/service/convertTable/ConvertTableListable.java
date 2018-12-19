/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.network.service.convertTable;

import java.util.List;

/**
 *
 * @param <T> 
 * @author caronyn
 */
public interface ConvertTableListable<T extends ConvertTable> {

	void addConvertTable(T e);

	void removeConvertTable(T e);

	List<T> getConvertTables();

}
