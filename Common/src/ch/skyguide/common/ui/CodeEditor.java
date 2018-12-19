/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.skyguide.common.ui;

import ch.skyguide.common.ui.documentStyleManager.SyntaxHighlightView;
import ch.skyguide.common.ui.documentStyleManager.scheme.DefaultStyleScheme;
import ch.skyguide.common.ui.documentStyleManager.scheme.StyleScheme;
import ch.skyguide.common.ui.documentStyleManager.update.UpdateStrategy;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JEditorPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 *
 * @author caronyn
 */
public class CodeEditor extends JEditorPane {
    // TODO : Trouver la solution pour voir une ligne
/*
    @Override
    public boolean getScrollableTracksViewportWidth() {
	return false;
    }

    private int getTextMaxWidth() {
	int max = -1;
	int ret = -1;
	Element maxE = null;

	Document doc = getDocument();
	Element root = doc.getRootElements()[0];

	for (int i = 0; i < root.getElementCount() - 1; i++) {
	    Element e = root.getElement(i);

	    int width = e.getEndOffset() - e.getStartOffset();

	    if (max < width) {
		max = width;
		maxE = e;
	    }

	}

	if (maxE != null) {
	    try {
		ret = (int)getGraphics().getFontMetrics().getStringBounds(getDocument().getText(maxE.getStartOffset(), maxE.getEndOffset()), getGraphics()).getBounds().getWidth();
	    } catch (BadLocationException ex) {
		Logger.getLogger(CodeEditor.class.getName()).log(Level.SEVERE, null, ex);
	    }

	}

	return ret;
    }

    @Override
    public Dimension getPreferredSize() {
	return new Dimension(getTextMaxWidth(), super.getPreferredSize().height);
    }
 */
    public final static int MARGIN_NUMBERING = 10;
    private boolean showCurrentLine = false;
    private boolean showLineNumbering = false;

    public boolean isShowCurrentLine() {
	return showCurrentLine;
    }

    public void setShowCurrentLine(boolean showCurrentLine) {
	this.showCurrentLine = showCurrentLine;
    }

    public boolean isShowLineNumbering() {
	return showLineNumbering;
    }

    public void setShowLineNumbering(boolean showLineNumbering) {
	this.showLineNumbering = showLineNumbering;
    }

    public int getLineAtCaret() {
	int caretPosition = getCaretPosition();
	Element root = getDocument().getDefaultRootElement();

	return root.getElementIndex(caretPosition);
    }

    public void setSyntaxHighlight(final UpdateStrategy updater, StyleScheme style) {
	updater.setComponent(this);
	this.setEditorKit(new StyledEditorKit() {

	    @Override
	    public ViewFactory getViewFactory() {
		return new ViewFactory() {

		    public View create(Element elem) {
			return new SyntaxHighlightView(elem, updater, new DefaultStyleScheme());
		    }
		};
	    }
	});
    }

    private int countLine() {
	Document doc = getDocument();
	int len = doc.getDefaultRootElement().getElementIndex(doc.getLength());
	return len;
    }

    private int getNumberingWidth() {
	return getGraphics().getFontMetrics().getWidths()['9'] * String.valueOf(countLine()).length();
    }

    private void paintLineNumbering(Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	int width = getNumberingWidth() + MARGIN_NUMBERING * 2;
	int topBound = (int) (g.getClipBounds().getY());
	int bottomBound = topBound + (int) (g.getClipBounds().getHeight()) + g.getFontMetrics().getHeight();

	if (g.getClipBounds().getX() <= width) {

	    g2d.setColor(Color.lightGray);
	    g.fillRect(0, topBound, width, bottomBound);

	    g2d.setColor(Color.GRAY);
	    g.drawLine(width, topBound, width, bottomBound);

	    g2d.setColor(Color.DARK_GRAY);
	    for (int i = 0; i <= countLine(); i++) {
		int y = (i + 1) * g.getFontMetrics().getHeight();
		if (y >= topBound && y <= bottomBound) {
		    g2d.drawString(String.valueOf(i), MARGIN_NUMBERING, y);
		}
	    }
	}

    }

    private void paintCurrentLine(Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
		RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f));

	Insets insets = getInsets();
	int y = (g.getFontMetrics().getHeight() * getLineAtCaret()) + insets.top;

	g2d.setColor(this.getSelectionColor());
	g2d.fillRect(insets.left, y, this.getWidth() - (insets.left + insets.right), g.getFontMetrics().getHeight());

    }

    public CodeEditor() {
	super();
	this.addCaretListener(new CaretListener() {

	    public void caretUpdate(CaretEvent e) {
		repaint();
	    }
	});
	this.addMouseMotionListener(new MouseMotionAdapter() {

	    @Override
	    public void mouseDragged(MouseEvent e) {
		repaint();
	    }
	});
    }

    @Override
    public void paint(Graphics g) {
	super.paint(g);
	if (showCurrentLine) {
	    paintCurrentLine(g);
	}
	if (showLineNumbering) {
	    paintLineNumbering(g);
	}
    }

    @Override
    public Insets getInsets() {
	Insets insets = super.getInsets();
	if (showLineNumbering) {
	    insets.left += getNumberingWidth() + MARGIN_NUMBERING * 2;
	}
	return insets;
    }
}
