/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.pvss.data.io;

import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.PVSSException;
import ch.skyguide.pvss.data.dataPointElement.ConfigAddress;
import ch.skyguide.pvss.data.dataPointElement.ConfigAlarm;
import ch.skyguide.pvss.data.dataPointElement.ConfigCommon;
import ch.skyguide.pvss.data.dataPointElement.ConfigDistrib;
import ch.skyguide.pvss.data.dataPointElement.ConfigValue;
import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.DPCoverVisitor;
import ch.skyguide.pvss.data.dataPointElement.Instance;
import ch.skyguide.pvss.data.dataPointElement.Root;
import ch.skyguide.pvss.data.dataPointType.DPType;
import ch.skyguide.pvss.data.dataPointType.DPTypeCoverVisitor;
import ch.skyguide.pvss.data.dataPointType.ElementType;
import ch.skyguide.pvss.data.dataPointType.NodeType;
import ch.skyguide.pvss.data.dataPointType.ReferenceType;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

/**
 *
 * @author CyaNn
 */
public class DatabaseWriter extends Writer implements RWConstants {

    // inner class
    class WriterVisitor implements DPTypeCoverVisitor {

        private int i = 0;

        public boolean linkReference() {
            return false;
        }

        public void NodeTypeCover(NodeType dpType, int level) {
            try {
                writeRepeat(DatabaseWriter.this, INDENT_CHAR, level);
                write(String.format(TYPE_FORMAT, dpType.getName(), dpType.getDataType().getId(), i++));
                write(RETURN_CHAR);
            } catch (IOException ex) {
                throw new PVSSException(ex.toString());
            }
        }

        public void ReferenceTypeCover(ReferenceType dpType, int level) {
            try {
                writeRepeat(DatabaseWriter.this, INDENT_CHAR, level);
                write(String.format(TYPE_FORMAT, dpType.getName(), dpType.getDataType().getId(), i++));
                write(String.format(REFERENCE_FORMAT, dpType.getDpType().getName()));
                write(RETURN_CHAR);
            } catch (IOException ex) {
                throw new PVSSException(ex.toString());
            }
        }

        public void ElementTypeCover(ElementType dpType, int level) {
            try {
                writeRepeat(DatabaseWriter.this, INDENT_CHAR, level);
                write(String.format(TYPE_FORMAT, dpType.getName(), dpType.getDataType().getId(), i++));
                write(RETURN_CHAR);
            } catch (IOException ex) {
                throw new PVSSException(ex.toString());
            }
        }
    }

    class AliasVisitor extends DPCoverVisitor {

        @Override
        protected void ConfigCommonCover(ConfigCommon config, int level) {
            try {
                // TODO : manage language
                // TODO : manage get full name
                write(String.format(ALIAS_FORMAT, config.getDp().getPath(), "", 1, 5, config.getAlias()));
                write(RETURN_CHAR);
            } catch (IOException ex) {
                throw new PVSSException(ex.toString());
            }
        }
    }

    class ValueVisitor extends DPCoverVisitor {

        @Override
        protected void ConfigValueCover(ConfigValue config, int level) {
            try {
                String value = config.getDp().getDataType().getManager().toDpl(config.getValue());
                write(String.format(VALUE_FORMAT, config.getDp().getPath(), config.getDp().getInstance().getDpType().getName(), value, config.getStatus(), config.getStatusDate()));
                write(RETURN_CHAR);
            } catch (IOException ex) {
                throw new PVSSException(ex.toString());
            }
        }
    }

    class AlarmVisitor extends DPCoverVisitor {

        @Override
        protected void ConfigAlarmCover(ConfigAlarm config, int level) {
            try {
                // TODO : complete write
                write(String.format(ALARM_FORMAT, config.getDp().getPath()));
                write(RETURN_CHAR);
            } catch (IOException ex) {
                throw new PVSSException(ex.toString());
            }
        }
    }

    class DistribVisitor extends DPCoverVisitor {

