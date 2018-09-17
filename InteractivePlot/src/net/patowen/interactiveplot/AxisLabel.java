package net.patowen.interactiveplot;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class AxisLabel extends JComponent {
	public enum Orientation {HORIZONTAL, VERTICAL};
	
	private Orientation orientation;
	private BufferedImage textImage;
	
	public AxisLabel(String text, Orientation orientation, Font font) {
		if (font == null) {
			font = getFont();
		}
		System.out.println(font);
		FontMetrics fontMetrics = getFontMetrics(font);
		Rectangle2D bounds = fontMetrics.getStringBounds(text, getGraphics());
		textImage = new BufferedImage((int)bounds.getWidth(), (int)bounds.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = textImage.getGraphics();
		g.drawString(text, (int)bounds.getX(), (int)bounds.getY());
		setPreferredSize(new Dimension(textImage.getWidth(), textImage.getHeight()));
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(textImage, 0, 0, null);
	}
}
