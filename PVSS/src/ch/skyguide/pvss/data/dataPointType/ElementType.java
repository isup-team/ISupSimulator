/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.dataPointType;

import ch.skyguide.pvss.data.dataPointElement.DP;
import ch.skyguide.pvss.data.dataPointElement.DPElement;
import ch.skyguide.pvss.data.valueManager.DataType;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 *
 * @author CyaNn
 */
public class ElementType extends DPType {

    private DataType dataType;

    public DataType getDataType() {
	return dataType;
    }

    @Override
    public DP createDataPoint(String name) {
	DPElement element = new DPElement(name, dataType);
	addNameChangedListener(element);
	return element;
    }

    public ElementType(String name, DataType dataType) {
        super(name);
        this.dataType = dataType;
    }

    @Override
    public void cover(DPTypeCoverVisitor visitor, int level) {
	visitor.ElementTypeCover(this, level);
    }

    // Tree node implementation
    public Enumeration children() {
	return null;
    }

    public boolean getAllowsChildren() {
	return false;
    }

    public DPType getChildAt(int childIndex) {
	return null;
    }

    public int getChildCount() {
	return 0;
    }

    public int getIndex(TreeNode node) {
	return -1;
    }

    public DPType getParent() {
	return parent;
    }

    public boolean isLeaf() {
	return true;
    }
}
