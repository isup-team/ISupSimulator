/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.persistency;

import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.util.logging.*;

/**
 *
 * @author caronyn
 */
public class XmlFilePersistenceStrategy<T> implements PersistenceStrategy<T> {

    // attributes
    private XStreamFormater formater;
    private String fileName;

    // methodes
    @Override
    public T retrieveObjectTree() {
	T object = null;
	XStream xstream = new XStream();
	formater.configureXml(xstream);

	Reader in = null;
	try {
	    in = new FileReader(fileName);
	    object = (T)xstream.fromXML(in);
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(XmlFilePersistenceStrategy.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    try {
		in.close();
	    } catch (IOException ex) {
		Logger.getLogger(XmlFilePersistenceStrategy.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

	return object;
    }

    @Override
    public void storeObjectTree(T object) {
	XStream xstream = new XStream();
	formater.configureXml(xstream);

	Writer out = null;
	try {
	    out = new FileWriter(fileName);
	    xstream.toXML(object, out);
	} catch (IOException ex) {
	    Logger.getLogger(XmlFilePersistenceStrategy.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    try {
		out.close();
	    } catch (IOException ex) {
		Logger.getLogger(XmlFilePersistenceStrategy.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}


    }

    // constructor
    public XmlFilePersistenceStrategy(XStreamFormater formater, String fileName) {
	this.formater = formater;
	this.fileName = fileName;
    }

}
