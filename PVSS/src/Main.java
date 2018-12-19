/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.skyguide.pvss.data.gui.TreeNodeDPAdapter;
import ch.skyguide.pvss.data.Database;
import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.io.DatabaseReader;
import ch.skyguide.pvss.data.io.DatabaseWriter;
import ch.skyguide.pvss.data.pattern.Pattern;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 *
 * @author CyaNn
 */
public class Main {

    public static void main(String[] args) {
        //Database db = new Database();
        testSerialization();
        //testParsing(db);
        //testPattern(db);
        //testTree();
    }

    private static void testSerialization() {
    }

    private static void testTree() {
        List<Integer> ints = new ArrayList<Integer>();
        ints.add(Integer.SIZE);
        ints.get(1);
    }

    public static void testParsing(Database db) {
        FileReader fr = null;

        try {

            fr = new FileReader("PVSSTypes.dpl");
            BufferedReader bfr = new BufferedReader(fr);
            DatabaseReader dbr = new DatabaseReader(bfr);
            dbr.readDatabase(db);
            dbr.close();

            JFrame f = new JFrame("test");
            //JTree t = new JTree(db.getDpType("Composite"));
            JTree t = new JTree(new TreeNodeDPAdapter(db.getDPRoot()));
            f.add(new JScrollPane(t));
            f.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    System.exit(0);
                }
            });
            f.setSize(new Dimension(600, 800));
            f.setVisible(true);

            OutputStreamWriter osw = new OutputStreamWriter(System.out);
            DatabaseWriter dw = new DatabaseWriter(osw);
            dw.writeDatabase(db);
            osw.flush();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void testPattern(Database db) {
        //Pattern pattern = new Pattern("sgFw*.Comp{onent.Label{0,X,_3},anion.lbl}{1,2,3,4}.PreStatus");
        long time = Calendar.getInstance().getTimeInMillis();

        System.out.println("PATTERN");
        Pattern pattern = new Pattern("DP.Structs.structchar[2]");
        System.out.println(pattern.toString());
        System.out.println("");
        System.out.println("RESULT");
        Collection<DP> dps = pattern.find(db.getDPRoot());

        time = (Calendar.getInstance().getTimeInMillis() - time);

        for (DP dp : dps) {
            System.out.println(dp.getPath().toString());
        }

        System.out.println("Time remained : " + time);
    }
}
