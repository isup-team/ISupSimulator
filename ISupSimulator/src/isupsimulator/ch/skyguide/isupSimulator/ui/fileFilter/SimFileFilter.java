package isupsimulator.ch.skyguide.isupSimulator.ui.fileFilter;

import isupsimulator.ch.skyguide.isupSimulator.conf.Config;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Filter for xml file extention.
 * @author caronyn
 */
public class SimFileFilter extends FileFilter {

	/**
	 * Inherited, accept if extention is xml.
	 * @param f file to accept
	 * @return Return true if format match
	 */
	@Override
	public boolean accept(File f) {
		return (f.isDirectory() || f.getName().endsWith("." + Config.SIM_EXTENSION));
	}

	/**
	 * Inherited, give the description of the file extension.
	 * @return Return the description.
	 */
	@Override
	public String getDescription() {
		return "Sim file (*." + Config.SIM_EXTENSION + ")";
	}
}
