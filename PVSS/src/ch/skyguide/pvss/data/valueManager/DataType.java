/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.valueManager;

import ch.skyguide.pvss.data.PVSSException;

/**
 *
 * @author caronyn
 */
public enum DataType {
    STRUCT		(1,	true,	null),
    TYPEREF		(41,    false,	null),
    CHAR		(19,    false,	new CharManager()),
    UINT		(20,    false,	new UIntManager()),
    INT			(21,    false,	new IntManager()),
    FLOAT		(22,    false,	new FloatManager()),
    BOOL		(23,    false,	new BoolManager()),
    BIT32		(24,    false,	new Bit32Manager()),
    STRING		(25,    false,	new StringManager()),
    TIME		(26,    false,	new TimeManager()),
    DPID		(27,    false,	new DpidManager()),
    LANGSTRING		(42,    false,	new LangStringManager()),
    BLOB		(46,    false,	new BlobManager()),
    DYN_CHAR		(3,	false,	new DynManager(CHAR.manager)),
    DYN_UINT		(4,	false,	new DynManager(UINT.manager)),
    DYN_INT		(5,	false,	new DynManager(INT.manager)),
    DYN_FLOAT		(6,	false,	new DynManager(FLOAT.manager)),
    DYN_BOOL		(7,	false,	new DynManager(BOOL.manager)),
    DYN_BIT32		(8,	false,	new DynManager(BIT32.manager)),
    DYN_STRING		(9,	false,	new DynManager(STRING.manager)),
    DYN_TIME		(10,    false,	new DynManager(TIME.manager)),
    DYN_DPID		(29,    false,	new DynManager(DPID.manager)),
    DYN_LANGSTRING	(44,    false,	new DynManager(LANGSTRING.manager)),
    DYN_BLOB		(48,    false,	new DynManager(BLOB.manager)),
    STRUCT_CHAR		(11,    true,	null),
    STRUCT_UINT		(12,    true,	null),
    STRUCT_INT		(13,    true,	null),
    STRUCT_FLOAT	(14,    true,	null),
    STRUCT_BOOL		(15,    true,	null),
    STRUCT_BIT32	(16,    true,	null),
    STRUCT_STRING	(17,    true,	null),
    STRUCT_TIME		(18,    true,	null),
    STRUCT_DPID		(39,    true,	null),
    STRUCT_LANGSTRING	(43,    true,	null),
    STRUCT_BLOB		(47,    true,	null);

    private int id;
    private boolean structure;
    private AbstractManager manager;

    public int getId() {
	return id;
    }

    public boolean isStructure() {
	return structure;
    }

    public AbstractManager getManager() {
	return manager;
    }

    private DataType(int id, boolean structure, AbstractManager manager) {
	this.id = id;
	this.structure = structure;
	this.manager = manager;
    }

    public static DataType get(int id) {
	for (DataType dataType : DataType.values()) {
	    if (dataType.id == id) {
		return dataType;
	    }
	}
	throw new PVSSException(String.format("Type [%d] does not exist", id));
    }

}
