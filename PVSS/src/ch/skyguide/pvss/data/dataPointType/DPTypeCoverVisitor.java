/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.skyguide.pvss.data.dataPointType;

/**
 *
 * @author CyaNn
 */
public interface DPTypeCoverVisitor {

    boolean linkReference();

    void NodeTypeCover(NodeType dpType, int level);
    void ReferenceTypeCover(ReferenceType dpType, int levelntext);
    void ElementTypeCover(ElementType dpType, int levelntext);

}
