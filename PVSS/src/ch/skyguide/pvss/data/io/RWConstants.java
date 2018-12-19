/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.io;

/**
 *
 * @author CyaNn
 */
public interface RWConstants {

    // Constant
    static final int BACK_CHAR_LIMIT = 5000;

    static final char PATH_SEPARATOR = '.';
    static final char RETURN_CHAR = '\n';
    static final char INDENT_CHAR = '\t';
    static final char SEPARATOR_CHAR = '\t';
    static final char ID_SEPARATOR_CHAR = '#';
    static final char REF_SEPARATOR_CHAR = ':';
    static final char STRING_QUOTE = '\"';
    static final char ALIAS_SPECIAL_CHAR = '@';
    static final String HEXA_PREFIX = "0x";
    static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss.SSS";

    static final String GLOBAL_COMMENT = "# ascii dump of database";

    static final String TYPE_COMMENT = "# DpType";
    static final String TYPE_HEADER = "TypeName";
    static final String TYPE_FORMAT = "%s\t%d#%d";
    static final String REFERENCE_FORMAT = ":%s";

    static final String INSTANCE_COMMENT = "# Datapoint/DpId";
    static final String INSTANCE_HEADER = "DpName\tTypeName\tID";
    static final String INSTANCE_FORMAT = "%s\t%s\t%d";

    static final String ALIAS_COMMENT = "# Aliases/Comments";
    static final String ALIAS_HEADER = "AliasId\tAliasName\tCommentName";
    static final String ALIAS_FORMAT = "%s\t\"%s\"\tlt:%d LANG:%d \"%s@@\"";

    static final String VALUE_COMMENT = "# DpValue";
    static final String VALUE_HEADER = "ElementName\tTypeName\t_original.._value\t_original.._status\t_original.._stimen";
    static final String VALUE_FORMAT = "%s\t%s\t%s\t0x%d\t%5$td:%5$tm:%5$tY %5$tH:%5$tM:%5$tS.%5$tL";

    static final String ALARM_COMMENT = "# AlertValue";
    static final String ALARM_HEADER = "ElementName\tTypeName\tDetailNr\t_alert_hdl.._type\t_alert_hdl.._l_limit\t_alert_hdl.._u_limit\t_alert_hdl.._l_incl\t_alert_hdl.._u_incl\t_alert_hdl.._panel\t_alert_hdl.._panel_param\t_alert_hdl.._help\t_alert_hdl.._min_prio\t_alert_hdl.._class\t_alert_hdl.._text\t_alert_hdl.._active\t_alert_hdl.._orig_hdl\t_alert_hdl.._ok_range\t_alert_hdl.._hyst_type\t_alert_hdl.._hyst_time\t_alert_hdl.._l_hyst_limit\t_alert_hdl.._u_hyst_limit\t_alert_hdl.._text1\t_alert_hdl.._text0\t_alert_hdl.._ack_has_prio\t_alert_hdl.._order\t_alert_hdl.._dp_pattern\t_alert_hdl.._dp_list\t_alert_hdl.._prio_pattern\t_alert_hdl.._abbr_pattern\t_alert_hdl.._ack_deletes\t_alert_hdl.._non_ack\t_alert_hdl.._came_ack\t_alert_hdl.._pair_ack\t_alert_hdl.._both_ack\t_alert_hdl.._impulse\t_alert_hdl.._filter_threshold";
    static final String ALARM_FORMAT = "%s";

    static final String DISTRIB_COMMENT = "# DistributionInfo";
    static final String DISTRIB_HEADER = "ElementName\tTypeName\t_distrib.._type\t_distrib.._driver";
    static final String DISTRIB_FORMAT = "%s\t%s\t%d\t\\%d";

    static final String ADDRESS_COMMENT = "# PeriphAddrMain";
    static final String ADDRESS_HEADER = "ElementName\tTypeName\t_address.._type\t_address.._reference\t_address.._poll_group\t_address.._offset\t_address.._subindex\t_address.._direction\t_address.._internal\t_address.._lowlevel\t_address.._active\t_address.._start\t_address.._interval\t_address.._reply\t_address.._datatype\t_address.._drv_ident";
    static final String ADDRESS_FORMAT = "%s\t%s\t%d\t\"%d_%s\"\t\"%s\"\t0\t0\t\\%d\t0\t0\t1\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t01.01.1970 00:00:00.000\t%d\t\"%s\"";

}
