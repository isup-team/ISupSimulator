/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.io;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.DPElement;
import ch.skyguide.pvss.data.dataPointType.DPType;
import ch.skyguide.pvss.data.dataPointType.ElementType;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.data.dataPointType.ReferenceType;
import ch.skyguide.pvss.data.io.parser.AliasParser;
import ch.skyguide.pvss.data.io.parser.InstanceParser;
import ch.skyguide.pvss.data.io.parser.TypeParser;
import ch.skyguide.pvss.data.io.parser.ValueParser;
import ch.skyguide.pvss.data.valueManager.DataType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

/**
 *
 * @author CyaNn
 */
public class DatabaseReader extends Reader implements RWConstants {

    // variables
    private BufferedReader reader;

    private static int getLevel(String line) {
	int i = 0;
	while (line.charAt(i) == INDENT_CHAR) {
	    i++;
	}
	return i;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
	return reader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
	reader.close();
    }

    public DatabaseReader(BufferedReader reader) {
	this.reader = reader;
    }

    public void readDatabase(Database db) throws IOException {

	String line;

	while ((line = reader.readLine()) != null) {
	    if (line.equals(TYPE_COMMENT)) {
		jumpLine();
		readTypes(db, 0);
	    } else if (line.equals(INSTANCE_COMMENT)) {
		jumpLine();
		readInstance(db);
	    } else if (line.equals(ALIAS_COMMENT)) {
		jumpLine();
		readAlias(db);
	    } else if (line.equals(VALUE_COMMENT)) {
		jumpLine();
		readValue(db);
	    }
	    // TODO : Finish read with alarm ect....
	}

    }

    private void jumpLine() throws IOException {
	if (reader.readLine() == null) {
	    throw new IOException("PVSS Ascii bad format");
	}
    }

    private Vector<DPType> readTypes(Database db, int level) throws IOException {
	Vector<DPType> ret = new Vector<DPType>();
	String line;

	reader.mark(BACK_CHAR_LIMIT);
	while ((line = reader.readLine()) != null && !line.equals("") && getLevel(line) == level) {
	    Vector<DPType> children = readTypes(db, level + 1);
	    line = line.substring(level);

	    TypeParser parse = new TypeParser(line);
	    if (level == 0) {
		NodeType node = (NodeType) db.getDpType(parse.getTypeName());
		node.setDataType(DataType.get(parse.getDataTypeId()));
		if (children.size() == 0) {
		    throw new IOException(String.format("PVSS Ascii bad format ! First level [%s] must be a node", parse.getTypeName()));
		}
		node.addAll(children);
	    } else if (DataType.get(parse.getDataTypeId()).isStructure()) {
		NodeType node = new NodeType(parse.getTypeName(), DataType.get(parse.getDataTypeId()));
		node.addAll(children);
		ret.add(node);
	    } else if (parse.getReferenceName() != null) {
		ret.add(new ReferenceType(parse.getTypeName(), db.getDpType(parse.getReferenceName())));
	    } else {
		ret.add(new ElementType(parse.getTypeName(), DataType.get(parse.getDataTypeId())));
	    }
	}
	reader.reset();

	return ret;

    }

    private void readInstance(Database db) throws IOException {
	String line;

	while ((line = reader.readLine()) != null && !line.equals("")) {
	    InstanceParser parse = new InstanceParser(line);
	    db.addInstance((NodeType) db.getDpType(parse.getTypeName()), parse.getDpName());
	}
    }

    private void readAlias(Database db) throws IOException {
	String line;

	while ((line = reader.readLine()) != null && !line.equals("")) {
	    AliasParser parse = new AliasParser(line);
	    DP point = db.getDataPoint(parse.getAliasId());

	    if (point == null) {
		throw new IOException(String.format("PVSS ascii bad format ! Point [%s] unreachable", parse.getAliasName()));
	    }
	    point.getCommonInstance().setAlias(parse.getCommentName());
	}

    }

    private void readValue(Database db) throws IOException {
	String line;

	while ((line = reader.readLine()) != null && !line.equals("")) {
	    ValueParser parse = new ValueParser(line);
	    DP point = db.getDataPoint(parse.getElementName());

	    if (point == null) {
		throw new IOException(String.format("PVSS ascii bad format ! Point [%s] unreachable", parse.getElementName()));
	    } else if (!(point instanceof DPElement)) {
		throw new IOException(String.format("PVSS ascii bad format ! Point [%s] is not an element, value cannot be set", parse.getElementName()));
	    }

	    DPElement element = (DPElement) point;

	    Object value = element.getDataType().getManager().fromDpl(parse.getOriginal_value());
	    element.getValueInstance().setValue(value, parse.getOriginal_status(), parse.getOriginal_stime());

	}

    }
}