        @Override
        protected void ConfigDistribCover(ConfigDistrib config, int level) {
            try {
                write(String.format(DISTRIB_FORMAT, config.getDp().getPath(), config.getDpType().getName(), 56, config.getDriverId()));
                write(RETURN_CHAR);
            } catch (IOException ex) {
                throw new PVSSException(ex.toString());
            }
        }
    }

    class AddressVisitor extends DPCoverVisitor {

        @Override
        protected void ConfigAddressCover(ConfigAddress config, int level) {
            try {
                write(String.format(ADDRESS_FORMAT, config.getDp().getPath(), config.getDpType().getName(), 16, config.getAgentId(), config.getAddress(), config.getPollGroup(), config.getDirection(), config.getDataType(), config.getDriverName()));
                write(RETURN_CHAR);
            } catch (IOException ex) {
                throw new PVSSException(ex.toString());
            }
        }
    }
    // variable
    private Writer writer;

    // abstract methode
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    public static void writeRepeat(Writer out, char c, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            out.write(c);
        }
    }

    // constructor
    public DatabaseWriter(Writer writer) {
        this.writer = writer;
    }

    public void writeHeader(Database db) throws IOException {
        if (db != null) {
            this.write(GLOBAL_COMMENT + RETURN_CHAR + RETURN_CHAR);
        }
    }

    public void writeType(Database db) throws IOException {
        if (db != null) {
            this.write(TYPE_COMMENT + RETURN_CHAR);
            this.write(TYPE_HEADER + RETURN_CHAR);
            for (DPType dpType : db.getDpTypes()) {
                dpType.cover(new WriterVisitor());
            }
            this.write(RETURN_CHAR);
        }
    }

    public void writeInstance(Database db) throws IOException {
        if (db != null) {
            this.write(INSTANCE_COMMENT + RETURN_CHAR);
            this.write(INSTANCE_HEADER + RETURN_CHAR);
            int i = 1;
            for (DP dp : db.getDPRoot().getChildren()) {
                Instance instance = (Instance) dp;
                this.write(String.format(INSTANCE_FORMAT, instance.getName(), instance.getDpType().getName(), i++));
                this.write(RETURN_CHAR);
            }
            this.write(RETURN_CHAR);
        }
    }

    public void writeAlias(Database db) throws IOException {
        if (db != null) {
            this.write(ALIAS_COMMENT + RETURN_CHAR);
            this.write(ALIAS_HEADER + RETURN_CHAR);
            db.getDPRoot().cover(new AliasVisitor());
            this.write(RETURN_CHAR);
        }
    }

    public void writeValue(Database db) throws IOException {
        if (db != null) {
            this.write(VALUE_COMMENT + RETURN_CHAR);
            this.write(VALUE_HEADER + RETURN_CHAR);
            db.getDPRoot().cover(new ValueVisitor());
            this.write(RETURN_CHAR);
        }
    }

    public void writeDistrib(Database db) throws IOException {
        if (db != null) {
            this.write(DISTRIB_COMMENT + RETURN_CHAR);
            this.write(DISTRIB_HEADER + RETURN_CHAR);
            db.getDPRoot().cover(new DistribVisitor());
            this.write(RETURN_CHAR);
        }
    }

    public void writeAddress(Database db) throws IOException {
        if (db != null) {
            this.write(ADDRESS_COMMENT + RETURN_CHAR);
            this.write(ADDRESS_HEADER + RETURN_CHAR);
            db.getDPRoot().cover(new AddressVisitor());
            this.write(RETURN_CHAR);
        }
    }

    public void writeAlarm(Database db) throws IOException {
        if (db != null) {
            this.write(ALARM_COMMENT + RETURN_CHAR);
            this.write(ALARM_HEADER + RETURN_CHAR);
            db.getDPRoot().cover(new AlarmVisitor());
            this.write(RETURN_CHAR);
        }
    }
    // methode

    public void writeDatabase(Database db) throws IOException {
        writeHeader(db);
        writeType(db);
        writeInstance(db);
        writeAlias(db);
        writeValue(db);



        writeDistrib(db);
        writeAddress(db);
    }
}
